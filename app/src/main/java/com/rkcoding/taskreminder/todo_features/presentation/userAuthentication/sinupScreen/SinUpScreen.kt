package com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinupScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.rkcoding.taskreminder.R
import com.rkcoding.taskreminder.core.navigation.Screen
import com.rkcoding.taskreminder.core.utils.UiEvent
import com.rkcoding.taskreminder.ui.theme.CustomPurple
import com.rkcoding.taskreminder.ui.theme.DarkOrange
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SinUpScreen(
    navController: NavController,
    viewModel: SinUpViewModel = hiltViewModel()
) {

    var selectImageUir by remember { mutableStateOf<Uri?>(null) }

    val state by viewModel.state.collectAsState()

    val snackBarState = remember {
        SnackbarHostState()
    }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isCfmPasswordVisible by remember { mutableStateOf(false) }

    // password $ cnmPassword error state
    var isPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    var isCnmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }

    isPasswordError = when{
        state.userPassword.isBlank() -> "Please enter password"
        state.userPassword.length < 6 -> "Enter at least 6 digit"
        else -> null
    }
    isCnmPasswordError = when{
        state.userConfirmPassword.isBlank() -> "Please enter password"
        state.userConfirmPassword.length < 6 -> "Enter at least 6 digit"
        else -> null
    }


    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->
            selectImageUir = uri
        }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when(event){
                UiEvent.NavigateUp -> Unit
                is UiEvent.ShowSnackBar -> {
                    snackBarState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }
                UiEvent.NavigateTo -> {
                    navController.navigate(Screen.SinInScreen.route)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "SinUp Here",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularImage(
                    imageUri = selectImageUir,
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.userName,
                onValueChange = {
                    viewModel.onEvent(SinUpEvent.OnNameValueChange(it))
                },
                label = {
                    Text(text = "Enter Name")
                },
                singleLine = true,
                maxLines = 1,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "person icon")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = state.userEmail,
                onValueChange = {
                    viewModel.onEvent(SinUpEvent.OnEmailValueChange(it))
                },
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
                value = state.userPassword,
                onValueChange = {
                    viewModel.onEvent(SinUpEvent.OnPasswordValueChange(it))
                },
                label = {
                    Text(text = "Enter Password")
                },
                singleLine = true,
                maxLines = 1,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "person icon")
                },
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff
                                         else Icons.Default.Visibility,
                            contentDescription = if (isPasswordVisible) "hide icon" else "show icon"
                        )
                    }

                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                                       else PasswordVisualTransformation(),
                isError = isPasswordError != null && state.userPassword.isNotBlank(),
                supportingText = { Text(text = isPasswordError.orEmpty()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = state.userConfirmPassword,
                onValueChange = {
                    viewModel.onEvent(SinUpEvent.OnConfirmPasswordValueChange(it))
                },
                label = {
                    Text(text = "Enter Confirm Password")
                },
                singleLine = true,
                maxLines = 1,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "person icon")
                },
                trailingIcon = {
                    IconButton(
                        onClick = { isCfmPasswordVisible = !isCfmPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (isCfmPasswordVisible) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = if (isCfmPasswordVisible) "hide icon" else "show icon"
                        )
                    }
                },
                visualTransformation = if (isCfmPasswordVisible) VisualTransformation.None
                                       else PasswordVisualTransformation(),
                isError = isCnmPasswordError != null && state.userConfirmPassword.isNotBlank(),
                supportingText = { Text(text = isCnmPasswordError.orEmpty()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.onEvent(SinUpEvent.SinUpButtonClick)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomPurple
                )
            ) {
                Text(text = "Sinup")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = buildAnnotatedString {
                    append("Already User click here?..")
                    withStyle(
                        SpanStyle(
                            color = DarkOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    ){
                        append(" Sinin Here")
                    }
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )


        }
    }



}

@Composable
fun CircularImage(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .padding(
                top = 4.dp,
                bottom = 4.dp,
                end = 4.dp
            )
            .size(120.dp)
            .clip(RoundedCornerShape(400.dp))
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(400.dp)
            )
            .clickable { onClick() }
    ) {

        if(imageUri != null){
            Image(
                painter = // Add a unique key to force Coil to fetch a new image
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = imageUri).apply(block = fun ImageRequest.Builder.() {
                        // Add a unique key to force Coil to fetch a new image
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }).build()
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

        }else{
            Image(
                painter = painterResource(id = R.drawable.profile__circle),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }


    }

}
