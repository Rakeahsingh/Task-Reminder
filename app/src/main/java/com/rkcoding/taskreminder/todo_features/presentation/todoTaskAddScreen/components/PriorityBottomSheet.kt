package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityBottomSheet(
    state: SheetState,
    isSheetShow: Boolean,
    priority: List<String>,
    onItemClick: (String) -> Unit,
    onDismissRequest: () -> Unit
) {

    if (isSheetShow){
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = { onDismissRequest() },
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(text = "Related to Priority")
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider()
                }
            },
            content = {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ){
                  items(priority){ priority ->
                      Box(
                          modifier = Modifier.fillMaxWidth()
                              .clickable { onItemClick(priority) }
                              .padding(8.dp)
                      )
                  }
                    if (priority.isEmpty()){
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Ready to begin? First, Add a Priority"
                            )
                        }
                    }
                }
            }
        )
    }

}