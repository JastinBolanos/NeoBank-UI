package com.neobank.app.cards.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.neobank.app.R
import com.neobank.app.cards.presentation.components.CardItem
import com.neobank.app.core.navigation.NavTab
import com.neobank.app.core.navigation.NeoBottomNavigationBar
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun CardsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    selectedTab: NavTab = NavTab.Cards,
    onTabSelected: (NavTab) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_neobank),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        val cards = remember {
            listOf(
                mapOf(
                    "holder" to "Saurabh Kumar",
                    "number" to "5764 9968 6789 1289",
                    "masked" to "**** **** **** 1289",
                    "expiry" to "09 / 25",
                    "cvv" to "654",
                    "balance" to "$5,750.20"
                ),
                mapOf(
                    "holder" to "Saurabh Kumar",
                    "number" to "5764 9968 6789 1388",
                    "masked" to "**** **** **** 1388",
                    "expiry" to "11 / 26",
                    "cvv" to "721",
                    "balance" to "$10,985.84"
                ),
                mapOf(
                    "holder" to "Saurabh Kumar",
                    "number" to "5764 9968 6789 4923",
                    "masked" to "**** **** **** 4923",
                    "expiry" to "01 / 27",
                    "cvv" to "903",
                    "balance" to "$2,340.00"
                )
            )
        }

        val pagerState = rememberPagerState(pageCount = { cards.size })

        Column(modifier = Modifier.fillMaxSize()) {
            // --- HEADER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 64.dp, bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cards",
                    color = Color.White,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // SCANNER Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_scanner),
                        contentDescription = "Scan card",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

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

            // --- CAROUSEL ---
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 48.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                val scale = lerp(
                    start = 0.85f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )

                val alpha = lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )

                val card = cards[page]
                CardItem(
                    balance = card["balance"]!!,
                    cardNumber = card["masked"]!!,
                    expiryDate = card["expiry"]!!,
                    modifier = Modifier.graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                )
            }

            // --- INDICATORS ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(cards.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.4f))
                    )
                    if (index < cards.size - 1) {
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }
        }

        // --- CARD DETAILS ---
        val currentCard = cards[pagerState.currentPage]
        val scrollState = rememberScrollState()
        var isCardNumberVisible by remember { mutableStateOf(false) }
        var isCvvVisible by remember { mutableStateOf(false) }
        var isVisible by remember { mutableStateOf(false) }

        LaunchedEffect(pagerState.currentPage) {
            isVisible = false
            isCardNumberVisible = false
            isCvvVisible = false
            delay(50)
            isVisible = true
        }

        val alphaAnim by animateFloatAsState(
            targetValue = if (isVisible) 1f else 0f,
            animationSpec = tween(durationMillis = 400),
            label = "fadeAnim"
        )
        val slideAnim by animateFloatAsState(
            targetValue = if (isVisible) 0f else 30f,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
            label = "slideAnim"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
                .align(Alignment.BottomCenter)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(bottom = 120.dp, top = 8.dp)
                .graphicsLayer {
                    alpha = alphaAnim
                    translationY = slideAnim
                }
        ) {
            @Composable
            fun PremiumDetailBlock(title: String, content: @Composable () -> Unit) {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = title,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(alpha = 0.35f))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 18.dp, vertical = 16.dp)
                    ) {
                        content()
                    }
                }
            }

            // 1. Cardholder
            PremiumDetailBlock(title = "Cardholder Name") {
                Text(
                    text = currentCard["holder"]!!,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // 2. Card Number
            PremiumDetailBlock(title = "Card Number") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.width(34.dp).height(20.dp)) {
                            Box(modifier = Modifier.size(20.dp).align(Alignment.CenterStart).clip(CircleShape).background(Color(0xFFEB001B)))
                            Box(modifier = Modifier.size(20.dp).align(Alignment.CenterEnd).clip(CircleShape).background(Color(0xFFF79E1B).copy(alpha = 0.9f)))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = if (isCardNumberVisible) currentCard["number"]!! else currentCard["masked"]!!,
                            color = Color.White,
                            fontSize = 16.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Icon(
                        imageVector = if (isCardNumberVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Number Visibility",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .clickable { isCardNumberVisible = !isCardNumberVisible }
                    )
                }
            }

            // 3. Account Number
            PremiumDetailBlock(title = "Account Number") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_visa),
                        contentDescription = "Visa Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.width(40.dp).height(21.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = currentCard["account"] ?: "0011-0815-0741312654",
                        color = Color.White,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // 4. Expiration + CVV
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    PremiumDetailBlock(title = "Expire Date") {
                        Text(
                            text = currentCard["expiry"]!!,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    PremiumDetailBlock(title = "CVV / CVC") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isCvvVisible) currentCard["cvv"]!! else "***",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = if (isCvvVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle CVV Visibility",
                                tint = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .clickable { isCvvVisible = !isCvvVisible }
                            )
                        }
                    }
                }
            }
        }

        // --- BOTTOM NAVIGATION ---
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            NeoBottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    when (tab) {
                        NavTab.Home     -> onNavigateToHome()
                        NavTab.History  -> onNavigateToHistory()
                        NavTab.Cards    -> { /* We are already here */ }
                        NavTab.Profile  -> onNavigateToProfile()
                        NavTab.Transfer -> { /* Currently unused */ }
                    }
                }
            )
        }
    }
}