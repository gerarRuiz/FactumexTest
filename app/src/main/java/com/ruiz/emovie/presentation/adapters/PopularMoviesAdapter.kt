package com.ruiz.emovie.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.MovieItemGridBinding
import com.ruiz.emovie.domain.model.MoviesPopular
import com.ruiz.emovie.util.constants.Constants

class PopularMoviesAdapter(
    var callback: (item: MoviesPopular?) -> Unit
) : PagingDataAdapter<MoviesPopular, PopularMoviesAdapter.ViewHolder>(
    DiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MovieItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: MovieItemGridBinding,
        private val callback: (item: MoviesPopular?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moviePopular: MoviesPopular?) {

            Glide.with(binding.imageView)
                .load("${Constants.BASE_URL_IMAGES}${moviePopular?.poster_path}")
                .error(R.drawable.ic_image)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView)

            binding.imageView.setOnClickListener { callback.invoke(moviePopular) }
        }

    }

    class DiffUtilCallback : DiffUtil.ItemCallback<MoviesPopular>() {
        override fun areItemsTheSame(oldItem: MoviesPopular, newItem: MoviesPopular): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MoviesPopular, newItem: MoviesPopular): Boolean {
            return oldItem == newItem
        }
    }

}