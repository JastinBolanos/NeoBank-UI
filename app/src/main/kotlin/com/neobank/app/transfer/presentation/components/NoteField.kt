package com.neobank.app.transfer.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PRESET_NOTES = listOf(
    "🍽️ Dinner & Drinks",
    "⚡ Aura Transfer",
    "🎁 Gift / Tip",
    "☕ Coffee & Snack",
    "🏠 Rent & Bills",
    "✈️ Travel / Trip",
    "💼 Freelance / Work"
)

@Composable
fun NoteField(
    noteText: String,
    onNoteChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Active Note Pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.07f))
                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                .clickable { isEditing = !isEditing }
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (noteText.isBlank()) "Add Note / Category" else noteText,
                color = if (noteText.isBlank()) Color.White.copy(alpha = 0.6f) else Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                contentDescription = "Edit note",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(13.dp)
            )
        }

        // Inline tag selector and text input when expanded
        AnimatedVisibility(visible = isEditing) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Quick chip row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(PRESET_NOTES) { preset ->
                        val isSelected = noteText == preset
                        val chipBg by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFF6366F1).copy(alpha = 0.35f) else Color.White.copy(alpha = 0.06f),
                            label = "chipBg"
                        )
                        val chipBorder by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFF818CF8) else Color.White.copy(alpha = 0.1f),
                            label = "chipBorder"
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(chipBg)
                                .border(1.dp, chipBorder, RoundedCornerShape(14.dp))
                                .clickable {
                                    onNoteChange(preset)
                                    isEditing = false
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = preset,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Custom typing field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black.copy(alpha = 0.3f))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = noteText,
                        onValueChange = onNoteChange,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        cursorBrush = SolidColor(Color(0xFF00F5FF)),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (noteText.isEmpty()) {
                                Text(
                                    text = "Type custom purpose...",
                                    color = Color.White.copy(alpha = 0.4f),
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    )

                    if (noteText.isNotEmpty()) {
                        Text(
                            text = "Done",
                            color = Color(0xFF00F5FF),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { isEditing = false }
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}