package com.rkcoding.taskreminder.core.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.auth.api.identity.Identity
import com.rkcoding.taskreminder.todo_features.presentation.sinInScreen.SinInScreen
import com.rkcoding.taskreminder.todo_features.presentation.sinInScreen.component.GoogleAuthUiClient
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.AddTaskScreen
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen.TaskListScreen
import kotlinx.coroutines.launch


@Composable
fun NavGraphBuilder() {


    val context = LocalContext.current
//
    val scope = rememberCoroutineScope()
//
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            onTapClient = Identity.getSignInClient(context)
        )
    }


    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SinInScreen.route,
    ){

        composable(Screen.SinInScreen.route){

            SinInScreen(
                navController = navController
            )
        }


        composable(Screen.TaskListScreen.route){
            TaskListScreen(
                userData = googleAuthUiClient.getSingedInUser(),
                onSinOut = {
                    scope.launch {
                        googleAuthUiClient.sinOut()
                        Toast.makeText(context, "SinOut Successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.SinInScreen.route)
                    }
                },
                navController = navController
            )
        }

        composable(
            route = Screen.AddTaskScreen.route + "?taskId={taskID}",
            arguments = listOf(
                navArgument("taskID"){
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ){
            val taskId = it.arguments?.getInt("taskID") ?: -1
            AddTaskScreen(navController = navController)
        }

    }

}



