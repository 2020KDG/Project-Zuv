package com.example.zuv

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object AppRoutes {
    const val LOGIN_SCREEN = "login"
    const val MAIN_SCREEN = "main"
    const val SEARCH_SCREEN = "search"
    const val HISTORY_SCREEN = "history"
    const val PAYMENT_SCREEN = "payment"
    const val CUSTOMER_SERVICE_SCREEN = "customer_service"
    const val SETTINGS_SCREEN = "settings"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.LOGIN_SCREEN) {
        composable(AppRoutes.LOGIN_SCREEN) {
            LoginScreen(
                onNavigateToMain = {
                    navController.navigate(AppRoutes.MAIN_SCREEN) {
                        popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoutes.MAIN_SCREEN) {
            // MainScreen이 NavController 전체가 아닌, 필요한 기능만 콜백으로 받도록 수정
            MainScreen(
                navController = navController, // 화면 간 결과 전달을 위해 이건 유지
                onNavigateToHistory = { navController.navigate(AppRoutes.HISTORY_SCREEN) },
                onNavigateToPayment = { navController.navigate(AppRoutes.PAYMENT_SCREEN) },
                onNavigateToCustomerService = { navController.navigate(AppRoutes.CUSTOMER_SERVICE_SCREEN) },
                onNavigateToSettings = { navController.navigate(AppRoutes.SETTINGS_SCREEN) }
            )
        }
        composable(AppRoutes.SEARCH_SCREEN) {
            SearchScreen(navController = navController)
        }
        composable(AppRoutes.HISTORY_SCREEN) {
            HistoryScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(AppRoutes.PAYMENT_SCREEN) {
            PaymentScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(AppRoutes.CUSTOMER_SERVICE_SCREEN) {
            CustomerServiceScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(AppRoutes.SETTINGS_SCREEN) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}