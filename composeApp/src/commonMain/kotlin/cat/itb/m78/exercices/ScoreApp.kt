// Essential Imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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
// fun MenuScreen(addCount: () -> Unit) Asi se pasa una funcion, y si la asignamos al OnClick la ejecutara
fun MenuScreen() {
    val myViewModel = viewModel { MyViewModel() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                Text(myViewModel.local.value.toString())
                Button(onClick = myViewModel::AddScoreLocal){
                    Text("Score")
                }
            }
            Column {
                Text(myViewModel.visit.value.toString())
                Button(onClick = myViewModel::AddScoreVisit ){
                    Text("Score")
                }
            }
        }
    }
}



// Main App
@Composable
fun ScoreApp() {
    MenuScreen()
    // Instanciar View Model: MyViewModel
    //MenuScreen(myViewModel::AddScoreLocal)
}
