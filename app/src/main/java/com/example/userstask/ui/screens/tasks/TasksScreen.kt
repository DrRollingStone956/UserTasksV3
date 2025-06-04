package com.example.userstask.ui.screens.tasks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.userstask.UsersTaskApplication
import com.example.userstask.nav.Screen
import com.example.userstask.ui.components.PostItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onPostClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val repository = (context.applicationContext as UsersTaskApplication).container.postsRepository
    val viewModel: TasksListViewModel = viewModel(factory = TasksListViewModel.Factory(repository))
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                }
            }

            else -> {
                Column {
                    CenterAlignedTopAppBar(
                        title = {
                            Text("Tasks", style = MaterialTheme.typography.headlineSmall)
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.navigate(Screen.MyDetails.createRoute())
                            }) {
                                Icon(Icons.Default.Person, contentDescription = "User Details")
                            }
                        }
                    )
                    HorizontalDivider()
                    val postsWithUsers by viewModel.tasksWithUsers.collectAsState()

                    LazyColumn {
                        items(postsWithUsers) { postWithUser ->
                            PostItem(
                                post = postWithUser.post,
                                user = postWithUser.user,
                                onPostClick = { onPostClick(postWithUser.post.postId) },
                                onUserClick = { onUserClick(postWithUser.user.userId) }
                            )
                        }
                    }
                }
            }
        }
    }
}