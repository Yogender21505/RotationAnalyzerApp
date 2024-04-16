package com.yogender.rotationanalyser

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import co.yml.charts.common.extensions.isNotNull
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    navController: NavHostController,
    mOrientation: Orientation,
    mainActivity: MainActivity,
    viewModel: RotationViewModel
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded= false)
    var showBottomSheet by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.welcome_img),
            contentDescription = "Welcome Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome To App", color = Color.White, fontSize = 30.sp)
            Text("Analyse your Phone Rotation", color = Color.White, fontSize = 20.sp)

        }
        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedButton(
                onClick = { showBottomSheet = true  },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier.size(80.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Open Slider",
                    tint = Color.Black
                )
            }
        }
    }
    if (showBottomSheet) {

        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF413e63),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BottomSheetItem(navController,mOrientation,mainActivity,viewModel) {
                    showBottomSheet  = false
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState", "SuspiciousIndentation")
@Composable
fun BottomSheetItem(
    navController: NavHostController,
    mOrientation: Orientation,
    mainActivity: MainActivity,
    viewModel: RotationViewModel,
    onClick: () -> Unit
) {
    var context = LocalContext.current
    var contentResolver = LocalContext.current.contentResolver
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add your content here
        var count = mutableStateOf(false);
        Button(modifier = Modifier
            .fillMaxWidth()
            ,colors = ButtonDefaults.buttonColors(Color.White),
            onClick = {
                navController.navigate("rotation_screen")
            },
            content ={
                mOrientation.startListening(mainActivity)
                Text("Start Rotation", color = Color.Black)
            }
        )
        var openHistory by remember { mutableStateOf(false) }

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color.White),
            onClick = {
                viewModel.getDatabase()
                count.value = true

                if (viewModel.databaseList.isNotEmpty()) {
                    navController.navigate("open_history")
                }
            }
        ) {
            Text("Open History", color = Color.Black)
            IndeterminateCircularIndicator(viewModel.isLoading, openHistory, viewModel, count)

        }

        val saveFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument()) { uri ->
            if (uri != null) {
                val outputStream = uri.let { contentResolver.openOutputStream(it) }
                outputStream?.use { stream ->
                    writeDatabaseToFile(viewModel.databaseList, stream,context)
                }
            }
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            ,colors = ButtonDefaults.buttonColors(Color.White),
            onClick = {
                saveFileLauncher.launch("database.txt")
            },
            content ={
                Text("Download Text File", color = Color.Black)
            }
        )
    }
}

@Composable
fun IndeterminateCircularIndicator(
    loading: MutableState<Boolean>,
    openHistory: Boolean,
    viewModel: RotationViewModel,
    count: MutableState<Boolean>
) {

    if(loading.value){
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.background,
            strokeWidth = 2.dp,
            modifier = Modifier.size(20.dp)
        )
    }

}

private fun writeDatabaseToFile(
    databaseList: List<Rotation>,
    outputStream: OutputStream,
    context: Context
) {
    if(databaseList.isNotNull()){
        val stringBuilder = StringBuilder()
        databaseList.forEachIndexed { index, rotation ->
            stringBuilder.append("${rotation.pitch}, ${rotation.roll}, ${rotation.yaw}, ${rotation.time}\n")
        }
        outputStream.write(stringBuilder.toString().toByteArray())
    }
    else{
        Toast.makeText(context, "The database is empty", Toast.LENGTH_SHORT).show()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun Preview() {
//    RotationAnalyserTheme {
//        StartScreen()
//    }
//}