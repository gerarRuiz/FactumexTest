package com.ruiz.emovie.presentation.common

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import androidx.viewbinding.ViewBinding
import org.greenrobot.eventbus.EventBus

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

@ExperimentalPagingApi
abstract class BaseFragment<T : ViewBinding>(private val inflate: Inflate<T>) : Fragment() {

    private var _binding: T? = null
    private val binding: T get() = _binding!!

    private var locationService: Intent? = null

    open fun T.initialize() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        binding.initialize()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Permissions Section



}