package com.neobank.app.transfer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R

val CinzelDecorative: FontFamily = FontFamily(
    Font(R.font.cinzel_decorative_regular, FontWeight.Normal)
)

@Composable
fun NoteField(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black.copy(alpha = 0.4f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_dots_grid),
            contentDescription = "Add note",
            tint = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "ADD NOTE",
            style = TextStyle(
                fontFamily = CinzelDecorative,
                fontWeight = FontWeight.Normal,
                fontSize = 15.63.sp,
                letterSpacing = (-0.25).sp,
                color = Color.White.copy(alpha = 0.85f)
            )
        )
    }
}