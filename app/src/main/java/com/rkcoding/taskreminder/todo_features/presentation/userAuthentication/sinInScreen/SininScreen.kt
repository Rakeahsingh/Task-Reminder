package com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinInScreen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.rkcoding.taskreminder.R
import com.rkcoding.taskreminder.core.navigation.Screen
import com.rkcoding.taskreminder.core.utils.UiEvent
import com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinInScreen.component.GoogleAuthUiClient
import com.rkcoding.taskreminder.ui.theme.CustomPurple
import com.rkcoding.taskreminder.ui.theme.DarkOrange
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun SinInScreen(
    navController: NavController,
    viewModel: SinInViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()


    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val snackBarState = remember { SnackbarHostState() }

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            onTapClient = Identity.getSignInClient(context)
        )
    }

    // email text
    var email by remember { mutableStateOf("") }

    // password
    var password by remember { mutableStateOf("") }


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


    LaunchedEffect(key1 = state.isSinInSuccess){
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

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        floatingActionButton = {
            FloatingGoogleButton {
            scope.launch {
                val sinInIntentSender = googleAuthUiClient.sinIn()
                launcher.launch(
                    IntentSenderRequest
                        .Builder(
                            sinInIntentSender ?: return@launch
                        )
                        .build()
                )
             }
           }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "SinIn Here",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))


            OutlinedTextField(
               value = email,
               onValueChange = { email = it },
               label = {
                       Text(text = "Enter Email")
               },
               singleLine = true,
               maxLines = 1,
               leadingIcon = {
                   Icon(imageVector = Icons.Default.Email, contentDescription = "person icon")
               },
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(horizontal = 12.dp)
           )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(text = "Enter Password")
                },
                singleLine = true,
                maxLines = 1,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "person icon")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "password visible icon"
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomPurple
                )
            ) {
                Text(text = "Sinin")
            }

            Spacer(modifier = Modifier.height(12.dp))



            Text(
                text = buildAnnotatedString {
                    append("New User click here?..")
                    withStyle(
                        SpanStyle(
                            color = DarkOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    ){
                        append(
                            " Sinup Here"
                        )

                    }
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.SinUpScreen.route)
                }
            )

        }
    }


}


@Composable
fun FloatingGoogleButton(
    onIconClick: () -> Unit
) {

    FloatingActionButton(
        onClick = { onIconClick() },
        containerColor = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "google image",
                modifier = Modifier.size(60.dp)
            )
        }
    }

}