package com.yildirimtechnologies.haritadankonumkaydetme.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yildirimtechnologies.haritadankonumkaydetme.model.Mekan

@Database(entities = [Mekan::class], version = 1)
abstract class MekanDatabase : RoomDatabase() {
    abstract fun mekanDao(): MekanDao
}