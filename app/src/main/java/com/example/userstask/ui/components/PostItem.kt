package com.example.userstask.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.userstask.data.model.Post
import com.example.userstask.data.model.User

@Composable
fun PostItem(
    post: Post,
    user: User,
    onPostClick: () -> Unit,
    onUserClick: () -> Unit,
) {
  Column(
      modifier = Modifier
          .fillMaxWidth()
          .clickable { onPostClick() }
          .padding(16.dp)
  ){
      Text(post.title, style = MaterialTheme.typography.headlineMedium)
      Spacer(Modifier.padding(8.dp))
      Text(post.body)
      Spacer(Modifier.padding(8.dp))
      Text("Author: ${user.username}", modifier = Modifier.clickable { onUserClick() })
  }
}