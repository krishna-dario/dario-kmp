package com.dario.kmp.discover.data.remote.dto

import com.dario.kmp.discover.domain.model.DetailContent
import com.dario.kmp.discover.domain.model.ItemType
import com.dario.kmp.discover.domain.model.NextUpItem

/**
 * Maps [ItemDetailResponse] → [DetailContent].
 * API responses carry real image URLs so imageKey is always null here.
 * The demo data fallback sets imageKey for local bundled images.
 */
object ItemDetailMapper {

    fun toDomain(response: ItemDetailResponse): DetailContent {
        return when (response.type.uppercase()) {
            ItemType.RECIPE.name -> mapRecipe(response)
            ItemType.ARTICLE.name -> mapArticle(response)
            ItemType.AUDIO.name -> mapAudio(response)
            else -> throw IllegalArgumentException("Unknown item type: ${response.type}")
        }
    }

    private fun mapRecipe(response: ItemDetailResponse): DetailContent.Recipe {
        val dto = requireNotNull(response.recipe) { "recipe payload missing for type RECIPE" }
        return DetailContent.Recipe(
            heroImageUrl = dto.heroImageUrl,
            heroImageKey = null,
            title = dto.title,
            description = dto.description,
            servings = dto.servings,
            duration = dto.duration,
            tags = dto.tags,
            ingredients = dto.ingredients,
            instructions = dto.instructions,
            nutrition = dto.nutrition.map { it.label to it.value },
            topics = dto.topics,
            nextUp = mapNextUp(response.nextUp)
        )
    }

    private fun mapArticle(response: ItemDetailResponse): DetailContent.Article {
        val dto = requireNotNull(response.article) { "article payload missing for type ARTICLE" }
        return DetailContent.Article(
            imageUrl = dto.imageUrl,
            imageKey = null,
            title = dto.title,
            body = dto.body,
            topics = dto.topics,
            nextUp = mapNextUp(response.nextUp)
        )
    }

    private fun mapAudio(response: ItemDetailResponse): DetailContent.Audio {
        val dto = requireNotNull(response.audio) { "audio payload missing for type AUDIO" }
        return DetailContent.Audio(
            imageUrl = dto.imageUrl,
            imageKey = null,
            title = dto.title,
            description = dto.description,
            duration = dto.duration,
            topics = dto.topics,
            nextUp = mapNextUp(response.nextUp)
        )
    }

    private fun mapNextUp(items: List<NextUpDto>): List<NextUpItem> =
        items.map { NextUpItem(title = it.title, type = it.type, imageUrl = it.imageUrl, imageKey = null) }
}
