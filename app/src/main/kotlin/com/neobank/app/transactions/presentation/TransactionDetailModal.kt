package com.neobank.app.transactions.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.neobank.app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class DetailedTransaction(
    val id: String,
    val merchant: String,
    val subtitle: String,
    val amount: String,
    val rawAmount: Double,
    @DrawableRes val iconRes: Int,
    val category: String,
    val date: String,
    val time: String,
    val referenceId: String,
    val authCode: String,
    val cardUsed: String,
    val location: String,
    val status: String = "Completed",
    val auraPointsEarned: Int,
    val brandColor: Color,
    val isSubscription: Boolean = true
)

object TransactionRepository {
    val sampleTransactions = listOf(
        DetailedTransaction(
            id = "txn_1",
            merchant = "Google",
            subtitle = "Google Workspace Subscription",
            amount = "- $12.00",
            rawAmount = 12.00,
            iconRes = R.drawable.ic_google,
            category = "Software & Cloud Services",
            date = "Nov 04, 2026",
            time = "14:32:18 EST",
            referenceId = "TXN-GOOG-882941-AURA",
            authCode = "AUTH #829104 (Google Pay)",
            cardUsed = "Aura Titanium Metal (•••• 1289)",
            location = "Mountain View, CA, USA",
            status = "Completed",
            auraPointsEarned = 24,
            brandColor = Color(0xFF4285F4)
        ),
        DetailedTransaction(
            id = "txn_2",
            merchant = "YouTube",
            subtitle = "YouTube Premium Family",
            amount = "- $11.99",
            rawAmount = 11.99,
            iconRes = R.drawable.ic_youtube,
            category = "Digital Media & Streaming",
            date = "Nov 03, 2026",
            time = "10:15:02 EST",
            referenceId = "TXN-YTBE-471092-AURA",
            authCode = "AUTH #619302 (Recurring AutoPay)",
            cardUsed = "Aura Titanium Metal (•••• 1289)",
            location = "San Bruno, CA, USA",
            status = "Completed",
            auraPointsEarned = 24,
            brandColor = Color(0xFFFF0000)
        ),
        DetailedTransaction(
            id = "txn_3",
            merchant = "Facebook",
            subtitle = "Ads Campaign Billing",
            amount = "- $50.00",
            rawAmount = 50.00,
            iconRes = R.drawable.ic_facebook,
            category = "Business Marketing & Ads",
            date = "Nov 02, 2026",
            time = "19:44:50 EST",
            referenceId = "TXN-META-190342-AURA",
            authCode = "AUTH #349120 (Direct Debit)",
            cardUsed = "Aura Virtual Commercial (•••• 1388)",
            location = "Menlo Park, CA, USA",
            status = "Completed",
            auraPointsEarned = 100,
            brandColor = Color(0xFF1877F2)
        ),
        DetailedTransaction(
            id = "txn_4",
            merchant = "Visa",
            subtitle = "Credit Card Payment",
            amount = "- $150.00",
            rawAmount = 150.00,
            iconRes = R.drawable.ic_visa,
            category = "Financial Services & Credit Line",
            date = "Nov 01, 2026",
            time = "08:00:12 EST",
            referenceId = "TXN-VISA-661902-AURA",
            authCode = "AUTH #991042 (ACH Instant Clearing)",
            cardUsed = "Primary Checking Vault #AURA-01",
            location = "Foster City, CA, USA",
            status = "Completed",
            auraPointsEarned = 300,
            brandColor = Color(0xFF1A1F71)
        ),
        DetailedTransaction(
            id = "txn_5",
            merchant = "Claro",
            subtitle = "Monthly Internet Bill",
            amount = "- $35.00",
            rawAmount = 35.00,
            iconRes = R.drawable.ic_claro,
            category = "Telecommunications & Fiber",
            date = "Oct 31, 2026",
            time = "16:20:00 EST",
            referenceId = "TXN-CLRO-312984-AURA",
            authCode = "AUTH #771239 (Pre-authorized)",
            cardUsed = "Aura Titanium Metal (•••• 1289)",
            location = "Av. Javier Prado 1200, Lima, PE",
            status = "Completed",
            auraPointsEarned = 70,
            brandColor = Color(0xFFE62117)
        ),
        DetailedTransaction(
            id = "txn_6",
            merchant = "Google",
            subtitle = "Cloud Storage Plan",
            amount = "- $9.99",
            rawAmount = 9.99,
            iconRes = R.drawable.ic_google,
            category = "Cloud Infrastructure & Backup",
            date = "Oct 29, 2026",
            time = "11:11:45 EST",
            referenceId = "TXN-GCP-771920-AURA",
            authCode = "AUTH #449201 (Cloud Billing)",
            cardUsed = "Aura Titanium Metal (•••• 1289)",
            location = "Mountain View, CA, USA",
            status = "Completed",
            auraPointsEarned = 20,
            brandColor = Color(0xFF0F9D58)
        ),
        DetailedTransaction(
            id = "txn_7",
            merchant = "YouTube",
            subtitle = "YouTube Music Premium",
            amount = "- $4.99",
            rawAmount = 4.99,
            iconRes = R.drawable.ic_youtube,
            category = "Digital Music & Audio",
            date = "Oct 27, 2026",
            time = "20:04:18 EST",
            referenceId = "TXN-YTMU-001928-AURA",
            authCode = "AUTH #128941",
            cardUsed = "Aura Titanium Metal (•••• 1289)",
            location = "San Bruno, CA, USA",
            status = "Completed",
            auraPointsEarned = 10,
            brandColor = Color(0xFFFF0033)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailModal(
    transaction: DetailedTransaction,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var toastText by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun showToast(msg: String) {
        toastText = msg
        coroutineScope.launch {
            delay(2400)
            toastText = null
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color(0xFF0C0E16),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 6.dp)
                    .width(46.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                transaction.brandColor,
                                Color(0xFF00F2FE),
                                Color(0xFF00E676)
                            )
                        )
                    )
            )
        },
        contentColor = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Row with Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00E676))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = transaction.status.uppercase(),
                            color = Color(0xFF00E676),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Merchant Brand Avatar with Glowing Ambient Halo
                Box(contentAlignment = Alignment.Center) {
                    // Ambient halo
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        transaction.brandColor.copy(alpha = 0.35f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Logo container
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF161926))
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(
                                    listOf(
                                        transaction.brandColor,
                                        Color.White.copy(alpha = 0.3f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(transaction.iconRes),
                            contentDescription = transaction.merchant,
                            modifier = Modifier.size(34.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Merchant Title & Category
                Text(
                    text = transaction.merchant,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transaction.category,
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Hero Amount Section
                Text(
                    text = transaction.amount,
                    color = Color.White,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rewards Earned Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF00F2FE).copy(alpha = 0.12f))
                        .border(
                            width = 1.dp,
                            color = Color(0xFF00F2FE).copy(alpha = 0.35f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00F2FE),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+${transaction.auraPointsEarned} AuraPoints Rewards Earned",
                            color = Color(0xFF00F2FE),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Detailed Receipt Card (Frosted Glass Container)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.04f))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        ReceiptDetailRow(label = "Date & Time", value = "${transaction.date} • ${transaction.time}")

                        ReceiptDetailRow(label = "Payment Method", value = transaction.cardUsed)

                        ReceiptDetailRow(label = "Type", value = transaction.subtitle)

                        ReceiptDetailRow(label = "Location", value = transaction.location)

                        ReceiptDetailRow(label = "Auth Code", value = transaction.authCode)

                        // Reference Number with Copy Action
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Reference No.",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = transaction.referenceId,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Transaction Reference", transaction.referenceId)
                                    clipboard.setPrimaryClip(clip)
                                    showToast("✓ Reference copied to clipboard")
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Reference",
                                    tint = Color(0xFF00F2FE),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Interactive Action Tiles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TransactionActionButton(
                        icon = Icons.Default.Share,
                        label = "Export PDF",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showToast("✓ PDF receipt downloaded to storage")
                        }
                    )

                    TransactionActionButton(
                        icon = Icons.AutoMirrored.Filled.Send,
                        label = "Split Bill",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showToast("✓ Split payment link generated")
                        }
                    )

                    TransactionActionButton(
                        icon = Icons.Default.Warning,
                        label = "Report",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showToast("✓ Ticket #SUP-481 opened with Priority Support")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Floating Toast inside sheet
            androidx.compose.animation.AnimatedVisibility(
                visible = toastText != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            ) {
                if (toastText != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF00E676), Color(0xFF00B0FF))
                                )
                            )
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = toastText!!,
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier.weight(0.9f)
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.3f)
        )
    }
}

@Composable
private fun TransactionActionButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF00F2FE),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
