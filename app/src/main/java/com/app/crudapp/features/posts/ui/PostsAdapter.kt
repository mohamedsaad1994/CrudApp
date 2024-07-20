package com.app.crudapp.features.posts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.crudapp.databinding.ItemPostBinding
import com.app.crudapp.features.posts.data.models.PostsListResponseItem

class PostsAdapter(
    val action: PostAction
) : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {
    private var data = ArrayList<PostsListResponseItem>()

    fun addData(data: List<PostsListResponseItem>, start: Int, count: Int) {
        this.data.addAll(data)
        notifyItemRangeChanged(start, count)
    }

    fun updateItem(index: Int, title: String, body: String) {
        data[index].title = title
        data[index].body = body
        notifyItemChanged(index)
    }
    fun deleteItem(adapterPosition: Int) {
        data[adapterPosition]
notifyItemRemoved(adapterPosition)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = data[adapterPosition]
            binding.tvPostTitle.text = item.title
            binding.tvPostBody.text = item.body
            actions(item)
        }

        private fun actions(item: PostsListResponseItem) {
            binding.tvEdit.setOnClickListener {
                action.edit(item, adapterPosition)
            }
            binding.tvDelete.setOnClickListener {
                action.delete(item, adapterPosition)
            }
        }
    }

    interface PostAction {
        fun edit(data: PostsListResponseItem, adapterPosition: Int)
        fun delete(data: PostsListResponseItem, adapterPosition: Int)
    }
}