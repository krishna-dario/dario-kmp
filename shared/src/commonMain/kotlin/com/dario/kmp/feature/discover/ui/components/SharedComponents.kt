package com.dario.kmp.feature.discover.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.dario.kmp.feature.discover.domain.model.DiscoverImageKey
import com.dario.kmp.feature.discover.domain.model.NextUpItem
import com.dario.kmp.feature.discover.theme.Accent
import com.dario.kmp.feature.discover.theme.BodyText
import com.dario.kmp.feature.discover.theme.ChipBackground
import com.dario.kmp.feature.discover.theme.DividerColor
import com.dario.kmp.feature.discover.theme.ImagePlaceholder
import com.dario.kmp.feature.discover.theme.PrimaryText
import com.dario.kmp.feature.discover.theme.SecondaryText
import com.dario.kmp.feature.discover.theme.TagBackground
import dariokmp.shared.generated.resources.Res
import dariokmp.shared.generated.resources.img_article
import dariokmp.shared.generated.resources.img_audio
import dariokmp.shared.generated.resources.img_recipe_1
import dariokmp.shared.generated.resources.img_recipe_2
import dariokmp.shared.generated.resources.img_recipe_banner
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

// ─── Image ───────────────────────────────────────────────────────────────────

/**
 * Resolves an image in priority order:
 * 1. Remote [url]  → Coil [AsyncImage]
 * 2. Local [key]   → bundled composeResources drawable
 * 3. Neither       → solid [ImagePlaceholder] colour block
 */
@Composable
fun DiscoverImage(
    url: String?,
    key: DiscoverImageKey?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    when {
        url != null -> AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier,
        )
        key != null -> Image(
            painter = painterResource(key.toDrawableResource()),
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier,
        )
        else -> Box(modifier = modifier.background(ImagePlaceholder))
    }
}

private fun DiscoverImageKey.toDrawableResource(): DrawableResource = when (this) {
    DiscoverImageKey.RECIPE_BANNER -> Res.drawable.img_recipe_banner
    DiscoverImageKey.ARTICLE       -> Res.drawable.img_article
    DiscoverImageKey.AUDIO         -> Res.drawable.img_audio
    DiscoverImageKey.RECIPE_1      -> Res.drawable.img_recipe_1
    DiscoverImageKey.RECIPE_2      -> Res.drawable.img_recipe_2
}

// ─── Hero header ─────────────────────────────────────────────────────────────

@Composable
fun DetailHeroHeader(
    imageUrl: String?,
    imageKey: DiscoverImageKey? = null,
    onClose: () -> Unit,
    bottomContent: @Composable (() -> Unit)? = null,
) {
    Box {
        DiscoverImage(
            url = imageUrl,
            key = imageKey,
            modifier = Modifier.fillMaxWidth().height(320.dp),
        )
        CloseButton(onClose = onClose)
        if (bottomContent != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
            ) { bottomContent() }
        }
    }
}

@Composable
fun CloseButton(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.96f)),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = PrimaryText,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

// ─── Info sections ────────────────────────────────────────────────────────────

@Composable
fun MainInfoSection(
    title: String,
    description: String? = null,
    tags: List<String>? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 26.dp),
    ) {
        MainTitle(title)
        Spacer(Modifier.height(16.dp))
        if (!tags.isNullOrEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) { tags.forEach { Chip(text = it) } }
            Spacer(Modifier.height(22.dp))
        }
        description?.let { DescriptionText(text = it) }
    }
}

@Composable
fun MainTitle(title: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                lineHeight = 38.sp,
                color = PrimaryText,
            ),
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(16.dp))
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Favourite",
                tint = SecondaryText,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

@Composable
fun DescriptionText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            lineHeight = 30.sp,
            color = BodyText,
            fontSize = 18.sp,
        ),
        modifier = modifier,
    )
}

@Composable
fun DetailSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        DividerSection()
        SectionTitle(title)
        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .padding(bottom = 26.dp),
        ) { content() }
    }
}

@Composable
fun NutritionSection(items: List<Pair<String, String>>) {
    DetailSection(title = "Nutrition Facts (Per Serving)") {
        Text(
            text = "Serving size: about 1½ cups",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryText,
        )
        Spacer(Modifier.height(24.dp))
        items.forEachIndexed { index, (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = label,
                    fontSize = 18.sp,
                    fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Medium,
                    color = PrimaryText,
                )
                Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = PrimaryText)
            }
            if (index != items.lastIndex) HorizontalDivider(color = DividerColor)
        }
    }
}

@Composable
fun TopicsSection(topics: List<String>) {
    DetailSection(title = "Topics") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) { topics.forEach { Chip(text = it, background = ChipBackground) } }
        Spacer(Modifier.height(30.dp))
    }
}

@Composable
fun NextUpSection(items: List<NextUpItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        DividerSection()
        SectionTitle("Next up")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) { items(items) { NextUpCard(it) } }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun NextUpCard(item: NextUpItem) {
    Card(
        modifier = Modifier.width(165.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column {
            Box {
                DiscoverImage(
                    url = item.imageUrl,
                    key = item.imageKey,
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                )
                Chip(
                    text = item.type,
                    background = if (item.type == "Recipe") Color(0xFFFFD6C7) else Color(0xFFD6F4EE),
                    modifier = Modifier.align(Alignment.BottomStart).padding(10.dp),
                    fontSize = 11.sp,
                )
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp,
                    color = PrimaryText,
                ),
                modifier = Modifier.padding(14.dp),
            )
        }
    }
}

// ─── Audio player ─────────────────────────────────────────────────────────────

@Composable
fun AudioPlayer() {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Slider(value = 0.22f, onValueChange = {})
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("03:42")
            Text("- 09:02:55")
        }
        Spacer(Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = Accent)
            }
            Spacer(Modifier.width(18.dp))
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Accent)
                    .clickable {},
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(34.dp))
            }
            Spacer(Modifier.width(18.dp))
            IconButton(onClick = {}) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Accent)
            }
        }
        Spacer(Modifier.height(26.dp))
    }
}

// ─── Primitives ───────────────────────────────────────────────────────────────

@Composable
fun BulletText(text: String) {
    Row(modifier = Modifier.padding(vertical = 7.dp), verticalAlignment = Alignment.Top) {
        Text(text = "•", fontSize = 20.sp, color = BodyText)
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 28.sp,
                color = BodyText,
                fontSize = 17.sp,
            ),
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.SemiBold,
            color = PrimaryText,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 18.dp),
    )
}

@Composable
fun DividerSection() {
    HorizontalDivider(color = DividerColor, thickness = 2.dp)
}

@Composable
fun InfoChip(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.97f))
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = PrimaryText, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(7.dp))
        Text(text = text, color = PrimaryText, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    background: Color = TagBackground,
    textColor: Color = SecondaryText,
    fontSize: TextUnit = 15.sp,
) {
    Text(
        text = text,
        color = textColor,
        fontSize = fontSize,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 7.dp),
    )
}
