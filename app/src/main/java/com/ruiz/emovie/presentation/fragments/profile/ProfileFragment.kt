package com.ruiz.emovie.presentation.fragments.profile

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.FragmentProfileBinding
import com.ruiz.emovie.domain.model.RequestApiSessionId
import com.ruiz.emovie.presentation.adapters.LoaderAdapter
import com.ruiz.emovie.presentation.adapters.MyFavoriteMoviesAdapter
import com.ruiz.emovie.presentation.common.BaseFragment
import com.ruiz.emovie.presentation.common.CustomLoader
import com.ruiz.emovie.util.constants.Constants
import com.ruiz.emovie.util.enums.DialogAnim
import com.ruiz.emovie.util.extensions.showBasicDialog
import com.ruiz.emovie.util.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalPagingApi
@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    private val customLoader: CustomLoader by lazy { CustomLoader(requireContext()) }

    private val safeArgs: ProfileFragmentArgs by navArgs()

    lateinit var adapterFavorite: MyFavoriteMoviesAdapter

    private val TAG = "ProfileFragment"

    private var sessionId = ""


    override fun FragmentProfileBinding.initialize() {

        binding = this

        initRecyclerViewFavoritesMovies()

        observerState()
        deepLinkAuthorized()
        checkDataStoreTokenInfo()

    }

    private fun initRecyclerViewFavoritesMovies() {

        binding.recyclerViewMyFavorites.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapterFavorite = MyFavoriteMoviesAdapter { movie ->
            }

            adapter = adapterFavorite.withLoadStateHeaderAndFooter(
                footer = LoaderAdapter(),
                header = LoaderAdapter()
            )

            handleMyFavoritesMoviesResult()
        }

    }

    private fun handleMyFavoritesMoviesResult() {

        lifecycleScope.launch {

            adapterFavorite.loadStateFlow.collectLatest { state ->

                val error = when {
                    state.refresh is LoadState.Error -> state.refresh as LoadState.Error
                    state.prepend is LoadState.Error -> state.prepend as LoadState.Error
                    state.append is LoadState.Error -> state.append as LoadState.Error
                    else -> null
                }

                when {

                    state.refresh is LoadState.Loading -> {
                        binding.loaderItemMyFavoritesMovies.root.visibility = View.VISIBLE
                        binding.noDataItemMyFavoritesMovies.root.visibility = View.GONE
                    }

                    error != null -> {
                        binding.loaderItemMyFavoritesMovies.root.visibility = View.GONE
                        binding.noDataItemMyFavoritesMovies.root.visibility = View.VISIBLE
                        binding.noDataItemMyFavoritesMovies.tvNoData.text =
                            error.error.localizedMessage
                    }

                    adapterFavorite.itemCount < 1 -> {
                        binding.loaderItemMyFavoritesMovies.root.visibility = View.GONE
                        binding.noDataItemMyFavoritesMovies.root.visibility = View.VISIBLE
                        binding.noDataItemMyFavoritesMovies.tvNoData.text = getString(R.string.no_info)
                    }

                    else -> {
                        binding.noDataItemMyFavoritesMovies.root.visibility = View.GONE
                        binding.loaderItemMyFavoritesMovies.root.visibility = View.GONE
                    }

                }

            }

        }
    }


    private fun deepLinkAuthorized() {
        safeArgs.requestToken?.let { requestToken ->

            viewModel.readSessionId()
            lifecycleScope.launch {
                viewModel.sessionId.collectLatest {
                    delay(500)
                    if (it.isEmpty()) {
                        viewModel.getSessionId(
                            RequestApiSessionId(
                                request_token = requestToken
                            )
                        )
                    } else {
                        sessionId = it
                        viewModel.getAccountData(it)
                    }
                }
            }

        }
    }

    private fun checkDataStoreTokenInfo() {
        lifecycleScope.launch {
            viewModel.requestToken.collectLatest { token ->
                delay(500)
                if (token.isEmpty()) {

                    Log.i(TAG, "checkDataStoreTokenInfo: Token Empty")
                    viewModel.getTokenRequest()

                } else {

                    viewModel.expireAt.collectLatest { expireAt ->

                        delay(500)
                        val simpleDateFormat =
                            SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault())
                        val date = simpleDateFormat.parse(expireAt)?.time

                        date?.let { date ->

                            viewModel.readSessionId()
                            delay(500)
                            viewModel.sessionId.collectLatest { sessionId ->

                                delay(500)
                                Log.i(
                                    TAG,
                                    "checkDataStoreTokenInfo: Fecha mal calculada DATE: $date SYSTEM: ${System.currentTimeMillis()} ID: $sessionId"
                                )
                                if (date < System.currentTimeMillis() && sessionId.isEmpty()) {
                                    viewModel.getTokenRequest()
                                } else {
                                    if (sessionId.isEmpty()) {
                                        viewModel.getSessionId(
                                            RequestApiSessionId(
                                                request_token = token
                                            )
                                        )
                                    } else {
                                        this@ProfileFragment.sessionId = sessionId
                                        viewModel.getAccountData(sessionId)
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }
    }

    private fun observerState() {

        viewModel.requestTokenState.observe(viewLifecycleOwner) { state ->

            when (state) {

                is UIState.Success -> {

                    viewModel.saveRequestToken(state.data.request_token ?: "")
                    viewModel.saveExpireAt(state.data.expires_at ?: "")

                    gotoUrl("${Constants.BASE_URL_AUTHENTICATE}${state.data.request_token}")

                }

                is UIState.Error -> {

                    showBasicDialog(
                        title = getString(R.string.error),
                        message = state.error,
                        textButton = getString(R.string.cerrar),
                        anim = DialogAnim.ERROR
                    ) {}

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

        viewModel.requestSessionIdState.observe(viewLifecycleOwner) { state ->

            when (state) {

                is UIState.Success -> {

                    viewModel.saveSessionId(state.data.session_id ?: "")
                    viewModel.readSessionId()

                    sessionId = state.data.session_id ?: ""
                    viewModel.getAccountData(state.data.session_id ?: "")

                }

                is UIState.Error -> {

                    showBasicDialog(
                        title = getString(R.string.error),
                        message = state.error,
                        textButton = getString(R.string.cerrar),
                        anim = DialogAnim.ERROR
                    ) {}

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

        viewModel.accountDatadState.observe(viewLifecycleOwner) { state ->

            when (state) {

                is UIState.Success -> {

                    val dataUser = state.data

                    with(binding) {

                        viewModel.saveAccountId(dataUser.id.toString())

                        tvUser.text = getString(R.string.usuario, dataUser.username)
                        tvName.text = getString(R.string.nombre, dataUser.name)

                        dataUser.avatar?.let { avatarData ->
                            if (avatarData.tmdb != null) {

                                Glide.with(binding.root)
                                    .load("${Constants.BASE_URL_IMAGES}${avatarData.tmdb.avatar_path}")
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .error(R.drawable.ic_image)
                                    .into(binding.imageAvatar)

                            }
                        }

                        lifecycleScope.launch {
                            viewModel.getFavoritesMovies(dataUser.id.toString(), sessionId)
                                .collectLatest {
                                    adapterFavorite.submitData(it)
                                }
                        }

                    }

                }

                is UIState.Error -> {

                    showBasicDialog(
                        title = getString(R.string.error),
                        message = state.error,
                        textButton = getString(R.string.cerrar),
                        anim = DialogAnim.ERROR
                    ) {}

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

    private fun gotoUrl(url: String) {

        val uri = Uri.parse(url)
        startActivity(Intent(Intent.ACTION_VIEW, uri))

    }

}