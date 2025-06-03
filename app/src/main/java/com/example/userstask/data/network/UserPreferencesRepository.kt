package com.example.userstask.data.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(context: Context) {

    private val dataStore = context.dataStore

    val userFirstName: Flow<String?> = dataStore.data
        .map { preferences -> preferences[UserPreferencesKeys.USER_FIRST_NAME] }

    val userLastName: Flow<String?> = dataStore.data
        .map { preferences -> preferences[UserPreferencesKeys.USER_LAST_NAME] }

    val userPhotoPath: Flow<String?> = dataStore.data
        .map { preferences -> preferences[UserPreferencesKeys.USER_PHOTO_PATH] }

    suspend fun saveUserDetails(firstName: String, lastName: String, photoPath: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.USER_FIRST_NAME] = firstName
            preferences[UserPreferencesKeys.USER_LAST_NAME] = lastName
            preferences[UserPreferencesKeys.USER_PHOTO_PATH] = photoPath
        }
    }
}

