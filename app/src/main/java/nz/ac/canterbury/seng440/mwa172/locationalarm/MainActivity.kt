package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val context = this
            Button(
                modifier = Modifier.fillMaxSize(0.5f),
                onClick = {
                    val intent = Intent(context, MapActivity::class.java)
                    startActivity(intent)
                }
            ) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "Go to map")
            }

        }
    }
}