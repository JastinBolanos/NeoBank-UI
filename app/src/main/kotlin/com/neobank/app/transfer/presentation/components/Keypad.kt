package com.neobank.app.transfer.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
    onClearAll: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KeypadButton(digit = "1", onClick = { onNumberClick("1") }, modifier = Modifier.weight(1f))
            KeypadButton(digit = "2", onClick = { onNumberClick("2") }, modifier = Modifier.weight(1f))
            KeypadButton(digit = "3", onClick = { onNumberClick("3") }, modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KeypadButton(digit = "4", onClick = { onNumberClick("4") }, modifier = Modifier.weight(1f))
            KeypadButton(digit = "5", onClick = { onNumberClick("5") }, modifier = Modifier.weight(1f))
            KeypadButton(digit = "6", onClick = { onNumberClick("6") }, modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KeypadButton(digit = "7", onClick = { onNumberClick("7") }, modifier = Modifier.weight(1f))
            KeypadButton(digit = "8", onClick = { onNumberClick("8") }, modifier = Modifier.weight(1f))
            KeypadButton(digit = "9", onClick = { onNumberClick("9") }, modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KeypadDotButton(onClick = onDotClick, modifier = Modifier.weight(1f))
            KeypadButton(digit = "0", onClick = { onNumberClick("0") }, modifier = Modifier.weight(1f))
            KeypadDeleteButton(
                onClick = onDeleteClick,
                onLongClick = onClearAll,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun KeypadButton(
    digit: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "btnScale"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isPressed) Color.White.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.08f),
        label = "btnBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isPressed) Color(0xFF00F5FF).copy(alpha = 0.6f) else Color.White.copy(alpha = 0.12f),
        label = "btnBorder"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun KeypadDotButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "dotScale"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isPressed) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.04f),
        label = "dotBg"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(RoundedCornerShape(3.5.dp))
                .background(Color.White.copy(alpha = 0.9f))
        )
    }
}

@Composable
private fun KeypadDeleteButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "delScale"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isPressed) Color(0xFFFF3366).copy(alpha = 0.25f) else Color.White.copy(alpha = 0.04f),
        label = "delBg"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() },
                    onLongPress = { onLongClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_backspace_custom),
            contentDescription = "Backspace",
            tint = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.size(22.dp)
        )
    }
}