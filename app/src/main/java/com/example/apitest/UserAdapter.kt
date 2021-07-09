package com.example.apitest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apitest.databinding.ItemTodoBinding

class UserAdapter(var users: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    inner class UserViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.apply {
            val user = users[position]
            tvTitle.text = "Username: " + user.username
            tvName.text = "Email: " + user.email
        }
    }

}