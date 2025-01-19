package com.example.testtaskgif.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testtaskgif.ui.screens.DetailScreen
import com.example.testtaskgif.ui.screens.HomeScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Routes.Home, modifier = modifier.fillMaxSize()) {
        composable(
            route = Routes.Home,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() + scaleOut() }
        ) {
            HomeScreen(onGifClick = { gifId ->
                navController.navigate("gif_details/$gifId")
            })
        }

        composable(
            route = Routes.GifDetails,
            arguments = listOf(navArgument("gifId") { type = NavType.StringType })
        ) { backStackEntry ->
            val gifId = backStackEntry.arguments?.getString("gifId") ?: ""
            DetailScreen(gifId = gifId)
        }
    }
}