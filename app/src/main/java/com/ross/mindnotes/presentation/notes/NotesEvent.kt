package com.ross.mindnotes.presentation.notes

import com.ross.mindnotes.domain.model.Note
import com.ross.mindnotes.domain.model.Category

sealed class NotesEvent {
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderSection : NotesEvent()
    data class SelectCategory(val category: Category?) : NotesEvent()
}
