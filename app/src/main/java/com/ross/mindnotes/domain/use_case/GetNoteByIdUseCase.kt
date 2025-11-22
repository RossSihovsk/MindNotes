package com.ross.mindnotes.domain.use_case

import com.ross.mindnotes.domain.model.Note
import com.ross.mindnotes.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Note? {
        return repository.getNoteById(id)
    }
}
