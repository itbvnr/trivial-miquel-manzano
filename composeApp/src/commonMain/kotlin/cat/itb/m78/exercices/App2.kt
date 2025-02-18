// Essential Imports
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

// ViewModel Imports
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel





// ViewModel
class MyViewModel : ViewModel() {
    val visit = mutableStateOf(0)
    val local = mutableStateOf(0)

    fun AddScoreVisit(){
        visit.value ++
    }
    fun AddScoreLocal(){
        local.value ++
    }
}

// Screen
@Composable
fun MenuScreen() {
    val viewModel = viewModel { MyViewModel() }
    Row {
        Column {
            Text(viewModel.local.value.toString())
            Button(onClick = viewModel::AddScoreLocal){
                Text("Score")
            }
        }
        Column {
            Text(viewModel.visit.value.toString())
            Button(onClick = viewModel::AddScoreVisit ){
                Text("Score")
            }
        }
    }
}



// Main App
@Composable
fun TrivialApp() {
    MenuScreen()
}
