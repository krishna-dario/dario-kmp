package com.dario.kmp.feature.discover.data.remote.dto

import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.dario.kmp.feature.discover.domain.model.NextUpItem

internal object ItemDetailMapper {

    fun toDomain(response: ItemDetailResponse): DetailContent =
        when (response.type.uppercase()) {
            ItemType.RECIPE.name  -> mapRecipe(response)
            ItemType.ARTICLE.name -> mapArticle(response)
            ItemType.AUDIO.name   -> mapAudio(response)
            else -> throw IllegalArgumentException("Unknown item type: ${response.type}")
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
            nextUp = response.nextUp.map(::mapNextUp),
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
            nextUp = response.nextUp.map(::mapNextUp),
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
            nextUp = response.nextUp.map(::mapNextUp),
        )
    }

    private fun mapNextUp(dto: NextUpDto) = NextUpItem(
        title = dto.title,
        type = dto.type,
        imageUrl = dto.imageUrl,
        imageKey = null,
    )
}
