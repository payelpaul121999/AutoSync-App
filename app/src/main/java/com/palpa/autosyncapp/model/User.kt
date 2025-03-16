package com.palpa.autosyncapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dob: String,
    val address: String,
    val pin: String,
    val district: String,
    val state: String,
    var imageUri: String // Store Image URI as String
)

