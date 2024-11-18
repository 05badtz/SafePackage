package com.dk.safepackage.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dk.safepackage.navigation.Router.HomeScreen
import com.dk.safepackage.navigation.Router.ListScreen
import com.dk.safepackage.navigation.Router.RegisterScreen
import com.dk.safepackage.ui.theme.Pinkish
import com.dk.safepackage.ui.theme.PurpleBg
import com.dk.safepackage.ui.theme.PurpleCo
import com.dk.safepackage.ui.theme.Yellowish

@Composable
fun BottomNavBar(navController: NavController) {
    val currentRoute by remember {
        derivedStateOf { navController.currentBackStackEntry?.destination?.route }
    }
    val navigationItem = listOf(
        HomeScreen,
        ListScreen
    )
    NavigationBar(
        modifier = Modifier.clip(RoundedCornerShape(25.dp)),
        containerColor = PurpleCo
    ) {
        navigationItem.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.router,
                onClick = {
                    navController.navigate(item.router) {
                        popUpTo(HomeScreen.router) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painterResource(item.icon),
                        item.title
                    )
                },
                label = {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemColors(
                    selectedIconColor = Yellowish,
                    selectedTextColor = Yellowish,
                    selectedIndicatorColor = PurpleBg,
                    unselectedIconColor = Pinkish,
                    unselectedTextColor = Pinkish,
                    disabledIconColor = Pinkish.copy(0f),
                    disabledTextColor = Pinkish.copy(0f)
                )
            )
        }
    }
}

@Composable
fun Fab(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate(RegisterScreen.router) },
        containerColor = Pinkish,
        contentColor = PurpleCo,
        shape = CircleShape

    ) {
        Icon(
            painterResource(RegisterScreen.icon),
            RegisterScreen.title
        )
    }
}