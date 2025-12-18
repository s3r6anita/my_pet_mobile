package com.serson.my_pet.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProcedureTitle(
    var name: String,
    val type: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0
)
