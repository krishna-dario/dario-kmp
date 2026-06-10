package com.dario.kmp

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Root UIViewController for the KMP demo iosApp shell.
 * Called from Swift: MainViewControllerKt.MainViewController()
 *
 * Consumer apps (Dario iOS, React Native iOS) should use
 * DiscoverViewControllerKt.DiscoverViewController() instead.
 */
fun MainViewController() = ComposeUIViewController { App() }
