package com.yogender.rotationanalyser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.yogender.rotationanalyser.ui.theme.RotationAnalyserTheme


class MainActivity : ComponentActivity(), Orientation.Listener {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            RotationDatabase::class.java,
            "rotations.db"
        ).build()
    }
    private val viewModel by viewModels<RotationViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RotationViewModel(db.rotationDao()) as T
                }
            }
        }
    )
    private val pitch = mutableStateOf(0f)
    private val roll = mutableStateOf(0f)
    private val yaw = mutableStateOf(0f)
    private lateinit var mOrientation: Orientation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mOrientation = Orientation(this)
        setContent {
            RotationAnalyserTheme {
                // A surface container using the 'background' color from the theme
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "start_screen"
                    ) {
                        composable("start_screen") {
                            StartScreen(navController,mOrientation,this@MainActivity,viewModel)
                        }
                        composable("rotation_screen") {
                            RotationScreen(navController,pitch,roll,yaw,mOrientation,viewModel)
                        }
                        composable("open_history") {
                            ChartClass(viewModel)
                        }
                    }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        mOrientation.startListening(this)
    }

    override fun onStop() {
        super.onStop()
        mOrientation.stopListening()
    }

    override fun onOrientationChanged(pitch: Float, roll: Float, yaw: Float) {
        this.pitch.value = pitch
        this.roll.value = roll
        this.yaw.value = yaw
    }
}