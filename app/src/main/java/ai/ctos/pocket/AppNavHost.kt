package ai.ctos.pocket

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ai.ctos.pocket.feature.onboarding.OnboardingScreen
import ai.ctos.pocket.feature.radar.RadarScreen
import ai.ctos.pocket.feature.settings.SettingsScreen
import ai.ctos.pocket.feature.target.TargetScreen
import ai.ctos.pocket.feature.terminal.TerminalScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") { OnboardingScreen(navController) }
        composable("radar") { RadarScreen(navController) }
        composable("target") { TargetScreen(navController) }
        composable("terminal") { TerminalScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}
