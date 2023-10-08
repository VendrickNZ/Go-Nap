package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.annotations.Contract

@Entity(tableName = "alarm")
data class Alarm(
    @ColumnInfo var name: String,
    @ColumnInfo var latitude: Double,
    @ColumnInfo var longitude: Double,
    @ColumnInfo var radius: Double
) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

    @get:Contract("->new")
    val location: LatLng
        get() = LatLng(latitude, longitude)

}
