package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.LatLng
import nz.ac.canterbury.seng440.mwa172.locationalarm.R
import org.jetbrains.annotations.Contract

@Entity(tableName = "alarm")
data class Alarm(
    @ColumnInfo var name: String,
    @ColumnInfo var latitude: Double,
    @ColumnInfo var longitude: Double,
    @ColumnInfo var radius: Double
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @get:Contract("->new")
    val location: LatLng
        get() = LatLng(latitude, longitude)


    @Contract("->new")
    fun createGeofence(): Geofence = Geofence.Builder()
        .setRequestId(this.id.toString())
        .setCircularRegion(
            this.latitude, this.longitude, this.radius.toFloat()
        )
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        .build()

}

fun Context.shareAlarm(alarm: Alarm) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(
        Intent.EXTRA_TEXT,
        getString(
            R.string.share_text,
            CreateAlarm.buildUrl(alarm)
        )
    )
    startActivity(intent)
}