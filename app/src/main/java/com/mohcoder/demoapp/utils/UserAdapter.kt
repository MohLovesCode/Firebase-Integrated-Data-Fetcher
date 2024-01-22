package com.mohcoder.demoapp.utils

/**
 * Copyright (c) 2024 MohLovesCode
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the “Software”), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * */

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohcoder.demoapp.R
import com.mohcoder.demoapp.model.User
import com.squareup.picasso.Picasso
import kotlin.math.min

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

class UserAdapter(private val context: Context, private val users: MutableList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePictureImageView: ImageView = itemView.findViewById(R.id.profilePictureImageView)
        val fullNameTextView: TextView = itemView.findViewById(R.id.fullNameTextView)
        val birthdateTextView: TextView = itemView.findViewById(R.id.birthdateTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val phoneTextView: TextView = itemView.findViewById(R.id.phoneTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]

        Picasso.get().load(user.image).into(holder.profilePictureImageView)
        holder.fullNameTextView.text =  context.getString(
            R.string.user_display_name, user.firstName,
            user.maidenName?.first()?.uppercase(),
            user.lastName
        )
        holder.birthdateTextView.text = user.birthDate
        holder.emailTextView.text= user.email
        holder.phoneTextView.text = user.phone
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(user)
        }
    }

    // Custom click listener
    private var onItemClickListener: ((User) -> Unit)? = null

    // Public method to set the click listener
    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }

    fun addUsers(newUsers: List<User>) {
        val startPosition = users.size
        users.addAll(newUsers)
        notifyItemRangeInserted(startPosition, newUsers.size)
    }

    fun refreshData(newUsers: List<User>) {
        val oldSize = users.size
        val newSize = newUsers.size
        val itemCountChanged = min(oldSize, newSize)
        users.clear()
        users.addAll(newUsers)
        if (oldSize > newSize) {
            if (newSize > 0) {
                notifyItemRangeRemoved(newSize - 1, oldSize - newSize)
            } else {
                notifyItemRangeRemoved(0, oldSize)
            }
        }
        else if (oldSize < newSize) notifyItemRangeInserted(oldSize, newSize - oldSize)
        else if (itemCountChanged > 0) notifyItemRangeChanged(0, itemCountChanged)
    }
}