package com.ruiz.emovie.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.MovieItemGridBinding

class GalleryAdapter(
    private val urlImages: MutableList<String>,
    private val onLongClick: (url: String) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onLongClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = urlImages[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = urlImages.size

    fun add(url: String){
        if (!urlImages.contains(url)){
            urlImages.add(url)
            notifyItemInserted(urlImages.size - 1)
        }else{
            update(url)
        }
    }

    fun update(url: String){
        val index = urlImages.indexOf(url)
        if (index != -1){
            urlImages[index] = url
            notifyItemChanged(index)
        }
    }

    fun delete(url: String){
        val index = urlImages.indexOf(url)
        if (index != -1){
            urlImages.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class ViewHolder(
        private val binding: MovieItemGridBinding,
        private val onLongClick: (url: String) -> Unit
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(url: String){

            Glide.with(binding.root)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_image)
                .into(binding.imageView)

            binding.imageView.setOnLongClickListener {
                onLongClick.invoke(url)
                true
            }

        }
    }

}
