package com.yildirimtechnologies.haritadankonumkaydetme.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yildirimtechnologies.haritadankonumkaydetme.model.Mekan
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable


@Dao
interface MekanDao {
    @Query("SELECT * FROM Mekan")
    fun getAll() : Flowable<List<Mekan>>

    @Insert
    fun insert(mekan: Mekan) : Completable

    @Delete
    fun delete(mekan: Mekan) : Completable

}