package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.rkcoding.taskreminder.R
import com.rkcoding.taskreminder.core.navigation.Screen
import com.rkcoding.taskreminder.core.utils.UiEvent
import com.rkcoding.taskreminder.todo_features.domain.model.UserData
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.DialogBox
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen.components.TaskListItem
import kotlinx.coroutines.flow.collectLatest


@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@OptIn( ExperimentalFoundationApi::class)
@Composable
fun TaskListScreen(
    userData: UserData?,
    onSinOut: () -> Unit,
    navController: NavController,
    viewModel: TaskListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

//    // coroutine scope
//    val scope = rememberCoroutineScope()

    // show sinOut dialog state
    var showSinOutDialog by remember { mutableStateOf(false) }

    // snackBar state
    val snackBarState = remember { SnackbarHostState() }

      // floating action button functionality
    val listState = rememberLazyListState()
    val isFabExtended by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    // category tab row state
    val pagerState = rememberPagerState(pageCount = {2})
    val category = listOf(
        "All Tasks", "Incomplete Task", "Complete Task"
    )
    var selectedTab by remember { mutableIntStateOf(0) }


    // Show SinOut Dialog
    DialogBox(
        text = "Google SinOut",
        bodyText = "Are you sure you want to SinOut your Account",
        iconImage = Icons.Default.GroupOff,
        isDialogShow = showSinOutDialog,
        onConfirmButtonClick = {
            showSinOutDialog = false
            onSinOut()
        },
        onDismissRequest = { showSinOutDialog = false },
        confirmText = "SinOut"
        )

    LaunchedEffect(key1 = Unit){
        viewModel.uiEvent.collectLatest { event ->
            when(event){
                UiEvent.NavigateTo -> Unit
                UiEvent.NavigateUp -> Unit
                is UiEvent.ShowSnackBar -> {
                    snackBarState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.AddTaskScreen.route) },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add tasks")},
                text = { Text(text = "Add Tasks") },
                expanded = isFabExtended
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state.isLoading) {
                CircularProgressIndicator()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Task Reminder",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                if (userData?.profileImageUrl != null) {
                    AsyncImage(
                        model = userData.profileImageUrl,
                        contentDescription = "profile image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable { showSinOutDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.search,
                onValueChange = {
                    viewModel.onEvent(TaskListEvent.OnSearchValueChange(it))
                },
                label = {
                    Text(text = "Search...")
                },
                trailingIcon = {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurface,
                indicator = { tabPosition ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPosition[selectedTab])
                    )
                }
            ) {
                category.forEachIndexed { index, category ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { selectedTab = index },
                        text = { Text(text = category) }
                    )
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            if (state.tasks.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(250.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.task),
                        contentDescription = "Books",
                        alignment = Alignment.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "You don't have any Tasks. \n Click the + Icon to Add Tasks",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {

                when (selectedTab) {

                    0 -> {
                        items(state.tasks) { task ->
                            TaskListItem(
                                task = task,
                                navController = navController,
                                snackBarState = snackBarState
                            )
                        }
                    }

                    1 -> {
                        items(state.tasks.filter { !it.isCompleted }) { task ->
                            TaskListItem(
                                task = task,
                                navController = navController,
                                snackBarState = snackBarState
                            )
                        }
                    }

                    2 -> {
                        items(state.tasks.filter { it.isCompleted }) { task ->
                            TaskListItem(
                                task = task,
                                navController = navController,
                                snackBarState = snackBarState
                            )
                        }
                    }

                }

            }

        }
    }

}

