package com.neobank.app.notifications.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

enum class NotificationCategory(val displayName: String) {
    ALL("All"),
    TRANSFERS("Transfers"),
    SECURITY("Security"),
    REWARDS("Rewards"),
    SYSTEM("System")
}

data class NeoNotification(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val category: NotificationCategory,
    val icon: ImageVector,
    val accentColor: Color,
    val amount: String? = null,
    val isPositiveAmount: Boolean = true,
    var isRead: Boolean = false,
    val actionText: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onNavigateToTransfer: () -> Unit = {},
    onNavigateToCards: () -> Unit = {}
) {
    val initialNotifications = remember {
        listOf(
            NeoNotification(
                id = "1",
                title = "Money Received via Plin",
                description = "Elena Cruz sent you an instant transfer into your Main account.",
                timestamp = "Just now",
                category = NotificationCategory.TRANSFERS,
                icon = Icons.Default.SwapHoriz,
                accentColor = Color(0xFF00E676),
                amount = "+$1,250.00",
                isPositiveAmount = true,
                isRead = false,
                actionText = "Send thank you note"
            ),
            NeoNotification(
                id = "2",
                title = "Security Alert: New Sign-In",
                description = "New device authorized from Lima, PE (Chrome on macOS). If this was not you, freeze your cards immediately.",
                timestamp = "18m ago",
                category = NotificationCategory.SECURITY,
                icon = Icons.Default.Shield,
                accentColor = Color(0xFFFF5252),
                isRead = false,
                actionText = "Review security settings"
            ),
            NeoNotification(
                id = "3",
                title = "Cashback Credited",
                description = "Your 5% NeoBank Metal tier reward has been unlocked and credited.",
                timestamp = "1h ago",
                category = NotificationCategory.REWARDS,
                icon = Icons.Default.Star,
                accentColor = Color(0xFFFFD700),
                amount = "+$45.80",
                isPositiveAmount = true,
                isRead = false,
                actionText = "View rewards wallet"
            ),
            NeoNotification(
                id = "4",
                title = "Apple Store Online",
                description = "Card payment approved for AuraNova Titanium Card ending in ••1289.",
                timestamp = "Yesterday",
                category = NotificationCategory.TRANSFERS,
                icon = Icons.Default.CreditCard,
                accentColor = Color(0xFF40C4FF),
                amount = "-$1,199.00",
                isPositiveAmount = false,
                isRead = true,
                actionText = "Download invoice"
            ),
            NeoNotification(
                id = "5",
                title = "Payroll Direct Deposit",
                description = "TechCorp Peru SAC deposited your monthly compensation.",
                timestamp = "3 days ago",
                category = NotificationCategory.TRANSFERS,
                icon = Icons.Default.AccountBalance,
                accentColor = Color(0xFF00E676),
                amount = "+$4,800.00",
                isPositiveAmount = true,
                isRead = true
            ),
            NeoNotification(
                id = "6",
                title = "Card PIN Updated",
                description = "The security PIN for your primary virtual card was modified via biometric authentication.",
                timestamp = "5 days ago",
                category = NotificationCategory.SECURITY,
                icon = Icons.Default.Lock,
                accentColor = Color(0xFFB388FF),
                isRead = true
            ),
            NeoNotification(
                id = "7",
                title = "VIP Airport Lounge Pass",
                description = "You have 2 complimentary VIP lounge visits ready at Jorge Chávez International.",
                timestamp = "1 week ago",
                category = NotificationCategory.REWARDS,
                icon = Icons.Default.Flight,
                accentColor = Color(0xFFFF80AB),
                isRead = true,
                actionText = "Show boarding voucher"
            )
        )
    }

    val notifications = remember { mutableStateListOf<NeoNotification>().apply { addAll(initialNotifications) } }
    var selectedCategory by remember { mutableStateOf(NotificationCategory.ALL) }
    var selectedDetailNotification by remember { mutableStateOf<NeoNotification?>(null) }

    val filteredList = remember(notifications.toList(), selectedCategory) {
        if (selectedCategory == NotificationCategory.ALL) {
            notifications
        } else {
            notifications.filter { it.category == selectedCategory }
        }
    }

    val unreadCount = notifications.count { !it.isRead }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
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
            // --- TOP BAR ---
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
                        text = "Notifications",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (unreadCount > 0) "$unreadCount unread alerts" else "All caught up",
                        color = if (unreadCount > 0) Color(0xFF00F2FE) else Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Mark All Read / Action
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .clickable {
                            notifications.forEachIndexed { index, item ->
                                notifications[index] = item.copy(isRead = true)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Mark all as read",
                        tint = if (unreadCount > 0) Color(0xFF00E676) else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // --- CATEGORY PILLS ---
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(NotificationCategory.values()) { category ->
                    val isSelected = selectedCategory == category
                    val count = if (category == NotificationCategory.ALL) {
                        notifications.size
                    } else {
                        notifications.count { it.category == category }
                    }

                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else Color.Black.copy(alpha = 0.4f),
                        animationSpec = tween(200),
                        label = "pillBg"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) Color.Black else Color.White.copy(alpha = 0.85f),
                        animationSpec = tween(200),
                        label = "pillText"
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(bgColor)
                            .border(
                                1.dp,
                                if (isSelected) Color.White else Color.White.copy(alpha = 0.15f),
                                RoundedCornerShape(24.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 16.dp, vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = category.displayName,
                                color = textColor,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color.Black.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.18f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = count.toString(),
                                    color = textColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // --- NOTIFICATIONS LIST ---
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No notifications in this tray",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "You will see transactions, security alerts and rewards here as they occur.",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        NotificationCard(
                            item = item,
                            onClick = {
                                // Mark as read and open detail
                                val index = notifications.indexOfFirst { it.id == item.id }
                                if (index != -1) {
                                    notifications[index] = item.copy(isRead = true)
                                }
                                selectedDetailNotification = item
                            }
                        )
                    }
                }
            }
        }

        // --- DETAIL BOTTOM SHEET ---
        if (selectedDetailNotification != null) {
            val item = selectedDetailNotification!!
            ModalBottomSheet(
                onDismissRequest = { selectedDetailNotification = null },
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
                            .background(item.accentColor.copy(alpha = 0.2f))
                            .border(1.5.dp, item.accentColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = item.accentColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = item.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    if (item.amount != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.amount,
                            color = if (item.isPositiveAmount) Color(0xFF00E676) else Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.timestamp,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.description,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (item.actionText != null) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .clickable {
                                    selectedDetailNotification = null
                                    if (item.category == NotificationCategory.TRANSFERS) {
                                        onNavigateToTransfer()
                                    } else if (item.category == NotificationCategory.SECURITY) {
                                        onNavigateToCards()
                                    }
                                },
                            color = Color.White,
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = item.actionText,
                                    color = Color(0xFF14161E),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Transparent)
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                            .clickable { selectedDetailNotification = null },
                        color = Color.Transparent,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Close",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    item: NeoNotification,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (item.isRead) Color.Black.copy(alpha = 0.35f) else Color.Black.copy(alpha = 0.55f))
            .border(
                width = 1.dp,
                brush = if (item.isRead) {
                    Brush.linearGradient(listOf(Color.White.copy(alpha = 0.12f), Color.White.copy(alpha = 0.05f)))
                } else {
                    Brush.linearGradient(listOf(item.accentColor.copy(alpha = 0.6f), Color.White.copy(alpha = 0.2f)))
                },
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Icon Pill
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(item.accentColor.copy(alpha = 0.15f))
                    .border(1.dp, item.accentColor.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = if (item.isRead) FontWeight.Medium else FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (!item.isRead) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00E676))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.description,
                    color = Color.White.copy(alpha = 0.72f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.timestamp,
                        color = Color.White.copy(alpha = 0.45f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (item.amount != null) {
                        Text(
                            text = item.amount,
                            color = if (item.isPositiveAmount) Color(0xFF00E676) else Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
