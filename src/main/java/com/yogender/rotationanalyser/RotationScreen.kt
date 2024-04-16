package com.yogender.rotationanalyser


import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@SuppressLint("UnrememberedMutableState", "ComposableNaming")
@Composable
fun RotationScreen(
    navController: NavHostController,
    pitch: MutableState<Float>,
    roll: MutableState<Float>,
    yaw: MutableState<Float>,
    mOrientation: Orientation,
    viewModel: RotationViewModel
){
    // Start updating rotation data when the screen is shown



    Box(modifier = Modifier.fillMaxSize())
    {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawLine(
                start = Offset(x = 0f, y = canvasHeight/2f),
                end = Offset(x = canvasWidth, y = canvasHeight/2f),
                color = Color.Blue,
                strokeWidth = 5f
            )

            val th = ((pitch.value*canvasHeight)/180f)
            val tw = ((yaw.value*canvasWidth)/180f)

            val start = Offset(x = 0f+tw, y = canvasHeight/2f+th)
            val end = Offset(x = canvasWidth+tw, y = canvasHeight/2f+th)

            val rect = Rect(Offset.Zero, size)

            rotate(degrees = roll.value, rect.center) {
                drawLine(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Red,
                            Color.White,
                            Color.Black
                        ),
                        start = start,
                        end = end
                    ),
                    start = start,
                    end = end,
                    strokeWidth = 60f,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            viewModel.pitch_view.value=pitch.value
            viewModel.roll_view.value=roll.value
            viewModel.yaw_view.value=yaw.value


            viewModel.updateDatabase()
            ElevatedCardExample(pitch,"Pitch")
            ElevatedCardExample(roll,"Roll")
            ElevatedCardExample(yaw,"Yaw")
            FloatingActionButton(modifier = Modifier
                .fillMaxWidth()
                .size(40.dp),
                containerColor = Color.White,
                shape = CircleShape,
                onClick = {
                    mOrientation.stopListening()
                    navController.popBackStack()
                },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close screen",
                        tint = Color.Black
                    )
                }
            )
        }
    }
}


@Composable
fun ElevatedCardExample(value: MutableState<Float>, name: String) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = (Color.White),
        ),
        modifier = Modifier
            .size(width = 250.dp, height = 80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {

        Row {
            Text(
            text = name,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
            )
            Text(
                text = "${value.value}",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }

    }
}