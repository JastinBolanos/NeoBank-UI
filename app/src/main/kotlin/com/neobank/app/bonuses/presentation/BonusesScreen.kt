package com.neobank.app.bonuses.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R
import com.neobank.app.core.navigation.KmpBackHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class BonusTray(val title: String, val icon: ImageVector) {
    BOOSTERS("Boosters", Icons.Default.TrendingUp),
    QUESTS("Quests", Icons.Default.MilitaryTech),
    MILES("Miles & Points", Icons.Default.Flight),
    REFERRALS("Referrals", Icons.Default.PersonAdd)
}

data class BoosterCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val multiplier: String,
    val icon: ImageVector,
    val color: Color,
    var isEnabled: Boolean
)

data class BankingQuest(
    val id: String,
    val title: String,
    val description: String,
    val reward: String,
    val currentProgress: Int,
    val targetProgress: Int,
    val isCompleted: Boolean,
    var isClaimed: Boolean = false
)

data class AirlinePartner(
    val name: String,
    val alliance: String,
    val conversionRate: String,
    val logoText: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonusesScreen(
    currentBalance: Double,
    onBackClick: () -> Unit,
    onRedeemBonus: (Double) -> Unit
) {
    KmpBackHandler { onBackClick() }

    var selectedTray by remember { mutableStateOf(BonusTray.BOOSTERS) }
    var availableCashback by remember { mutableDoubleStateOf(485.60) }
    var auraPoints by remember { mutableIntStateOf(32400) }
    var showRedeemSheet by remember { mutableStateOf(false) }
    var showToastMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun showToast(msg: String) {
        showToastMessage = msg
        coroutineScope.launch {
            delay(2200)
            showToastMessage = null
        }
    }

    // Boosters Data
    val boosters = remember {
        mutableStateListOf(
            BoosterCategory("1", "Travel & Airlines", "AuraNova Metal 5x on flights & stays", "5.0% Cashback", Icons.Default.Flight, Color(0xFF00F2FE), true),
            BoosterCategory("2", "Fine Dining & Bars", "Michelin & curated restaurants", "4.0% Cashback", Icons.Default.Restaurant, Color(0xFFFF80AB), true),
            BoosterCategory("3", "Vault Crypto Yield", "Extra APY on fixed yield deposits", "+1.8% APY Boost", Icons.Default.TrendingUp, Color(0xFF00E676), true),
            BoosterCategory("4", "Tech & Electronics", "Apple Store, BestBuy & gadgets", "3.5% Cashback", Icons.Default.Workspaces, Color(0xFFB388FF), false),
            BoosterCategory("5", "EV Charging & Fuel", "Tesla Superchargers & Repsol", "2.5% Cashback", Icons.Default.LocalGasStation, Color(0xFFFFD700), false),
            BoosterCategory("6", "Gourmet Groceries", "Supermarkets & organic markets", "2.0% Cashback", Icons.Default.ShoppingCart, Color(0xFFFF5252), false)
        )
    }

    // Quests Data
    val quests = remember {
        mutableStateListOf(
            BankingQuest(
                id = "q1",
                title = "Contactless Master",
                description = "Complete 15 tap-to-pay transactions with your Titanium card.",
                reward = "+2,500 AuraPoints",
                currentProgress = 15,
                targetProgress = 15,
                isCompleted = true,
                isClaimed = false
            ),
            BankingQuest(
                id = "q2",
                title = "High-Yield Vault Saver",
                description = "Maintain at least $10,000 in your Vault Pot for 30 consecutive days.",
                reward = "+$150.00 Cash Bonus",
                currentProgress = 24,
                targetProgress = 30,
                isCompleted = false
            ),
            BankingQuest(
                id = "q3",
                title = "Global FX Explorer",
                description = "Make purchases in 2 different foreign currencies with 0% exchange fees.",
                reward = "+$50.00 FX Rebate",
                currentProgress = 1,
                targetProgress = 2,
                isCompleted = false
            ),
            BankingQuest(
                id = "q4",
                title = "Payroll Direct Deposit",
                description = "Link your monthly salary compensation to activate the 1.5x Multiplier.",
                reward = "+5,000 Points & Tier UP",
                currentProgress = 2,
                targetProgress = 2,
                isCompleted = true,
                isClaimed = true
            )
        )
    }

    // Airline Partners
    val airlines = remember {
        listOf(
            AirlinePartner("Delta SkyMiles", "SkyTeam Alliance", "1 pt = 1.25 miles", "DELTA", Color(0xFFE51937)),
            AirlinePartner("LATAM Pass", "South America Premier", "1 pt = 1.30 km", "LATAM", Color(0xFF101865)),
            AirlinePartner("Iberia Plus", "oneworld Alliance", "1 pt = 1.20 Avios", "IBERIA", Color(0xFFD3002D)),
            AirlinePartner("Emirates Skywards", "Global Luxury Carrier", "1 pt = 1.15 miles", "EMIRATES", Color(0xFFC7082A))
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Deep background
        Image(
            painter = painterResource(R.drawable.bg_neobank),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // --- TOP HEADER ---
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
                        .background(Color.Black.copy(alpha = 0.4f))
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

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "My Bonuses",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Titanium Elite Tier",
                        color = Color(0xFF00F2FE),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }

                // Help/Info icon pill
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .clickable {
                            showToast("Your bonuses accrue daily at 00:00 UTC")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Paid,
                        contentDescription = "Rewards Info",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // --- SCROLLABLE CONTENT ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // 1. HERO VAULT CARD
                item {
                    HeroBonusVault(
                        cashback = availableCashback,
                        points = auraPoints,
                        onRedeemClick = { showRedeemSheet = true }
                    )
                }

                // 2. TIER STATUS & PROGRESSION
                item {
                    TierProgressionCard(
                        currentSpend = 12450.00,
                        targetSpend = 15000.00,
                        nextTier = "Aura Diamond Reserve"
                    )
                }

                // 3. TRAY TABS
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "BONUS TRAY EXPLORER",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(BonusTray.values()) { tray ->
                            val isSelected = selectedTray == tray
                            val bgColor by animateColorAsState(
                                targetValue = if (isSelected) Color.White else Color.Black.copy(alpha = 0.45f),
                                animationSpec = tween(200),
                                label = "trayBg"
                            )
                            val textColor by animateColorAsState(
                                targetValue = if (isSelected) Color.Black else Color.White.copy(alpha = 0.8f),
                                animationSpec = tween(200),
                                label = "trayText"
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(bgColor)
                                    .border(
                                        1.dp,
                                        if (isSelected) Color.White else Color.White.copy(alpha = 0.15f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { selectedTray = tray }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = tray.icon,
                                        contentDescription = null,
                                        tint = textColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = tray.title,
                                        color = textColor,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // 4. ACTIVE TRAY CONTENT
                when (selectedTray) {
                    BonusTray.BOOSTERS -> {
                        item {
                            BoostersTrayView(
                                boosters = boosters,
                                onToggle = { id ->
                                    val index = boosters.indexOfFirst { it.id == id }
                                    if (index != -1) {
                                        val item = boosters[index]
                                        boosters[index] = item.copy(isEnabled = !item.isEnabled)
                                        showToast(if (!item.isEnabled) "Booster activated!" else "Booster paused")
                                    }
                                }
                            )
                        }
                    }
                    BonusTray.QUESTS -> {
                        item {
                            QuestsTrayView(
                                quests = quests,
                                onClaim = { qId ->
                                    val index = quests.indexOfFirst { it.id == qId }
                                    if (index != -1) {
                                        val q = quests[index]
                                        if (q.isCompleted && !q.isClaimed) {
                                            quests[index] = q.copy(isClaimed = true)
                                            if (q.reward.contains("Points")) {
                                                auraPoints += 2500
                                                showToast("✓ Claimed +2,500 AuraPoints!")
                                            } else {
                                                availableCashback += 150.00
                                                showToast("✓ Claimed +$150.00 Cash Bonus!")
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                    BonusTray.MILES -> {
                        item {
                            MilesAndPointsTrayView(
                                currentPoints = auraPoints,
                                airlines = airlines,
                                onConvertPoints = { ptsToConvert ->
                                    if (auraPoints >= ptsToConvert) {
                                        val cashEquivalent = ptsToConvert / 100.0
                                        auraPoints -= ptsToConvert
                                        availableCashback += cashEquivalent
                                        showToast("✓ Converted $ptsToConvert pts to +$${String.format(java.util.Locale.US, "%.2f", cashEquivalent)} cash!")
                                    }
                                },
                                onTransferAirlines = { airline ->
                                    showToast("Transferred 5,000 pts to ${airline.name}")
                                }
                            )
                        }
                    }
                    BonusTray.REFERRALS -> {
                        item {
                            ReferralTrayView(
                                onCopyCode = { showToast("✓ Referral code copied: AURA-TITANIUM-789") },
                                onShare = { showToast("Referral link prepared for sharing!") }
                            )
                        }
                    }
                }

                // 5. RECENT REWARDS LEDGER
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "RECENT BONUS CREDITS",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                    )
                }

                item {
                    RecentBonusLedgerSection()
                }
            }
        }

        // --- FLOATING FEEDBACK TOAST ---
        AnimatedVisibility(
            visible = showToastMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) {
            if (showToastMessage != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF00E676))
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = showToastMessage!!,
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // --- CASH OUT MODAL BOTTOM SHEET ---
        if (showRedeemSheet) {
            ModalBottomSheet(
                onDismissRequest = { showRedeemSheet = false },
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
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676).copy(alpha = 0.2f))
                            .border(1.5.dp, Color(0xFF00E676), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Paid,
                            contentDescription = null,
                            tint = Color(0xFF00E676),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Cash Out to Main Balance",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Instant credit with zero fees directly to your checkings balance",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Redemption Options
                    val redeemOptions = listOf(
                        50.00,
                        100.00,
                        availableCashback
                    )

                    redeemOptions.forEach { amount ->
                        val isAll = amount == availableCashback
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .height(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, if (isAll) Color(0xFF00E676) else Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                                .clickable {
                                    if (availableCashback >= amount) {
                                        availableCashback -= amount
                                        onRedeemBonus(amount)
                                        showRedeemSheet = false
                                        showToast("✓ Deposited $${String.format(java.util.Locale.US, "%.2f", amount)} into your Main Balance!")
                                    }
                                },
                            color = if (isAll) Color(0xFF00E676).copy(alpha = 0.15f) else Color.White.copy(alpha = 0.06f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isAll) "All Available ($${"%.2f".format(amount)})" else "Cash Out $${amount.toInt()}.00",
                                    color = if (isAll) Color(0xFF00E676) else Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Instant Transfer →",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

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
                            .clickable { showRedeemSheet = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Cancel", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// =========================================================================
// 1. HERO VAULT COMPONENT
// =========================================================================
@Composable
private fun HeroBonusVault(
    cashback: Double,
    points: Int,
    onRedeemClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF1F2438),
                        Color(0xFF141624),
                        Color(0xFF0C0E16)
                    )
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF00F2FE).copy(alpha = 0.6f),
                        Color(0xFF9B51E0).copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.1f)
                    )
                ),
                RoundedCornerShape(28.dp)
            )
            .padding(22.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00F2FE).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = null,
                            tint = Color(0xFF00F2FE),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "TOTAL REDEEMABLE VAULT",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF00E676).copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "READY TO CASH",
                        color = Color(0xFF00E676),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "$${"%.2f".format(cashback)}",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Liquid Cashback Balance",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "%,d".format(points),
                        color = Color(0xFF00F2FE),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AuraPoints Available",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Cash out button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onRedeemClick() },
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Paid,
                        contentDescription = null,
                        tint = Color(0xFF14161E),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cash Out to Main Account",
                        color = Color(0xFF14161E),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// =========================================================================
// 2. TIER PROGRESSION COMPONENT
// =========================================================================
@Composable
private fun TierProgressionCard(
    currentSpend: Double,
    targetSpend: Double,
    nextTier: String
) {
    val progressRatio = (currentSpend / targetSpend).toFloat().coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressRatio,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "tierProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.4f))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Next Tier: $nextTier",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Unlock 6.5% Universal Cashback & Lounge Key",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                    }
                }

                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    color = Color(0xFFFFD700),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF00F2FE),
                                    Color(0xFFFFD700)
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$${"%,d".format(currentSpend.toInt())} spent",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
                Text(
                    text = "$${"%,d".format((targetSpend - currentSpend).toInt())} to upgrade",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

// =========================================================================
// 3. TRAY 1: BOOSTERS VIEW
// =========================================================================
@Composable
private fun BoostersTrayView(
    boosters: List<BoosterCategory>,
    onToggle: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.Black.copy(alpha = 0.42f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(22.dp))
    ) {
        Column {
            boosters.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(item.color.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = item.color,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.title,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.multiplier,
                                color = item.color,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.subtitle,
                            color = Color.White.copy(alpha = 0.55f),
                            fontSize = 11.5.sp
                        )
                    }

                    Switch(
                        checked = item.isEnabled,
                        onCheckedChange = { onToggle(item.id) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = item.color,
                            uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                            uncheckedTrackColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                }

                if (index < boosters.size - 1) {
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.06f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(start = 72.dp, end = 16.dp)
                    )
                }
            }
        }
    }
}

// =========================================================================
// 4. TRAY 2: QUESTS & CHALLENGES VIEW
// =========================================================================
@Composable
private fun QuestsTrayView(
    quests: List<BankingQuest>,
    onClaim: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.Black.copy(alpha = 0.4f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(22.dp))
    ) {
        Column {
            quests.forEachIndexed { index, quest ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = quest.title,
                            color = Color.White,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = quest.reward,
                            color = if (quest.reward.contains("$")) Color(0xFF00E676) else Color(0xFF00F2FE),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = quest.description,
                        color = Color.White.copy(alpha = 0.65f),
                        fontSize = 11.5.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress bar or Claim Action
                    if (quest.isCompleted) {
                        if (quest.isClaimed) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF00E676),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Reward Claimed & Added to Balance",
                                    color = Color(0xFF00E676),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { onClaim(quest.id) },
                                color = Color(0xFF00E676),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "⚡ Claim ${quest.reward}",
                                        color = Color.Black,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    } else {
                        // Progress bar
                        val progressFraction = (quest.currentProgress.toFloat() / quest.targetProgress.toFloat()).coerceIn(0f, 1f)
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Progress",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "${quest.currentProgress} / ${quest.targetProgress}",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.1f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(progressFraction)
                                        .clip(CircleShape)
                                        .background(Color(0xFF00F2FE))
                                )
                            }
                        }
                    }
                }

                if (index < quests.size - 1) {
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.06f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

// =========================================================================
// 5. TRAY 3: MILES & POINTS EXCHANGE
// =========================================================================
@Composable
private fun MilesAndPointsTrayView(
    currentPoints: Int,
    airlines: List<AirlinePartner>,
    onConvertPoints: (Int) -> Unit,
    onTransferAirlines: (AirlinePartner) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Converter Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .border(1.dp, Color(0xFF00F2FE).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column {
                Text(
                    text = "AuraPoints Instant Exchange",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Convert 5,000 points into $50.00 cash or airline miles",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(22.dp)
                            )
                            .clickable { onConvertPoints(5000) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "5,000 pts → $50 Cash", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .clickable { onConvertPoints(10000) },
                        color = Color.White,
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "10,000 pts → $100 Cash", color = Color(0xFF14161E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Text(
            text = "PARTNER AIRLINES & ALLIANCES",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        airlines.forEach { airline ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(18.dp))
                    .clickable { onTransferAirlines(airline) }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(airline.color.copy(alpha = 0.2f))
                                .border(1.dp, airline.color.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = airline.logoText.take(2),
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = airline.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = airline.alliance,
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = airline.conversionRate,
                            color = Color(0xFF00F2FE),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Transfer Miles →",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 6. TRAY 4: REFERRAL WEALTH NETWORK
// =========================================================================
@Composable
private fun ReferralTrayView(
    onCopyCode: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VIP Wealth Referral",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF00E676).copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$50 PER FRIEND",
                            color = Color(0xFF008940),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Give your friends $25 on their first deposit and earn $50 instant cash directly into your checkings account.",
                    color = Color.Black.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Referral Code Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF1F3F8))
                        .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "YOUR EXCLUSIVE CODE", color = Color.Black.copy(alpha = 0.45f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(text = "AURA-TITANIUM-789", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onCopyCode() },
                            color = Color.Black,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Copy", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .clickable { onShare() },
                    color = Color(0xFF14161E),
                    shape = RoundedCornerShape(23.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Share Invite Link", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Stats card
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(text = "Friends Joined", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "9 Members", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(text = "Total Earned", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "$450.00 USD", color = Color(0xFF00E676), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// =========================================================================
// 7. RECENT BONUS LEDGER SECTION
// =========================================================================
@Composable
private fun RecentBonusLedgerSection() {
    val items = listOf(
        Triple("Apple Store 5% Metal Cashback", "Approved purchase • Card ••1289", "+$59.95"),
        Triple("Weekly Contactless Challenge", "Quest complete • 15/15 taps", "+2,500 pts"),
        Triple("Referral Bonus: Marcos Silva", "Friend joined & deposited >$100", "+$50.00"),
        Triple("Astrid & Gastón Dining Multiplier", "4.0% Fine dining booster", "+$18.40"),
        Triple("Monthly Savings Vault Staking", "3.5% APY compounding payout", "+$72.85")
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.Black.copy(alpha = 0.35f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(22.dp))
    ) {
        Column {
            items.forEachIndexed { index, (title, subtitle, amount) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF00E676),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            color = Color.White.copy(alpha = 0.55f),
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        text = amount,
                        color = if (amount.contains("pts")) Color(0xFF00F2FE) else Color(0xFF00E676),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (index < items.size - 1) {
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.06f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(start = 66.dp, end = 16.dp)
                    )
                }
            }
        }
    }
}

