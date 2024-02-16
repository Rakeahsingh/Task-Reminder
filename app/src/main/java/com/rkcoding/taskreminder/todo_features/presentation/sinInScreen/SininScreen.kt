package com.rkcoding.taskreminder.todo_features.presentation.sinInScreen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.rkcoding.taskreminder.core.navigation.Screen
import com.rkcoding.taskreminder.core.utils.SnackBarEvent
import com.rkcoding.taskreminder.todo_features.presentation.sinInScreen.component.GoogleAuthUiClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SinInScreen(
//    state: SinInState,
//    onSinInClick: () -> Unit,
    navController: NavController,
    viewModel: SinInViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            onTapClient = Identity.getSignInClient(context)
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    val signInResult = googleAuthUiClient.sinInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onEvent(SinInEvent.SinInButtonClick(signInResult))
                }
            }
        }
    )


    LaunchedEffect(key1 = true){
        viewModel.snackBarEvent.collectLatest { event ->
            when(event){
                SnackBarEvent.NavigateUp -> Unit

                is SnackBarEvent.ShowSnackBar -> {
                    SnackBarEvent.ShowSnackBar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackBarEvent.Navigate -> {
                    navController.navigate(Screen.TaskListScreen.route)
                }

            }
        }

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {
                scope.launch {
                    val sinInIntentSender = googleAuthUiClient.sinIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            sinInIntentSender ?: return@launch
                        ).build()
                    )
                    navController.navigate(Screen.TaskListScreen.route)
                }
            }
        ) {
            Text(text = "SinIn")
        }

    }


}