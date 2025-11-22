package com.ross.mindnotes.data.source

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ross.mindnotes.domain.model.Category

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val timestamp: Long,
    val category: Category,
    val mood: Int,
    val imageUri: String?
)
