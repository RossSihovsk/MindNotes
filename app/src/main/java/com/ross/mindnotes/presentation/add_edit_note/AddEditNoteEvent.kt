package com.ross.mindnotes.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState
import com.ross.mindnotes.domain.model.Category

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class ChangeMood(val mood: Int) : AddEditNoteEvent()
    data class SelectCategory(val category: Category) : AddEditNoteEvent()
    data class ChangeDate(val timestamp: Long) : AddEditNoteEvent()
    data class AddImages(val images: List<String>) : AddEditNoteEvent()
    data class RemoveImage(val imageUri: String) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()
    object DeleteNote : AddEditNoteEvent()
}
