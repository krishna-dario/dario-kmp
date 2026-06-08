package com.dario.kmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dario.kmp.feature.discover.di.DiscoverModule
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.dario.kmp.feature.discover.ui.ItemDetailScreen

/**
 * Demo entry point used by the [androidApp] and [iosApp] shells in this project.
 * The real Android and iOS host apps embed [ItemDetailScreen] directly via
 * [DiscoverModule.provideViewModel].
 */
@Composable
fun App() {
    var currentType: ItemType? by remember { mutableStateOf(null) }
    val viewModel = remember { DiscoverModule.provideViewModel(baseUrl = "https://api.example.com/") }

    if (currentType != null) {
        ItemDetailScreen(
            viewModel = viewModel,
            itemType = currentType!!,
            onClose = { currentType = null },
        )
    } else {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(onClick = { currentType = ItemType.RECIPE }) { Text("Open Recipe") }
            Button(onClick = { currentType = ItemType.ARTICLE }) { Text("Open Article") }
            Button(onClick = { currentType = ItemType.AUDIO }) { Text("Open Audio") }
        }
    }
}