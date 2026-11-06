package com.neobank.app.home.presentation

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.neobank.app.R
import com.neobank.app.core.navigation.KmpBackHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ProductHighlight(
    val title: String,
    val description: String,
    val icon: ImageVector
)

enum class ProductActionType {
    DISBURSE_CREDIT,
    ORDER_IPHONE,
    INVEST_MONEY,
    UPGRADE_METAL,
    EXPLORE_TRAVEL
}

data class ProductDetail(
    val id: String,
    val title: String,
    val subtitle: String,
    val categoryBadge: String,
    val accentColor: Color,
    val secondaryColor: Color,
    val internetImageUrl: String,
    @DrawableRes val fallbackImageRes: Int? = null,
    val fullDescription: String,
    val highlights: List<ProductHighlight>,
    val specs: List<Pair<String, String>>,
    val primaryActionText: String,
    val actionType: ProductActionType
)

object ProductCatalog {
    val products: Map<String, ProductDetail> = mapOf(
        "Credit Line Approved" to ProductDetail(
            id = "credit_line",
            title = "Credit Line Approved",
            subtitle = "You have $15,000 available. Instant disbursement.",
            categoryBadge = "REVOLVING CREDIT LINE",
            accentColor = Color(0xFFFF7A45),
            secondaryColor = Color(0xFFFF4D4F),
            internetImageUrl = "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?auto=format&fit=crop&w=1200&q=80",
            fallbackImageRes = R.drawable.img_credit_lock,
            fullDescription = "Your exclusive revolving line of credit of up to $15,000 has been officially approved. Draw funds whenever you need them with zero origination fees and automatic real-time disbursement directly into your Aura checking balance.",
            highlights = listOf(
                ProductHighlight("Instant Liquidity", "Transfers to your primary checking account in under 3 seconds.", Icons.Default.Speed),
                ProductHighlight("Tailored Terms", "Choose from 6 to 36 month flexible repayment schedules.", Icons.Default.DateRange),
                ProductHighlight("Zero Penalty", "Prepay or clear your balance anytime with zero hidden charges.", Icons.Default.Security)
            ),
            specs = listOf(
                "Approved Maximum Limit" to "$15,000.00 USD",
                "Fixed Annual Percentage" to "11.5% APR",
                "Origination & Setup Fee" to "$0.00 (Waived)",
                "Disbursement Latency" to "Instant (< 3 sec)"
            ),
            primaryActionText = "Disburse Funds to Checking",
            actionType = ProductActionType.DISBURSE_CREDIT
        ),

        "iPhone 15 Pro Titanium" to ProductDetail(
            id = "iphone_titanium",
            title = "iPhone 15 Pro Titanium",
            subtitle = "0% interest and 24 installments with your NeoBank card.",
            categoryBadge = "APPLE OFFICIAL FINANCING",
            accentColor = Color(0xFF00F2FE),
            secondaryColor = Color(0xFF4FACFE),
            internetImageUrl = "https://images.unsplash.com/photo-1695048133142-1a20484d2569?auto=format&fit=crop&w=1200&q=80",
            fallbackImageRes = R.drawable.img_iphone_titanium,
            fullDescription = "Experience the strength and lightness of aerospace-grade titanium, the beastly A17 Pro chip, and the pro-grade camera system. Exclusively available with 0% interest and 24 effortless installments on your NeoBank card.",
            highlights = listOf(
                ProductHighlight("Aerospace Titanium", "Remarkably strong and light forged enclosure with brushed finish.", Icons.Default.Smartphone),
                ProductHighlight("24 Installments at 0%", "Split the cost smoothly without paying a single cent of interest.", Icons.Default.AccountBalanceWallet),
                ProductHighlight("AppleCare+ Included", "1 year complimentary accidental and theft coverage.", Icons.Default.VerifiedUser)
            ),
            specs = listOf(
                "Model" to "iPhone 15 Pro Natural Titanium",
                "Display" to "6.1\" Super Retina XDR ProMotion",
                "Installment Plan" to "$41.62 / month (24 months)",
                "Down Payment" to "$0.00 with NeoBank Card"
            ),
            primaryActionText = "Order with NeoBank Card (0% APR)",
            actionType = ProductActionType.ORDER_IPHONE
        ),

        "Make your money grow" to ProductDetail(
            id = "make_money_grow",
            title = "Make your money grow",
            subtitle = "Invest in the top 500 US companies starting from $10.",
            categoryBadge = "WEALTH & GLOBAL EQUITIES",
            accentColor = Color(0xFF00E676),
            secondaryColor = Color(0xFF00B0FF),
            internetImageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=1200&q=80",
            fallbackImageRes = R.drawable.img_invest_coins,
            fullDescription = "Put your money to work in the global market. Invest in fractional shares of market leaders like Apple, Microsoft, Nvidia, and top-performing S&P 500 ETFs with zero brokerage commission.",
            highlights = listOf(
                ProductHighlight("Fractional Shares", "Start investing with as little as $10 in high-value equities.", Icons.Default.TrendingUp),
                ProductHighlight("Zero Commission", "Buy and sell US equities with zero trading or custody fees.", Icons.Default.AttachMoney),
                ProductHighlight("Automated Dollar-Cost", "Automate weekly or monthly deposits for long-term compounding.", Icons.Default.Autorenew)
            ),
            specs = listOf(
                "Supported Assets" to "S&P 500, NASDAQ-100 & Global ETFs",
                "Minimum Investment" to "$10.00 USD",
                "Historical Return" to "+12.4% Annualized Avg.",
                "Trading Hours" to "24/7 Fractional Orders"
            ),
            primaryActionText = "Execute Investment Order",
            actionType = ProductActionType.INVEST_MONEY
        ),

        "Upgrade to Metal" to ProductDetail(
            id = "upgrade_metal",
            title = "Upgrade to Metal",
            subtitle = "Request your NeoBank Metal card and get 5% cashback.",
            categoryBadge = "AURA BLACK VIP",
            accentColor = Color(0xFFFFD700),
            secondaryColor = Color(0xFFFFA000),
            internetImageUrl = "https://images.unsplash.com/photo-1589758438368-0ad531db3366?auto=format&fit=crop&w=1200&q=80",
            fallbackImageRes = R.drawable.img_card_metal,
            fullDescription = "Elevate your lifestyle with the prestigious NeoBank Metal Card. Machined from 18 grams of solid metal alloy with custom laser etching, 5% cashback on daily purchases, and global concierge.",
            highlights = listOf(
                ProductHighlight("18g Solid Metal", "Weighted luxury feel with laser-etched name and matte tactile finish.", Icons.Default.CreditCard),
                ProductHighlight("5% Unlimited Cashback", "Earn on dining, travel, shopping, and subscription payments.", Icons.Default.Percent),
                ProductHighlight("24/7 Global Concierge", "Personal lifestyle manager for reservations, tickets, and VIP bookings.", Icons.Default.HeadsetMic)
            ),
            specs = listOf(
                "Material" to "18-Gram Titanium Steel Alloy",
                "Cashback Rate" to "5% Unlimited on all categories",
                "FX Exchange Fee" to "0% Interbank rate worldwide",
                "Insurance" to "Up to $1,000,000 Travel & Tech Cover"
            ),
            primaryActionText = "Claim 18g Titanium Metal Card",
            actionType = ProductActionType.UPGRADE_METAL
        ),

        "The world awaits you" to ProductDetail(
            id = "world_awaits",
            title = "The world awaits you",
            subtitle = "Earn double miles and get VIP Lounge access.",
            categoryBadge = "GLOBAL TRAVEL PRIVILEGES",
            accentColor = Color(0xFFE040FB),
            secondaryColor = Color(0xFF7C4DFF),
            internetImageUrl = "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?auto=format&fit=crop&w=1200&q=80",
            fallbackImageRes = R.drawable.img_travel_miles,
            fullDescription = "Unlock a world of luxury travel. Gain complimentary access to over 1,400 airport lounges worldwide, earn double miles on every flight and hotel booking, and enjoy fee-free foreign transactions.",
            highlights = listOf(
                ProductHighlight("1,400+ VIP Lounges", "Unlimited Priority Pass access with free gourmet dining and bars.", Icons.Default.FlightTakeoff),
                ProductHighlight("2x AuraMiles", "Accumulate double airline miles transferable to 30+ carrier partners.", Icons.Default.Star),
                ProductHighlight("Zero Foreign Fees", "Spend seamlessly in 180+ countries with no currency markup.", Icons.Default.Public)
            ),
            specs = listOf(
                "Lounge Access" to "Priority Pass Worldwide (Free Guest)",
                "Miles Accrual" to "2 Miles per $1 spent on travel",
                "Flight Delay Protection" to "Complimentary hotel voucher after 2h",
                "Fast Track Security" to "TSA PreCheck / Global Entry Credit"
            ),
            primaryActionText = "Activate Digital Travel Pass",
            actionType = ProductActionType.EXPLORE_TRAVEL
        )
    )
}

@Composable
fun ProductDetailScreen(
    product: ProductDetail,
    currentBalance: Double,
    onBackClick: () -> Unit,
    onDisburseCredit: (Double) -> Unit = {},
    onInvestMoney: (Double) -> Unit = {}
) {
    val context = LocalContext.current
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var actionExecuted by remember { mutableStateOf(false) }

    // Screen entrance animation state
    var isScreenVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(60)
        isScreenVisible = true
    }

    val contentAlpha by animateFloatAsState(
        targetValue = if (isScreenVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "contentAlpha"
    )

    val contentSlideY by animateFloatAsState(
        targetValue = if (isScreenVisible) 0f else 40f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "contentSlideY"
    )

    // Breathing motion for hero asset
    val infiniteTransition = rememberInfiniteTransition(label = "ambient_motion")
    val heroPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.025f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heroPulse"
    )

    val ambientGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambientGlowAlpha"
    )

    // Interactive state for Credit Line
    var creditAmount by remember { mutableFloatStateOf(15000f) }
    var selectedTermMonths by remember { mutableIntStateOf(24) }

    // Interactive state for iPhone
    var selectedColor by remember { mutableStateOf("Natural Titanium") }
    var selectedStorage by remember { mutableStateOf("256 GB") }

    // Interactive state for Investment
    var selectedInvestAmount by remember { mutableDoubleStateOf(100.0) }

    // Interactive state for Metal Card
    var selectedCardFinish by remember { mutableStateOf("Obsidian Black") }

    // Interactive state for Travel Pass
    var selectedAirportHub by remember { mutableStateOf("JFK New York") }

    val coroutineScope = rememberCoroutineScope()

    KmpBackHandler {
        onBackClick()
    }

    fun showToast(msg: String) {
        toastMessage = msg
        coroutineScope.launch {
            delay(3200)
            toastMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Deep atmosphere background matching AuraNova dark banking vibe
        Image(
            painter = painterResource(R.drawable.bg_neobank),
            contentDescription = "Atmospheric Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(28.dp)
        )

        // Radial glowing aura matching product color
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            product.accentColor.copy(alpha = ambientGlowAlpha * 0.35f),
                            Color(0xFF090B13).copy(alpha = 0.94f),
                            Color(0xFF040508)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // =========================================================
            // TOP EXECUTIVE APP BAR (CLEAN & FLOATING)
            // =========================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button with soft tap feedback
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f))
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Minimal Brand Header
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = product.categoryBadge,
                        color = product.accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = product.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Verified Shield / Privilege icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(product.accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Privilege",
                        tint = product.accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // =========================================================
            // MAIN SCROLLABLE CONTENT (BREATHABLE, FLUID & BORDERLESS)
            // =========================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .graphicsLayer {
                        alpha = contentAlpha
                        translationY = contentSlideY
                    }
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Title and Subtitle as clean, bold typography (NO BOX)
                Text(
                    text = product.title,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = product.subtitle,
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // =========================================================
                // HERO IMAGE WITH DYNAMIC BREATHING MOTION
                // =========================================================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .graphicsLayer {
                            scaleX = heroPulse
                            scaleY = heroPulse
                        }
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF10131E))
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(product.internetImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF10131E)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = product.accentColor,
                                    modifier = Modifier.size(36.dp),
                                    strokeWidth = 3.dp
                                )
                            }
                        },
                        error = {
                            if (product.fallbackImageRes != null) {
                                Image(
                                    painter = painterResource(product.fallbackImageRes),
                                    contentDescription = product.title,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp)
                                )
                            }
                        }
                    )

                    // Atmospheric gradient fade at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0xFF040508).copy(alpha = 0.85f))
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // =========================================================
                // BORDERLESS STREAMLINED STATS BAR (NO BOXES!)
                // =========================================================
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    product.specs.take(2).forEachIndexed { index, (label, value) ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = label.uppercase(),
                                color = Color.White.copy(alpha = 0.45f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = value,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (index == 0) {
                            Box(
                                modifier = Modifier
                                    .height(34.dp)
                                    .width(1.dp)
                                    .background(Color.White.copy(alpha = 0.12f))
                                    .padding(horizontal = 12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(18.dp))

                // =========================================================
                // EDITORIAL OVERVIEW (NO BOX, CLEAN AND READABLE)
                // =========================================================
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(product.accentColor)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = product.fullDescription,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.5.sp,
                        lineHeight = 21.sp
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                // =========================================================
                // THE INTERACTIVE STUDIO (SINGLE FOCUSED CONTROL PANEL)
                // =========================================================
                when (product.actionType) {
                    ProductActionType.DISBURSE_CREDIT -> {
                        CreditLineExecutiveSection(
                            amount = creditAmount,
                            onAmountChange = { creditAmount = it },
                            selectedTerm = selectedTermMonths,
                            onTermChange = { selectedTermMonths = it },
                            accentColor = product.accentColor
                        )
                    }
                    ProductActionType.ORDER_IPHONE -> {
                        IPhoneExecutiveSection(
                            selectedColor = selectedColor,
                            onColorChange = { selectedColor = it },
                            selectedStorage = selectedStorage,
                            onStorageChange = { selectedStorage = it },
                            accentColor = product.accentColor
                        )
                    }
                    ProductActionType.INVEST_MONEY -> {
                        InvestmentExecutiveSection(
                            amount = selectedInvestAmount,
                            onAmountChange = { selectedInvestAmount = it },
                            accentColor = product.accentColor
                        )
                    }
                    ProductActionType.UPGRADE_METAL -> {
                        MetalCardExecutiveSection(
                            selectedFinish = selectedCardFinish,
                            onFinishChange = { selectedCardFinish = it },
                            accentColor = product.accentColor
                        )
                    }
                    ProductActionType.EXPLORE_TRAVEL -> {
                        TravelPassExecutiveSection(
                            selectedHub = selectedAirportHub,
                            onHubChange = { selectedAirportHub = it },
                            accentColor = product.accentColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // =========================================================
                // PRIVILEGES & BENEFITS (BORDERLESS NATURAL LIST)
                // =========================================================
                Text(
                    text = "KEY PRIVILEGES",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                product.highlights.forEachIndexed { index, highlight ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Minimalist circular icon badge
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(product.accentColor.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = highlight.icon,
                                contentDescription = highlight.title,
                                tint = product.accentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = highlight.title,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = highlight.description,
                                color = Color.White.copy(alpha = 0.65f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    if (index < product.highlights.size - 1) {
                        HorizontalDivider(
                            color = Color.White.copy(alpha = 0.05f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(start = 56.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(20.dp))

                // =========================================================
                // CLEAN TERMS TABLE (BORDERLESS)
                // =========================================================
                Text(
                    text = "PRODUCT SPECIFICATIONS",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                product.specs.forEach { (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = value,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }

            // =========================================================
            // ELEVATED BOTTOM ACTION BAR WITH ANIMATED BUTTON
            // =========================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(Color(0xFF07090F).copy(alpha = 0.96f))
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Button(
                    onClick = {
                        actionExecuted = true
                        when (product.actionType) {
                            ProductActionType.DISBURSE_CREDIT -> {
                                val amount = creditAmount.toDouble()
                                onDisburseCredit(amount)
                                showToast("✓ $${amount.toInt()} credited to your Checking Balance!")
                            }
                            ProductActionType.ORDER_IPHONE -> {
                                showToast("✓ Order confirmed: $selectedColor ($selectedStorage) with 0% APR.")
                            }
                            ProductActionType.INVEST_MONEY -> {
                                onInvestMoney(selectedInvestAmount)
                                showToast("✓ Investment of $$selectedInvestAmount executed in S&P 500.")
                            }
                            ProductActionType.UPGRADE_METAL -> {
                                showToast("✓ 18g Metal Titanium Card ($selectedCardFinish) ordered.")
                            }
                            ProductActionType.EXPLORE_TRAVEL -> {
                                showToast("✓ Priority Pass Travel membership activated for $selectedAirportHub.")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = product.accentColor
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (actionExecuted) "Request Approved & Executed" else product.primaryActionText,
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = if (actionExecuted) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Floating Toast message
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 70.dp, start = 20.dp, end = 20.dp)
        ) {
            if (toastMessage != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF00E676), Color(0xFF00B0FF))
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = toastMessage!!,
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SECTION 1: CREDIT LINE INTERACTIVE CONTROLLER
// -------------------------------------------------------------
@Composable
private fun CreditLineExecutiveSection(
    amount: Float,
    onAmountChange: (Float) -> Unit,
    selectedTerm: Int,
    onTermChange: (Int) -> Unit,
    accentColor: Color
) {
    val monthlyPayment = (amount / selectedTerm) * 1.05f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DISBURSEMENT AMOUNT",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Up to $15,000",
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "$${amount.toInt()}",
            color = accentColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Slider(
            value = amount,
            onValueChange = onAmountChange,
            valueRange = 1000f..15000f,
            steps = 13,
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = Color.White.copy(alpha = 0.15f)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "REPAYMENT TERM",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(6, 12, 24, 36).forEach { term ->
                val isSelected = selectedTerm == term
                val buttonBg by animateColorAsState(
                    targetValue = if (isSelected) accentColor else Color.White.copy(alpha = 0.06f),
                    label = "termBg"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(buttonBg)
                        .clickable { onTermChange(term) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${term}m",
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Estimated Monthly Payment:",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 12.sp
            )
            Text(
                text = "$${monthlyPayment.toInt()} / mo",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

// -------------------------------------------------------------
// SECTION 2: IPHONE INTERACTIVE FINANCING
// -------------------------------------------------------------
@Composable
private fun IPhoneExecutiveSection(
    selectedColor: String,
    onColorChange: (String) -> Unit,
    selectedStorage: String,
    onStorageChange: (String) -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .padding(18.dp)
    ) {
        Text(
            text = "TITANIUM FINISH",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        val colors = listOf("Natural Titanium", "Blue Titanium", "White Titanium", "Black Titanium")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            colors.forEach { colorName ->
                val isSelected = selectedColor == colorName
                val chipBg by animateColorAsState(
                    targetValue = if (isSelected) accentColor else Color.White.copy(alpha = 0.06f),
                    label = "colorBg"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(chipBg)
                        .clickable { onColorChange(colorName) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = colorName.replace(" Titanium", ""),
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "STORAGE CAPACITY",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        val storages = listOf("128 GB", "256 GB", "512 GB", "1 TB")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            storages.forEach { storage ->
                val isSelected = selectedStorage == storage
                val storageBg by animateColorAsState(
                    targetValue = if (isSelected) accentColor else Color.White.copy(alpha = 0.06f),
                    label = "storageBg"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(storageBg)
                        .clickable { onStorageChange(storage) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = storage,
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "24 Months Installment at 0% APR:",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 12.sp
            )
            Text(
                text = "$41.62 / mo",
                color = Color(0xFF00E676),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// -------------------------------------------------------------
// SECTION 3: S&P 500 INVESTMENT SELECTOR
// -------------------------------------------------------------
@Composable
private fun InvestmentExecutiveSection(
    amount: Double,
    onAmountChange: (Double) -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "INVESTMENT AMOUNT",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Fractional Shares",
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "$${amount.toInt()}.00 USD",
            color = accentColor,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        val amounts = listOf(10.0, 50.0, 100.0, 250.0, 500.0)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            amounts.forEach { optAmount ->
                val isSelected = amount == optAmount
                val chipBg by animateColorAsState(
                    targetValue = if (isSelected) accentColor else Color.White.copy(alpha = 0.06f),
                    label = "investBg"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(chipBg)
                        .clickable { onAmountChange(optAmount) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$${optAmount.toInt()}",
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Top Holdings Allocation:",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
            Text(
                text = "Apple, Microsoft, NVIDIA, Amazon",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// -------------------------------------------------------------
// SECTION 4: METAL CARD FINISH SELECTOR
// -------------------------------------------------------------
@Composable
private fun MetalCardExecutiveSection(
    selectedFinish: String,
    onFinishChange: (String) -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .padding(18.dp)
    ) {
        Text(
            text = "TITANIUM ALLOY FINISH",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        val finishes = listOf("Obsidian Black", "Space Grey", "Champagne Gold")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            finishes.forEach { finish ->
                val isSelected = selectedFinish == finish
                val finishBg by animateColorAsState(
                    targetValue = if (isSelected) accentColor else Color.White.copy(alpha = 0.06f),
                    label = "metalBg"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(finishBg)
                        .clickable { onFinishChange(finish) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = finish,
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SECTION 5: TRAVEL PASS AIRPORT HUB SELECTOR
// -------------------------------------------------------------
@Composable
private fun TravelPassExecutiveSection(
    selectedHub: String,
    onHubChange: (String) -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FEATURED AIRPORT LOUNGE HUBS",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Priority Pass",
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        val hubs = listOf("JFK New York", "LHR London", "MIA Miami")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            hubs.forEach { hub ->
                val isSelected = selectedHub == hub
                val hubBg by animateColorAsState(
                    targetValue = if (isSelected) accentColor else Color.White.copy(alpha = 0.06f),
                    label = "hubBg"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(hubBg)
                        .clickable { onHubChange(hub) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = hub,
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Priority Pass ID:",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 12.sp
            )
            Text(
                text = "AURA-VIP-88194",
                color = accentColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
