package com.neobank.app.core.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.neobank.app.R

enum class NavTab {
    Home,
    History,
    Transfer,
    Cards,
    Profile
}

@Composable
fun NeoBottomNavigationBar(
    selectedTab: NavTab,
    isDarkMode: Boolean = false,
    onTabSelected: (NavTab) -> Unit
) {
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val activeColor = Color(0xFF3F1D6B)
    val inactiveColor = if (isDarkMode) Color(0xFF8E8E93) else Color(0xFF797979)
    val infiniteTransition = rememberInfiniteTransition(label = "epic_glow")

    // 1. Borde giratorio de colores
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // 2. Latido (Pulso)
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Colores RGB Neón Premium
    val epicColors = listOf(
        Color(0xFF00F2FE),
        Color(0xFF4FACFE),
        Color(0xFFFF0844),
        Color(0xFFFFB199),
        Color(0xFF00F2FE)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .graphicsLayer {
                scaleX = pulse
                scaleY = pulse
            }
            .clip(RoundedCornerShape(50))
            .drawBehind {
                rotate(rotation) {
                    drawRect(
                        brush = Brush.sweepGradient(epicColors),
                        topLeft = Offset(-size.width, -size.height),
                        size = size * 3f
                    )
                }
            }
            .padding(3.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = backgroundColor,
            shape = RoundedCornerShape(50),
            shadowElevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Home Icon
                val homeColor = if (selectedTab == NavTab.Home) activeColor else inactiveColor
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onTabSelected(NavTab.Home) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_nav_home),
                        contentDescription = "Home",
                        tint = homeColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 2. History Icon
                val historyColor = if (selectedTab == NavTab.History) activeColor else inactiveColor
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onTabSelected(NavTab.History) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_history),
                        contentDescription = "History",
                        tint = historyColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 3. Botón de Transferencia (Medio - Oscuro y Destacado)
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E1E1E))
                        .clickable { onTabSelected(NavTab.Transfer) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Transfer",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 4. Cards Icon
                val cardsColor = if (selectedTab == NavTab.Cards) activeColor else inactiveColor
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onTabSelected(NavTab.Cards) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cards),
                        contentDescription = "Cards",
                        tint = cardsColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 5. Profile Avatar
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (selectedTab == NavTab.Profile) activeColor else Color(0xFF2F80ED))
                        .clickable { onTabSelected(NavTab.Profile) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}