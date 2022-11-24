package com.ruiz.emovie.presentation.fragments.details

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.FragmentDetailsBinding
import com.ruiz.emovie.domain.model.ApiMarkFavoriteRequest
import com.ruiz.emovie.domain.model.MovieTopRated
import com.ruiz.emovie.domain.model.MovieUpcoming
import com.ruiz.emovie.domain.model.MoviesPopular
import com.ruiz.emovie.presentation.adapters.GenresAdapter
import com.ruiz.emovie.presentation.common.BaseFragment
import com.ruiz.emovie.presentation.common.CustomLoader
import com.ruiz.emovie.util.constants.Constants
import com.ruiz.emovie.util.constants.Constants.MEDIA_TYPE_MOVIE
import com.ruiz.emovie.util.enums.DialogAnim
import com.ruiz.emovie.util.extensions.showBasicDialog
import com.ruiz.emovie.util.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate) {

    private lateinit var binding: FragmentDetailsBinding

    private val viewModel: DetailsViewModel by viewModels()

    private val safeArgs: DetailsFragmentArgs by navArgs()

    private lateinit var genresAdapter: GenresAdapter

    private val customLoader: CustomLoader by lazy { CustomLoader(requireContext()) }

    private var moviedId: Int = 0
    private var sessionId: String = ""
    private var accountId: String = ""

    private var movieTopRated: MovieTopRated? = null
    private var movieUpcoming: MovieUpcoming? = null
    private var moviePopular: MoviesPopular? = null

    override fun FragmentDetailsBinding.initialize() {
        binding = this
        observerState()
        initSessionId()
        initAccountId()
        listeners()
        initRecyclerViewGenders()

        when (safeArgs.typeMovie) {

            Constants.TYPE_MOVIE_UPCOMING -> executeGetSelectedUpComingMovie()

            Constants.TYPE_MOVIE_TOP -> executeGetSelectedTopRatedMovie()

            Constants.TYPE_MOVIE_POPULAR -> executeGetSelectedPopularMovie()

        }
    }

    private fun listeners() {

        binding.imgBtnBack.setOnClickListener { findNavController().popBackStack() }

        binding.layoutInfo.btnWatchTrailer.setOnClickListener {
            val action =
                DetailsFragmentDirections.actionDetailsFragmentToVideoPlayerFragment(movieId = safeArgs.movieId)
            findNavController().navigate(action)
        }

        binding.btnMarkFavorite.setOnClickListener {
            if (sessionId.isEmpty()) {
                showBasicDialog(
                    title = getString(R.string.error),
                    message = getString(R.string.text_error_no_session),
                    textButton = getString(R.string.text_de_acuerdo),
                    anim = DialogAnim.ERROR
                ) {}
            } else {

                val favorite = if (movieTopRated != null){
                    !movieTopRated!!.favorite
                } else if (movieUpcoming != null){
                    !movieUpcoming!!.favorite
                } else {
                    !moviePopular!!.favorite
                }

                viewModel.markFavorite(
                    accountId = accountId,
                    sessionId = sessionId,
                    ApiMarkFavoriteRequest(
                        media_type = MEDIA_TYPE_MOVIE,
                        media_id = moviedId,
                        favorite = favorite
                    )
                )
                Log.i("TAG", "listeners: $favorite")
            }
        }

    }

    private fun observerState() {

        viewModel.markFavoriteState.observe(viewLifecycleOwner) { state ->

            when (state) {

                is UIState.Success -> {
                    if (state.data.success) {

                        var message = ""

                        if (movieTopRated != null){
                            movieTopRated!!.favorite = !movieTopRated!!.favorite
                            message = if (movieTopRated!!.favorite) getString(R.string.adde_to_favorites) else getString(R.string.deleted_to_favorites)
                            if (movieTopRated!!.favorite) binding.btnMarkFavorite.setImageResource(R.drawable.ic_favorite) else binding.btnMarkFavorite.setImageResource(R.drawable.ic_no_favorite)
                            viewModel.markFavoriteTopRatedMovie(movieTopRated!!.id.toInt(), movieTopRated!!.favorite)
                        }

                        if (movieUpcoming != null){
                            movieUpcoming!!.favorite = !movieUpcoming!!.favorite
                            message = if (movieUpcoming!!.favorite) getString(R.string.adde_to_favorites) else getString(R.string.deleted_to_favorites)
                            if (movieUpcoming!!.favorite) binding.btnMarkFavorite.setImageResource(R.drawable.ic_favorite) else binding.btnMarkFavorite.setImageResource(R.drawable.ic_no_favorite)
                            viewModel.markFavoriteUpComingMovie(movieUpcoming!!.id.toInt(), movieUpcoming!!.favorite)
                        }

                        if (moviePopular != null){
                            moviePopular!!.favorite = !moviePopular!!.favorite
                            message = if (moviePopular!!.favorite) getString(R.string.adde_to_favorites) else getString(R.string.deleted_to_favorites)
                            if (moviePopular!!.favorite) binding.btnMarkFavorite.setImageResource(R.drawable.ic_favorite) else binding.btnMarkFavorite.setImageResource(R.drawable.ic_no_favorite)
                            viewModel.markFavoritePopularMovie(moviePopular!!.id.toInt(), moviePopular!!.favorite)
                        }

                        Toast.makeText(
                            requireContext(),
                            message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                is UIState.Error -> {
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                is UIState.Loading -> {
                    when (state.status) {
                        true -> customLoader.show()
                        false -> customLoader.getDialog().cancel()
                    }
                }

                else -> Unit
            }
        }

    }

    private fun initSessionId() {
        lifecycleScope.launch {
            viewModel.sessionId.collectLatest {
                sessionId = it
            }
        }
    }

    private fun initAccountId() {
        lifecycleScope.launch {
            viewModel.accountId.collectLatest {
                accountId = it
            }
        }
    }

    private fun initRecyclerViewGenders() {

        binding.layoutInfo.recyclerViewGenres.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            genresAdapter = GenresAdapter()
            adapter = genresAdapter
        }

    }

    private fun executeGetSelectedTopRatedMovie() {

        lifecycleScope.launch {
            viewModel.getSelectedMovieTopRated(safeArgs.movieId.toLong())
            viewModel.selectedTopRatedMovie.collectLatest { value ->
                moviedId = value?.id?.toInt() ?: 0
                getImage(binding.rootImageView, movieTopRated = value)
                setData(movieTopRated = value)
            }

        }

    }


    private fun executeGetSelectedUpComingMovie() {
        lifecycleScope.launch {
            viewModel.getSelectedMovieUpComing(safeArgs.movieId.toLong())
            viewModel.selectedUpcomingMovie.collectLatest { value ->
                moviedId = value?.id?.toInt() ?: 0
                getImage(binding.rootImageView, movieUpcoming = value)
                setData(movieUpcoming = value)
            }
        }
    }

    private fun executeGetSelectedPopularMovie() {
        lifecycleScope.launch {
            viewModel.getSelectedMoviePopular(safeArgs.movieId.toLong())
            viewModel.selectedPopularMovie.collectLatest { value ->
                moviedId = value?.id?.toInt() ?: 0
                getImage(binding.rootImageView, moviePopular = value)
                setData(moviePopular = value)
            }
        }
    }

    private fun getImage(
        imgView: ImageView,
        movieTopRated: MovieTopRated? = null,
        movieUpcoming: MovieUpcoming? = null,
        moviePopular: MoviesPopular? = null
    ) {

        var value = ""
        if (movieTopRated != null) {
            value = movieTopRated.backdrop_path
        }

        if (movieUpcoming != null) {
            value = movieUpcoming.backdrop_path ?: ""
        }

        if (moviePopular != null) {
            value = moviePopular.backdrop_path ?: ""
        }

        Glide.with(imgView)
            .load("${Constants.BASE_URL_IMAGES}${value}")
            .centerCrop()
            .into(imgView)

    }

    private fun setData(
        movieTopRated: MovieTopRated? = null,
        movieUpcoming: MovieUpcoming? = null,
        moviePopular: MoviesPopular? = null
    ) {

        with(binding) {

            if (movieTopRated != null) {
                this@DetailsFragment.movieTopRated = movieTopRated
                if (movieTopRated.favorite) btnMarkFavorite.setImageResource(R.drawable.ic_favorite) else btnMarkFavorite.setImageResource(R.drawable.ic_no_favorite)
                with(layoutInfo) {
                    tvMovieTitle.text = movieTopRated.title
                    tvInfoYear.text = movieTopRated.release_date.substring(0, 4)
                    tvOriginalLanguage.text = movieTopRated.original_language
                    tvInfoRating.text = movieTopRated.vote_average.toString()
                    tvInfoOverview.text = movieTopRated.overview
                }
            }

            if (movieUpcoming != null) {
                this@DetailsFragment.movieUpcoming = movieUpcoming
                if (movieUpcoming.favorite) btnMarkFavorite.setImageResource(R.drawable.ic_favorite) else btnMarkFavorite.setImageResource(R.drawable.ic_no_favorite)
                with(layoutInfo) {
                    tvMovieTitle.text = movieUpcoming.title
                    tvInfoYear.text = movieUpcoming.release_date?.substring(0, 4)
                    tvOriginalLanguage.text = movieUpcoming.original_language
                    tvInfoRating.text = movieUpcoming.vote_average?.toString()
                    tvInfoOverview.text = movieUpcoming.overview
                }
            }

            if (moviePopular != null) {
                this@DetailsFragment.moviePopular = moviePopular
                if (moviePopular.favorite) btnMarkFavorite.setImageResource(R.drawable.ic_favorite) else btnMarkFavorite.setImageResource(R.drawable.ic_no_favorite)
                with(layoutInfo) {
                    tvMovieTitle.text = moviePopular.title
                    tvInfoYear.text = moviePopular.release_date.substring(0, 4)
                    tvOriginalLanguage.text = moviePopular.original_language
                    tvInfoRating.text = moviePopular.vote_average.toString()
                    tvInfoOverview.text = moviePopular.overview
                }
            }

        }

        executeGetGenders(movieTopRated, movieUpcoming)

    }


    private fun executeGetGenders(
        movieTopRated: MovieTopRated? = null,
        movieUpcoming: MovieUpcoming? = null,
        moviesPopular: MoviesPopular? = null
    ) {

        lifecycleScope.launch(Dispatchers.IO) {

            try {

                if (movieTopRated != null) {
                    viewModel.getAllGenresMovies(movieTopRated.genre_ids)
                }

                if (movieUpcoming != null) {
                    viewModel.getAllGenresMovies(movieUpcoming.genre_ids ?: listOf())
                }

                if (moviesPopular != null) {
                    viewModel.getAllGenresMovies(moviesPopular.genre_ids)
                }

                viewModel.genresMovies.collectLatest { genre ->
                    val nameGenres: ArrayList<String> = arrayListOf()
                    genre?.forEach { item ->
                        nameGenres.add(item.name)
                    }

                    genresAdapter.submitList(nameGenres)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

    }

}