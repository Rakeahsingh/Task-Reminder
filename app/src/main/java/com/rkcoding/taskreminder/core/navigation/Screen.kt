package com.rkcoding.taskreminder.core.navigation

sealed class Screen(val route: String) {

    data object SinInScreen: Screen("SinInScreen")

    data object TaskListScreen: Screen("TaskListScreen")

}