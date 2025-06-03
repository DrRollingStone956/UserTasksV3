package com.example.userstask.ui.screens.mydetails

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController

@Composable
fun MyDetailsScreen(
    viewModel: MyDetailsViewModel,
    navController: NavController,
    context: Context = LocalContext.current
) {
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val photoPath by viewModel.photoPath.collectAsState()
    var inputFirstName by remember { mutableStateOf("") }
    var inputLastName by remember { mutableStateOf("") }
    var showFullScreenImage by remember { mutableStateOf(false) }

    LaunchedEffect(firstName) {
        firstName?.let { name ->
            if (name != inputFirstName) {
                inputFirstName = name
            }
        }
    }

    LaunchedEffect(lastName) {
        lastName?.let { name ->
            if (name != inputLastName) {
                inputLastName = name
            }
        }
    }

    var bitmapImage by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(photoPath) {
        photoPath?.let {
            bitmapImage = BitmapFactory.decodeFile(it)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            bitmapImage = bitmap
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        if (showFullScreenImage && bitmapImage != null) {
            Dialog(
                onDismissRequest = { showFullScreenImage = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .clickable { showFullScreenImage = false }
                ) {
                    Image(
                        bitmap = bitmapImage!!.asImageBitmap(),
                        contentDescription = "Full Screen Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "My Details",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp),
                contentAlignment = Alignment.Center
            ) {
                bitmapImage?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                            .clickable { showFullScreenImage = true }
                    )
                }
            }
            OutlinedTextField(
                value = inputFirstName,
                onValueChange = { inputFirstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = inputLastName,
                onValueChange = { inputLastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = CircleShape,
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text("Select Photo")
                }
                Button(
                    onClick = {
                        bitmapImage?.let { bmp ->
                            viewModel.saveUser(context, inputFirstName, inputLastName, bmp)
                        }
                    },
                    enabled = inputFirstName.isNotBlank() && inputLastName.isNotBlank() && bitmapImage != null,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = CircleShape,
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}