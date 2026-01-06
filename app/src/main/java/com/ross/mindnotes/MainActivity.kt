package com.ross.mindnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ross.mindnotes.presentation.add_edit_note.AddEditNoteScreen
import com.ross.mindnotes.presentation.calendar.CalendarScreen
import com.ross.mindnotes.presentation.notes.NotesScreen
import com.ross.mindnotes.presentation.theme.MindNotesTheme
import com.ross.mindnotes.presentation.util.Screen
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.ross.mindnotes.presentation.components.FloatingBottomBar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindNotesTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val showBottomBar = currentRoute == Screen.NotesScreen.route || 
                                      currentRoute == Screen.CalendarScreen.route ||
                                      currentRoute == Screen.ThemesScreen.route

                    Box(modifier = Modifier.fillMaxSize()) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize()
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = Screen.NotesScreen.route,
                                modifier = Modifier.padding(
                                    top = innerPadding.calculateTopPadding(),
                                    start = innerPadding.calculateLeftPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
                                    end = innerPadding.calculateRightPadding(androidx.compose.ui.unit.LayoutDirection.Ltr)
                                    // Bottom padding intentionally omitted to allow content to flow under the floating bar
                                )
                            ) {
                                composable(
                                    route = Screen.NotesScreen.route,
                                    enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
                                    exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
                                    popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) },
                                    popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) }
                                ) {
                                    NotesScreen(navController = navController)
                                }
                                composable(
                                    route = Screen.AddEditNoteScreen.route + "?noteId={noteId}",
                                    arguments = listOf(
                                        navArgument(
                                            name = "noteId"
                                        ) {
                                            type = NavType.LongType
                                            defaultValue = -1L
                                        }
                                    ),
                                    enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
                                    exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
                                    popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) },
                                    popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) }
                                ) {
                                    AddEditNoteScreen(
                                        navController = navController
                                    )
                                }
                                composable(
                                    route = Screen.CalendarScreen.route,
                                    enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
                                    exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
                                    popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) },
                                    popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) }
                                ) {
                                    CalendarScreen(navController = navController)
                                }
                                composable(
                                    route = Screen.ThemesScreen.route,
                                    enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
                                    exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
                                    popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) },
                                    popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) }
                                ) {
                                    com.ross.mindnotes.presentation.themes_screen.ThemesScreen(navController = navController)
                                }
                            }
                        }

                        if (showBottomBar) {
                            FloatingBottomBar(
                                currentRoute = currentRoute,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo(Screen.NotesScreen.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}