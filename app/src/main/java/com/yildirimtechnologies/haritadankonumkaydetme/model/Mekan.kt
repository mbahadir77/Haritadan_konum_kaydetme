package com.yildirimtechnologies.haritadankonumkaydetme.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Mekan(
    @ColumnInfo(name = "isim")
    var isim : String,
    @ColumnInfo(name = "latitude")
    var latitude : Double,
    @ColumnInfo(name = "longtitude")
    var longtitude : Double
    ) {
    @PrimaryKey(autoGenerate = true)
    var id = 0


}