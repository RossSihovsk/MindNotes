package com.ross.mindnotes.data.repository

import com.ross.mindnotes.data.source.NoteDao
import com.ross.mindnotes.data.source.NoteEntity
import com.ross.mindnotes.domain.model.Note
import com.ross.mindnotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes().map { entities ->
            entities.map { it.toNote() }
        }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return dao.getNoteById(id)?.toNote()
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note.toNoteEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toNoteEntity())
    }

    private fun NoteEntity.toNote(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            timestamp = timestamp,
            category = category,
            mood = mood,
            imageUri = imageUri
        )
    }

    private fun Note.toNoteEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            content = content,
            timestamp = timestamp,
            category = category,
            mood = mood,
            imageUri = imageUri
        )
    }
}
