// ============================================================
//  iOS Integration Guide — Dario KMP Shared Framework
//  Copy the code blocks you need into your iOS project.
// ============================================================
//
//  STEP 1 — Add the framework to Xcode
//  ─────────────────────────────────────
//  1. Download Shared-XCFramework-release-device-only from
//     GitHub Actions → Build iOS Framework artifacts.
//  2. In Xcode: select your app target → General tab →
//     Frameworks, Libraries, and Embedded Content →
//     drag Shared.xcframework in → set to "Embed & Sign".
//  3. Intel Mac only: Build Settings →
//     EXCLUDED_ARCHS[sdk=iphonesimulator*] = x86_64
//
//  STEP 2 — Add these files to your Xcode project
//  ────────────────────────────────────────────────
//  • DiscoverItemType.swift  (item type enum)
//  • DiscoverScreen.swift    (SwiftUI wrapper — embed inline)
//  • DiscoverPresenter.swift (UIKit helper — present modally)
//
// ============================================================

import SwiftUI
import UIKit
import Shared   // ← the KMP XCFramework

// ============================================================
// FILE: DiscoverItemType.swift
// ============================================================

enum DiscoverItemType: String {
    case recipe  = "RECIPE"
    case article = "ARTICLE"
    case audio   = "AUDIO"
}

// ============================================================
// FILE: DiscoverScreen.swift
// SwiftUI — embed the KMP Discover screen as a SwiftUI view.
// ============================================================

struct DiscoverScreen: UIViewControllerRepresentable {

    // Which content type to display.
    let type: DiscoverItemType

    // Your CMS base URL. Pass "" when skipApiCall is true.
    let baseUrl: String

    // true  → always show bundled demo data, no network call.
    // false → fetch live data from baseUrl.
    let skipApiCall: Bool

    // Called when the user taps the back / close button.
    var onClose: (() -> Void)?

    init(
        type: DiscoverItemType,
        baseUrl: String,
        skipApiCall: Bool = false,
        onClose: (() -> Void)? = nil
    ) {
        self.type = type
        self.baseUrl = baseUrl
        self.skipApiCall = skipApiCall
        self.onClose = onClose
    }

    func makeUIViewController(context: Context) -> UIViewController {
        DiscoverViewControllerKt.DiscoverViewController(
            type: type.rawValue,
            baseUrl: baseUrl,
            skipApiCall: skipApiCall,
            onClose: { onClose?() }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

// ── SwiftUI usage examples ─────────────────────────────────

// Inline (full screen)
struct RecipeScreenView: View {
    @Environment(\.dismiss) var dismiss

    var body: some View {
        DiscoverScreen(
            type: .recipe,
            baseUrl: "https://api.dario.com/",
            skipApiCall: false,
            onClose: { dismiss() }
        )
        .ignoresSafeArea()
    }
}

// Presented as a sheet
struct HomeView: View {
    @State private var showRecipe = false

    var body: some View {
        Button("Open Recipe") { showRecipe = true }
            .sheet(isPresented: $showRecipe) {
                DiscoverScreen(
                    type: .recipe,
                    baseUrl: "https://api.dario.com/",
                    onClose: { showRecipe = false }
                )
                .ignoresSafeArea()
            }
    }
}

// ============================================================
// FILE: DiscoverPresenter.swift
// UIKit — present the KMP Discover screen from any UIViewController.
// ============================================================

final class DiscoverPresenter {

    // Present modally from a UIViewController.
    static func present(
        from viewController: UIViewController,
        type: DiscoverItemType,
        baseUrl: String,
        skipApiCall: Bool = false
    ) {
        let vc = makeViewController(type: type, baseUrl: baseUrl, skipApiCall: skipApiCall) {
            viewController.dismiss(animated: true)
        }
        vc.modalPresentationStyle = .fullScreen
        viewController.present(vc, animated: true)
    }

    // Push onto a UINavigationController.
    static func push(
        onto navigationController: UINavigationController,
        type: DiscoverItemType,
        baseUrl: String,
        skipApiCall: Bool = false
    ) {
        let vc = makeViewController(type: type, baseUrl: baseUrl, skipApiCall: skipApiCall) {
            navigationController.popViewController(animated: true)
        }
        navigationController.pushViewController(vc, animated: true)
    }

    // Build the UIViewController directly.
    static func makeViewController(
        type: DiscoverItemType,
        baseUrl: String,
        skipApiCall: Bool = false,
        onClose: @escaping () -> Void
    ) -> UIViewController {
        DiscoverViewControllerKt.DiscoverViewController(
            type: type.rawValue,
            baseUrl: baseUrl,
            skipApiCall: skipApiCall,
            onClose: onClose
        )
    }
}

// ── UIKit usage examples ───────────────────────────────────

class HomeViewController: UIViewController {

    // ── Present modally ──────────────────────────────────────
    func openRecipe() {
        DiscoverPresenter.present(
            from: self,
            type: .recipe,
            baseUrl: "https://api.dario.com/"
        )
    }

    // ── Push on navigation stack ─────────────────────────────
    func pushArticle() {
        guard let nav = navigationController else { return }
        DiscoverPresenter.push(
            onto: nav,
            type: .article,
            baseUrl: "https://api.dario.com/"
        )
    }

    // ── Demo / testing with dummy data ───────────────────────
    func openDemoAudio() {
        DiscoverPresenter.present(
            from: self,
            type: .audio,
            baseUrl: "",         // ignored when skipApiCall is true
            skipApiCall: true
        )
    }
}

// ============================================================
// React Native iOS — add this to your RN Native Module
// ============================================================
//
// In your React Native project, create a file like:
//   ios/DiscoverKmpModule.swift
//
// @objc(DiscoverKmp)
// class DiscoverKmpModule: NSObject {
//
//     @objc
//     func openDiscover(_ typeStr: String, baseUrl: String, skipApiCall: Bool) {
//         DispatchQueue.main.async {
//             guard let root = UIApplication.shared.keyWindow?.rootViewController else { return }
//             DiscoverPresenter.present(
//                 from: root,
//                 type: DiscoverItemType(rawValue: typeStr) ?? .recipe,
//                 baseUrl: baseUrl,
//                 skipApiCall: skipApiCall
//             )
//         }
//     }
//
//     @objc static func requiresMainQueueSetup() -> Bool { true }
// }
