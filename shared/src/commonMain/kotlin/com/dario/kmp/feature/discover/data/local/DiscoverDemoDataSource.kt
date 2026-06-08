package com.dario.kmp.feature.discover.data.local

import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.domain.model.DiscoverImageKey
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.dario.kmp.feature.discover.domain.model.NextUpItem

/**
 * Bundled fallback content used when the remote API is unavailable.
 * Replace or remove once the CMS API is live.
 */
internal object DiscoverDemoDataSource {

    fun getContent(type: ItemType): DetailContent = when (type) {
        ItemType.RECIPE  -> recipe()
        ItemType.ARTICLE -> article()
        ItemType.AUDIO   -> audio()
    }

    private fun recipe() = DetailContent.Recipe(
        heroImageUrl = null,
        heroImageKey = DiscoverImageKey.RECIPE_BANNER,
        title = "Sesame-Spiced Pork Tenderloin",
        description = "Sesame-spiced pork tenderloin with fresh mango and papaya — bold, tropical flavor in every bite.",
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
            "⅛ tsp ground cinnamon",
        ),
        instructions = listOf(
            "Preheat oven to 400°F. Lightly coat a baking dish with cooking spray.",
            "Toast sesame seeds in a dry skillet over low heat until golden (1–2 min).",
            "Mix sesame seeds, coriander, cayenne, celery seed, onion, cumin, and cinnamon.",
            "Rub spice mix evenly onto pork. Place in baking dish.",
            "Bake for 15 minutes until cooked through.",
        ),
        nutrition = listOf(
            "Calories" to "176",
            "Total fat" to "8g",
            "Saturated fat" to "2g",
            "Cholesterol" to "25mg",
            "Sodium" to "61mg",
            "Carbs" to "8g",
            "Protein" to "21g",
        ),
        topics = listOf("GLP-1", "Diabetes", "Hypertension"),
        nextUp = nextUp(),
    )

    private fun article() = DetailContent.Article(
        imageUrl = null,
        imageKey = DiscoverImageKey.ARTICLE,
        title = "Kindness Is a Group Effort In This Charming Animated Short",
        body = "Small acts of kindness compound into something larger — a lesson beautifully illustrated in this short animation that's well worth two minutes of your day.",
        topics = listOf("Mental Health", "Stress", "Mindfulness"),
        nextUp = nextUp(),
    )

    private fun audio() = DetailContent.Audio(
        imageUrl = null,
        imageKey = DiscoverImageKey.AUDIO,
        title = "Basic Body Scan Meditation",
        description = "A guided body-scan meditation to help you release tension and reconnect with the present moment.",
        duration = "9 min",
        topics = listOf("Meditation", "Stress", "Anxiety"),
        nextUp = nextUp(),
    )

    private fun nextUp() = listOf(
        NextUpItem(title = "Vegetable Minestrone Soup", type = "Recipe", imageUrl = null, imageKey = DiscoverImageKey.RECIPE_1),
        NextUpItem(title = "10-min weights set", type = "Exercise", imageUrl = null, imageKey = DiscoverImageKey.RECIPE_2),
    )
}
