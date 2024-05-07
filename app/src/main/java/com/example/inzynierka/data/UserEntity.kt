package com.example.inzynierka.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uid: String,
    val name: String,
    val surname: String,
    val email: String,
    val number: Int

//    @ColumnInfo(name = "имя") val name: String,
//    @ColumnInfo(name = "номер") val number: Int
)
