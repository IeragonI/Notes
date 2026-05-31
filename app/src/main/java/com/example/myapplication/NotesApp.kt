package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NotesApp(repository: NoteRepository) {
    val navController = rememberNavController()
    val factory = ViewModelFactory(repository)

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            val viewModel: MainViewModel = viewModel(factory = factory)
            MainScreen(
                viewModel = viewModel,
                onAddNote = { navController.navigate("edit/-1") },
                onEditNote = { id -> navController.navigate("edit/$id") }
            )
        }
        composable(
            route = "edit/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId")
            val viewModel: EditViewModel = viewModel(factory = factory)
            EditScreen(
                viewModel = viewModel,
                noteId = noteId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
