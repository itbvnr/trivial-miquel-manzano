
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension


fun main() = application {
    Window(
        title = "M78Exercices",
        state = rememberWindowState(width = 720.dp, height = 1280.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(720, 1280)
        TrivialApp()
    }
}

@Composable
fun AppPreview() { TrivialApp() }