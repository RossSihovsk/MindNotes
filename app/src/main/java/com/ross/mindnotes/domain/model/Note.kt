package com.ross.mindnotes.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val timestamp: Long,
    val category: Category,
    val mood: Int, // 1-5 scale
    val images: List<String> = emptyList()
)
