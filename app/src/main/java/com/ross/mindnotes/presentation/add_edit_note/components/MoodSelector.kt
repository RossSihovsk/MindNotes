package com.ross.mindnotes.presentation.add_edit_note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoodSelector(
    selectedMood: Int,
    onMoodSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val moods = listOf(
        1 to Color(0xFFEF9A9A), // Red
        2 to Color(0xFFFFCC80), // Orange
        3 to Color(0xFFE0E0E0), // Light Grey
        4 to Color(0xFFA5D6A7), // Light Green
        5 to Color(0xFF80CBC4)  // Green
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        moods.forEach { (moodValue, color) ->
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(15.dp, CircleShape)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = 3.dp,
                        color = if (selectedMood == moodValue) Color.Black else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable {
                        onMoodSelected(moodValue)
                    }
            )
        }
    }
}
