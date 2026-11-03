package com.neobank.app.auth.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R
import com.neobank.app.auth.presentation.components.GlassCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(onNavigateToHome: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_neobank),
            contentDescription = "Premium NeoBank Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // --- TOP TITLE (Brand Logo) ---
            Text(
                text = "AuraNova",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp,
                modifier = Modifier.padding(top = 48.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- CARD ZONE ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                // Card 1: Current Balance (Back)
                GlassCard(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxWidth(0.85f)
                        .height(200.dp)
                        .offset(x = 80.dp),
                    containerAlpha = 0.08f
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Part: Balance
                        Column {
                            Text(
                                text = "Current Balance",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$10,985.84",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column {
                            Text(
                                text = "**** **** **** 1289",
                                color = Color.White,
                                fontSize = 16.sp,
                                letterSpacing = 3.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "09/25",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                }

                // Card 2: SBI Card (Front)
                GlassCard(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(0.9f)
                        .height(210.dp)
                        .offset(y = (-20).dp),
                    containerAlpha = 0.25f
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "SBI Card",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )

                        // Bottom Row: Numbers, Name, and Logo
                        Column {
                            Text(
                                text = "4521  7896  5412  3698",
                                color = Color.White,
                                fontSize = 18.sp,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        text = "SAURABH KUMAR",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = "03/25",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 11.sp
                                    )
                                }
                                MastercardLogo()
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            // --- BOTTOM TEXTS (Greeting) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp)
            ) {
                Text(
                    text = "Welcome back,",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "Saurabh Kumar",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(40.dp))

            // --- BIOMETRIC TRIGGER ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val scanProgress = remember { Animatable(0f) }
                val coroutineScope = rememberCoroutineScope()

                // Scanner Ring
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(
                            color = Color(0xFF14141E).copy(alpha = 0.6f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF40B143), Color(0xFF493B94))
                            ),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    val animJob = coroutineScope.launch {
                                        scanProgress.animateTo(
                                            targetValue = 1f,
                                            animationSpec = tween(
                                                durationMillis = 500,
                                                easing = LinearEasing
                                            )
                                        )
                                    }

                                    val timerJob = coroutineScope.launch {
                                        delay(500L)
                                        onNavigateToHome()
                                    }
                                    tryAwaitRelease()
                                    timerJob.cancel()
                                    animJob.cancel()

                                    coroutineScope.launch {
                                        scanProgress.animateTo(
                                            targetValue = 0f,
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = FastOutSlowInEasing
                                            )
                                        )
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {

                    // --- EFFECT 1: Expanding Aura (Scanner Glow) ---
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = 1f + (scanProgress.value * 0.8f)
                                scaleY = 1f + (scanProgress.value * 0.8f)
                                alpha = if (scanProgress.value > 0f) 0.35f * (1f - scanProgress.value) else 0f
                            }
                            .background(Color(0xFF40B143), androidx.compose.foundation.shape.CircleShape)
                    )

                    // --- EFFECT 2 and 3: Color Transition and Shrink Effect ---
                    val iconTint = lerp(
                        start = Color.White,
                        stop = Color(0xFF40B143),
                        fraction = scanProgress.value
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_fingerprint),
                        contentDescription = "Unlock with fingerprint",
                        tint = iconTint,
                        modifier = Modifier
                            .size(50.dp)
                            .graphicsLayer {
                                val scale = 1f - (scanProgress.value * 0.15f)
                                scaleX = scale
                                scaleY = scale
                            }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Use PIN code",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun MastercardLogo() {
    Box(
        modifier = Modifier
            .width(44.dp)
            .height(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(28.dp)
                .background(Color(0xFFEB001B), shape = androidx.compose.foundation.shape.CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(28.dp)
                .background(Color(0xFFF79E1B).copy(alpha = 0.9f), shape = androidx.compose.foundation.shape.CircleShape)
        )
    }
}