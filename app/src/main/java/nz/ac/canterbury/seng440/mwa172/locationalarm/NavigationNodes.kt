package nz.ac.canterbury.seng440.mwa172.locationalarm

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource

enum class NavigationNodes(
    val url: String,
    val children: @Composable () -> Unit
) {

    Alarms(
        url = "alarms",
        children = {
            Icon(
                imageVector = Icons.Outlined.Alarm,
                contentDescription = "Alarm"
            )
        }
    ),
    Map(
        url = "map",
        children = {
            Icon(
                imageVector = Icons.Outlined.Explore,
                contentDescription = "Map"
            )
        }
    ),
    Settings(
        url = "settings",
        children = {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings"
            )
        }
    );

    companion object {
        fun buildMapURL(lat: Double, long: Double) = "map?lat=$lat,long=$long"
    }

}