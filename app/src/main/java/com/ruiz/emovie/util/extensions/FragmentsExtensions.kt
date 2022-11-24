package com.ruiz.emovie.util.extensions

import androidx.fragment.app.Fragment
import com.ruiz.emovie.domain.model.UploadImagePost
import com.ruiz.emovie.presentation.common.DialogBasic
import com.ruiz.emovie.presentation.common.DialogUploadImage
import com.ruiz.emovie.util.constants.ConstantsDialogs.DIALOG_BASIC
import com.ruiz.emovie.util.constants.ConstantsDialogs.DIALOG_UPLOAD_IMAGE
import com.ruiz.emovie.util.enums.DialogAnim

fun Fragment.showBasicDialog(
    title: String,
    message: String,
    textButton: String,
    anim: DialogAnim,
    onAction: () -> Unit
) {

    DialogBasic.newInstance(
        title,
        message,
        textButton,
        anim,
        false,
        ""
    ).apply {
        this.setOnAction(onAction)
    }.show(childFragmentManager, DIALOG_BASIC)

}

fun Fragment.showBasicDialogWithCancelOption(
    title: String,
    message: String,
    textButton: String,
    textCancelButton: String,
    anim: DialogAnim,
    onAction: () -> Unit,
    onCancel: () -> Unit
) {

    DialogBasic.newInstance(
        title,
        message,
        textButton,
        anim,
        true,
        textCancelButton
    ).apply {
        this.setOnAction(onAction)
        this.setOnCancel(onCancel)
    }.show(childFragmentManager, DIALOG_BASIC)

}

fun Fragment.showUploadImageDialog(
    title: String,
    textButton: String,
    sessionId: String,
    callback: (UploadImagePost) -> Unit
){
    DialogUploadImage.newInstance(
        title,
        textButton,
        sessionId
    ).apply {
        setCallback(callback)
    }.show(childFragmentManager, DIALOG_UPLOAD_IMAGE)
}