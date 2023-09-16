package com.example.articlesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.articlesapp.data.local.dao.ArticlesDao
import com.example.articlesapp.data.local.entity.ArticlesEntity
import com.example.articlesapp.data.local.entity.Source

@Database(entities = [ArticlesEntity::class], version = 1)
@TypeConverters(SourceTypeConverter::class, Converters::class)

abstract class AppDataBase : RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}

class SourceTypeConverter {
    @TypeConverter
    fun fromSource(source: Source?): String? {
        return source?.let {
            "${it.name},${it.id}"
        }
    }

    @TypeConverter
    fun toSource(sourceString: String?): Source? {
        if (sourceString == null) return null
        val parts = sourceString.split(",")
        return if (parts.size == 2) {
            Source(parts[0], parts[1])
        } else {
            null
        }
    }
}