package com.ruiz.emovie

import android.Manifest
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.paging.ExperimentalPagingApi
import com.ruiz.emovie.databinding.ActivityMainBinding
import com.ruiz.emovie.services.LocationService
import com.ruiz.emovie.util.constants.Constants.ACTION_START_LOCATION_SERVICE
import com.ruiz.emovie.util.constants.Constants.NOTIFICATION_ID
import com.ruiz.emovie.util.constants.ConstantsDialogs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var locationService: Intent? = null

    private val viewModel: MainViewModel by viewModels()

    private var sessionId = ""

    private val TAG = "MainActivity"

    private var isCoarseLocationPermissionGranted = false
    private var isFineLocationPermissionGranted = false
    private var isNotificationPermissionGranted = false

    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            when {

                it[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {

                   isCoarseLocationPermissionGranted = true

                }

                it[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                    isFineLocationPermissionGranted = true
                }

                it[Manifest.permission.POST_NOTIFICATIONS] == true -> {
                    isNotificationPermissionGranted = true
                }

            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSessionId()
        setUpBottomNav()

        checkLocationPermissions()

    }

    private fun initSessionId() {
        lifecycleScope.launch {
            viewModel.sessionId.collectLatest {
                sessionId = it
                delay(500)
                locationService = Intent(this@MainActivity, LocationService::class.java).apply {
                    action = ACTION_START_LOCATION_SERVICE
                    putExtra(ConstantsDialogs.DIALOG_SESSION_ID, it)
                }
                checkLocationPermissions()
            }
        }
    }

    private fun setUpBottomNav() {

        val bottomNavigationBar = binding.bottomNavigationBar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.homeFragment -> showBottomNav()
                R.id.locationHistoryFragment -> showBottomNav()
                R.id.galleryFragment -> showBottomNav()
                R.id.webFragment -> showBottomNav()
                else -> hideBottomNav()

            }

        }

    }

    private fun showBottomNav() {
        binding.bottomNavigationBar.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigationBar.visibility = View.GONE
    }

    private fun checkLocationPermissions() {

        isCoarseLocationPermissionGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        isFineLocationPermissionGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            isNotificationPermissionGranted = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }

        val permissionRequest = arrayListOf<String>()

        if (!isCoarseLocationPermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (!isFineLocationPermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (!isNotificationPermissionGranted){
                permissionRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissionRequest.isNotEmpty()){
            permission.launch(permissionRequest.toTypedArray())
        }else{
            if (!foregroundServiceRunning()) {
                if (sessionId.isNotEmpty()) {
                    Log.i(TAG, "checkLocationPermissions: Service Started")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(locationService)
                    }
                }
            }
        }

    }

    private fun foregroundServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (item in activityManager.getRunningServices(NOTIFICATION_ID)) {
            if (LocationService::class.simpleName == item.javaClass.simpleName) {
                if (item.foreground) {
                    return true
                }
            }
        }
        return false
    }


}