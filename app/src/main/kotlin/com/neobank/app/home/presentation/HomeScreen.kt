package com.neobank.app.home.presentation

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R
import com.neobank.app.core.navigation.NavTab
import com.neobank.app.core.navigation.NeoBottomNavigationBar
import com.neobank.app.menu.presentation.MenuPanel
import kotlinx.coroutines.delay

data class PromoBanner(
    val title: String,
    val subtitle: String,
    val backgroundColor: Color,
    @DrawableRes val imageRes: Int? = null
)

@Composable
fun HomeScreen(
    balance: Double,
    onNavigateToCards: () -> Unit = {},
    onNavigateToSendMoney: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    selectedTab: NavTab = NavTab.Home,
    onTabSelected: (NavTab) -> Unit = {}
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bg_neobank),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // --- TOP HALF ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.28f)
            ) {
                Spacer(modifier = Modifier.height(64.dp))
                TopBarSection(modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(modifier = Modifier.height(32.dp))
                BalanceSection(balance = balance)
                Spacer(modifier = Modifier.height(32.dp))
                QuickActionsSection(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    onCardsClick = onNavigateToCards,
                    onTransferClick = onNavigateToSendMoney,
                    onMoreClick = { isMenuOpen = true }
                )
                Spacer(modifier = Modifier.height(28.dp))
                PromoCardsSection()
            }

            // --- BOTTOM HALF ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.background),
                    contentDescription = "Transactions Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                TransactionsSection(bottomPadding = 90.dp)
            }
        }

        // BOTTOM NAVIGATION BAR
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            NeoBottomNavigationBar(
                selectedTab = NavTab.Home,
                onTabSelected = { tab ->
                    when (tab) {
                        NavTab.Cards -> onNavigateToCards()
                        NavTab.History -> onNavigateToHistory()
                        NavTab.Profile -> onNavigateToProfile()
                        NavTab.Home -> { /* We are already here */ }
                        NavTab.Transfer -> onNavigateToSendMoney()
                    }
                }
            )
        }

        // --- SIDE MENU ANIMATIONS ---
        AnimatedVisibility(
            visible = isMenuOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { isMenuOpen = false }
            )
        }

        // Sliding Panel
        AnimatedVisibility(
            visible = isMenuOpen,
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            MenuPanel(
                onClose = { isMenuOpen = false },
                onNavigateToProfile = onNavigateToProfile
            )
        }
    }
}

// --- SUB-COMPONENTS ---
@Composable
private fun TopBarSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hi Saurabh Kumar",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Text(
                text = "Welcome Back",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // SCANNER Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_scanner),
                    contentDescription = "Scan code",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // NOTIFICATIONS Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bell),
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

fun Double.toFormattedCurrency(): String {
    val parts = this.toString().split(".")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1].padEnd(2, '0').take(2) else "00"
    val formattedInteger = integerPart.reversed().chunked(3).joinToString(",").reversed()
    return "$formattedInteger.$decimalPart"
}

// 2. BALANCE SECTION
@Composable
private fun BalanceSection(balance: Double) {
    var isBalanceVisible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isBalanceVisible) "$${balance.toFormattedCurrency()}" else "$••••••",
            color = Color.White,
            fontSize = 38.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { isBalanceVisible = !isBalanceVisible }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Total Balance",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "Toggle Visibility",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun QuickActionsSection(
    modifier: Modifier = Modifier,
    onCardsClick: () -> Unit,
    onTransferClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionItemPainter(
            painter = painterResource(R.drawable.ic_transfer),
            label = "Transfer",
            onClick = onTransferClick
        )

        ActionItemPainter(
            painter = painterResource(R.drawable.ic_cards),
            label = "Cards",
            onClick = onCardsClick
        )

        ActionItemPainter(
            painter = painterResource(R.drawable.ic_bonuses),
            label = "My bonuses",
            onClick = { /* Future logic */ }
        )

        ActionItemPainter(
            painter = painterResource(R.drawable.ic_more),
            label = "More",
            onClick = onMoreClick
        )
    }
}

@Composable
private fun ActionItemPainter(
    painter: Painter,
    label: String,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "button_pulse")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(68.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer {
                        scaleX = pulseScale
                        scaleY = pulseScale
                        alpha = pulseAlpha
                    }
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painter,
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// =========================================================================
// SECTION: INFINITE CAROUSEL WITH REAL BANNERS AND 3D IMAGES
// =========================================================================
@Composable
private fun PromoCardsSection() {
    val banners = listOf(
        PromoBanner(
            title = "Upgrade to Metal",
            subtitle = "Request your NeoBank Metal card and get 5% cashback.",
            backgroundColor = Color(0xFF181A20),
            imageRes = R.drawable.img_card_metal
        ),
        PromoBanner(
            title = "iPhone 15 Pro Titanium",
            subtitle = "0% interest and 24 installments with your NeoBank card.",
            backgroundColor = Color(0xFF232529),
            imageRes = R.drawable.img_iphone_titanium
        ),
        PromoBanner(
            title = "The world awaits you",
            subtitle = "Earn double miles and get VIP Lounge access.",
            backgroundColor = Color(0xFF6A11CB),
            imageRes = R.drawable.img_travel_miles
        ),
        PromoBanner(
            title = "Make your money grow",
            subtitle = "Invest in the top 500 US companies starting from $10.",
            backgroundColor = Color(0xFF15171E),
            imageRes = R.drawable.img_invest_coins
        ),
        PromoBanner(
            title = "Credit Line Approved",
            subtitle = "You have $15,000 available. Instant disbursement.",
            backgroundColor = Color(0xFFFF9580),
            imageRes = R.drawable.img_credit_lock
        ),
    )

    val actualPageCount = banners.size
    val initialPage = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % actualPageCount)
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )

    LaunchedEffect(pagerState.settledPage) {
        delay(3000)
        pagerState.animateScrollToPage(
            page = pagerState.currentPage + 1,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->

            val actualPage = page % actualPageCount
            val currentBanner = banners[actualPage]

            // BANNER DESIGN
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(currentBanner.backgroundColor)
            ) {
                // --------------------------------------------------------
                // 1. EXCLUSIVE BACKGROUNDS
                // --------------------------------------------------------
                if (currentBanner.title.contains("iPhone")) {
                    Image(
                        painter = painterResource(R.drawable.bg_iphone_banner),
                        contentDescription = "Premium iPhone Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (currentBanner.title.contains("Credit")) {
                    Image(
                        painter = painterResource(R.drawable.bg_credit_banner),
                        contentDescription = "Premium Credit Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (currentBanner.title.contains("Metal")) {
                    Image(
                        painter = painterResource(R.drawable.bg_metal_banner),
                        contentDescription = "Premium Metal Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (currentBanner.title.contains("awaits")) {
                    Image(
                        painter = painterResource(R.drawable.bg_travel_banner),
                        contentDescription = "Premium Travel Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Transparent Circle
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 20.dp, y = (-20).dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.05f))
                )

                // Banner Texts
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = if (currentBanner.imageRes != null) 90.dp else 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentBanner.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentBanner.subtitle,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // 3D Render
                if (currentBanner.imageRes != null) {
                    Image(
                        painter = painterResource(currentBanner.imageRes),
                        contentDescription = "3D Illustration",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 12.dp)
                            .size(80.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        val currentActualPage = pagerState.currentPage % actualPageCount
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(actualPageCount) { index ->
                LoadingDot(isActive = index == currentActualPage)
            }
        }
    }
}

@Composable
private fun LoadingDot(isActive: Boolean) {
    val animatedWidth by animateDpAsState(
        targetValue = if (isActive) 32.dp else 6.dp,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "dotWidthAnimation"
    )

    val progress = remember { Animatable(0f) }

    LaunchedEffect(isActive) {
        if (isActive) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
            )
        } else {
            progress.snapTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .height(6.dp)
            .width(animatedWidth)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.3f))
    ) {
        if (isActive && progress.value > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.value)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

@Composable
private fun TransactionsSection(bottomPadding: Dp = 0.dp) {
    var isTransactionsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isTransactionsVisible = true
    }

    val alphaAnim by animateFloatAsState(
        targetValue = if (isTransactionsVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "fadeAnim"
    )

    val slideAnim by animateFloatAsState(
        targetValue = if (isTransactionsVisible) 0f else 50f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "slideAnim"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = alphaAnim
                translationY = slideAnim
            },
        contentPadding = PaddingValues(
            start = 14.dp,
            end = 14.dp,
            top = 20.dp,
            bottom = bottomPadding
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "TRANSACTIONS",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontFamily = FontFamily.Serif,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        item {
            TransactionItem(
                painter = painterResource(R.drawable.ic_google),
                title = "Google",
                subtitle = "Google Workspace Subscription",
                amount = "- $12.00"
            )
        }
        item {
            TransactionItem(
                painter = painterResource(R.drawable.ic_youtube),
                title = "YouTube",
                subtitle = "YouTube Premium Family",
                amount = "- $11.99"
            )
        }
        item {
            TransactionItem(
                painter = painterResource(R.drawable.ic_facebook),
                title = "Facebook",
                subtitle = "Ads Campaign Billing",
                amount = "- $50.00"
            )
        }
        item {
            TransactionItem(
                painter = painterResource(R.drawable.ic_visa),
                title = "Visa",
                subtitle = "Credit Card Payment",
                amount = "- $150.00"
            )
        }
        item {
            TransactionItem(
                painter = painterResource(R.drawable.ic_claro),
                title = "Claro",
                subtitle = "Monthly Internet Bill",
                amount = "- $35.00"
            )
        }
    }
}

@Composable
private fun TransactionItem(
    painter: Painter,
    title: String,
    subtitle: String,
    amount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.35f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = amount,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}