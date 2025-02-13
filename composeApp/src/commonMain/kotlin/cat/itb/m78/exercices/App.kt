import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import kotlinx.coroutines.delay


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextFieldDefaults

// Data Model
data class Question(val text: String, val options: List<String>, val correctAnswer: Int)

data class GameSettings(var rounds: Int = 5, var difficulty: String = "Medium", var timePerRound: Int = 10)

// ViewModel
class TrivialViewModel : ViewModel() {
    var settings by mutableStateOf(GameSettings())
    var currentQuestionIndex by mutableStateOf(0)
    var score by mutableStateOf(0)
    // Here
    var timeLeft by mutableStateOf(settings.timePerRound)
    /* Si implemento el xml con las preguntas, la funcionalidad de random es mas facil
    * He de cambiar la forma de validar las preguntas correctas, trabajar las respuestas de forma induvidual como objeto.*/
    private val questions = listOf(
        Question("Best pizza?", listOf("Piña pizza", "Pepperoni", "4 cheese", "BBQ"), 0),
        Question("What sound make a cat?", listOf("Muuu", "Beeeee", "Miaw", "Grraaaa"), 0),
        Question("What is Java?", listOf("Coffee", "Programming Language", "Planet", "Movie"), 1),
        Question("What is Python?", listOf("Snake", "Car", "Language", "Country"), 2),
        Question("What is Swift?", listOf("Bird", "Apple's Language", "Fast", "Tool"), 1),
        Question("What is Kotlin?", listOf("Language", "Food", "Animal", "Car"), 0),
        Question("What is HTML?", listOf("Markup", "Protocol", "Database", "None"), 0),
        Question("What is Java?", listOf("Coffee", "Programming Language", "Planet", "Movie"), 1),
        Question("What is Python?", listOf("Snake", "Car", "Language", "Country"), 2),
        Question("What is Swift?", listOf("Bird", "Apple's Language", "Fast", "Tool"), 1)
    )

    fun getCurrentQuestion() = questions[currentQuestionIndex % settings.rounds]
    fun answerQuestion(answerIndex: Int, navController: NavController) {
        if (answerIndex == getCurrentQuestion().correctAnswer) {
            score++
        }
        if (currentQuestionIndex < settings.rounds - 1) {
            currentQuestionIndex++
            resetTimer()
        } else {
            navController.navigate("result")
        }
    }
    fun resetTimer() {
        timeLeft = settings.timePerRound
    }

    suspend fun startTimer(onTimeUp: () -> Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        onTimeUp()
    }
}

// Screens
@Composable
fun MenuScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = {
            navController.navigate("game")
        }) { Text("Start Game") }
        Button(onClick = { navController.navigate("settings") }) { Text("Settings") }
    }
}

@Composable
fun GameScreen(navController: NavController, viewModel: TrivialViewModel) {
    val question = viewModel.getCurrentQuestion()
    val timeLeft = viewModel.timeLeft

    LaunchedEffect(question) {
        viewModel.resetTimer()
        viewModel.startTimer {
            viewModel.answerQuestion(-1, navController)
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text(question.text)
        question.options.forEachIndexed { index, option ->
            Button(onClick = { viewModel.answerQuestion(index, navController) }) { Text(option) }
        }
        Text("Time left: $timeLeft seconds")
    }
}

@Composable
fun ResultScreen(navController: NavController, viewModel: TrivialViewModel) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text("Your score: ${viewModel.score}")
        Button(onClick = {
            viewModel.currentQuestionIndex = 0
            viewModel.score = 0
            viewModel.resetTimer()
            navController.navigate("menu")
        }) { Text("Back to Menu") }
    }
}

// Estoy usando una API?? omaiga
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: TrivialViewModel) {
    var rounds by remember { mutableStateOf(viewModel.settings.rounds) }
    var selectedDifficulty by remember { mutableStateOf(viewModel.settings.difficulty) }
    var timePerRound by remember { mutableStateOf(viewModel.settings.timePerRound) }
    var expanded by remember { mutableStateOf(false) }

    val difficultyOptions = listOf("Easy", "Medium", "Hard")

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text("Settings")
        Slider(value = rounds.toFloat(), onValueChange = { rounds = it.toInt() }, valueRange = 3f..10f, steps = 7)
        Text("Rounds: $rounds")

        // exposedDropdownMenuBOx del material3 no del 2
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = selectedDifficulty,
                onValueChange = {},
                readOnly = true,
                label = { Text("Difficulty") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                difficultyOptions.forEach { difficulty ->
                    DropdownMenuItem(
                        text = { Text(difficulty) },
                        onClick = {
                            selectedDifficulty = difficulty
                            expanded = false
                        }
                    )
                }
            }
        }

        Text("Selected Difficulty: $selectedDifficulty")

        Slider(value = timePerRound.toFloat(), onValueChange = { timePerRound = it.toInt() }, valueRange = 5f..20f, steps = 15)
        Text("Time per Round: $timePerRound seconds")

        Button(onClick = {
            viewModel.settings = GameSettings(rounds, selectedDifficulty, timePerRound)
            navController.navigate("menu")
        }) { Text("Save Settings") }
    }
}

// Navigation Setup
@Composable
fun TrivialNavHost(navController: NavHostController, viewModel: TrivialViewModel) {
    // NAvHost es un contenedor para navegar via NavControler https://developer.android.com/reference/androidx/navigation/NavHost
    NavHost(navController, startDestination = "menu") {
        composable("menu") { MenuScreen(navController) }
        composable("game") { GameScreen(navController, viewModel) }
        composable("result") { ResultScreen(navController, viewModel) }
        composable("settings") { SettingsScreen(navController, viewModel) }
    }
}

// Main App
@Composable
fun TrivialApp() {
    val navController = rememberNavController()
    val viewModel = remember { TrivialViewModel() }
    TrivialNavHost(navController, viewModel)
}
