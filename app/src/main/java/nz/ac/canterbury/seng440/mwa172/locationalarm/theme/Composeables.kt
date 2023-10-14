package nz.ac.canterbury.seng440.mwa172.locationalarm.theme

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import nz.ac.canterbury.seng440.mwa172.locationalarm.R

@Composable
fun BasicAlert(
    title: String,
    desc: String = "",
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            if (desc.isNotEmpty()) {
                Text(desc)
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(stringResource(R.string.button_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.button_cancel))
            }
        }
    )
}