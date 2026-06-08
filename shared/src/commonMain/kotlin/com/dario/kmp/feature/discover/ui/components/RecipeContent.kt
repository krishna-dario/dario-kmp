package com.dario.kmp.feature.discover.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.theme.ScreenBackground

@Composable
fun RecipeContent(state: DetailContent.Recipe, onClose: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        item {
            DetailHeroHeader(
                imageUrl = state.heroImageUrl,
                imageKey = state.heroImageKey,
                onClose = onClose,
                bottomContent = {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        InfoChip(icon = Icons.Default.Person, text = state.servings)
                        InfoChip(icon = Icons.Default.AccessTime, text = state.duration)
                    }
                },
            )
        }
        item { MainInfoSection(title = state.title, description = state.description, tags = state.tags) }
        item {
            DetailSection(title = "Ingredients") {
                state.ingredients.forEach { BulletText(it) }
            }
        }
        item {
            DetailSection(title = "Instructions") {
                state.instructions.forEach { BulletText(it) }
            }
        }
        item { NutritionSection(items = state.nutrition) }
        item { NextUpSection(items = state.nextUp) }
        item { TopicsSection(topics = state.topics) }
    }
}
