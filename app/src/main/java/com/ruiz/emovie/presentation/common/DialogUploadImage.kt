package com.ruiz.emovie.presentation.common

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.AlertDialogBasicBinding
import com.ruiz.emovie.databinding.AlertDialogUploadImageBinding
import com.ruiz.emovie.domain.model.UploadImagePost
import com.ruiz.emovie.util.constants.ConstantsDialogs.DIALOG_DESCRIPTION
import com.ruiz.emovie.util.constants.ConstantsDialogs.DIALOG_ICON_NAME
import com.ruiz.emovie.util.constants.ConstantsDialogs.DIALOG_SESSION_ID
import com.ruiz.emovie.util.constants.ConstantsDialogs.DIALOG_TEXT_BUTTON
import com.ruiz.emovie.util.constants.ConstantsDialogs.DIALOG_TITLE
import com.ruiz.emovie.util.constants.ConstantsFirebase.PATH_GALLERY_IMAGES
import com.ruiz.emovie.util.enums.DialogAnim
import java.io.ByteArrayOutputStream
import kotlin.math.max

class DialogUploadImage : DialogFragment() {

    private var binding: AlertDialogUploadImageBinding? = null

    private var photoSelectedUri: Uri? = null

    private var finishedCallback: (UploadImagePost) -> Unit = {}

    private var sessionId = ""

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                photoSelectedUri = result.data?.data

                binding?.let { binding ->

                    Glide.with(this)
                        .load(photoSelectedUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding.imageSelected)

                }

            }

        }

    companion object {

        fun newInstance(
            title: String,
            textButton: String,
            sessionId: String
        ): DialogUploadImage {
            return DialogUploadImage().apply {
                arguments = Bundle().apply {
                    putString(DIALOG_TITLE, title)
                    putString(DIALOG_TEXT_BUTTON, textButton)
                    putSerializable(DIALOG_SESSION_ID, sessionId)
                }
            }
        }

    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            window.setLayout(width, height)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlertDialogUploadImageBinding.inflate(inflater, container, false)
        isCancelable = false
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            it.getString(DIALOG_TITLE)?.let { title ->
                binding?.tvDialogTitle?.text = title
            }
            it.getString(DIALOG_TEXT_BUTTON)?.let { textButton ->
                binding?.dialogButtonClose?.text = textButton
            }

            it.getString(DIALOG_SESSION_ID)?.let { id ->
                sessionId = id
            }

        }

        configButtons()


    }

    private fun configButtons() {

        binding?.dialogButtonClose?.setOnClickListener {
            enableUI(false)
            uploadCompressedImage {
               finishedCallback(it)
            }
        }

        binding?.btnSearchImage?.setOnClickListener { openGallery() }

        binding?.btnClose?.setOnClickListener { dismiss() }

    }

    private fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)

    }

    private fun uploadCompressedImage(callback: (UploadImagePost) -> Unit) {

        val uploadImagePost = UploadImagePost()

        val imagesRef = Firebase.storage.reference
            .child(sessionId)
            .child(PATH_GALLERY_IMAGES)

        var imageName = ""

        imagesRef.listAll().addOnSuccessListener {

            imageName = "image${it.items.size + 1}"

            val photoRef = imagesRef.child(imageName)

            if (photoSelectedUri == null) {
                callback(uploadImagePost)
            } else {
                binding?.let { binding ->

                    getBitmapFromUri(photoSelectedUri!!)?.let {bitmap ->

                        binding.progressBarUploadImage.visibility = View.VISIBLE

                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)

                        photoRef.putBytes(baos.toByteArray())
                            .addOnProgressListener { task ->
                                val progress = (100 * task.bytesTransferred / task.totalByteCount).toInt()
                                task.run {
                                    binding.progressBarUploadImage.progress = progress
                                    binding.tvProgress.text = String.format("%s%%", progress)
                                }

                            }
                            .addOnSuccessListener {uploadTask ->
                                uploadTask.storage.downloadUrl.addOnSuccessListener { url ->
                                    uploadImagePost.isSuccess = true
                                    uploadImagePost.photoUrl = url.toString()
                                    callback(uploadImagePost)
                                }
                            }
                            .addOnFailureListener{
                                binding.progressBarUploadImage.visibility = View.GONE
                                binding.tvProgress.text = ""

                                uploadImagePost.isSuccess = false

                                enableUI(true)

                                callback(uploadImagePost)
                            }
                            .addOnCompleteListener {
                                binding.progressBarUploadImage.visibility = View.GONE
                                enableUI(true)
                                dismiss()
                            }

                    }

                }
            }

        }


    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {

        activity?.let { activity ->

            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                val source = ImageDecoder.createSource(activity.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)

            } else {
                MediaStore.Images.Media.getBitmap(activity.contentResolver, uri)
            }

            return getResizedImage(bitmap, 320)

        }

        return null

    }

    private fun getResizedImage(image: Bitmap, maxSize: Int): Bitmap {

        var width = image.width
        var height = image.height

        if (width <= maxSize && height <= maxSize) return image

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            width = (height / bitmapRatio).toInt()
            height = maxSize
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun enableUI(enable: Boolean){
        binding?.let { binding ->

            with(binding){

                btnClose.isEnabled = enable
                btnSearchImage.isEnabled = enable
                dialogButtonClose.isEnabled = enable

            }

        }
    }

    fun setCallback(finishedCallback: (UploadImagePost) -> Unit){
        this.finishedCallback = finishedCallback
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}