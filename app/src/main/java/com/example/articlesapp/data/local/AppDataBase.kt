package com.example.articlesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.articlesapp.data.local.dao.ArticlesDao
import com.example.articlesapp.data.local.entity.ArticlesEntity

@Database(entities = [ArticlesEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}