package com.ruiz.emovie.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ruiz.emovie.databinding.GenresItemBinding
import com.ruiz.emovie.domain.model.MovieTopRated
import com.ruiz.emovie.domain.model.MoviesGenres

class GenresAdapter : ListAdapter<String, GenresAdapter.ViewHolder>(
    DiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GenresItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val item = getItem(position)
            holder.bind(item)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    class ViewHolder(
        private val binding: GenresItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: String?) {
            binding.tvGenre.text = genre
        }

    }

}

class DiffUtilCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

