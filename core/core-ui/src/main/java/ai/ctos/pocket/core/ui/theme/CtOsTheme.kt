package ai.ctos.pocket.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

public object CtOsUiPalette {
    val Background = Color(0xFF07151C)
    val Surface = Color(0xFF10232D)
    val Accent = Color(0xFF6EE7FF)
    val Alert = Color(0xFFFF5A78)
    val Success = Color(0xFF83FFB7)
    val Text = Color(0xFFEBF6FF)
    val Ghost = Color(0xFF8AA6B6)
}

private val CtOsDarkColors = darkColorScheme(
    background = CtOsUiPalette.Background,
    surface = CtOsUiPalette.Surface,
    primary = CtOsUiPalette.Accent,
    secondary = CtOsUiPalette.Accent,
    tertiary = CtOsUiPalette.Success,
    onBackground = CtOsUiPalette.Text,
    onSurface = CtOsUiPalette.Text,
    error = CtOsUiPalette.Alert,
)

@Composable
public fun CtOsTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = CtOsDarkColors, content = content)
}
