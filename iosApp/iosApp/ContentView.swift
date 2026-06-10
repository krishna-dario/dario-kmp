import UIKit
import SwiftUI
import Shared

// MARK: - KMP Compose bridge
// Wraps the Kotlin Compose UIViewController as a SwiftUI view.
// Use this anywhere in your app to embed the KMP Discover screen.

struct DiscoverScreen: UIViewControllerRepresentable {

    let type: DiscoverItemType
    let baseUrl: String
    let skipApiCall: Bool
    var onClose: (() -> Void)?

    init(
        type: DiscoverItemType = .recipe,
        baseUrl: String = "",
        skipApiCall: Bool = true,
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

// MARK: - Item type

enum DiscoverItemType: String {
    case recipe  = "RECIPE"
    case article = "ARTICLE"
    case audio   = "AUDIO"
}

// MARK: - Demo app root (iosApp shell)
// For production, use DiscoverScreen directly in your view hierarchy.

struct ContentView: View {
    var body: some View {
        DiscoverScreen(type: .recipe, skipApiCall: true)
            .ignoresSafeArea()
    }
}
