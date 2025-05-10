package com.chirick.myai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chirick.myai.ui.screens.answerscreen.AnswerScreen
import com.chirick.myai.ui.screens.camerascreen.CameraScreen
import com.chirick.myai.ui.screens.homescreen.HomeScreen
import com.chirick.myai.ui.theme.MyAITheme
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen {
    object Home : Screen()
    object Settings : Screen()
    object Camera : Screen()
    object Profile : Screen() // example 4th screen, adjust as needed
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAITheme(darkTheme = true) {
                Scaffold { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        composable("home") {
                            SwipeNavigationApp(
                                navController,
                                innerPadding
                            )
                        }
                        composable("camera") {
                            CameraScreen(
                                navController,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                        composable("answer") {
                            AnswerScreen(
                                navController,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeNavigationApp(navController: NavHostController, innerPadding: PaddingValues) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val x = 20
    var modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(currentScreen) {
                detectDragGestures { change, dragAmount ->
                    val (dx, dy) = dragAmount

                    // simple threshold to detect swipe direction
                    if (dx > x) {
                        if (currentScreen == Screen.Home) currentScreen = Screen.Camera
                    } else if (dx < -x) {
                        if (currentScreen == Screen.Camera) currentScreen = Screen.Home
                    }

                    if (dy < -x) {
                        if (currentScreen == Screen.Home) currentScreen = Screen.Settings
                    } else if (dy > x) {
                        if (currentScreen == Screen.Settings) currentScreen = Screen.Home
                    }
                }
            }) {
        when (currentScreen) {
            is Screen.Home -> HomeScreen(navController, modifier)
            is Screen.Settings -> SettingsScreen()
            is Screen.Camera -> CameraScreen(navController, modifier)
            is Screen.Profile -> ProfileScreen() // optional
        }
    }
}


@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings Screen")
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Screen")
    }
}