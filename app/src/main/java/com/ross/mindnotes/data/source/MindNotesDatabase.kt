package com.ross.mindnotes.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.ross.mindnotes.domain.model.Category

@Database(
    entities = [NoteEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class MindNotesDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "mindnotes_db"
    }
}

class Converters {
    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(name: String): Category {
        return Category.valueOf(name)
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(separator = "|||") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        if (value.isEmpty()) return emptyList()
        return value.split("|||")
    }
}
