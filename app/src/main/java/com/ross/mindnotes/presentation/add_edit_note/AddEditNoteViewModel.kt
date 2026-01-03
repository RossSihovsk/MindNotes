package com.ross.mindnotes.presentation.add_edit_note

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ross.mindnotes.domain.model.Category
import com.ross.mindnotes.domain.model.Note
import com.ross.mindnotes.domain.use_case.AddNoteUseCase
import com.ross.mindnotes.domain.use_case.DeleteNoteUseCase
import com.ross.mindnotes.domain.use_case.GetNoteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    savedStateHandle: SavedStateHandle,
    private val application: Application
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title..."))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter some content"))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteMood = mutableIntStateOf(3) // Default neutral mood
    val noteMood: State<Int> = _noteMood

    private val _noteCategory = mutableStateOf(Category.OTHER)
    val noteCategory: State<Category> = _noteCategory

    private val _noteDate = mutableLongStateOf(System.currentTimeMillis())
    val noteDate: State<Long> = _noteDate

    private val _noteImages = mutableStateOf<List<String>>(emptyList())
    val noteImages: State<List<String>> = _noteImages

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Long? = null

    init {
        savedStateHandle.get<Long>("noteId")?.let { noteId ->
            if (noteId != -1L) {
                viewModelScope.launch {
                    getNoteByIdUseCase(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteMood.intValue = note.mood
                        _noteCategory.value = note.category
                        _noteImages.value = note.images
                        _noteDate.longValue = note.timestamp
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeMood -> {
                _noteMood.intValue = event.mood
            }
            is AddEditNoteEvent.SelectCategory -> {
                _noteCategory.value = event.category
            }
            is AddEditNoteEvent.ChangeDate -> {
                _noteDate.longValue = event.timestamp
            }
            is AddEditNoteEvent.AddImages -> {
                 event.images.forEach { uriString ->
                     try {
                         val uri = android.net.Uri.parse(uriString)
                         val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                         application.contentResolver.takePersistableUriPermission(uri, flag)
                     } catch (e: Exception) {
                         e.printStackTrace()
                     }
                 }
                 _noteImages.value = _noteImages.value + event.images
            }
            is AddEditNoteEvent.RemoveImage -> {
                _noteImages.value = _noteImages.value - event.imageUri
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        addNoteUseCase(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = _noteDate.longValue,
                                mood = noteMood.value,
                                category = noteCategory.value,
                                images = _noteImages.value,
                                id = currentNoteId ?: 0
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
            is AddEditNoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    if (currentNoteId != null) {
                         deleteNoteUseCase(
                             Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = _noteDate.longValue,
                                mood = noteMood.value,
                                category = noteCategory.value,
                                images = _noteImages.value,
                                id = currentNoteId!!
                            )
                         )
                    }
                    _eventFlow.emit(UiEvent.SaveNote)
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}
