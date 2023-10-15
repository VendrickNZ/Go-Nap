package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapState
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModelFactory
import nz.ac.canterbury.seng440.mwa172.locationalarm.NavigationNodes
import nz.ac.canterbury.seng440.mwa172.locationalarm.R

object CreateAlarm {

    const val Uri: String = "https://www.gonap.nz"

    private const val BaseUrl: String = "alarm/create"

    const val Route: String = "$BaseUrl?lat={lat}&long={long}&name={name}&radius={radius}"

    const val Lat: String = "lat"

    const val Long: String = "long"

    fun buildUrl(lat: Double, long: Double): String = "$BaseUrl?$Lat=$lat&$Long=$long"

    fun buildUrl(lat: Double, long: Double, name: String, radius: Double): String = "${buildUrl(lat, long)}&name=$name&radius=$radius"

    fun buildUrl(alarm: Alarm) = buildUrl(alarm.latitude, alarm.longitude, alarm.name, alarm.radius)
}

fun createAlarmNode(
    builder: NavGraphBuilder,
    resources: Resources,
    navController: NavController
) {

    builder.composable(
        CreateAlarm.Route,
        arguments = listOf(
            navArgument(CreateAlarm.Lat) {
                type = NavType.FloatType
            },
            navArgument(CreateAlarm.Long) {
                type = NavType.FloatType
            },
            navArgument("name") {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument("radius") {
                type = NavType.FloatType
                defaultValue = Float.NaN
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${CreateAlarm.Uri}/${CreateAlarm.Route}"
            }
        )
    ) { backStackEntry ->
        val state = (LocalContext.current.applicationContext as GoNapApplication).state
        val settings = state.settings

        val lat: Double = backStackEntry.arguments?.getFloat(CreateAlarm.Lat)?.toDouble() ?: 0.0
        val long: Double = backStackEntry.arguments?.getFloat(CreateAlarm.Long)?.toDouble() ?: 0.0

        var name: String = backStackEntry.arguments?.getString("name") ?: settings.defaultName
        if (name.isEmpty()) {
            name = settings.defaultName
        }

        var radius: Double = backStackEntry.arguments?.getFloat("radius")?.toDouble() ?: settings.defaultRadius
        if (radius.isNaN()) {
            radius = settings.defaultRadius
        }

        val alarm = Alarm(
            name = name,
            radius = radius,
            latitude = lat,
            longitude = long
        )

        CreateAlarmScreen(
            alarm,
            resources,
            navController,
            state
        )
    }
}

@Composable
fun CreateAlarmScreen(
    alarm: Alarm,
    resources: Resources,
    navController: NavController,
    state: GoNapState,
    viewModel: GoNapViewModel = viewModel(
        factory = GoNapViewModelFactory((LocalContext.current.applicationContext as GoNapApplication).goNapRepository)
    )
) {

    var currentName by rememberSaveable { mutableStateOf(state.settings.defaultName) }
    var currentRadius by rememberSaveable { mutableDoubleStateOf(state.settings.defaultRadius) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.primary)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = resources.getString(R.string.title_create_alarm),
                style = MaterialTheme.typography.h5
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        AlarmNameInput(
            currentName = currentName,
            onUpdateCurrentName = { newName -> currentName = newName.ifEmpty { "" } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        RadiusInput(
            currentRadius = currentRadius,
            onUpdateCurrentRadius = { newRadius -> currentRadius = newRadius },
            commonRadii = listOf(20.0, 100.0, 200.0, 500.0)
        )

        Row {
            OutlinedButton(
                onClick = {
                    navController.navigate(NavigationNodes.Map.url)
                },
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = resources.getString(R.string.button_cancel),
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.button
                )
            }

            Button(
                onClick = {
                    viewModel.addAlarm(alarm)
                    navController.navigate(NavigationNodes.buildMapURL(alarm.latitude, alarm.longitude))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = resources.getString(R.string.button_create),
                    style = MaterialTheme.typography.button
                )
            }

        }
    }
}

@Composable
fun AlarmNameInput(
    currentName: String,
    onUpdateCurrentName: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = stringResource(R.string.label_alarm_name),
            style = MaterialTheme.typography.h5
        )

        TextField(
            value = currentName,
            onValueChange = onUpdateCurrentName,
            modifier = Modifier
                .width(140.dp)
                .height(52.dp)
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RadiusInput(
    currentRadius: Double,
    onUpdateCurrentRadius: (Double) -> Unit,
    commonRadii: List<Double>
) {
    val expanded = remember { mutableStateOf(false) }
    val customValue = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.label_radius),
            style = MaterialTheme.typography.h5
        )

        Box {
            if (currentRadius != -1.0) {
                Text(
                    text = "$currentRadius ${stringResource(R.string.label_metres)}",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.clickable { expanded.value = true }
                )
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    commonRadii.forEach { radii ->
                        DropdownMenuItem(onClick = {
                            onUpdateCurrentRadius(radii)
                            expanded.value = false
                        }) {
                            Text(
                                text = radii.toString(),
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }
                    DropdownMenuItem(
                        onClick = {
                            onUpdateCurrentRadius(-1.0)
                            expanded.value = false
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.label_custom_value),
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
            }
        }

        if (currentRadius == -1.0) {
            TextField(
                modifier = Modifier
                    .height(52.dp)
                    .width(140.dp),
                value = customValue.value,
                onValueChange = { customValue.value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        val newRadius = customValue.value.toDoubleOrNull() ?: 0.0
                        onUpdateCurrentRadius(newRadius)
                        customValue.value = ""
                    }
                )
            )
        }
    }
}