package com.example.birthdaycardcodelab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.birthdaycardcodelab.ui.MainScreen
import com.example.birthdaycardcodelab.ui.ResultScreen

private const val MAIN_ROUTE = "main"
private const val RESULT_ROUTE = "result/{screen}/{value}"

@Composable
fun DiceApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MAIN_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MAIN_ROUTE) {
                MainScreen(onGoToResult = { roll -> navController.navigate("result/$roll/$roll") })
            }
            composable(
                route = RESULT_ROUTE,
                arguments = listOf(
                    navArgument("screen") { type = NavType.IntType },
                    navArgument("value") { type = NavType.IntType }
                )
            ) { entry ->
                ResultScreen(
                    screen = entry.arguments?.getInt("screen") ?: 1,
                    value = entry.arguments?.getInt("value") ?: 1,
                    onBack = { navController.popBackStack() },
                    onNavigate = { screen, value -> navController.navigate("result/$screen/$value") }
                )
            }
        }
    }
}
