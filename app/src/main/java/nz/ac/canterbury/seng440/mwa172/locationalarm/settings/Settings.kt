package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "default_name") val defaultName: String = "My Alarm",
    @ColumnInfo(name = "default_radius") val defaultRadius: Double = 100.0,
    @ColumnInfo(name = "default_sound") val defaultSound: String = "default_sound_uri",
    @ColumnInfo(name = "default_vibration") val defaultVibration: Boolean = true
)
