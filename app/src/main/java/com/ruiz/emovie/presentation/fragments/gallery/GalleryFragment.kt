package com.ruiz.emovie.presentation.fragments.gallery

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.FragmentGalleryBinding
import com.ruiz.emovie.presentation.adapters.GalleryAdapter
import com.ruiz.emovie.presentation.common.BaseFragment
import com.ruiz.emovie.util.constants.ConstantsFirebase.PATH_GALLERY_IMAGES
import com.ruiz.emovie.util.enums.DialogAnim
import com.ruiz.emovie.util.extensions.showBasicDialog
import com.ruiz.emovie.util.extensions.showBasicDialogWithCancelOption
import com.ruiz.emovie.util.extensions.showUploadImageDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class GalleryFragment : BaseFragment<FragmentGalleryBinding>(FragmentGalleryBinding::inflate) {

    private lateinit var binding: FragmentGalleryBinding

    private lateinit var galleryAdapter: GalleryAdapter

    private val viewModel: GalleryViewModel by viewModels()

    private var sessionId = ""

    override fun FragmentGalleryBinding.initialize() {
        binding = this

        initSessionId()
        initRecyclerView()

        binding.btnAddImage.setOnClickListener {

            showUploadImageDialog(
                getString(R.string.text_upload_image),
                getString(R.string.upload),
                sessionId
            ) { result ->
                if (result.isSuccess) {
                    galleryAdapter.add(result.photoUrl)
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        if (sessionId.isNotEmpty()) getImages()
    }

    private fun initSessionId() {
        lifecycleScope.launch {
            viewModel.sessionId.collectLatest {
                sessionId = it
                delay(500)
                if (sessionId.isEmpty()) {
                    showBasicDialog(
                        title = getString(R.string.error),
                        message = getString(R.string.text_error_no_session),
                        textButton = getString(R.string.text_de_acuerdo),
                        anim = DialogAnim.ERROR
                    ) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }


    private fun initRecyclerView() {

        binding.recyclerViewGallery.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            galleryAdapter = GalleryAdapter(mutableListOf()) { url ->
                showBasicDialogWithCancelOption(
                    title = context.getString(R.string.text_title_delete_picture),
                    message = context.getString(R.string.text_message_delete_picture),
                    textButton = context.getString(R.string.text_delete),
                    textCancelButton = getString(R.string.cerrar),
                    anim = DialogAnim.INFORMATIVE,
                    onAction = {
                        deleteImage(url)
                    },
                    onCancel = {}
                )
            }
            adapter = galleryAdapter
        }

    }

    private fun deleteImage(url: String) {

        try {
            val photoRef = Firebase.storage.getReferenceFromUrl(url)

            photoRef
                .delete()
                .addOnSuccessListener {
                    galleryAdapter.delete(url)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.text_message_success_delete_picture),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), getString(R.string.text_message_error_delete_image), Toast.LENGTH_SHORT).show()
                }

        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun getImages() {

        Firebase.storage.reference
            .child(sessionId)
            .child(PATH_GALLERY_IMAGES)
            .listAll()
            .addOnSuccessListener {
                for (item in it.items) {
                    item.downloadUrl.addOnSuccessListener { uri ->
                        galleryAdapter.add(uri.toString())
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }


}