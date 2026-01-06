package com.ross.mindnotes.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ross.mindnotes.presentation.notes.components.CompactNoteItem
import com.ross.mindnotes.presentation.util.Screen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val daysInMonth = state.currentMonth.lengthOfMonth()
    val firstDayOfMonth = state.currentMonth.atDay(1)
    val dayOfWeekOffset = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday, etc. depending on locale, adjusting for grid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Month Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${state.currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${state.currentMonth.year}",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Days Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Day Names
            items(7) { index ->
                val dayName = java.time.DayOfWeek.of((index + 6) % 7 + 1) // Adjust to start Mon or Sun
                    .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                Text(
                    text = dayName,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Empty slots for offset
            items(dayOfWeekOffset) {
                Box(modifier = Modifier.aspectRatio(1f))
            }

            // Days
            items(daysInMonth) { day ->
                val date = state.currentMonth.atDay(day + 1)
                val notesForDay = state.notesByDate[date] ?: emptyList()
                val isSelected = date == state.selectedDate
                
                // Calculate average mood color if notes exist
                val isDark = androidx.compose.foundation.isSystemInDarkTheme()
                val moodColor = if (notesForDay.isNotEmpty()) {
                    val avgMood = notesForDay.map { it.mood }.average().toInt()
                    if (isDark) {
                        when (avgMood) {
                            1 -> Color(0xFFEF9A9A) // Light Red
                            2 -> Color(0xFFFFCC80) // Light Orange
                            3 -> Color(0xFF81D4FA) // Light Blue (instead of yellow)
                            4 -> Color(0xFFA5D6A7) // Light Green
                            5 -> Color(0xFF80CBC4) // Light Teal
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    } else {
                        when (avgMood) {
                            1 -> Color(0xFFD32F2F) // Red
                            2 -> Color(0xFFF57C00) // Orange
                            3 -> Color(0xFFFBC02D) // Yellow
                            4 -> Color(0xFF388E3C) // Green
                            5 -> Color(0xFF00796B) // Teal
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    }
                } else {
                    MaterialTheme.colorScheme.surface
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else moodColor)
                        .clickable { viewModel.onEvent(CalendarEvent.SelectDate(date)) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (day + 1).toString(),
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else if (notesForDay.isNotEmpty()) {
                            Color(0xFF202020)
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM d yyyy", Locale.getDefault()) }
        Text(
            text = "Notes for ${state.selectedDate.format(dateFormatter)}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Notes List for Selected Date
        val selectedNotes = state.notesByDate[state.selectedDate] ?: emptyList()
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedNotes) { note ->
                CompactNoteItem(
                    note = note,
                    onClick = {
                        navController.navigate(Screen.AddEditNoteScreen.route + "?noteId=${note.id}")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (selectedNotes.isEmpty()) {
                item {
                    Text(
                        text = "No notes for this day.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
