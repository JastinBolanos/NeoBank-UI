package com.neobank.app.cards.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardItem(
    balance: String,
    cardNumber: String,
    expiryDate: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.58f)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // --- GROUP 1 (TOP): Title and Balance ---
            Column {
                Text(
                    text = "Current Balance",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = balance,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.15f),
                            offset = Offset(0f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
            }

            // --- GROUP 2 (MIDDLE): Card Number ---
            Text(
                text = cardNumber.replace(" ", "  "),
                color = Color.White,
                fontSize = 18.sp,
                letterSpacing = 2.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.1f),
                        offset = Offset(0f, 2f),
                        blurRadius = 4f
                    )
                )
            )

            // --- GROUP 3 (BOTTOM): Date and Logo ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Expiry Date
                Text(
                    text = expiryDate,
                    color = Color.White,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp
                )

                // Mastercard Logo
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(26.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .align(Alignment.CenterStart)
                                .clip(CircleShape)
                                .background(Color(0xFFEB001B))
                        )
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .align(Alignment.CenterEnd)
                                .clip(CircleShape)
                                .background(Color(0xFFF79E1B).copy(alpha = 0.9f))
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "mastercard",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}