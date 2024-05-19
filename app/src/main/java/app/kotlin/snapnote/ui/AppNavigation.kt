package app.kotlin.snapnote.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.kotlin.snapnote.ui.views.CreateNoteScreen
import app.kotlin.snapnote.ui.views.Destination
import app.kotlin.snapnote.ui.views.MainScreen
import app.kotlin.snapnote.ui.views.PermissionRequestScreen
import app.kotlin.snapnote.ui.views.SearchingScreen
import app.kotlin.snapnote.ui.views.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.WelcomeScreen.route) {
        composable(route = Destination.WelcomeScreen.route) {
            WelcomeScreen(navController = navController)
        }
        composable(route = Destination.PermissionRequestScreen.route) {
            PermissionRequestScreen(navController = navController)
        }
        composable(route = Destination.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(route = Destination.SearchingScreen.route) {
            SearchingScreen(navController = navController)
        }
        composable(route = Destination.CreateNoteScreen.route) {
            CreateNoteScreen(navController = navController)
        }
    }
}