package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class Alarm(
    @ColumnInfo var name: String,
    @ColumnInfo var latitude: Double,
    @ColumnInfo var longitude: Double,
    @ColumnInfo var radius: Double
) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

}
