package com.ross.mindnotes.di

import android.app.Application
import androidx.room.Room
import com.ross.mindnotes.data.repository.NoteRepositoryImpl
import com.ross.mindnotes.data.source.MindNotesDatabase
import com.ross.mindnotes.domain.repository.NoteRepository
import com.ross.mindnotes.domain.use_case.AddNoteUseCase
import com.ross.mindnotes.domain.use_case.DeleteNoteUseCase
import com.ross.mindnotes.domain.use_case.GetNoteByIdUseCase
import com.ross.mindnotes.domain.use_case.GetNotesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMindNotesDatabase(app: Application): MindNotesDatabase {
        return Room.databaseBuilder(
            app,
            MindNotesDatabase::class.java,
            MindNotesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: MindNotesDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideGetNotesUseCase(repository: NoteRepository): GetNotesUseCase {
        return GetNotesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteNoteUseCase(repository: NoteRepository): DeleteNoteUseCase {
        return DeleteNoteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddNoteUseCase(repository: NoteRepository): AddNoteUseCase {
        return AddNoteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetNoteByIdUseCase(repository: NoteRepository): GetNoteByIdUseCase {
        return GetNoteByIdUseCase(repository)
    }
}
