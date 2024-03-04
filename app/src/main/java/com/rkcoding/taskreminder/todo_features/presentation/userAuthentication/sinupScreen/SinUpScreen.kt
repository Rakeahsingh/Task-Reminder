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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.rkcoding.taskreminder.R
import com.rkcoding.taskreminder.ui.theme.CustomPurple
import com.rkcoding.taskreminder.ui.theme.DarkOrange

@Composable
fun SinUpScreen(
    navController: NavController
) {

    var selectImageUir by remember{
        mutableStateOf<Uri?>(null)
    }
    
    // email text
    var name by remember { mutableStateOf("") }

    // password
    var email by remember { mutableStateOf("") }

    // email text
    var password by remember { mutableStateOf("") }

    // password
    var cnmPassword by remember { mutableStateOf("") }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->
            selectImageUir = uri
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "SinUp Here",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
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
            value = name,
            onValueChange = { name = it },
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
                Icon(imageVector = Icons.Default.Visibility, contentDescription = "visible icon")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))


        OutlinedTextField(
            value = cnmPassword,
            onValueChange = { cnmPassword = it },
            label = {
                Text(text = "Enter Confirm Password")
            },
            singleLine = true,
            maxLines = 1,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "person icon")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Visibility, contentDescription = "visible icon")
            },
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
