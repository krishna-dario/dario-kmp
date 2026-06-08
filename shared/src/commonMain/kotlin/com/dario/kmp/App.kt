package com.dario.kmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dario.kmp.discover.di.DiscoverModule
import com.dario.kmp.discover.domain.model.ItemType
import com.dario.kmp.discover.feature.itemdetail.ui.ItemDetailRoute
import dariokmp.shared.generated.resources.Res
import dariokmp.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showDiscover by remember { mutableStateOf(false) }
        var discoverType by remember { mutableStateOf(ItemType.RECIPE) }

        if (showDiscover) {
            // Example: launch the Discover item-detail screen cross-platform.
            // Replace the baseUrl with your real CMS endpoint.
            val viewModel = remember {
                DiscoverModule.provideItemDetailViewModel(baseUrl = "https://api.example.com/")
            }
            ItemDetailRoute(
                viewModel = viewModel,
                itemType = discoverType,
                onClose = { showDiscover = false }
            )
        } else {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { discoverType = ItemType.RECIPE; showDiscover = true }) {
                    Text("Open Recipe Detail")
                }
                Button(onClick = { discoverType = ItemType.ARTICLE; showDiscover = true }) {
                    Text("Open Article Detail")
                }
                Button(onClick = { discoverType = ItemType.AUDIO; showDiscover = true }) {
                    Text("Open Audio Detail")
                }
                AnimatedVisibility(true) {
                    val greeting = remember { Greeting().greet() }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                    }
                }
            }
        }
    }
}