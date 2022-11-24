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
import com.ruiz.emovie.domain.model.MyFavoriteMovies
import com.ruiz.emovie.util.constants.Constants

class MyFavoriteMoviesAdapter(
    var callback: (item: MyFavoriteMovies?) -> Unit
) : PagingDataAdapter<MyFavoriteMovies, MyFavoriteMoviesAdapter.ViewHolder>(
    DiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: MovieItemBinding,
        private val callback: (item: MyFavoriteMovies?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(myFavoriteMovies: MyFavoriteMovies?) {

            Glide.with(binding.imageView)
                .load("${Constants.BASE_URL_IMAGES}${myFavoriteMovies?.poster_path}")
                .error(R.drawable.ic_image)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView)

            binding.imageView.setOnClickListener { callback.invoke(myFavoriteMovies) }
        }

    }

    class DiffUtilCallback : DiffUtil.ItemCallback<MyFavoriteMovies>() {
        override fun areItemsTheSame(
            oldItem: MyFavoriteMovies,
            newItem: MyFavoriteMovies
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MyFavoriteMovies,
            newItem: MyFavoriteMovies
        ): Boolean {
            return oldItem == newItem
        }
    }

}