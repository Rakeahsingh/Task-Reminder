package com.rkcoding.taskreminder.core.navigation

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinInScreen.SinInScreen
import com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinInScreen.component.GoogleAuthUiClient
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.AddTaskScreen
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen.TaskListScreen
import com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinupScreen.SinUpScreen
import kotlinx.coroutines.launch


@Composable
fun NavGraphBuilder(
    firebaseAuth: FirebaseAuth
) {

    val hasUser = firebaseAuth.currentUser?.uid

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            onTapClient = Identity.getSignInClient(context)
        )
    }


    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (hasUser != null) Screen.TaskListScreen.route else Screen.SinInScreen.route
    ){

        composable(Screen.SinInScreen.route){

            SinInScreen(
                navController = navController
            )
        }

        composable(Screen.SinUpScreen.route){

            SinUpScreen(
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
            route = Screen.AddTaskScreen.route + "?taskId={taskId}",
            deepLinks = listOf(navDeepLink {
                uriPattern = "task_reminder://dashboard/taskList"
                action = Intent.ACTION_VIEW
            }),
            arguments = listOf(
                navArgument("taskId"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ){
            val taskId = it.arguments?.getString("taskId") ?: ""
            AddTaskScreen(
                navController = navController
            )
        }

    }

}



