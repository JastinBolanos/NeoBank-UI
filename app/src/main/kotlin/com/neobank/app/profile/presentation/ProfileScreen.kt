package com.neobank.app.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R
import com.neobank.app.core.navigation.NavTab
import com.neobank.app.core.navigation.NeoBottomNavigationBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToCards: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    selectedTab: NavTab = NavTab.Profile,
    onTabSelected: (NavTab) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var selectedModalInfo by remember { mutableStateOf<Pair<String, String>?>(null) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun showToast(msg: String) {
        toastMessage = msg
        coroutineScope.launch {
            delay(2200)
            toastMessage = null
        }
    }

    // High performance GPU-only rotation (runs entirely on RenderThread)
    val infiniteTransition = rememberInfiniteTransition(label = "halo_rotation")
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_rot"
    )

    val holographicColors = remember {
        listOf(
            Color(0xFF00F2FE),
            Color(0xFF6B11FF),
            Color(0xFFFF007F),
            Color(0xFFFFD700),
            Color(0xFF00E676),
            Color(0xFF00F2FE)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Deep Obsidian Atmospheric Background
        Image(
            painter = painterResource(R.drawable.bg_neobank),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Radial ambient glow overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF1E284A).copy(alpha = 0.6f),
                            Color(0xFF0E1324).copy(alpha = 0.85f),
                            Color(0xFF070913)
                        ),
                        radius = 1100f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // ==========================================
            // 1. TOP BAR
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back Button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.35f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                        .clickable { onNavigateToHome() },
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
                    text = "Profile & Wealth Hub",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Share QR Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.35f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                        .clickable {
                            showToast("✓ Personal QR copied: @kumar10")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "Share Profile QR",
                        tint = Color(0xFF00F2FE),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // ==========================================
            // 2. SCROLLABLE BODY
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 110.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // --- HERO AVATAR WITH HOLOGRAPHIC ROTATING RING ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(112.dp)
                    ) {
                        // Rotating GPU-accelerated Neon Gradient Halo
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationZ = ringRotation
                                }
                                .clip(CircleShape)
                                .background(Brush.sweepGradient(holographicColors))
                        )

                        // Dark spacer ring
                        Box(
                            modifier = Modifier
                                .size(104.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0B0D16))
                        )

                        // Avatar photo
                        Image(
                            painter = painterResource(R.drawable.img_profile),
                            contentDescription = "Profile Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                        )

                        // VIP Verified Tick Badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-4).dp, y = (-4).dp)
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Brush.horizontalGradient(listOf(Color(0xFF00F2FE), Color(0xFF00E676))))
                                .border(2.dp, Color(0xFF070913), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Verified VIP",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Saurabh Kumar",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .clickable { showToast("Alias: aura.saurabh copied") }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "@kumar10 • aura.saurabh",
                            color = Color(0xFF00F2FE),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = Color(0xFF00F2FE),
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tier VIP Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFFFD700).copy(alpha = 0.2f), Color(0xFFFF9100).copy(alpha = 0.2f))
                                )
                            )
                            .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "✦", color = Color(0xFFFFD700), fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Titanium Elite Tier • Level 4",
                                color = Color(0xFFFFD700),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- 3 QUICK-STAT TILES (Glassmorphic & Interactive) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickStatTile(
                        modifier = Modifier.weight(1f),
                        title = "Security",
                        value = "100%",
                        caption = "Shielded",
                        accentColor = Color(0xFF00E676),
                        icon = Icons.Default.Shield,
                        onClick = { showToast("Security status: Fully protected with KeyStore v3") }
                    )

                    QuickStatTile(
                        modifier = Modifier.weight(1f),
                        title = "Cashback",
                        value = "5.0%",
                        caption = "Metal Tier",
                        accentColor = Color(0xFF00F2FE),
                        icon = Icons.Default.Paid,
                        onClick = { showToast("Active 5% Universal Cashback across all cards") }
                    )

                    QuickStatTile(
                        modifier = Modifier.weight(1f),
                        title = "Points",
                        value = "32.4k",
                        caption = "AuraPoints",
                        accentColor = Color(0xFFFF80AB),
                        icon = Icons.Default.Diamond,
                        onClick = { showToast("32,400 AuraPoints available in your Vault") }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- 2 FEATURE HERO CARDS (Plan & Referrals) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    VibrantActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Plan Titanium",
                        subtitle = "Exclusive concierge & 0% FX fees",
                        badge = "ACTIVE",
                        badgeColor = Color(0xFF00F2FE),
                        gradient = listOf(Color(0xFF142442), Color(0xFF0F172C), Color(0xFF090D18)),
                        borderGradient = listOf(Color(0xFF00F2FE), Color(0xFF6B11FF)),
                        icon = Icons.Default.CreditCard,
                        onClick = {
                            selectedModalInfo = Pair("Titanium Reserve Plan", "Your Titanium membership grants zero foreign exchange fees, dedicated 24/7 wealth concierge, and 5 complimentary airport lounge visits.")
                        }
                    )

                    VibrantActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Invite & Earn",
                        subtitle = "$50 for you, $25 for your friend",
                        badge = "$450 WON",
                        badgeColor = Color(0xFFFFD700),
                        gradient = listOf(Color(0xFF33240F), Color(0xFF1C1307), Color(0xFF0D0903)),
                        borderGradient = listOf(Color(0xFFFFD700), Color(0xFFFF9100)),
                        icon = Icons.Default.PersonAdd,
                        onClick = {
                            selectedModalInfo = Pair("Referral Wealth Network", "Share your exclusive code 'AURA-TITANIUM-789'. You earn $50 instant cash when your friend activates their Titanium card.")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- SETTINGS HUB SECTION 1: ACCOUNT & STATEMENTS ---
                SectionHeader("ACCOUNT & STATEMENTS")
                Spacer(modifier = Modifier.height(8.dp))
                GlassSettingsCluster {
                    VibrantSettingItem(
                        icon = Icons.Default.Person,
                        iconBg = Color(0xFF00F2FE),
                        title = "Personal Information",
                        subtitle = "Name, legal DNI, verified email & phone",
                        onClick = {
                            selectedModalInfo = Pair("Personal Information", "Account Holder: Saurabh Kumar\nDNI / Tax ID: 74819203\nEmail: saurabh.k@auranova.bank\nPhone: +51 984 123 456")
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                    VibrantSettingItem(
                        icon = Icons.AutoMirrored.Filled.List,
                        iconBg = Color(0xFF6B11FF),
                        title = "Documents & Monthly Statements",
                        subtitle = "Official monthly PDF account summaries",
                        onClick = {
                            showToast("✓ October 2026 statement PDF downloaded")
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                    VibrantSettingItem(
                        icon = Icons.Default.AccountBalance,
                        iconBg = Color(0xFF00E676),
                        title = "Linked Accounts & Direct Deposit",
                        subtitle = "TechCorp payroll & linked bank routing",
                        onClick = {
                            selectedModalInfo = Pair("Linked Banking & Routing", "Routing Number: 021000089\nAccount Number: •••• 4923\nSalary Deposit: Active (1.5x APY Booster)")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- SETTINGS HUB SECTION 2: SECURITY & DEVICE SHIELD ---
                SectionHeader("SECURITY & APP SHIELD")
                Spacer(modifier = Modifier.height(8.dp))
                GlassSettingsCluster {
                    VibrantSettingItem(
                        icon = Icons.Default.Fingerprint,
                        iconBg = Color(0xFF00E676),
                        title = "Biometric Lock",
                        subtitle = "Fingerprint / FaceID security active",
                        trailingContent = {
                            Text("ENABLED", color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        },
                        onClick = { showToast("Biometric verification is permanently enabled") }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                    VibrantSettingItem(
                        icon = Icons.Default.Lock,
                        iconBg = Color(0xFFFFB300),
                        title = "Change App Passcode",
                        subtitle = "Update 4-digit master access PIN",
                        onClick = {
                            selectedModalInfo = Pair("Change Security Passcode", "Enter your current 4-digit PIN to establish a new encrypted access credential.")
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                    VibrantSettingItem(
                        icon = Icons.Default.Devices,
                        iconBg = Color(0xFF00F2FE),
                        title = "Authorized Devices",
                        subtitle = "Pixel 8 Pro • Lima, PE (Current)",
                        trailingContent = {
                            Text("1 Device", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                        },
                        onClick = { showToast("Only your Pixel 8 Pro is authorized") }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- SETTINGS HUB SECTION 3: APP PREFERENCES ---
                SectionHeader("PREFERENCES & SUPPORT")
                Spacer(modifier = Modifier.height(8.dp))
                GlassSettingsCluster {
                    VibrantSettingItem(
                        icon = Icons.Default.Notifications,
                        iconBg = Color(0xFFFF80AB),
                        title = "Notification Preferences",
                        subtitle = "Spend alerts, security & marketing",
                        onClick = { showToast("Push notifications configured") }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                    VibrantSettingItem(
                        icon = Icons.AutoMirrored.Filled.Help,
                        iconBg = Color(0xFF00F2FE),
                        title = "24/7 VIP Concierge Support",
                        subtitle = "Direct chat with private wealth banker",
                        trailingContent = {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF00E676).copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text("ONLINE", color = Color(0xFF00E676), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        },
                        onClick = {
                            selectedModalInfo = Pair("VIP Wealth Concierge", "Your dedicated banker, Mateo Arriaga, is available 24/7 via secure end-to-end encrypted messaging.")
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                    VibrantSettingItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        iconBg = Color(0xFFFF5252),
                        title = "Log Out of AuraNova",
                        subtitle = "Safely close active banking session",
                        textColor = Color(0xFFFF5252),
                        onClick = { showLogoutDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // --- APP VERSION & REGULATION INFO ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "AuraNova Digital Bank v10.68 (Build 2026.11)",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Regulated by SBS & Protected by Banking Insurance Fund",
                        color = Color.White.copy(alpha = 0.35f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // --- BOTTOM NAVIGATION BAR ---
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            NeoBottomNavigationBar(
                selectedTab = NavTab.Profile,
                onTabSelected = { tab ->
                    when (tab) {
                        NavTab.Home -> onNavigateToHome()
                        NavTab.Cards -> onNavigateToCards()
                        NavTab.History -> onNavigateToHistory()
                        NavTab.Profile -> {}
                        NavTab.Transfer -> {}
                    }
                }
            )
        }

        // --- FLOATING TOAST ---
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp)
        ) {
            if (toastMessage != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFF00F2FE), Color(0xFF00E676)))
                        )
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = toastMessage!!,
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // --- DETAIL INFORMATION BOTTOM SHEET ---
        if (selectedModalInfo != null) {
            val (title, description) = selectedModalInfo!!
            ModalBottomSheet(
                onDismissRequest = { selectedModalInfo = null },
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
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF00F2FE), Color(0xFF6B11FF), Color(0xFF00E676))
                                )
                            )
                    )
                },
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
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00F2FE).copy(alpha = 0.2f))
                            .border(1.5.dp, Color(0xFF00F2FE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF00F2FE), modifier = Modifier.size(30.dp))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = description,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .clickable { selectedModalInfo = null },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Close", color = Color(0xFF0A0D16), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- LOGOUT CONFIRMATION DIALOG ---
        if (showLogoutDialog) {
            ModalBottomSheet(
                onDismissRequest = { showLogoutDialog = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = Color(0xFF0F111A),
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
                            .background(Color(0xFFFF5252).copy(alpha = 0.2f))
                            .border(1.5.dp, Color(0xFFFF5252), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(32.dp))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Log out of AuraNova?", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "You will need to re-authenticate with your biometrics or passcode to access your accounts.",
                        color = Color.White.copy(alpha = 0.65f),
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
                            .clickable {
                                showLogoutDialog = false
                                onLogout()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Confirm Log Out", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                            .clickable { showLogoutDialog = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cancel", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// =============================================================
// SUB-COMPONENTS FOR REDESIGNED PROFILE
// =============================================================

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF00F2FE),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun GlassSettingsCluster(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF111422).copy(alpha = 0.75f))
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.04f))
                ),
                RoundedCornerShape(22.dp)
            ),
        content = content
    )
}

@Composable
private fun QuickStatTile(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    caption: String,
    accentColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF101322).copy(alpha = 0.8f))
            .border(
                1.dp,
                Brush.verticalGradient(listOf(accentColor.copy(alpha = 0.45f), Color.White.copy(alpha = 0.05f))),
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = value, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(text = caption, color = Color.White.copy(alpha = 0.55f), fontSize = 11.sp)
        }
    }
}

@Composable
private fun VibrantActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    gradient: List<Color>,
    borderGradient: List<Color>,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(gradient))
            .border(1.2.dp, Brush.horizontalGradient(borderGradient), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(22.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeColor.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = badge, color = badgeColor, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun VibrantSettingItem(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String,
    textColor: Color = Color.White,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(iconBg.copy(alpha = 0.18f))
                .border(1.dp, iconBg.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconBg,
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 11.sp
            )
        }

        if (trailingContent != null) {
            trailingContent()
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.35f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}