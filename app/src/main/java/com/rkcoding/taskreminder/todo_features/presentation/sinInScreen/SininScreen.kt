package com.rkcoding.taskreminder.todo_features.presentation.sinInScreen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.rkcoding.taskreminder.R
import com.rkcoding.taskreminder.core.navigation.Screen
import com.rkcoding.taskreminder.core.utils.UiEvent
import com.rkcoding.taskreminder.todo_features.presentation.sinInScreen.component.GoogleAuthUiClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun SinInScreen(
    navController: NavController,
    viewModel: SinInViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    // text visible
    val isTextVisible by remember { mutableStateOf(false) }

    // target value
    val textTargetValue by animateFloatAsState(
        targetValue = if (isTextVisible) 1f else 0f,
        label = "animate"
    )

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val snackBarState = remember { SnackbarHostState() }

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


    LaunchedEffect(key1 = Unit){
        viewModel.snackBarEvent.collectLatest { event ->
            when(event){
                UiEvent.NavigateUp -> Unit

                is UiEvent.ShowSnackBar -> {
                    snackBarState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                UiEvent.NavigateTo -> {
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
                }
            }
        ) {
            Text(text = "SinIn")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clickable {
                    scope.launch {
                        val sinInIntentSender = googleAuthUiClient.sinIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                sinInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "google image",
                    modifier = Modifier.size(80.dp)
                )

                AnimatedContent(
                    targetState = textTargetValue,
                    label = "Animated content"
                ) {
                    Text(
                        text = "SinIn with Google",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

    }




}