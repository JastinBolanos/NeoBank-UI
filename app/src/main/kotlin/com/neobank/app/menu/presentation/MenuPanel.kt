package com.neobank.app.menu.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R

data class MenuOption(
    val icon: ImageVector,
    val title: String,
    val tint: Color = Color(0xFFFFFFFF)
)

@Composable
fun MenuPanel(
    onClose: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val menuOptions = listOf(
        MenuOption(Icons.Default.Settings, "Configuration"),
        MenuOption(Icons.Default.Lock, "Digital Token"),
        MenuOption(Icons.Default.Person, "Security and privacy"),
        MenuOption(Icons.AutoMirrored.Filled.List, "Operational"),
        MenuOption(Icons.Default.Share, "Transact with QR / Plin"),
        MenuOption(Icons.Default.Star, "Points and promotions"),
        MenuOption(Icons.Default.Favorite, "Experiences"),
        MenuOption(Icons.Default.Refresh, "Cardless withdrawal history"),
        MenuOption(Icons.Default.LocationOn, "Service points"),
        MenuOption(Icons.Default.ShoppingCart, "payment area"),
        MenuOption(Icons.AutoMirrored.Filled.ArrowBack, "Go out", Color(0xFFFFB300))
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.75f)
            .clip(RoundedCornerShape(topStart = 40.dp, bottomStart = 40.dp))
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 15) {
                        onClose()
                    }
                }
            }
    ) {
        // FONDO
        Image(
            painter = painterResource(R.drawable.bg_menu_panel),
            contentDescription = "Fondo del panel de menú",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // CONTENIDO
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(top = 24.dp, start = 20.dp, end = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text(
                    text = "Saurabh Kumar",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Aura Points: 0",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Profile",
                    color = Color(0xFF79C3FF),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .clickable {
                            onClose()
                            onNavigateToProfile()
                        }
                        .padding(vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(36.dp))
            }

            items(menuOptions) { option ->

                if (option.title == "Go out") {
                    Spacer(modifier = Modifier.height(40.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            if (option.title == "Go out") onClose()
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = option.title,
                        tint = option.tint,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = option.title,
                        color = option.tint,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}