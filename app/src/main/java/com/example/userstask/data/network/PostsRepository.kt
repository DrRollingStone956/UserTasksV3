package com.example.userstask.data.network

import com.example.userstask.data.model.Post
import com.example.userstask.data.model.Todo
import com.example.userstask.data.model.User

interface PostsRepository {
    suspend fun getPosts(): List<Post>
    suspend fun getPostById(postId: Int): Post
    suspend fun getUsers(): List<User>
    suspend fun getUserById(userId: Int): User
    suspend fun getTodosByUser(userId: Int): List<Todo>
}

class DefaultPostsRepository(
    private val postsService: PostsService
) : PostsRepository {

    override suspend fun getPosts(): List<Post> {
        return postsService.getPosts()
    }

    override suspend fun getPostById(postId: Int): Post {
        return postsService.getPostById(postId)
    }

    override suspend fun getUsers(): List<User> {
        return postsService.getUsers()
    }

    override suspend fun getUserById(userId: Int): User {
        return postsService.getUserById(userId)
    }

    override suspend fun getTodosByUser(userId: Int): List<Todo> {
        return postsService.getTodosByUser(userId)
    }
}

