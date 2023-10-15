package nz.ac.canterbury.seng440.mwa172.locationalarm.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = DarkPrimaryColor,
    primaryVariant = DarkSecondaryColor,
    secondary = DarkAccentColor,
    background = DarkBackgroundColor,
    onBackground = DarkTextColor,
)

private val LightColorPalette = lightColors(
    primary = PrimaryColor,
    primaryVariant = SecondaryColor,
    secondary = AccentColor,
    background = BackgroundColor,
    onBackground = TextColor,
)

@Composable
fun LocationAlarmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
