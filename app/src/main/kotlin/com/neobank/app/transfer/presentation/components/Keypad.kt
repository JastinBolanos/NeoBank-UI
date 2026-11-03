package com.neobank.app.transfer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neobank.app.R

@Composable
fun Keypad(
    onNumberClick: (String) -> Unit,
    onDotClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KeypadButton(text = "1", onClick = { onNumberClick("1") }, modifier = Modifier.weight(1f))
            KeypadButton(text = "2", onClick = { onNumberClick("2") }, modifier = Modifier.weight(1f))
            KeypadButton(text = "3", onClick = { onNumberClick("3") }, modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KeypadButton(text = "4", onClick = { onNumberClick("4") }, modifier = Modifier.weight(1f))
            KeypadButton(text = "5", onClick = { onNumberClick("5") }, modifier = Modifier.weight(1f))
            KeypadButton(text = "6", onClick = { onNumberClick("6") }, modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KeypadButton(text = "7", onClick = { onNumberClick("7") }, modifier = Modifier.weight(1f))
            KeypadButton(text = "8", onClick = { onNumberClick("8") }, modifier = Modifier.weight(1f))
            KeypadButton(text = "9", onClick = { onNumberClick("9") }, modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KeypadButtonPlain(text = ".", onClick = onDotClick, modifier = Modifier.weight(1f))
            KeypadButton(text = "0", onClick = { onNumberClick("0") }, modifier = Modifier.weight(1f))
            KeypadButtonDeletePlain(
                icon = painterResource(R.drawable.ic_backspace_custom),
                onClick = onDeleteClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun KeypadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(53.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun KeypadButtonPlain(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(53.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun KeypadButtonDeletePlain(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(53.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = "Delete",
            tint = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.size(24.dp)
        )
    }
}