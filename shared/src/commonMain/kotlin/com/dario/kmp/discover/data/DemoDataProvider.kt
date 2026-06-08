package com.dario.kmp.discover.data

import com.dario.kmp.discover.domain.model.DetailContent
import com.dario.kmp.discover.domain.model.DiscoverImageKey
import com.dario.kmp.discover.domain.model.ItemType
import com.dario.kmp.discover.domain.model.NextUpItem

/**
 * Fallback demo data used when the CMS API is unavailable.
 * Images use KMP resource keys resolved at runtime via [DiscoverImageKey].
 */
object DemoDataProvider {

    fun getContent(type: ItemType): DetailContent = when (type) {
        ItemType.RECIPE -> recipeData()
        ItemType.ARTICLE -> articleData()
        ItemType.AUDIO -> audioData()
    }

    private fun recipeData() = DetailContent.Recipe(
        heroImageUrl = null,
        heroImageKey = DiscoverImageKey.RECIPE_BANNER,
        title = "Sesame-Spiced Pork Tenderloin",
        description = "Sesame-spiced pork tenderloin with fresh mango and papaya, a bold, tropical flavor in every bite",
        servings = "4 servings",
        duration = "5 min",
        tags = listOf("Moderate Sodium", "Heart Healthy", "Balanced Meal"),
        ingredients = listOf(
            "2 tbsp sesame seeds",
            "1 tsp ground coriander",
            "¼ tsp cayenne pepper",
            "⅛ tsp celery seed",
            "½ tsp dried minced onion",
            "¼ tsp ground cumin",
            "⅛ tsp ground cinnamon"
        ),
        instructions = listOf(
            "Preheat oven to 400°F. Lightly coat a baking dish with cooking spray.",
            "In a dry skillet, toast sesame seeds over low heat, stirring constantly until golden and fragrant (1–2 min). Remove from pan and let cool.",
            "In a small bowl, mix sesame seeds, coriander, cayenne, celery seed, onion, cumin, cinnamon, and sesame oil.",
            "Place pork pieces in prepared dish. Rub spice mix evenly onto pork.",
            "Toast sesame seeds until golden.",
            "Mix spices and sesame oil.",
            "Bake for 15 minutes."
        ),
        nutrition = listOf(
            "Calories" to "176",
            "Total fat" to "8g",
            "Saturated fat" to "2g",
            "Trans fat" to "2g",
            "Monounsaturated fat" to "8g",
            "Cholesterol" to "25g",
            "Sodium" to "61g",
            "Carbs" to "8g",
            "Added sugar" to "25g",
            "Protein" to "61g",
            "Total sugars" to "15g"
        ),
        topics = listOf("GLP-1", "Diabetes", "Hypertension"),
        nextUp = nextUp()
    )

    private fun articleData() = DetailContent.Article(
        imageUrl = null,
        imageKey = DiscoverImageKey.ARTICLE,
        title = "Kindness Is a Group Effort In This Charming Animated Short",
        body = "It may feel like we've been dealing with quarantines and restrictions forever, but the first pandemic lockdowns actually started a few weeks after February 14th last year, making this the first official COVID-era Valentine's Day. And while it may seem a little frivolous to think about heart-shaped boxes of chocolate when so much of the world is still dealing with virus surges and vaccine shortages, this is exactly why we should be excited about a holiday that celebrates love.",
        topics = listOf("Mental Health", "Stress", "Mindfulness"),
        nextUp = nextUp()
    )

    private fun audioData() = DetailContent.Audio(
        imageUrl = null,
        imageKey = DiscoverImageKey.AUDIO,
        title = "Basic Body Scan Meditation",
        description = "Lorem ipsum dolor sit amet consectetur. Est in praesent vitae euismod neque quam et facilisi eget. Id odio duis massa ut id nisl laoreet sagittis. Dolor egestas orci tellus tincidunt sed facilisis elementum.",
        duration = "9 min",
        topics = listOf("Meditation", "Stress", "Anxiety"),
        nextUp = nextUp()
    )

    private fun nextUp() = listOf(
        NextUpItem(title = "Vegetable Minestrone Soup", type = "Recipe", imageUrl = null, imageKey = DiscoverImageKey.RECIPE_1),
        NextUpItem(title = "10-min weights set", type = "Exercise", imageUrl = null, imageKey = DiscoverImageKey.RECIPE_2)
    )
}
