package com.example.userstask.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.userstask.data.network.PostsRepository
import com.example.userstask.data.model.PostWithUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TasksListViewModel(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _tasksWithUsers = MutableStateFlow<List<PostWithUser>>(emptyList())
    val tasksWithUsers: StateFlow<List<PostWithUser>> = _tasksWithUsers

    private fun loadTasksWithUsers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val posts = postsRepository.getPosts()

                val postsWithUsers = mutableListOf<PostWithUser>()
                for (post in posts) {
                    val user = postsRepository.getUserById(post.userId)
                    postsWithUsers.add(PostWithUser(post, user))
                }

                _tasksWithUsers.value = postsWithUsers
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    init {
        loadTasksWithUsers()
    }

    class Factory(
        private val postsRepository: PostsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TasksListViewModel::class.java)) {
                return TasksListViewModel(postsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
