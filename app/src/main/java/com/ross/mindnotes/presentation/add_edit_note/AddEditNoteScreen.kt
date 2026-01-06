package com.ross.mindnotes.presentation.add_edit_note

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Precision
import coil.size.Size
import com.ross.mindnotes.domain.model.Category
import com.ross.mindnotes.presentation.add_edit_note.components.CategorySelector
import com.ross.mindnotes.presentation.add_edit_note.components.TransparentHintTextField
import com.ross.mindnotes.presentation.util.description
import com.ross.mindnotes.presentation.util.displayName
import com.ross.mindnotes.presentation.util.color
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val noteCategory = viewModel.noteCategory.value
    val noteImages = viewModel.noteImages.value
    val noteDate = viewModel.noteDate.value
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Multiple Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            val uriStrings = uris.map { it.toString() }
            viewModel.onEvent(AddEditNoteEvent.AddImages(uriStrings))
        }
    )

    // Date Picker State
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = noteDate)

    // Category Selector Dialog (Simple implementation)
    var showCategorySelector by remember { mutableStateOf(false) }
    
    // Full Screen Image Viewer State
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event: AddEditNoteViewModel.UiEvent ->
            when(event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onEvent(AddEditNoteEvent.ChangeDate(it))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    // Full Screen Image Dialog
    selectedImageUri?.let { uri ->
        Dialog(
            onDismissRequest = { selectedImageUri = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(Color.Black)
                     .clickable { selectedImageUri = null }, // Click to close
                 contentAlignment = Alignment.Center
            ) {
                 Image(
                     painter = rememberAsyncImagePainter(
                         model = ImageRequest.Builder(LocalContext.current)
                             .data(uri)
                             .size(Size.ORIGINAL)
                             .precision(Precision.EXACT)
                             .allowHardware(false)
                             .crossfade(true)
                             .build(),
                         filterQuality = FilterQuality.High
                     ),
                     contentDescription = "Full screen image",
                     modifier = Modifier.fillMaxSize(),
                     contentScale = ContentScale.Fit
                 )
                // Close button overlay
                IconButton(
                    onClick = { selectedImageUri = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Back")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                   IconButton(onClick = {
                        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                   }) {
                       Icon(imageVector = Icons.Default.Add, contentDescription = "Add Image")
                   }
                   Spacer(modifier = Modifier.width(8.dp))
                   Button(
                       onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
                       colors = ButtonDefaults.buttonColors(
                           containerColor = MaterialTheme.colorScheme.primary,
                           contentColor = MaterialTheme.colorScheme.onPrimary
                       ),
                       shape = RoundedCornerShape(20.dp)
                   ) {
                       Text("Save")
                   }
                   Spacer(modifier = Modifier.width(8.dp))
                   IconButton(onClick = { viewModel.onEvent(AddEditNoteEvent.DeleteNote) }) {
                       Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
                   }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Topic Selector
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { showCategorySelector = !showCategorySelector }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Topic",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            if (showCategorySelector) {
                 // Inline selector logic (handled below for now in Body if visible, or here if Overlay)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Image Gallery (Collage Chunks Horizontal)
            if (noteImages.isNotEmpty()) {
                val chunks = noteImages.chunked(3)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(chunks) { chunk ->
                        ImageCollageBlock(
                            images = chunk,
                            onImageClick = { uri -> selectedImageUri = uri },
                            onImageRemove = { uri -> viewModel.onEvent(AddEditNoteEvent.RemoveImage(uri)) },
                            modifier = Modifier.fillParentMaxWidth(0.95f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            if(showCategorySelector) {
                CategorySelector(
                    selectedCategory = noteCategory,
                    onCategorySelected = {
                        viewModel.onEvent(AddEditNoteEvent.SelectCategory(it))
                        showCategorySelector = false
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Category Info Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer) // Dark grey card
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = noteCategory.displayName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = noteCategory.color 
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = noteCategory.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = false,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date Selection
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showDatePicker = true }
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
                Text(
                    text = dateFormat.format(Date(noteDate)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxSize()
            )
            
            Spacer(modifier = Modifier.height(80.dp)) // Space for scrolling
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCollageBlock(
    images: List<String>,
    onImageClick: (String) -> Unit,
    onImageRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val collageHeight = 250.dp
    val cornerRadius = 16.dp

    when (images.size) {
        1 -> {
            val uri = images[0]
            CollageImageItem(
                uri = uri,
                modifier = modifier
                    .height(collageHeight)
                    .clip(RoundedCornerShape(cornerRadius)),
                onClick = { onImageClick(uri) },
                onLongClick = { onImageRemove(uri) }
            )
        }
        2 -> {
            Row(
                modifier = modifier
                    .height(collageHeight)
                    .clip(RoundedCornerShape(cornerRadius))
            ) {
                CollageImageItem(
                    uri = images[0],
                    modifier = Modifier.weight(1f).fillMaxSize(),
                    onClick = { onImageClick(images[0]) },
                    onLongClick = { onImageRemove(images[0]) }
                )
                Spacer(modifier = Modifier.width(4.dp))
                CollageImageItem(
                    uri = images[1],
                    modifier = Modifier.weight(1f).fillMaxSize(),
                    onClick = { onImageClick(images[1]) },
                    onLongClick = { onImageRemove(images[1]) }
                )
            }
        }
        else -> { // 3 (chunked max is 3)
            Row(
                modifier = modifier
                    .height(collageHeight)
                    .clip(RoundedCornerShape(cornerRadius))
            ) {
                CollageImageItem(
                    uri = images[0],
                    modifier = Modifier.weight(1f).fillMaxSize(),
                    onClick = { onImageClick(images[0]) },
                    onLongClick = { onImageRemove(images[0]) }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column(
                    modifier = Modifier.weight(1f).fillMaxSize()
                ) {
                    CollageImageItem(
                        uri = images[1],
                        modifier = Modifier.weight(1f).fillMaxSize(),
                        onClick = { onImageClick(images[1]) },
                        onLongClick = { onImageRemove(images[1]) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CollageImageItem(
                        uri = images[2],
                        modifier = Modifier.weight(1f).fillMaxSize(),
                        onClick = { onImageClick(images[2]) },
                        onLongClick = { onImageRemove(images[2]) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollageImageItem(
    uri: String,
    modifier: Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .precision(Precision.EXACT)
                .size(1000)
                .allowHardware(false)
                .build(),
            filterQuality = FilterQuality.High
        ),
        contentDescription = "Note Image",
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        contentScale = ContentScale.Crop
    )
}
