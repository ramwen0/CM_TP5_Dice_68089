package com.example.birthdaycardcodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birthdaycardcodelab.ui.theme.BirthdayCardCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BirthdayCardCodelabTheme {
                DiceApp()
            }
        }
    }
}

// Custom Colors
private val JapanRed = Color(0xFFBC002D)
private val CherryPink = Color(0xFFFFB7C5)
private val DeepNavy = Color(0xFF1A2332)
private val OffWhite = Color(0xFFFBFBFD)
private val AutumnColor = Color(0xFFD96346)
private val CardBg = Color(0xFFFFFFFF)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JapanTravelBadgeScreen(
    temperature: Int,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF3F4F8),
                        Color(0xFFE5E8F0),
                        Color(0xFFD8DCE8)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.die_result_one),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BadgeHeaderBanner(
                        isFavorite = isFavorite,
                        onFavoriteToggle = { isFavorite = !isFavorite }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        HeroLandscapeSection(temperature)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Destination Title & Kanji Subtitle
                        DestinationTitleSection()

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "BRIEFING",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = JapanRed,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        QuickFactsGrid()

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "HIGHLIGHTS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = JapanRed,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HighlightsSection()

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onRefresh,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = JapanRed)
                            ) {
                                Text(text = stringResource(R.string.refresh))
                            }
                            OutlinedButton(
                                onClick = onBack,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = stringResource(R.string.back))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

//  Header Banner
@Composable
fun BadgeHeaderBanner(
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = OffWhite)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Branding Section
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(JapanRed),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "日",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "JAPAN",
                    color = JapanRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Travel Badge",
                    color = CherryPink,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Right Save Button
        OutlinedButton(
            onClick = onFavoriteToggle,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isFavorite) JapanRed else DeepNavy
            )
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) JapanRed else DeepNavy,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isFavorite) "Saved" else "Save",
                fontSize = 13.sp
            )
        }
    }
}

// Hero Landscape Canvas & Weather
@Composable
fun HeroLandscapeSection(temperature: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E2AD2),
                        Color(0xFF7A88FB),
                        Color(0xFFFFD1DC)
                    )
                )
            )
    ) {
        // Mt. Fuji & Sun Graphic
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Rising Sun
            drawCircle(
                color = JapanRed.copy(alpha = 0.85f),
                radius = 45.dp.toPx(),
                center = Offset(width * 0.5f, height * 0.42f)
            )

            // Mt. Fuji Base Mountain Path
            val mountainPath = Path().apply {
                moveTo(width * 0.1f, height)
                lineTo(width * 0.5f, height * 0.32f)
                lineTo(width * 0.9f, height)
                close()
            }
            drawPath(path = mountainPath, color = DeepNavy)

            // Snow Cap on Mt. Fuji
            val snowCapPath = Path().apply {
                moveTo(width * 0.5f, height * 0.32f)
                lineTo(width * 0.41f, height * 0.48f)
                lineTo(width * 0.46f, height * 0.45f)
                lineTo(width * 0.5f, height * 0.50f)
                lineTo(width * 0.54f, height * 0.45f)
                lineTo(width * 0.59f, height * 0.48f)
                close()
            }
            drawPath(path = snowCapPath, color = Color.White)
        }

        // Overlay Weather & Season Chip
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            color = Color.Black.copy(alpha = 0.55f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$temperature°C", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Autumn", color = AutumnColor, fontSize = 11.sp)
            }
        }

        // Overlay Location Pin Badge
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
            color = JapanRed,
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "East Asia • Pacific",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Destination Title
@Composable
fun DestinationTitleSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tokyo, Kyoto and Osaka",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "日本国",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = JapanRed
                )
            }
        }
    }
}

// Quick Facts
@Composable
fun QuickFactsGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickFactCard(
                modifier = Modifier.weight(1f),
                label = "Timezone",
                value = "JST (UTC +9)"
            )
            QuickFactCard(
                modifier = Modifier.weight(1f),
                label = "Currency",
                value = "Japanese Yen (¥)"
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickFactCard(
                modifier = Modifier.weight(1f),
                label = "Flight Time",
                value = "~11.5 Hours"
            )
            QuickFactCard(
                modifier = Modifier.weight(1f),
                label = "Best Season",
                value = "March – April"
            )
        }
    }
}

@Composable
fun QuickFactCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Surface(
        modifier = modifier,
        color = OffWhite,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE8E8EE))
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepNavy
                )
            }
        }
    }
}

// Destination Highlights
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HighlightsSection() {
    val highlights = listOf(
        "Mt. Fuji" to "3,776m Peak",
        "Universal Studios Japan" to "Amusement Park",
        "Nara Park" to "Deer park",
        "Shibuya/Shinjuku" to "Shopping Hubs",
        "Ikebukuro" to "Nerd Haven",
        "Osaka Aquarium" to "Aqua gate",
        "Tower Records" to "Iconic record shop",
        "Kyoto Temples" to "Historical sites",
        "Bamboo Fores" to "Scenery & stroll"
    )

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        highlights.forEach { (title, subtitle) ->
            Surface(
                color = Color(0xFFF0F4F8),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Text(
                            text = subtitle,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

// Compose Previews
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JapanTravelBadgePreview() {
    BirthdayCardCodelabTheme {
        JapanTravelBadgeScreen(temperature = 18, onRefresh = {}, onBack = {})
    }
}