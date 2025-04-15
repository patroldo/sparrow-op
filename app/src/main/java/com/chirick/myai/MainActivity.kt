package com.chirick.myai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chirick.myai.ui.screens.AnswerScreen
import com.chirick.myai.ui.screens.HomeScreen
import com.chirick.myai.ui.theme.MyAITheme
import dagger.hilt.android.AndroidEntryPoint

const val exampleMessage: String = "Sayboard uses for the following permissions"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAITheme {
                Scaffold { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(
                                navController,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                        composable("answer") { backStackEntry ->
                            AnswerScreen(
                                navController,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
//                        }
                        }
                    }
                }
            }
        }
    }
}
