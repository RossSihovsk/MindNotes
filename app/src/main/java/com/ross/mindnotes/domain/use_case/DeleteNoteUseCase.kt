package com.ross.mindnotes.domain.use_case

import com.ross.mindnotes.domain.model.Note
import com.ross.mindnotes.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}
