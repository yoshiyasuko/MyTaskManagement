package com.dashimaki_dofu.mytaskmanagement.view.composable

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dashimaki_dofu.mytaskmanagement.NavLinks


/**
 * MyBottomNavigation
 *
 * Created by Yoshiyasu on 2024/03/11
 */

enum class BottomNavigationScreen(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    TASK_LIST(NavLinks.TaskList.route, Icons.Filled.Check, "課題"),
    CALENDAR(NavLinks.Calendar.route, Icons.Filled.DateRange, "カレンダー")
}

@Composable
fun MyBottomNavigation(
    navController: NavController
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomNavigationScreen.entries.forEach { screen ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                label = {
                    Text(text = screen.title)
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null
                    )
                },
                onClick = {
                    navController.navigate(screen.route)
                }
            )
        }
    }
}
