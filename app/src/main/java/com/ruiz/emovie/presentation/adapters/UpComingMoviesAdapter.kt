package com.ruiz.emovie.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.MovieItemBinding
import com.ruiz.emovie.domain.model.MovieUpcoming
import com.ruiz.emovie.util.constants.Constants

class UpComingMoviesAdapter(
    var callback: (item: MovieUpcoming?) -> Unit
) : PagingDataAdapter<MovieUpcoming, UpComingMoviesAdapter.ViewHolder>(
    DiffUtilCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, callback)
    }

    class ViewHolder(
        private val binding: MovieItemBinding,
        private val callback: (item: MovieUpcoming?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieUpcoming: MovieUpcoming?) {
            Glide.with(binding.imageView)
                .load("${Constants.BASE_URL_IMAGES}${movieUpcoming?.poster_path}")
                .error(R.drawable.ic_image)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView)

            binding.imageView.setOnClickListener { callback.invoke(movieUpcoming) }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<MovieUpcoming>() {
        override fun areItemsTheSame(oldItem: MovieUpcoming, newItem: MovieUpcoming): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieUpcoming, newItem: MovieUpcoming): Boolean {
            return oldItem == newItem
        }
    }

}