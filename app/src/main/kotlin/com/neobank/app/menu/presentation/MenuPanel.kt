package com.neobank.app.menu.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MenuOptionItem(
    val icon: ImageVector,
    val title: String,
    val modalType: MenuModalType
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPanel(
    onClose: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToScanner: () -> Unit = {},
    onNavigateToBonuses: () -> Unit = {},
    onLogout: () -> Unit = {},
    onDeductBalance: (Double) -> Unit = {},
    currentBalance: Double = 50000.00,
    modifier: Modifier = Modifier
) {
    // Lectura de las dimensiones de la pantalla del usuario en tiempo real
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    // Detección de factores de forma (pantallas altas modernas 20:9, tablets, etc.)
    val isTallScreen = screenHeight >= 820
    val isExtraTallScreen = screenHeight >= 900
    val isCompactScreen = screenHeight < 680

    // Ancho adaptativo del cajón lateral
    val drawerWidthFraction = if (screenWidth > 600) 0.50f else 0.80f

    // Espaciados proporcionales a la altura de la pantalla
    val topContentPadding = when {
        isExtraTallScreen -> 32.dp
        isTallScreen -> 24.dp
        isCompactScreen -> 12.dp
        else -> 18.dp
    }

    val itemVerticalPadding = when {
        isExtraTallScreen -> 14.dp
        isTallScreen -> 13.dp
        isCompactScreen -> 9.dp
        else -> 11.dp
    }

    val headerSpacing = when {
        isExtraTallScreen -> 22.dp
        isTallScreen -> 18.dp
        else -> 14.dp
    }

    var activeModal by remember { mutableStateOf<MenuModalType?>(null) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun showToast(msg: String) {
        toastMessage = msg
        coroutineScope.launch {
            delay(2200)
            toastMessage = null
        }
    }

    val menuOptions = remember {
        listOf(
            MenuOptionItem(Icons.Default.Settings, "Configuration", MenuModalType.CONFIGURATION),
            MenuOptionItem(Icons.Default.Lock, "Digital Token", MenuModalType.DIGITAL_TOKEN),
            MenuOptionItem(Icons.Default.Person, "Security and privacy", MenuModalType.SECURITY_PRIVACY),
            MenuOptionItem(Icons.AutoMirrored.Filled.List, "Operational", MenuModalType.OPERATIONAL_LIMITS),
            MenuOptionItem(Icons.Default.Share, "Transact with QR / Plin", MenuModalType.TRANSACT_QR_PLIN),
            MenuOptionItem(Icons.Default.Star, "Points and promotions", MenuModalType.POINTS_PROMOTIONS),
            MenuOptionItem(Icons.Default.Favorite, "Experiences", MenuModalType.EXPERIENCES),
            MenuOptionItem(Icons.Default.Refresh, "Cardless withdrawal history", MenuModalType.CARDLESS_WITHDRAWAL),
            MenuOptionItem(Icons.Default.LocationOn, "Service points", MenuModalType.SERVICE_POINTS),
            MenuOptionItem(Icons.Default.ShoppingCart, "payment area", MenuModalType.PAYMENT_AREA),
            MenuOptionItem(Icons.AutoMirrored.Filled.ArrowBack, "Go out", MenuModalType.LOGOUT_CONFIRM)
        )
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(drawerWidthFraction)
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

        // CONTENIDO ALINEADO AL TOPE (Arrangement.Top) CON RESPONSIVIDAD PARA PANTALLAS GRANDES
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(start = 22.dp, end = 14.dp),
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(
                top = topContentPadding,
                bottom = 36.dp
            )
        ) {
            item {
                Text(
                    text = "Saurabh Kumar",
                    color = Color.White,
                    fontSize = if (isCompactScreen) 24.sp else 28.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onClose()
                            onNavigateToBonuses()
                        }
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "Aura Points: 32,400",
                        color = Color(0xFF00F2FE),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• Titanium VIP",
                        color = Color(0xFFFFD700),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(headerSpacing))

                // Fina línea divisoria sutil bajo el bloque de cabecera
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.08f))
                )

                Spacer(modifier = Modifier.height(headerSpacing))
            }

            items(menuOptions) { option ->
                val isLogout = option.modalType == MenuModalType.LOGOUT_CONFIRM
                if (isLogout) {
                    Spacer(modifier = Modifier.height(if (isTallScreen) 24.dp else 16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(1.dp)
                            .background(Color.White.copy(alpha = 0.08f))
                    )
                    Spacer(modifier = Modifier.height(if (isTallScreen) 14.dp else 10.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            activeModal = option.modalType
                        }
                        .padding(horizontal = 14.dp, vertical = itemVerticalPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = option.title,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = option.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Floating Toast
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            if (toastMessage != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            androidx.compose.ui.graphics.Brush.horizontalGradient(
                                listOf(Color(0xFF00E676), Color(0xFF00B0FF))
                            )
                        )
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = toastMessage!!,
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }

    // --- MODAL BOTTOM SHEET FOR ACTIVE MENU ITEM ---
    if (activeModal != null) {
        ModalBottomSheet(
            onDismissRequest = { activeModal = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFF0F111A),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                        .width(48.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(
                            androidx.compose.ui.graphics.Brush.horizontalGradient(
                                listOf(Color(0xFF00F2FE), Color(0xFF6B11FF), Color(0xFF00E676))
                            )
                        )
                )
            },
            contentColor = Color.White
        ) {
            when (activeModal!!) {
                MenuModalType.DIGITAL_TOKEN -> {
                    DigitalTokenModal(
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.CONFIGURATION -> {
                    ConfigurationModal(
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.SECURITY_PRIVACY -> {
                    SecurityPrivacyModal(
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.OPERATIONAL_LIMITS -> {
                    OperationalLimitsModal(
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.TRANSACT_QR_PLIN -> {
                    TransactQrPlinModal(
                        currentBalance = currentBalance,
                        onNavigateToScanner = {
                            activeModal = null
                            onClose()
                            onNavigateToScanner()
                        },
                        onDeductBalance = onDeductBalance,
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.POINTS_PROMOTIONS -> {
                    PointsPromotionsModal(
                        onNavigateToBonuses = {
                            activeModal = null
                            onClose()
                            onNavigateToBonuses()
                        },
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.EXPERIENCES -> {
                    ExperiencesModal(
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.CARDLESS_WITHDRAWAL -> {
                    CardlessWithdrawalModal(
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.SERVICE_POINTS -> {
                    ServicePointsModal(
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.PAYMENT_AREA -> {
                    PaymentAreaModal(
                        currentBalance = currentBalance,
                        onDeductBalance = onDeductBalance,
                        onDismiss = { activeModal = null },
                        onShowToast = { showToast(it) }
                    )
                }
                MenuModalType.LOGOUT_CONFIRM -> {
                    LogoutConfirmModal(
                        onConfirmLogout = {
                            activeModal = null
                            onClose()
                            onLogout()
                        },
                        onDismiss = { activeModal = null }
                    )
                }
            }
        }
    }
}