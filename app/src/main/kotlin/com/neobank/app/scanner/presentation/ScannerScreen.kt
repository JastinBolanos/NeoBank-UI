package com.neobank.app.scanner.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ScannerTab(val title: String) {
    SCAN_QR("Scan QR"),
    MY_QR("My QR Code"),
    SCAN_CARD("Scan Card")
}

data class ScannedPaymentPayload(
    val recipient: String,
    val serviceType: String,
    val amount: Double,
    val avatarColor: Color,
    val reference: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    currentBalance: Double,
    onBackClick: () -> Unit,
    onPaymentSuccess: (Double) -> Unit
) {
    var selectedTab by remember { mutableStateOf(ScannerTab.SCAN_QR) }
    var isFlashOn by remember { mutableStateOf(false) }
    var pendingPayment by remember { mutableStateOf<ScannedPaymentPayload?>(null) }
    var isPaymentDone by remember { mutableStateOf(false) }
    var showCopiedToast by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Laser Animation for scanner
    val infiniteTransition = rememberInfiniteTransition(label = "laser_sweep")
    val laserPosition by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pos"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0D14))
    ) {
        // Ambient background glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (isFlashOn) Color(0xFF2A364F) else Color(0xFF14192B),
                            Color(0xFF07080E)
                        ),
                        radius = 1200f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ==========================================
            // 1. TOP CONTROLS
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back Button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = when (selectedTab) {
                        ScannerTab.SCAN_QR -> "Scan & Pay"
                        ScannerTab.MY_QR -> "Receive via QR"
                        ScannerTab.SCAN_CARD -> "Card Scanner"
                    },
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Flashlight
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isFlashOn) Color(0xFFFFD700).copy(alpha = 0.25f) else Color.White.copy(alpha = 0.12f))
                            .border(
                                1.dp,
                                if (isFlashOn) Color(0xFFFFD700) else Color.White.copy(alpha = 0.25f),
                                CircleShape
                            )
                            .clickable { isFlashOn = !isFlashOn },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            contentDescription = "Flashlight",
                            tint = if (isFlashOn) Color(0xFFFFD700) else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Gallery / File import
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                            .clickable {
                                // Simulate gallery QR code detection
                                pendingPayment = ScannedPaymentPayload(
                                    recipient = "Starbucks Reserve",
                                    serviceType = "Point of Sale (POS)",
                                    amount = 8.50,
                                    avatarColor = Color(0xFF00704A),
                                    reference = "AURA-QR-98213"
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Import from gallery",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ==========================================
            // 2. CENTRAL VIEWPORT AREA
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (selectedTab) {
                    ScannerTab.SCAN_QR -> {
                        ScannerTargetView(
                            laserProgress = laserPosition,
                            isFlashOn = isFlashOn,
                            onScanPreset = { payload -> pendingPayment = payload }
                        )
                    }
                    ScannerTab.MY_QR -> {
                        MyQrCodeCard(
                            onCopy = {
                                showCopiedToast = true
                                coroutineScope.launch {
                                    delay(2000)
                                    showCopiedToast = false
                                }
                            }
                        )
                    }
                    ScannerTab.SCAN_CARD -> {
                        CardScannerTargetView(
                            onCardScanned = {
                                pendingPayment = ScannedPaymentPayload(
                                    recipient = "Saurabh Kumar",
                                    serviceType = "Add New Card to AuraNova",
                                    amount = 0.00,
                                    avatarColor = Color(0xFF6B11FF),
                                    reference = "CARD-••••-5412"
                                )
                            }
                        )
                    }
                }
            }

            // ==========================================
            // 3. BOTTOM TAB SELECTOR
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(32.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ScannerTab.values().forEach { tab ->
                            val isSelected = selectedTab == tab
                            val bgColor by animateColorAsState(
                                targetValue = if (isSelected) Color.White else Color.Transparent,
                                animationSpec = tween(200),
                                label = "tabBg"
                            )
                            val textColor by animateColorAsState(
                                targetValue = if (isSelected) Color.Black else Color.White.copy(alpha = 0.7f),
                                animationSpec = tween(200),
                                label = "tabText"
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(bgColor)
                                    .clickable { selectedTab = tab },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = when (tab) {
                                            ScannerTab.SCAN_QR -> Icons.Default.QrCodeScanner
                                            ScannerTab.MY_QR -> Icons.Default.QrCode
                                            ScannerTab.SCAN_CARD -> Icons.Default.CreditCard
                                        },
                                        contentDescription = null,
                                        tint = textColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = tab.title,
                                        color = textColor,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // TOAST FEEDBACK
        // ==========================================
        AnimatedVisibility(
            visible = showCopiedToast,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF00E676))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "✓ Alias & QR copied to clipboard",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ==========================================
        // 4. PAYMENT / CONFIRMATION BOTTOM SHEET
        // ==========================================
        if (pendingPayment != null) {
            val payload = pendingPayment!!
            ModalBottomSheet(
                onDismissRequest = {
                    if (!isPaymentDone) pendingPayment = null
                },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = Color(0xFF14161E),
                contentColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isPaymentDone) {
                        // Success View
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00E676).copy(alpha = 0.2f))
                                .border(2.dp, Color(0xFF00E676), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Done",
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (payload.amount > 0) "Payment Successful!" else "Card Added!",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (payload.amount > 0) "Transferred $${String.format(java.util.Locale.US, "%.2f", payload.amount)} to ${payload.recipient}" else "${payload.recipient}'s card registered",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .clickable {
                                    val amountPaid = payload.amount
                                    pendingPayment = null
                                    isPaymentDone = false
                                    onPaymentSuccess(amountPaid)
                                    onBackClick()
                                },
                            color = Color.White,
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Back to Home",
                                    color = Color(0xFF14161E),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        // Confirmation View
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(payload.avatarColor.copy(alpha = 0.25f))
                                .border(1.5.dp, payload.avatarColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = payload.recipient.take(2).uppercase(),
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = payload.recipient,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = payload.serviceType,
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 13.sp
                        )

                        if (payload.amount > 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "$${String.format(java.util.Locale.US, "%.2f", payload.amount)}",
                                color = Color.White,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Zero transaction fee • AuraPay",
                                color = Color(0xFF00E676),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Details Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color.White.copy(alpha = 0.06f))
                                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Paying from", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
                                    Text(text = "Main Balance ($${String.format(java.util.Locale.US, "%,.2f", currentBalance)})", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Reference ID", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
                                    Text(text = payload.reference, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Pay Button
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clip(RoundedCornerShape(26.dp))
                                .clickable { isPaymentDone = true },
                            color = Color.White,
                            shape = RoundedCornerShape(26.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (payload.amount > 0) "Confirm & Pay $${String.format(java.util.Locale.US, "%.2f", payload.amount)}" else "Confirm Registration",
                                    color = Color(0xFF14161E),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(23.dp))
                                .background(Color.White.copy(alpha = 0.04f))
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(23.dp)
                                )
                                .clickable { pendingPayment = null },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Cancel", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScannerTargetView(
    laserProgress: Float,
    isFlashOn: Boolean,
    onScanPreset: (ScannedPaymentPayload) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Target Box
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
        ) {
            // Viewfinder reticle
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cornerLen = 38f
                val strokeW = 4.dp.toPx()
                val cornerRadius = 24.dp.toPx()

                // Corner brackets
                drawRoundRect(
                    color = Color(0xFF00F2FE),
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Stroke(
                        width = strokeW,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(cornerLen, size.width / 2f - cornerLen), 0f)
                    )
                )

                // Laser Beam
                val yPos = size.height * laserProgress
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF00F2FE),
                            Color(0xFFFF0844),
                            Color(0xFF00F2FE),
                            Color.Transparent
                        )
                    ),
                    start = Offset(20f, yPos),
                    end = Offset(size.width - 20f, yPos),
                    strokeWidth = 3.dp.toPx()
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "AuraPay Scanner",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 12.sp,
                    letterSpacing = 2.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Point camera at any QR, Plin, or Yape code",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Preset Quick Scan Trays
        Text(
            text = "DEMO SCAN PRESETS",
            color = Color(0xFF00F2FE),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickScanButton(
                label = "Starbucks ($8.50)",
                modifier = Modifier.weight(1f)
            ) {
                onScanPreset(
                    ScannedPaymentPayload(
                        recipient = "Starbucks Reserve",
                        serviceType = "Point of Sale QR",
                        amount = 8.50,
                        avatarColor = Color(0xFF00704A),
                        reference = "STB-LIM-4891"
                    )
                )
            }

            QuickScanButton(
                label = "Elena Cruz ($45.00)",
                modifier = Modifier.weight(1f)
            ) {
                onScanPreset(
                    ScannedPaymentPayload(
                        recipient = "Elena Cruz",
                        serviceType = "Plin Instant Contact",
                        amount = 45.00,
                        avatarColor = Color(0xFFE94D4D),
                        reference = "PLIN-984210"
                    )
                )
            }

            QuickScanButton(
                label = "Uber ($14.20)",
                modifier = Modifier.weight(1f)
            ) {
                onScanPreset(
                    ScannedPaymentPayload(
                        recipient = "Uber Trip",
                        serviceType = "Ride Payment QR",
                        amount = 14.20,
                        avatarColor = Color(0xFF1E1E1E),
                        reference = "UBR-772911"
                    )
                )
            }
        }
    }
}

@Composable
private fun QuickScanButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun MyQrCodeCard(
    onCopy: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AuraNova Pay",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF00E676).copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PLIN READY",
                            color = Color(0xFF008940),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // High Fidelity Simulated QR Code
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val gridSize = 19
                        val cellSize = size.width / gridSize
                        val randomGen = java.util.Random(42)

                        for (i in 0 until gridSize) {
                            for (j in 0 until gridSize) {
                                // Corners anchors
                                val isCorner = (i < 5 && j < 5) || (i > gridSize - 6 && j < 5) || (i < 5 && j > gridSize - 6)
                                val isCornerCenter = (i in 1..3 && j in 1..3) || (i in gridSize - 5..gridSize - 2 && j in 1..3) || (i in 1..3 && j in gridSize - 5..gridSize - 2)
                                val isCornerRing = isCorner && (i == 0 || i == 4 || j == 0 || j == 4 || i == gridSize - 5 || i == gridSize - 1 || j == gridSize - 5 || j == gridSize - 1)

                                val shouldDraw = if (isCorner) {
                                    isCornerRing || isCornerCenter
                                } else {
                                    randomGen.nextFloat() > 0.52f
                                }

                                if (shouldDraw) {
                                    drawRect(
                                        color = Color.Black,
                                        topLeft = Offset(i * cellSize, j * cellSize),
                                        size = Size(cellSize - 0.5f, cellSize - 0.5f)
                                    )
                                }
                            }
                        }
                    }

                    // Center Logo Badge
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✦", color = Color(0xFF00F2FE), fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Saurabh Kumar",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Alias: aura.saurabh • @kumar10",
                    color = Color.Black.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Copy and Share Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { onCopy() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Copy Alias", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onCopy() },
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color(0xFF14161E), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Share QR", color = Color(0xFF14161E), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CardScannerTargetView(
    onCardScanned: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(320.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = 0.45f))
                .border(2.dp, Color(0xFF4FACFE), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp, 26.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFFD700).copy(alpha = 0.8f))
                    )
                    Text(
                        text = "DEBIT / CREDIT",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "•••• •••• •••• ••••",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 20.sp,
                    letterSpacing = 4.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "CARDHOLDER NAME", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                    Text(text = "MM/YY", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Fit your physical card inside the border",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { onCardScanned() }
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚡ Simulate Instant Card OCR Scan",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
