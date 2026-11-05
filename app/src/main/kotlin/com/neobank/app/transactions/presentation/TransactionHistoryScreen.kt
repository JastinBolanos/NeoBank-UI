package com.neobank.app.transactions.presentation

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontFamily
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

data class TransactionUi(
    val id: Int,
    val name: String,
    val description: String,
    val amount: String,
    @DrawableRes val icon: Int
)

private val transactions = listOf(
    TransactionUi(1, "Google",   "Google Workspace Subscription", "- $12.00",  R.drawable.ic_google),
    TransactionUi(2, "YouTube",  "YouTube Premium Family",         "- $11.99",  R.drawable.ic_youtube),
    TransactionUi(3, "Facebook", "Ads Campaign Billing",           "- $50.00",  R.drawable.ic_facebook),
    TransactionUi(4, "Visa",     "Credit Card Payment",            "- $150.00", R.drawable.ic_visa),
    TransactionUi(5, "Claro",    "Monthly Internet Bill",          "- $35.00",  R.drawable.ic_claro),
    TransactionUi(6, "Google",   "Cloud Storage Plan",             "- $9.99",   R.drawable.ic_google),
    TransactionUi(7, "YouTube",  "YouTube Music Premium",          "- $4.99",   R.drawable.ic_youtube)
)

@Composable
fun TransactionHistoryScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToCards: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToScanner: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    selectedTab: NavTab = NavTab.History,
    onTabSelected: (NavTab) -> Unit = {}
) {
    val cardsData = remember {
        listOf(
            mapOf("balance" to "$5 750,20", "masked" to "**** **** **** 1289", "expiry" to "09 / 25"),
            mapOf("balance" to "$10 985,84", "masked" to "**** **** **** 1388", "expiry" to "11 / 26"),
            mapOf("balance" to "$2 340,00",  "masked" to "**** **** **** 4923", "expiry" to "01 / 27")
        )
    }

    val pagerState: PagerState = rememberPagerState(initialPage = 0, pageCount = { cardsData.size })
    var selectedTransaction by remember { mutableStateOf<DetailedTransaction?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bg_neobank),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 65.dp, bottom = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "History",
                    color = Color.White,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 40.sp,
                    modifier = Modifier.weight(1f)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircleIcon(
                        res = R.drawable.ic_scanner,
                        onClick = onNavigateToScanner
                    )
                    CircleIcon(
                        res = R.drawable.ic_bell,
                        onClick = onNavigateToNotifications
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 48.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val card = cardsData[page]

                CardItem(
                    balance = card["balance"]!!,
                    cardNumber = card["masked"]!!,
                    expiryDate = card["expiry"]!!,
                    modifier = Modifier.graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) +
                                        pagerState.currentPageOffsetFraction
                                ).absoluteValue

                        val scale = lerp(0.85f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                        val alpha = lerp(0.50f, 1f, 1f - pageOffset.coerceIn(0f, 1f))

                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(cardsData.size) { i ->
                    val selected = pagerState.currentPage == i
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) Color.White
                                else Color.White.copy(alpha = 0.4f)
                            )
                    )
                    if (i < cardsData.lastIndex) Spacer(modifier = Modifier.width(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TRANSACCIONES
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp)
                    .graphicsLayer {
                        alpha = alphaAnim
                        translationY = slideAnim
                    }
            ) {
                Text(
                    text = "TRANSACTIONS",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    contentPadding = PaddingValues(bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(TransactionRepository.sampleTransactions) { tx ->
                        TransactionRow(
                            item = tx,
                            onClick = { selectedTransaction = tx }
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            NeoBottomNavigationBar(
                selectedTab = NavTab.History,
                onTabSelected = {
                    when (it) {
                        NavTab.Home -> onNavigateToHome()
                        NavTab.Cards -> onNavigateToCards()
                        NavTab.Profile -> onNavigateToProfile()
                        else -> {}
                    }
                }
            )
        }

        if (selectedTransaction != null) {
            TransactionDetailModal(
                transaction = selectedTransaction!!,
                onDismiss = { selectedTransaction = null }
            )
        }
    }
}

@Composable
private fun CircleIcon(
    @DrawableRes res: Int,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f))
            .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(res),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun TransactionRow(
    item: DetailedTransaction,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.35f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f))
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(item.iconRes),
                contentDescription = item.merchant,
                modifier = Modifier.size(22.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.merchant,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = item.subtitle,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Text(
            text = item.amount,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}