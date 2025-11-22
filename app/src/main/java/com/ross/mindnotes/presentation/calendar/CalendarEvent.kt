package com.ross.mindnotes.presentation.calendar

import java.time.LocalDate

sealed class CalendarEvent {
    object NextMonth : CalendarEvent()
    object PreviousMonth : CalendarEvent()
    data class SelectDate(val date: LocalDate) : CalendarEvent()
}
