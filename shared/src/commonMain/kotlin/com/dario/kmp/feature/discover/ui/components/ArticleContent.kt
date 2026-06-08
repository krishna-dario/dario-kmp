package com.dario.kmp.feature.discover.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.theme.ScreenBackground

@Composable
fun ArticleContent(state: DetailContent.Article, onClose: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        item {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(),
            ) {
                DetailHeroHeader(imageUrl = state.imageUrl, imageKey = state.imageKey, onClose = onClose)
                Spacer(Modifier.height(10.dp))
                MainInfoSection(title = state.title, description = state.body)
                Spacer(Modifier.height(14.dp))
            }
        }
        item { NextUpSection(state.nextUp) }
        item { TopicsSection(state.topics) }
    }
}
