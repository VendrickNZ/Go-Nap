package nz.ac.canterbury.seng440.mwa172.locationalarm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationNodes(
    val url: String,
    val icon: ImageVector
) {

    Alarms("alarms", Icons.Filled.AccountCircle),
    Map("map", Icons.Filled.LocationOn),
    Settings("settings", Icons.Filled.Settings);

}