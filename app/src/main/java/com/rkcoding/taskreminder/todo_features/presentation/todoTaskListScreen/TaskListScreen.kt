package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rkcoding.taskreminder.todo_features.domain.model.UserData

@Composable
fun TaskListScreen(
    userData: UserData?,
    onSinOut: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (userData?.profileImageUrl != null){
                AsyncImage(
                    model = userData.profileImageUrl,
                    contentDescription = "profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            if (userData?.userName != null){
                Text(
                    text = userData.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = onSinOut
            ) {
                Text(text = "SinOut")
            }

        }

    }

}