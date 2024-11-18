package com.dk.safepackage.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dk.safepackage.entities.PackageEntity
import com.dk.safepackage.screens.DetailScreen
import com.dk.safepackage.screens.HomeScreen
import com.dk.safepackage.screens.ListScreen
import com.dk.safepackage.screens.RegisterScreen
import com.dk.safepackage.viewmodel.PackageViewModel

@Composable
//se pasa el parametro para que cada componente acceda a la funcion que necesite
fun Navigation(packageViewModel: PackageViewModel){
    val navController = rememberNavController() 
    NavHost(navController, Router.HomeScreen.router) {
        composable(Router.HomeScreen.router) {
            HomeScreen(navController, packageViewModel)
        }
        composable(Router.RegisterScreen.router) {
            RegisterScreen(navController, packageViewModel)
        }
        composable(Router.ListScreen.router) {
            ListScreen(navController, packageViewModel)
        }
        composable(
            "${Router.DetailScreen.router}?packageId={packageId}",
            arguments = listOf(
                navArgument("packageId"){
                    defaultValue = 0
                    nullable = false
                    type = NavType.IntType
                }
            )
        ) {
            backStackEntry ->
            val packageId = backStackEntry.arguments?.getInt("packageId")
            if (packageId != null) {
                DetailScreen(navController, packageId, packageViewModel)
            }
        }
    }
}