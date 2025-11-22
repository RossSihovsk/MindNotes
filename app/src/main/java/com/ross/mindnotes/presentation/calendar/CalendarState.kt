package com.ross.mindnotes.presentation.calendar

import com.ross.mindnotes.domain.model.Note
import java.time.LocalDate
import java.time.YearMonth

data class CalendarState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val notesByDate: Map<LocalDate, List<Note>> = emptyMap()
)
