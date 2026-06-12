package com.example.incluapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.incluapp.ui.screen.help.HelpScreen
import com.example.incluapp.ui.screen.history.HistoryScreen
import com.example.incluapp.ui.screen.home.HomeScreen
import com.example.incluapp.ui.screen.reader.ReaderScreen
import com.example.incluapp.ui.screen.splash.SplashScreen

/**
 * Grafo de navegación tipada de LexiEdu.
 * Usa rutas @Serializable en lugar de Strings para type-safety en tiempo de compilación.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController  = navController,
        startDestination = Splash
    ) {

        composable<Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            HomeScreen(
                onNavigateToReader  = { imagePath ->
                    navController.navigate(Reader(imagePath = imagePath))
                },
                onNavigateToHistory = { navController.navigate(History) },
                onNavigateToHelp    = { navController.navigate(Help) }
            )
        }

        composable<Reader> { backStackEntry ->
            val route: Reader = backStackEntry.toRoute()
            ReaderScreen(
                readingId      = route.readingId,
                imagePath      = route.imagePath,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<History> {
            HistoryScreen(
                onNavigateBack  = { navController.popBackStack() },
                onOpenReading   = { readingId ->
                    navController.navigate(Reader(readingId = readingId))
                }
            )
        }

        composable<Help> {
            HelpScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
