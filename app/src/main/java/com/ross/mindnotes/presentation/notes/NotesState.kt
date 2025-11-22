package com.ross.mindnotes.presentation.notes

import com.ross.mindnotes.domain.model.Note
import com.ross.mindnotes.domain.model.Category

data class NotesState(
    val notes: List<Note> = emptyList(),
    val selectedCategory: Category? = null,
    val isOrderSectionVisible: Boolean = false
)
