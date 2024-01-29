package com.example.posts.Ui.Posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.exampel.posts.Model.Post
import com.example.posts.databinding.PostItemLayoutBinding


class PostsAdapter(val onPostItemClicked: OnPostItemClicked) : Adapter<PostsAdapter.PostsViewHolder>() {
    var posts :List<Post> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = PostItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding = PostItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostsViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val post = posts[position]
        with(holder){
            binding.postTitle.text = post.title
            binding.postBody.text = post.body
            itemView.setOnClickListener { onPostItemClicked.onPostClicked(post.id!!) }
        }
    }

    interface OnPostItemClicked {
        fun onPostClicked(id:Int)
    }
}