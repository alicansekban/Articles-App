package com.example.articlesapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticlesEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int
)
