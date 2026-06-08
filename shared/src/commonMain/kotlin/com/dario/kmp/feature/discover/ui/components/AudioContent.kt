package com.dario.kmp.feature.discover.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.theme.ScreenBackground

@Composable
fun AudioContent(state: DetailContent.Audio, onClose: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        item {
            Column(modifier = Modifier.background(Color.White)) {
                DetailHeroHeader(imageUrl = state.imageUrl, imageKey = state.imageKey, onClose = onClose)
                Spacer(Modifier.height(10.dp))
                MainInfoSection(title = state.title)
                Spacer(Modifier.height(10.dp))
                AudioPlayer()
                DividerSection()
                DescriptionText(text = state.description, modifier = Modifier.padding(24.dp))
            }
        }
        item { NextUpSection(state.nextUp) }
        item { TopicsSection(state.topics) }
    }
}
