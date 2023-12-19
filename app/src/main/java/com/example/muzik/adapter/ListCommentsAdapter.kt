package com.example.muzik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data_model.standard_model.Comment
import com.squareup.picasso.Picasso

class ListCommentsAdapter(private val comments: List<Comment>) :
    RecyclerView.Adapter<ListCommentsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentAccountImageView: ImageView = itemView.findViewById(R.id.comment_account_image)
        val commentTextView: TextView = itemView.findViewById(R.id.comment_text_view)
        val commentUsernameTextView: TextView =
            itemView.findViewById(R.id.comment_account_username_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.commentUsernameTextView.text = comment.username
        holder.commentTextView.text = comment.comment
        Picasso.get().load(comment.avatarUri).into(holder.commentAccountImageView)
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}
