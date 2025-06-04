package com.example.userstask.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val userId: Int,
    @SerialName("id") val todoId: Int,
    val title: String,
    val completed: Boolean

)
