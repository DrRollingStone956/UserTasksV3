package com.example.userstask.ui.screens.userdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.userstask.ui.components.TodoItem
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.userstask.UsersTaskApplication
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    userId: Int,
    navController: NavController
) {
    val context = LocalContext.current
    val repository = (context.applicationContext as UsersTaskApplication).container.postsRepository
    val viewModel: UserDetailsViewModel = viewModel(factory = UserDetailsViewModel.Factory(repository))
    val user by viewModel.user.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val filter by viewModel.filterCompleted.collectAsState()
    val filteredTodos by viewModel.filteredTodos.collectAsState(initial = emptyList())
    val locationPermissions = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(userId, locationPermissions.permissions) {
        locationPermissions.launchMultiplePermissionRequest()
        viewModel.loadUser(userId)
    }

    when{
        loading -> {
            CircularProgressIndicator()
        }
        error != null -> {
            Text("Error: $error")
        }
        user != null -> {

            val userPosition = LatLng(user!!.address.geo.lat.toDouble(), user!!.address.geo.lng.toDouble())

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userPosition, 10f)
            }
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                LazyColumn {
                    item {
                        CenterAlignedTopAppBar(
                            title = {
                                Text("User Details", style = MaterialTheme.typography.headlineSmall)
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                        HorizontalDivider()
                    }

                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Spacer(Modifier.height(16.dp))
                            Text(user!!.name, style = MaterialTheme.typography.headlineMedium)
                            Text("Username: ${user!!.username}")
                            Text("Email: ${user!!.email}")
                            Text("Phone: ${user!!.phone}")
                            Text("Website: ${user!!.website}")
                            Text("Company: ${user!!.company.name}")
                            Text("Address: ${user!!.address.city} ${user!!.address.street} ${user!!.address.suite} ${user!!.address.zipcode}")
                            Spacer(Modifier.height(16.dp))
                            Text("Location:", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Text("Latitude: ${user!!.address.geo.lat}")
                            Text("Longitude: ${user!!.address.geo.lng}")
                            Spacer(Modifier.height(16.dp))

                            if (locationPermissions.allPermissionsGranted) {
                                GoogleMap(
                                    modifier = Modifier
                                        .height(300.dp)
                                        .pointerInput(Unit) {},
                                    cameraPositionState = cameraPositionState,
                                ) {
                                    Marker(
                                        state = com.google.maps.android.compose.MarkerState(position = userPosition),
                                        title = user?.name
                                    )
                                }
                            } else {
                                Text("No permissions granted")
                            }

                            Spacer(Modifier.height(16.dp))
                            Text("Todos:", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Row {
                                FilterButton("All", filter == null) { viewModel.setFilter(null) }
                                FilterButton("Done", filter == true) { viewModel.setFilter(true) }
                                FilterButton("Undone", filter == false) { viewModel.setFilter(false) }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }

                    items(filteredTodos) { todo ->
                        TodoItem(todo = todo)
                    }
                }

            }

        }
    }
}
@Composable
fun FilterButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = if (selected) 4.dp else 0.dp,
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}