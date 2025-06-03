package com.example.userstask.ui.screens.mydetails

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userstask.data.network.UserPreferencesRepository
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class MyDetailsViewModel(private val repo: UserPreferencesRepository) : ViewModel() {

    val firstName: StateFlow<String?> = repo.userFirstName
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val lastName: StateFlow<String?> = repo.userLastName
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val photoPath: StateFlow<String?> = repo.userPhotoPath
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun saveUser(context: Context, first: String, last: String, bitmap: Bitmap) {
        viewModelScope.launch {
            val path = saveImageToInternalStorage(context, bitmap)
            repo.saveUserDetails(first, last, path)
        }
    }

    private fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): String {
        val filename = "${UUID.randomUUID()}.jpg"
        val file = File(context.filesDir, filename)
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return file.absolutePath
    }
}
