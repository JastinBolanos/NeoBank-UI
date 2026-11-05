package com.neobank.app.menu.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class MenuModalType {
    DIGITAL_TOKEN,
    CONFIGURATION,
    SECURITY_PRIVACY,
    OPERATIONAL_LIMITS,
    TRANSACT_QR_PLIN,
    POINTS_PROMOTIONS,
    EXPERIENCES,
    CARDLESS_WITHDRAWAL,
    SERVICE_POINTS,
    PAYMENT_AREA,
    LOGOUT_CONFIRM
}

// =============================================================
// REUSABLE PREMIUM GRADIENT CARD WITH ROTATING BORDER ANIMATION
// =============================================================
@Composable
fun AnimatedGradientCard(
    modifier: Modifier = Modifier,
    gradientColors: List<Color>,
    backgroundBrush: Brush,
    cornerRadius: Dp = 22.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "border_shimmer")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle_rotation"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .drawBehind {
                rotate(angle) {
                    drawRect(
                        brush = Brush.sweepGradient(gradientColors),
                        topLeft = Offset(-size.width, -size.height),
                        size = size * 3f
                    )
                }
            }
            .padding(1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius - 1.5.dp))
                .background(backgroundBrush)
                .padding(18.dp),
            content = content
        )
    }
}

// -------------------------------------------------------------
// 1. DIGITAL TOKEN MODAL (Aurora Neon Matrix)
// -------------------------------------------------------------
@Composable
fun DigitalTokenModal(
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var secondsRemaining by remember { mutableIntStateOf(28) }
    var tokenCode by remember { mutableStateOf("749 183") }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (secondsRemaining > 1) {
                secondsRemaining--
            } else {
                secondsRemaining = 30
                val r1 = (100..999).random()
                val r2 = (100..999).random()
                tokenCode = "$r1 $r2"
            }
        }
    }

    val progress by animateFloatAsState(
        targetValue = secondsRemaining / 30f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "token_timer"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Glowing Icon Badge
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF00F2FE).copy(alpha = 0.4f), Color(0xFF6B11FF).copy(alpha = 0.2f), Color.Transparent)
                    )
                )
                .border(
                    2.dp,
                    Brush.sweepGradient(listOf(Color(0xFF00F2FE), Color(0xFF00E676), Color(0xFF6B11FF), Color(0xFF00F2FE))),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF00F2FE), modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("AuraNova Digital Token", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Dynamic 6-digit rolling OTP for secure authorization", color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(24.dp))

        // Big Token Box with animated gradient border & radial glow
        AnimatedGradientCard(
            modifier = Modifier.fillMaxWidth(),
            gradientColors = listOf(Color(0xFF00F2FE), Color(0xFF00E676), Color(0xFF9B51E0), Color(0xFFFF007F), Color(0xFF00F2FE)),
            backgroundBrush = Brush.radialGradient(
                colors = listOf(Color(0xFF1A2644), Color(0xFF0D1222), Color(0xFF080B14)),
                radius = 800f
            ),
            cornerRadius = 24.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HARDWARE TOKEN CODE",
                    color = Color(0xFF00F2FE),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tokenCode,
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 6.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(18.dp),
                        color = Color(0xFF00E676),
                        strokeWidth = 2.5.dp,
                        trackColor = Color.White.copy(alpha = 0.12f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Renews in $secondsRemaining seconds",
                        color = Color(0xFF00E676),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Keystore indicator box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF00E676).copy(alpha = 0.15f), Color(0xFF00F2FE).copy(alpha = 0.08f), Color.Transparent)
                    )
                )
                .border(1.dp, Color(0xFF00E676).copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Protected by Android KeyStore v3", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Biometrically linked to Pixel 8 Pro", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .clickable {
                    onShowToast("✓ Token $tokenCode copied to clipboard")
                },
            color = Color.White,
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF14161E), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Copy Digital Token", color = Color(0xFF14161E), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// 2. CONFIGURATION MODAL (Electric Blue & Indigo Gradients)
// -------------------------------------------------------------
@Composable
fun ConfigurationModal(
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedCurrency by remember { mutableStateOf("USD ($)") }
    var hideBalanceByDefault by remember { mutableStateOf(false) }
    var biometricTransfers by remember { mutableStateOf(true) }
    var pushNotifications by remember { mutableStateOf(true) }
    var language by remember { mutableStateOf("English (US)") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Configuration", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Currency Setting with Vibrant Gradient Pills
        Text("PRIMARY DISPLAY CURRENCY", color = Color(0xFF00F2FE), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("USD ($)", "EUR (€)", "PEN (S/.)").forEach { curr ->
                val isSelected = selectedCurrency == curr
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(
                            if (isSelected) Brush.horizontalGradient(listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)))
                            else Brush.horizontalGradient(listOf(Color(0xFF1A1D2E), Color(0xFF131522)))
                        )
                        .border(
                            1.dp,
                            if (isSelected) Color(0xFF00F2FE) else Color.White.copy(alpha = 0.12f),
                            RoundedCornerShape(21.dp)
                        )
                        .clickable {
                            selectedCurrency = curr
                            onShowToast("Primary currency set to $curr")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(curr, color = if (isSelected) Color(0xFF0A0E1A) else Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Preference Settings with Gradient Cards
        Text("PREFERENCES & PRIVACY", color = Color(0xFF00F2FE), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))

        GradientSettingCard(
            title = "Hide balance on open",
            subtitle = "Shield sensitive amounts on app launch",
            icon = Icons.Default.VisibilityOff,
            checked = hideBalanceByDefault,
            glowColor = Color(0xFF00F2FE),
            onCheckedChange = {
                hideBalanceByDefault = it
                onShowToast(if (it) "Balance will be hidden on launch" else "Balance shown by default")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        GradientSettingCard(
            title = "Biometrics for transfers",
            subtitle = "Require fingerprint/FaceID before sending",
            icon = Icons.Default.Fingerprint,
            checked = biometricTransfers,
            glowColor = Color(0xFF6B11FF),
            onCheckedChange = {
                biometricTransfers = it
                onShowToast("Biometric verification updated")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        GradientSettingCard(
            title = "Instant push alerts",
            subtitle = "Real-time alerts for incoming & outgoing spend",
            icon = Icons.Default.NotificationsActive,
            checked = pushNotifications,
            glowColor = Color(0xFF00E676),
            onCheckedChange = {
                pushNotifications = it
                onShowToast("Notifications preferences saved")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("APP LANGUAGE", color = Color(0xFF00F2FE), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("English (US)", "Español (PE)").forEach { lang ->
                val isSelected = language == lang
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) Brush.horizontalGradient(listOf(Color(0xFF6B11FF), Color(0xFFB388FF)))
                            else Brush.horizontalGradient(listOf(Color(0xFF1A1D2E), Color(0xFF131522)))
                        )
                        .border(
                            1.dp,
                            if (isSelected) Color(0xFFB388FF) else Color.White.copy(alpha = 0.12f),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            language = lang
                            onShowToast("Language set to $lang")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(lang, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. SECURITY & PRIVACY MODAL (Ruby & Crimson Danger Gradients)
// -------------------------------------------------------------
@Composable
fun SecurityPrivacyModal(
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var isMasterCardLocked by remember { mutableStateOf(false) }
    var isOnlinePurchasesEnabled by remember { mutableStateOf(true) }
    var isInternationalEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Security & Privacy", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Emergency Card Freeze Card with Dramatic Red Danger Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    if (isMasterCardLocked) {
                        Brush.linearGradient(listOf(Color(0xFF5E0B19), Color(0xFF33040C), Color(0xFF1B0206)))
                    } else {
                        Brush.linearGradient(listOf(Color(0xFF261014), Color(0xFF180A0D), Color(0xFF100709)))
                    }
                )
                .border(
                    1.5.dp,
                    if (isMasterCardLocked) Brush.sweepGradient(listOf(Color(0xFFFF0844), Color(0xFFFF4E50), Color(0xFFFFB199), Color(0xFFFF0844)))
                    else Brush.horizontalGradient(listOf(Color(0xFFFF5252).copy(alpha = 0.4f), Color.Transparent)),
                    RoundedCornerShape(22.dp)
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF5252).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isMasterCardLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = null,
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = if (isMasterCardLocked) "All Cards Locked" else "Emergency Card Freeze",
                            color = if (isMasterCardLocked) Color(0xFFFF5252) else Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Instantly block all payments across cards",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 11.sp
                        )
                    }
                }

                Switch(
                    checked = isMasterCardLocked,
                    onCheckedChange = {
                        isMasterCardLocked = it
                        onShowToast(if (it) "⚠️ All cards are now FROZEN" else "✓ Cards reactivated successfully")
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFF0844),
                        uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                        uncheckedTrackColor = Color.White.copy(alpha = 0.15f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        GradientSettingCard(
            title = "Online / E-commerce spend",
            subtitle = "Authorize Amazon, Uber, streaming and internet checkouts",
            icon = Icons.Default.ShoppingCart,
            checked = isOnlinePurchasesEnabled,
            glowColor = Color(0xFF00F2FE),
            onCheckedChange = {
                isOnlinePurchasesEnabled = it
                onShowToast("Online transactions ${if (it) "allowed" else "blocked"}")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        GradientSettingCard(
            title = "International spending",
            subtitle = "Enable zero-FX card transactions abroad",
            icon = Icons.Default.Flight,
            checked = isInternationalEnabled,
            glowColor = Color(0xFF00E676),
            onCheckedChange = {
                isInternationalEnabled = it
                onShowToast("International transactions ${if (it) "enabled" else "disabled"}")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Active Session with Emerald Glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF0A2418), Color(0xFF05160E), Color(0xFF030C08))
                    )
                )
                .border(1.dp, Color(0xFF00E676).copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF00E676)))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Pixel 8 Pro (Current Device)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Lima, Peru • Biometrics Active", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                    }
                }
                Text("SECURE", color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

// -------------------------------------------------------------
// 4. OPERATIONAL LIMITS MODAL (Titanium Carbon & Cyan Neon)
// -------------------------------------------------------------
@Composable
fun OperationalLimitsModal(
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var transferLimit by remember { mutableFloatStateOf(10000f) }
    var atmLimit by remember { mutableFloatStateOf(2000f) }
    var onlineLimit by remember { mutableFloatStateOf(15000f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Operational Limits", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Text("Adjust your daily operation thresholds dynamically", color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp)

        Spacer(modifier = Modifier.height(20.dp))

        GradientLimitCard(
            title = "Daily Transfers & Plin",
            currentValue = "$${"%,d".format(transferLimit.toInt())}",
            value = transferLimit,
            range = 1000f..30000f,
            accentGradient = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)),
            onValueChange = { transferLimit = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        GradientLimitCard(
            title = "ATM Cash Withdrawal",
            currentValue = "$${"%,d".format(atmLimit.toInt())}",
            value = atmLimit,
            range = 500f..5000f,
            accentGradient = listOf(Color(0xFF00E676), Color(0xFF00B0FF)),
            onValueChange = { atmLimit = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        GradientLimitCard(
            title = "Daily Online Purchases",
            currentValue = "$${"%,d".format(onlineLimit.toInt())}",
            value = onlineLimit,
            range = 1000f..40000f,
            accentGradient = listOf(Color(0xFFFFD700), Color(0xFFFF9100)),
            onValueChange = { onlineLimit = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF00F2FE), Color(0xFF4FACFE), Color(0xFF00E676))))
                .clickable {
                    onShowToast("✓ Operational limits updated successfully!")
                    onDismiss()
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Save New Limits", color = Color(0xFF0B0F19), fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

// -------------------------------------------------------------
// 5. TRANSACT QR / PLIN MODAL (Iridescent Cyan & Emerald)
// -------------------------------------------------------------
@Composable
fun TransactQrPlinModal(
    currentBalance: Double,
    onNavigateToScanner: () -> Unit,
    onDeductBalance: (Double) -> Unit,
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val quickContacts = listOf(
        Pair("Elena Cruz", "+51 984 123 456"),
        Pair("Carlos Mendoza", "+51 971 654 987"),
        Pair("Lucia Ramos", "+51 945 889 012")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Transact with QR / Plin", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Open AuraScanner Spectacular Gradient Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF00F2FE), Color(0xFF4FACFE), Color(0xFF00E676))
                    )
                )
                .clickable {
                    onDismiss()
                    onNavigateToScanner()
                }
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.Black.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.Black, modifier = Modifier.size(26.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("Open Camera QR Scanner", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Pay Yape, Plin, or POS terminal in 1 tap", color = Color.Black.copy(alpha = 0.75f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Black, modifier = Modifier.size(24.dp))
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text("SEND INSTANT PLIN TO FREQUENT CONTACTS", color = Color(0xFF00F2FE), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        quickContacts.forEach { (name, phone) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF161928), Color(0xFF10121D), Color(0xFF0B0D14))
                        )
                    )
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(Color(0xFF00E676).copy(alpha = 0.4f), Color(0xFF00F2FE).copy(alpha = 0.2f))),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(Color(0xFF00E676), Color(0xFF00B0FF)))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(name.take(2).uppercase(), color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(phone, color = Color.White.copy(alpha = 0.55f), fontSize = 11.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF00E676), Color(0xFF00B0FF))))
                            .clickable {
                                onDeductBalance(20.00)
                                onShowToast("✓ Sent $20.00 to $name via Plin!")
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("Plin $20", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6. POINTS & PROMOTIONS MODAL (Solar Flare & Gold Gradients)
// -------------------------------------------------------------
@Composable
fun PointsPromotionsModal(
    onNavigateToBonuses: () -> Unit,
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val promos = listOf(
        Triple("Starbucks Reserve", "20% OFF on cold brew & specialty beverages", "AURA-STARBUCKS"),
        Triple("Apple Online Store", "5% Instant cashback on Mac & iPhone", "AURA-APPLE5"),
        Triple("Uber One Pass", "15% discount on Uber Black and Comfort rides", "AURA-UBERVIP")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Points & Promotions", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hero Gold VIP Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFF9100), Color(0xFFFF3D00))))
                .clickable {
                    onDismiss()
                    onNavigateToBonuses()
                }
                .padding(horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Diamond, contentDescription = null, tint = Color.Black, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Open Full Bonuses & Quests Vault", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("EXCLUSIVE PARTNER OFFERS", color = Color(0xFFFFD700), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        promos.forEach { (brand, desc, code) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF261D0F), Color(0xFF181208), Color(0xFF0F0B05)))
                    )
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(Color(0xFFFFD700).copy(alpha = 0.5f), Color(0xFFFF9100).copy(alpha = 0.2f))),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(brand, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.horizontalGradient(listOf(Color(0xFFFFD700), Color(0xFFFF9100))))
                                .clickable { onShowToast("✓ Promo code $code copied!") }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(code, color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(desc, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 7. EXPERIENCES MODAL (Royal Violet & Magenta Luxury)
// -------------------------------------------------------------
@Composable
fun ExperiencesModal(
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val experiences = listOf(
        Pair("Central Tasting Menu • Virgilio Martínez", "Private table & sommelier pairing for 2 members"),
        Pair("Formula 1 Hospitality Suite", "VIP Paddock Club access with paddock walks"),
        Pair("Coldplay VIP Box at Estadio Nacional", "Complimentary lounge access & premium tickets")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Aura Experiences", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Text("Curated luxury privileges reserved for NeoBank Metal & Titanium", color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))

        experiences.forEach { (title, subtitle) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF2E103F), Color(0xFF1B0827), Color(0xFF0F0417))
                        )
                    )
                    .border(
                        1.2.dp,
                        Brush.sweepGradient(listOf(Color(0xFFFF80AB), Color(0xFF9B51E0), Color(0xFFFF007F), Color(0xFFFF80AB))),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(26.dp).clip(CircleShape).background(Color(0xFFFFD700).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(subtitle, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .clip(RoundedCornerShape(21.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFFFF80AB), Color(0xFF9B51E0))))
                            .clickable {
                                onShowToast("✓ RSVP Confirmed! Concierge will email your invitation.")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Reserve VIP Access", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 8. CARDLESS WITHDRAWAL MODAL (Neon Matrix & Emerald Glow)
// -------------------------------------------------------------
@Composable
fun CardlessWithdrawalModal(
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var generatedOtp by remember { mutableStateOf<String?>(null) }
    var selectedAmount by remember { mutableStateOf("$100") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Cardless ATM Cash", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text("Withdraw cash at any KasNet or GlobalNet ATM without your physical card", color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(18.dp))

        if (generatedOtp != null) {
            AnimatedGradientCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(Color(0xFF00E676), Color(0xFF00B0FF), Color(0xFF00E676)),
                backgroundBrush = Brush.radialGradient(listOf(Color(0xFF0F3520), Color(0xFF071C11), Color(0xFF040F09))),
                cornerRadius = 22.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ATM WITHDRAWAL CODE ($selectedAmount)", color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(generatedOtp!!, color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 6.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Valid for 15 minutes at any ATM nationwide", color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp)
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        Text("CHOOSE AMOUNT", color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("$50", "$100", "$200", "$500").forEach { amt ->
                val isSelected = selectedAmount == amt
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            if (isSelected) Brush.horizontalGradient(listOf(Color(0xFF00E676), Color(0xFF00B0FF)))
                            else Brush.horizontalGradient(listOf(Color(0xFF161A26), Color(0xFF0F121C)))
                        )
                        .border(
                            1.dp,
                            if (isSelected) Color(0xFF00E676) else Color.White.copy(alpha = 0.12f),
                            RoundedCornerShape(22.dp)
                        )
                        .clickable { selectedAmount = amt },
                    contentAlignment = Alignment.Center
                ) {
                    Text(amt, color = if (isSelected) Color.Black else Color.White, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF00E676), Color(0xFF00F2FE))))
                .clickable {
                    val code = (100000..999999).random().toString()
                    generatedOtp = "${code.take(3)} ${code.takeLast(3)}"
                    onShowToast("✓ ATM Code generated: $generatedOtp")
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Generate ATM Code", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

// -------------------------------------------------------------
// 9. SERVICE POINTS MODAL (Ocean & Midnight Cyan Gradients)
// -------------------------------------------------------------
@Composable
fun ServicePointsModal(
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val points = listOf(
        Triple("AuraNova Flagship Hub", "Av. Larco 680, Miraflores • 0.3 km", "Open until 18:00"),
        Triple("GlobalNet 24/7 ATM", "Av. Benavides 1200, Miraflores • 0.6 km", "0% Cash Withdrawal"),
        Triple("KasNet Express Point", "Calle Schell 340, Miraflores • 0.8 km", "Cash In & Out")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Service Points", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Text("Nearby partner ATMs, deposit points and private banking hubs", color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))

        points.forEach { (name, address, badge) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF132431), Color(0xFF0C1720), Color(0xFF060B10)))
                    )
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(Color(0xFF00F2FE).copy(alpha = 0.5f), Color(0xFF00E676).copy(alpha = 0.2f))),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(address, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(badge, color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFF00F2FE), Color(0xFF4FACFE))))
                            .clickable { onShowToast("Directions to $name opened in Maps") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Directions, contentDescription = "Directions", tint = Color.Black, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 10. PAYMENT AREA MODAL (Themed Service Gradients with Live Paid State)
// -------------------------------------------------------------
@Composable
fun PaymentAreaModal(
    currentBalance: Double,
    onDeductBalance: (Double) -> Unit,
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var bills = remember {
        mutableStateListOf(
            Triple("Luz del Sur (Electricity)", 45.20, false),
            Triple("Sedapal (Water Service)", 22.50, false),
            Triple("Win Fibra 500Mbps", 38.00, false),
            Triple("Calidda Natural Gas", 14.80, false)
        )
    }

    val gradients = listOf(
        Brush.linearGradient(listOf(Color(0xFF382A0B), Color(0xFF1E1706), Color(0xFF110C03))), // Electricity Amber
        Brush.linearGradient(listOf(Color(0xFF0B2F38), Color(0xFF061A1F), Color(0xFF030E12))), // Water Aqua
        Brush.linearGradient(listOf(Color(0xFF2C0B38), Color(0xFF18061F), Color(0xFF0D0312))), // Fiber Magenta
        Brush.linearGradient(listOf(Color(0xFF38150B), Color(0xFF1F0A06), Color(0xFF120503)))  // Gas Warm
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Payment Area", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Text("Pay public utilities, internet and phone bills with zero surcharge", color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))

        bills.forEachIndexed { index, (service, amount, isPaid) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(gradients[index % gradients.size])
                    .border(
                        1.dp,
                        if (isPaid) Color(0xFF00E676).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.15f),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(service, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Invoice due: $${String.format(java.util.Locale.US, "%.2f", amount)}", color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp)
                    }

                    if (isPaid) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF00E676).copy(alpha = 0.2f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("PAID ✓", color = Color(0xFF00E676), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .clickable {
                                    bills[index] = Triple(service, amount, true)
                                    onDeductBalance(amount)
                                    onShowToast("✓ Paid $${String.format(java.util.Locale.US, "%.2f", amount)} to $service!")
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text("Pay $${String.format(java.util.Locale.US, "%.2f", amount)}", color = Color(0xFF14161E), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 11. LOGOUT CONFIRM MODAL (Amber & Crimson Danger Aura)
// -------------------------------------------------------------
@Composable
fun LogoutConfirmModal(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Color(0xFFFFB300).copy(alpha = 0.35f), Color.Transparent)))
                .border(2.dp, Color(0xFFFFB300), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("Log out of AuraNova?", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "You will need to re-authenticate with your biometrics or passcode to access your accounts.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFFFF0844), Color(0xFFFF5252))))
                .clickable { onConfirmLogout() },
            contentAlignment = Alignment.Center
        ) {
            Text("Confirm Log Out", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.04f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Text("Cancel", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// Helper Gradient Setting Card
@Composable
private fun GradientSettingCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    glowColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF191D2C), Color(0xFF121420), Color(0xFF0C0E17))
                )
            )
            .border(
                1.dp,
                if (checked) glowColor.copy(alpha = 0.45f) else Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(glowColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = glowColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(subtitle, color = Color.White.copy(alpha = 0.55f), fontSize = 11.sp)
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = glowColor,
                    uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                    uncheckedTrackColor = Color.White.copy(alpha = 0.15f)
                )
            )
        }
    }
}

// Helper Gradient Limit Card
@Composable
private fun GradientLimitCard(
    title: String,
    currentValue: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    accentGradient: List<Color>,
    onValueChange: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF1A1E2D), Color(0xFF131520), Color(0xFF0D0F16))
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Brush.horizontalGradient(accentGradient))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(currentValue, color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = range,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = accentGradient.first(),
                    inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                )
            )
        }
    }
}
