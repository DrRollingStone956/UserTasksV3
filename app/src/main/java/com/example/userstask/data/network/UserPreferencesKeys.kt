package com.example.userstask.data.network

import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
    val USER_LAST_NAME = stringPreferencesKey("user_last_name")
    val USER_PHOTO_PATH = stringPreferencesKey("user_photo_path")
}