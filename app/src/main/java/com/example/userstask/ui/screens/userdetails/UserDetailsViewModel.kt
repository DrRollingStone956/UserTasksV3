package com.example.userstask.ui.screens.userdetails


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.userstask.data.model.Todo
import com.example.userstask.data.model.User
import com.example.userstask.data.network.PostsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class UserDetailsViewModel (
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user : StateFlow<User?> = _user

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    private val _filterCompleted = MutableStateFlow<Boolean?>(null)
    val filterCompleted: StateFlow<Boolean?> = _filterCompleted

    val filteredTodos: StateFlow<List<Todo>> = combine(_todos, _filterCompleted) { todos, filter ->
        when (filter) {
            null -> todos
            true -> todos.filter { it.completed }
            false -> todos.filter { !it.completed }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(completed: Boolean?) {
        _filterCompleted.value = completed
    }

    private val _isLoading = MutableStateFlow(true)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error : StateFlow<String?> = _error

    fun loadUser(userId: Int)
    {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _user.value = postsRepository.getUserById(userId)
                val user = postsRepository.getUserById(userId)
                _user.value = user
                _todos.value = postsRepository.getTodosByUser(user.userId)
            }
            catch (e: Exception) {
                _error.value = e.message
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    class Factory(
        private val postsRepository: PostsRepository
    ) : ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserDetailsViewModel::class.java)) {
                return UserDetailsViewModel(postsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}