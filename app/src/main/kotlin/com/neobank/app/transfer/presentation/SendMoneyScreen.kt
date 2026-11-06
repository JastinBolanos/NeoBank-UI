package com.neobank.app.transfer.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R
import com.neobank.app.transfer.presentation.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class TransferRecipient(
    val id: String,
    val name: String,
    val handle: String,
    val initial: String,
    val gradientColors: List<Color>,
    val verified: Boolean = true
)

enum class TransferStep {
    Draft,
    Processing,
    Success
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreen(
    currentBalance: Double,
    onSendMoney: (Double) -> Unit,
    onBackClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var amount by remember { mutableStateOf("0") }
    var selectedNote by remember { mutableStateOf("⚡ Aura Instant Transfer") }
    val maxLimit = 25000.0

    // Sample accounts
    val accounts = remember(currentBalance) {
        listOf(
            TransferAccount("main", "Main Vault", "•••• 4092", currentBalance, Color(0xFF00F5FF)),
            TransferAccount("stash", "High-Yield Stash", "•••• 9814", 12450.00, Color(0xFF8B5CF6)),
            TransferAccount("reserve", "Reserve Stash", "•••• 1102", 8920.00, Color(0xFF10B981))
        )
    }
    var selectedAccount by remember { mutableStateOf(accounts[0]) }

    // Sample VIP recipients
    var recipients by remember {
        mutableStateOf(
            listOf(
                TransferRecipient("1", "Sarah Miller", "@sarahm", "S", listOf(Color(0xFF6B11FF), Color(0xFF00F5FF))),
                TransferRecipient("2", "Elena Cruz", "@elenac", "E", listOf(Color(0xFFFF2A85), Color(0xFFFF7300))),
                TransferRecipient("3", "David Chen", "@dchen", "D", listOf(Color(0xFF00E676), Color(0xFF00B0FF))),
                TransferRecipient("4", "Michael Scott", "@mscott", "M", listOf(Color(0xFFFFB300), Color(0xFFF06292))),
                TransferRecipient("5", "Sofia Alvarez", "@sofiaa", "S", listOf(Color(0xFF8E24AA), Color(0xFF40C4FF)))
            )
        )
    }
    var selectedRecipient by remember { mutableStateOf(recipients[0]) }
    var showAddRecipientModal by remember { mutableStateOf(false) }

    // Transfer execution state
    var transferStep by remember { mutableStateOf(TransferStep.Draft) }
    var transactionRef by remember { mutableStateOf("") }

    val numericAmount = amount.toDoubleOrNull() ?: 0.0
    val isAmountValid = numericAmount > 0.0 && numericAmount <= selectedAccount.balance && numericAmount <= maxLimit
    val isOverBalance = numericAmount > selectedAccount.balance

    // Dynamic Font Sizing for Amount
    val amountFontSize = when {
        amount.length <= 4 -> 54.sp
        amount.length <= 6 -> 42.sp
        else -> 32.sp
    }

    // Cursor pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "cursorBlink")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    // Entrance animation
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Luxury Neobank Mesh Background
        Image(
            painter = painterResource(R.drawable.bg_neobank),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 30.dp)
        )

        // Subtle glowing aurora gradients
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4F46E5).copy(alpha = 0.22f),
                            Color(0xFF000000).copy(alpha = 0.65f),
                            Color(0xFF0A0C14).copy(alpha = 0.95f)
                        )
                    )
                )
        )

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { 40 }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ==========================================
                // 1. TOP BAR (Executive & Balanced)
                // ==========================================
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back_custom),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Aura Transfer",
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 1.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981))
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "AuraFast Network • 256-Bit Encrypted",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Security",
                            tint = Color(0xFF00F5FF),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // ==========================================
                // 2. RECIPIENT CAROUSEL (Glowing Avatars)
                // ==========================================
                Text(
                    text = "RECIPIENT",
                    color = Color.White.copy(alpha = 0.45f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 8.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    // + New Recipient Chip
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(68.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { showAddRecipientModal = true }
                                .padding(vertical = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(62.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.05f))
                                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add Recipient",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "New",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // VIP Recipients
                    items(recipients) { recipient ->
                        val isSelected = recipient.id == selectedRecipient.id
                        val avatarSize by animateDpAsState(
                            targetValue = if (isSelected) 56.dp else 50.dp,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "avatarSize"
                        )
                        val borderWidth by animateDpAsState(
                            targetValue = if (isSelected) 2.5.dp else 1.dp,
                            label = "borderWidth"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFF00F5FF) else Color.White.copy(alpha = 0.18f),
                            label = "borderColor"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(68.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { selectedRecipient = recipient }
                                .padding(vertical = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(62.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // 1. Avatar Circle (Always 100% circular, never clipped into a square)
                                Box(
                                    modifier = Modifier
                                        .size(avatarSize)
                                        .clip(CircleShape)
                                        .background(Brush.sweepGradient(recipient.gradientColors))
                                        .border(borderWidth, borderColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = recipient.initial,
                                        color = Color.White,
                                        fontSize = if (isSelected) 20.sp else 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // 2. Floating Verified Badge (Clean circle floating on bottom-right)
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .offset(x = (-1).dp, y = (-1).dp)
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF00F5FF))
                                            .border(2.dp, Color(0xFF0A0C14), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Active",
                                            tint = Color.Black,
                                            modifier = Modifier.size(11.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = recipient.name.split(" ").first(),
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ==========================================
                // 3. HERO AMOUNT DISPLAY (Dynamic & Interactive)
                // ==========================================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Currency + Digit display
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$",
                            color = Color(0xFF00F5FF),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp, end = 4.dp)
                        )

                        Text(
                            text = amount,
                            color = if (isOverBalance) Color(0xFFFF6B6B) else Color.White,
                            fontSize = amountFontSize,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )

                        // Pulsing neon cursor
                        Box(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(width = 3.dp, height = 38.dp)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(Color(0xFF00F5FF).copy(alpha = cursorAlpha))
                        )
                    }

                    // Balance status pill / warning
                    AnimatedContent(
                        targetState = isOverBalance,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "balanceNotice"
                    ) { overLimit ->
                        if (overLimit) {
                            Row(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFF3366).copy(alpha = 0.2f))
                                    .border(1.dp, Color(0xFFFF3366).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Exceeds available balance ($%,.2f)".format(selectedAccount.balance),
                                    color = Color(0xFFFF8595),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            Text(
                                text = "Zero transfer fees • Instant settlement",
                                color = Color.White.copy(alpha = 0.55f),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Preset Chips (+$25, +$50, +$100, +$250, MAX)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(25, 50, 100, 250).forEach { preset ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.06f))
                                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                                    .clickable {
                                        val currentNum = amount.toDoubleOrNull() ?: 0.0
                                        val nextNum = if (amount == "0") preset.toDouble() else currentNum + preset
                                        if (nextNum <= maxLimit) {
                                            amount = if (nextNum % 1.0 == 0.0) nextNum.toInt().toString() else "%.2f".format(nextNum)
                                        }
                                    }
                                    .padding(horizontal = 11.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "+$$preset",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // MAX Chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF6366F1).copy(alpha = 0.25f))
                                .border(1.dp, Color(0xFF818CF8).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                .clickable {
                                    val balanceToSet = selectedAccount.balance
                                    amount = if (balanceToSet % 1.0 == 0.0) balanceToSet.toInt().toString() else "%.2f".format(balanceToSet)
                                }
                                .padding(horizontal = 11.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "MAX",
                                color = Color(0xFFC7D2FE),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Account Selector & Note pill in a streamlined layout (no redundant boxes)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AccountSelector(
                            selectedAccount = selectedAccount,
                            accounts = accounts,
                            onSelectAccount = { selectedAccount = it }
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        NoteField(
                            noteText = selectedNote,
                            onNoteChange = { selectedNote = it }
                        )
                    }
                }

                // ==========================================
                // 4. KEYPAD (Tactile & Refined)
                // ==========================================
                Keypad(
                    onNumberClick = { number ->
                        val newAmount = if (amount == "0") number else amount + number
                        if ((newAmount.toDoubleOrNull() ?: 0.0) <= maxLimit) {
                            amount = newAmount
                        }
                    },
                    onDotClick = {
                        if (!amount.contains(".")) {
                            amount = "$amount."
                        }
                    },
                    onDeleteClick = {
                        amount = if (amount.length > 1) amount.dropLast(1) else "0"
                    },
                    onClearAll = {
                        amount = "0"
                    },
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // ==========================================
                // 5. PRIMARY CTA BUTTON (Luxe Send Button)
                // ==========================================
                val ctaGradient = if (isAmountValid) {
                    Brush.horizontalGradient(
                        listOf(Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFF00F5FF))
                    )
                } else {
                    Brush.horizontalGradient(
                        listOf(Color.White.copy(alpha = 0.12f), Color.White.copy(alpha = 0.08f))
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(ctaGradient)
                        .clickable(enabled = isAmountValid && transferStep == TransferStep.Draft) {
                            coroutineScope.launch {
                                transferStep = TransferStep.Processing
                                delay(1200) // Simulated secure biometric/network handshake
                                transactionRef = "AUR-${System.currentTimeMillis() % 1000000}"
                                transferStep = TransferStep.Success
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (transferStep == TransferStep.Processing) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.5.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Authorizing AuraFast...",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (numericAmount > 0.0) {
                                    "Send $%,.2f to %s".format(numericAmount, selectedRecipient.name.split(" ").first())
                                } else {
                                    "Enter Amount"
                                },
                                color = if (isAmountValid) Color.White else Color.White.copy(alpha = 0.45f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // 6. SUCCESS CELEBRATION OVERLAY (Executive Motion)
        // ==========================================
        if (transferStep == TransferStep.Success) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.88f))
                    .blur(radius = 10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Expanding neon checkmark badge
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFF10B981).copy(alpha = 0.35f), Color.Transparent)
                            )
                        )
                        .border(2.dp, Color(0xFF10B981), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Transfer Successful!",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Funds delivered instantly via AuraFast",
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Receipt Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF131625))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$%,.2f".format(numericAmount),
                        color = Color(0xFF00F5FF),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = selectedNote,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                    )

                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 0.8.dp)

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Recipient", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                        Text(text = "${selectedRecipient.name} (${selectedRecipient.handle})", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Debit Account", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                        Text(text = selectedAccount.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Transaction Ref", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                        Text(text = transactionRef, color = Color(0xFFC7D2FE), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "New Vault Balance", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                        val newBal = (selectedAccount.balance - numericAmount).coerceAtLeast(0.0)
                        Text(text = "$%,.2f".format(newBal), color = Color(0xFF10B981), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Done Button
                Button(
                    onClick = {
                        onSendMoney(numericAmount)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text(
                        text = "Back to Home",
                        color = Color(0xFF0F111D),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ==========================================
        // 7. ADD RECIPIENT BOTTOM SHEET
        // ==========================================
        if (showAddRecipientModal) {
            var newName by remember { mutableStateOf("") }
            var newHandle by remember { mutableStateOf("@") }

            ModalBottomSheet(
                onDismissRequest = { showAddRecipientModal = false },
                containerColor = Color(0xFF121422),
                contentColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 36.dp)
                ) {
                    Text(
                        text = "Add New Recipient",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Send instantly to any AuraNova user or handle",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 2.dp, bottom = 20.dp)
                    )

                    // Recipient Name
                    Text(
                        text = "FULL NAME",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, bottom = 14.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        BasicTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            textStyle = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium),
                            cursorBrush = SolidColor(Color(0xFF00F5FF)),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { inner ->
                                if (newName.isEmpty()) {
                                    Text("e.g. Alex Morgan", color = Color.White.copy(alpha = 0.4f), fontSize = 15.sp)
                                }
                                inner()
                            }
                        )
                    }

                    // Recipient Handle / Tag
                    Text(
                        text = "USERNAME OR CASHTAG",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, bottom = 22.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        BasicTextField(
                            value = newHandle,
                            onValueChange = { newHandle = it },
                            textStyle = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium),
                            cursorBrush = SolidColor(Color(0xFF00F5FF)),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(
                        onClick = {
                            if (newName.isNotBlank()) {
                                val cleanHandle = if (newHandle.startsWith("@")) newHandle else "@$newHandle"
                                val newRecipient = TransferRecipient(
                                    id = System.currentTimeMillis().toString(),
                                    name = newName.trim(),
                                    handle = cleanHandle.trim(),
                                    initial = newName.trim().take(1).uppercase(),
                                    gradientColors = listOf(Color(0xFF00F5FF), Color(0xFF6366F1))
                                )
                                recipients = listOf(newRecipient) + recipients
                                selectedRecipient = newRecipient
                                showAddRecipientModal = false
                            }
                        },
                        enabled = newName.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6366F1),
                            disabledContainerColor = Color.White.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text(
                            text = "Add Recipient",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}