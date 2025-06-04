package com.example.userstask.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName("id") val postId: Int,
    val userId: Int,
    val title: String,
    val body: String
)
