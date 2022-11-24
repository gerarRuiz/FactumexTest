package com.ruiz.emovie.presentation.fragments.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.FragmentHomeBinding
import com.ruiz.emovie.domain.model.MovieTopRated
import com.ruiz.emovie.presentation.adapters.LoaderAdapter
import com.ruiz.emovie.presentation.adapters.PopularMoviesAdapter
import com.ruiz.emovie.presentation.adapters.TopRatedMoviesAdapter
import com.ruiz.emovie.presentation.adapters.UpComingMoviesAdapter
import com.ruiz.emovie.presentation.common.BaseFragment
import com.ruiz.emovie.util.constants.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    //Adapters
    lateinit var adapterTop: TopRatedMoviesAdapter
    lateinit var upcomingAdapter: UpComingMoviesAdapter
    lateinit var popularMoviesAdapter: PopularMoviesAdapter

    override fun FragmentHomeBinding.initialize() {
        binding = this
        initRecyclerViewUpComingMovies()
        initRecyclerViewTopRatedMovies()
        initRecyclerViewPopularMovies()
        configSwipe()

        callData()
    }

    private fun initRecyclerViewTopRatedMovies() {
        binding.recyclerViewTopRated.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapterTop = TopRatedMoviesAdapter { movie ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    movieId = movie?.id?.toInt() ?: 0, typeMovie = Constants.TYPE_MOVIE_TOP
                )
                findNavController().navigate(action)
            }
            adapter = adapterTop.withLoadStateHeaderAndFooter(
                footer = LoaderAdapter(),
                header = LoaderAdapter()
            )

            handleTopRatedResult()

        }
    }

    private fun initRecyclerViewUpComingMovies() {
        binding.recyclerViewUpcoming.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            upcomingAdapter = UpComingMoviesAdapter { movie ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    movieId = movie?.id?.toInt() ?: 0, typeMovie = Constants.TYPE_MOVIE_UPCOMING
                )
                findNavController().navigate(action)
            }

            adapter = upcomingAdapter.withLoadStateHeaderAndFooter(
                footer = LoaderAdapter(),
                header = LoaderAdapter()
            )

            handleUpComingResult()

        }
    }

    private fun initRecyclerViewPopularMovies() {

        binding.recyclerViewRecomendaciones.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularMoviesAdapter = PopularMoviesAdapter { item ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    movieId = item?.id?.toInt() ?: 0,
                    typeMovie = Constants.TYPE_MOVIE_POPULAR
                )
                findNavController().navigate(action)
            }

            adapter = popularMoviesAdapter.withLoadStateHeaderAndFooter(
                footer = LoaderAdapter(),
                header = LoaderAdapter()
            )

            handlePopularResult()
        }

    }

    private fun configSwipe() {
        binding.swipe.setOnRefreshListener {
            handleUpComingResult()
            callUpComingMovies()
            handleTopRatedResult()
            callTopRatedMovies()
            handlePopularResult()
            callPopularMovies()
        }
    }

    private fun handleUpComingResult() {

        lifecycleScope.launch {

            upcomingAdapter.loadStateFlow.collectLatest { state ->

                val error = when {
                    state.refresh is LoadState.Error -> state.refresh as LoadState.Error
                    state.prepend is LoadState.Error -> state.prepend as LoadState.Error
                    state.append is LoadState.Error -> state.append as LoadState.Error
                    else -> null
                }

                when {

                    state.refresh is LoadState.Loading -> {
                        binding.loaderItemUpcomingMovies.root.visibility = View.VISIBLE
                        binding.noDataItemUpcomingMovies.root.visibility = View.GONE
                    }

                    error != null -> {
                        binding.swipe.isRefreshing = false
                        binding.loaderItemUpcomingMovies.root.visibility = View.GONE
                        binding.noDataItemUpcomingMovies.root.visibility = View.VISIBLE
                        binding.noDataItemUpcomingMovies.tvNoData.text =
                            error.error.localizedMessage
                    }

                    upcomingAdapter.itemCount < 1 -> {
                        binding.swipe.isRefreshing = false
                        binding.loaderItemUpcomingMovies.root.visibility = View.GONE
                        binding.noDataItemUpcomingMovies.root.visibility = View.VISIBLE
                        binding.noDataItemUpcomingMovies.tvNoData.text = getString(R.string.no_info)
                    }

                    else -> {
                        binding.swipe.isRefreshing = false
                        binding.rootScroll.visibility = View.VISIBLE
                        binding.noDataItemUpcomingMovies.root.visibility = View.GONE
                        binding.loaderItemUpcomingMovies.root.visibility = View.GONE
                    }

                }

            }

        }
    }

    private fun handleTopRatedResult() {

        lifecycleScope.launch {

            adapterTop.loadStateFlow.collectLatest { state ->

                val error = when {
                    state.refresh is LoadState.Error -> state.refresh as LoadState.Error
                    state.prepend is LoadState.Error -> state.prepend as LoadState.Error
                    state.append is LoadState.Error -> state.append as LoadState.Error
                    else -> null
                }

                when {

                    state.refresh is LoadState.Loading -> {
                        binding.loaderItemTopratedMovies.root.visibility = View.VISIBLE
                        binding.noDataTopratedMovies.root.visibility = View.GONE
                    }

                    error != null -> {
                        binding.swipe.isRefreshing = false
                        binding.loaderItemTopratedMovies.root.visibility = View.GONE
                        binding.noDataTopratedMovies.root.visibility = View.VISIBLE
                        binding.noDataTopratedMovies.tvNoData.text =
                            error.error.localizedMessage

                    }

                    adapterTop.itemCount < 1 -> {
                        binding.swipe.isRefreshing = false
                        binding.loaderItemTopratedMovies.root.visibility = View.GONE
                        binding.noDataTopratedMovies.root.visibility = View.VISIBLE
                        binding.noDataTopratedMovies.tvNoData.text = getString(R.string.no_info)
                    }

                    else -> {
                        binding.swipe.isRefreshing = false
                        binding.rootScroll.visibility = View.VISIBLE
                        binding.noDataTopratedMovies.root.visibility = View.GONE
                        binding.loaderItemTopratedMovies.root.visibility = View.GONE
                    }

                }

            }

        }
    }

    private fun handlePopularResult() {

        lifecycleScope.launch {

            popularMoviesAdapter.loadStateFlow.collectLatest { state ->

                val error = when {
                    state.refresh is LoadState.Error -> state.refresh as LoadState.Error
                    state.prepend is LoadState.Error -> state.prepend as LoadState.Error
                    state.append is LoadState.Error -> state.append as LoadState.Error
                    else -> null
                }

                when {

                    state.refresh is LoadState.Loading -> {
                        binding.loaderPopularMovies.root.visibility = View.VISIBLE
                        binding.noDataPopularMovies.root.visibility = View.GONE
                    }

                    error != null -> {
                        binding.swipe.isRefreshing = false
                        binding.loaderPopularMovies.root.visibility = View.GONE
                        binding.noDataPopularMovies.root.visibility = View.VISIBLE
                        binding.noDataPopularMovies.tvNoData.text =
                            error.error.localizedMessage

                    }

                    adapterTop.itemCount < 1 -> {
                        binding.swipe.isRefreshing = false
                        binding.loaderPopularMovies.root.visibility = View.GONE
                        binding.noDataPopularMovies.root.visibility = View.VISIBLE
                        binding.noDataPopularMovies.tvNoData.text = getString(R.string.no_info)
                    }

                    else -> {
                        binding.swipe.isRefreshing = false
                        binding.rootScroll.visibility = View.VISIBLE
                        binding.noDataPopularMovies.root.visibility = View.GONE
                        binding.loaderPopularMovies.root.visibility = View.GONE
                    }

                }

            }

        }
    }


    private fun callData() {

        try {

            callUpComingMovies()
            callTopRatedMovies()
            callPopularMovies()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun callTopRatedMovies() {
        lifecycleScope.launch {
            viewModel.getAllTopRatedMovies.collectLatest {
                adapterTop.submitData(it)
            }
        }
    }

    private fun callUpComingMovies() {
        lifecycleScope.launch {
            viewModel.getAllUpComingMoviesUseCase.collectLatest {
                upcomingAdapter.submitData(it)
            }
        }
    }

    private fun callPopularMovies() {
        lifecycleScope.launch {
            viewModel.getAllPopularMoviesUseCase.collectLatest {
                popularMoviesAdapter.submitData(it)
            }
        }
    }

}