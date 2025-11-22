package com.ross.mindnotes.presentation.calendar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ross.mindnotes.domain.model.Note
import com.ross.mindnotes.domain.use_case.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CalendarState())
    val state: State<CalendarState> = _state

    init {
        getNotes()
    }

    private fun getNotes() {
        getNotesUseCase().onEach { notes ->
            val notesByDate = notes.groupBy { note ->
                Instant.ofEpochMilli(note.timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
            _state.value = state.value.copy(
                notesByDate = notesByDate
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.NextMonth -> {
                _state.value = state.value.copy(
                    currentMonth = state.value.currentMonth.plusMonths(1)
                )
            }
            is CalendarEvent.PreviousMonth -> {
                _state.value = state.value.copy(
                    currentMonth = state.value.currentMonth.minusMonths(1)
                )
            }
            is CalendarEvent.SelectDate -> {
                _state.value = state.value.copy(
                    selectedDate = event.date
                )
            }
        }
    }
}
