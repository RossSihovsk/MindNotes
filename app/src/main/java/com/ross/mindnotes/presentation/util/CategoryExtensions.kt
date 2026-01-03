package com.ross.mindnotes.presentation.util

import androidx.compose.ui.graphics.Color
import com.ross.mindnotes.domain.model.Category

val Category.color: Color
    get() = when (this) {
        Category.STOICISM -> Color(0xFF90CAF9) // Blue
        Category.DAILY_SUMMARY -> Color(0xFFA5D6A7) // Green
        Category.THANKFULNESS -> Color(0xFFFFF59D) // Yellow
        Category.ANALYSIS -> Color(0xFFCE93D8) // Purple
        Category.OTHER -> Color(0xFFE0E0E0) // Grey
    }

val Category.displayName: String
    get() = when (this) {
        Category.STOICISM -> "Stoicism"
        Category.DAILY_SUMMARY -> "Daily Summary"
        Category.THANKFULNESS -> "Thankfulness"
        Category.ANALYSIS -> "Analysis"
        Category.OTHER -> "Other"
    }

val Category.description: String
    get() = when (this) {
        Category.STOICISM -> "Reflect on what you can control and accept what you cannot."
        Category.DAILY_SUMMARY -> "What's one thing you learned today? How did your day go?"
        Category.THANKFULNESS -> "List three things you are grateful for right now."
        Category.ANALYSIS -> "Analyze a situation objectively. What are the facts?"
        Category.OTHER -> "General notes and thoughts without a specific theme."
    }
