package com.ruiz.emovie.presentation.fragments.video_player

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.FragmentVideoPlayerBinding
import com.ruiz.emovie.presentation.common.BaseFragment
import com.ruiz.emovie.presentation.common.CustomLoader
import com.ruiz.emovie.util.constants.Constants.VIDEO_SITE
import com.ruiz.emovie.util.constants.Constants.VIDEO_TYPE
import com.ruiz.emovie.util.enums.DialogAnim
import com.ruiz.emovie.util.extensions.showBasicDialog
import com.ruiz.emovie.util.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagingApi
@AndroidEntryPoint
class VideoPlayerFragment :
    BaseFragment<FragmentVideoPlayerBinding>(FragmentVideoPlayerBinding::inflate) {

    private lateinit var binding: FragmentVideoPlayerBinding

    private val safeArgs: VideoPlayerFragmentArgs by navArgs()

    private val viewModel: VideoPlayerViewModel by viewModels()

    private val customLoader: CustomLoader by lazy { CustomLoader(requireContext()) }

    override fun FragmentVideoPlayerBinding.initialize() {
        binding = this
        viewLifecycleOwner.lifecycle.addObserver(binding.youtubePlayer)
        observerState()
        viewModel.getMovieVideos(safeArgs.movieId)
    }

    private fun observerState() {

        viewModel.videosMovieState.observe(viewLifecycleOwner) { state ->

            when (state) {

                is UIState.Success -> {

                    val list = state.data.results

                    if (list.isNotEmpty()) {

                        list.find { item -> item.official && item.site == VIDEO_SITE && item.type == VIDEO_TYPE }
                            ?.let {
                                playVideo(it.key)
                            } ?: run {
                            basicDialog(
                                title = getString(R.string.atencion),
                                message = getString(R.string.movie_without_trailer),
                                textButton = getString(R.string.aceptar),
                                anim = DialogAnim.INFORMATIVE
                            )
                        }


                    } else {
                        basicDialog(
                            title = getString(R.string.error),
                            message = getString(R.string.movie_no_has_videos),
                            textButton = getString(R.string.cerrar),
                            anim = DialogAnim.ERROR
                        )
                    }


                }

                is UIState.Error -> {
                    basicDialog(
                        title = getString(R.string.error),
                        message = state.error,
                        textButton = getString(R.string.cerrar),
                        anim = DialogAnim.ERROR
                    )
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

    private fun playVideo(keyVideo: String) {
        binding.youtubePlayer.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadOrCueVideo(
                    lifecycle,
                    keyVideo,
                    0.0f
                )
            }

        })
    }

    private fun basicDialog(title: String, message: String, textButton: String, anim: DialogAnim) {
        showBasicDialog(
            title,
            message,
            textButton,
            anim
        ) {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.youtubePlayer.release()
    }

}