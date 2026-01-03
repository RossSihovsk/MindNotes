package com.ross.mindnotes.presentation.notes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.ross.mindnotes.domain.model.Note
import com.ross.mindnotes.presentation.util.color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecentNoteItem(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val date = remember(note.timestamp) { Date(note.timestamp) }
    val monthFormat = remember { SimpleDateFormat("MMM", Locale.getDefault()) }
    val dayFormat = remember { SimpleDateFormat("dd", Locale.getDefault()) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(note.category.color.copy(alpha = 0.2f)) // Tinted background
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(
                        text = monthFormat.format(date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = dayFormat.format(date),
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Image area (placeholder simulation or actual image if available)
            if (note.images.isNotEmpty()) {
                val images = note.images
                val imageCount = images.size
                val collageHeight = 250.dp
                val cornerRadius = 16.dp

                when (imageCount) {
                    1 -> {
                        Image(
                            painter = rememberAsyncImagePainter(images[0]),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(collageHeight)
                                .clip(RoundedCornerShape(cornerRadius)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    2 -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(collageHeight)
                                .clip(RoundedCornerShape(cornerRadius))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(images[0]),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Image(
                                painter = rememberAsyncImagePainter(images[1]),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    else -> { // 3 or more
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(collageHeight)
                                .clip(RoundedCornerShape(cornerRadius))
                        ) {
                            // First image takes left half
                            Image(
                                painter = rememberAsyncImagePainter(images[0]),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            // Next two images stacked on right half
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(images[1]),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Image(
                                    painter = rememberAsyncImagePainter(images[2]), // Use 3rd image
                                    contentDescription = null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            } else {
                // Formatting for text preview or "No Image" state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                ) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 6,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
