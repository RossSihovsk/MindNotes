package com.ross.mindnotes.domain.use_case

import com.ross.mindnotes.domain.model.Note
import com.ross.mindnotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            notes.sortedByDescending { it.timestamp }
        }
    }
}
