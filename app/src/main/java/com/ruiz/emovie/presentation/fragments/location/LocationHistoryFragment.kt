package com.ruiz.emovie.presentation.fragments.location

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ruiz.emovie.R
import com.ruiz.emovie.databinding.FragmentLocationHistoryBinding
import com.ruiz.emovie.domain.model.LocationEvent
import com.ruiz.emovie.presentation.adapters.LocationAdapter
import com.ruiz.emovie.presentation.common.BaseFragment
import com.ruiz.emovie.util.constants.ConstantsFirebase.COLL_LOCATION
import com.ruiz.emovie.util.constants.ConstantsFirebase.COLL_LOCATIONS
import com.ruiz.emovie.util.enums.DialogAnim
import com.ruiz.emovie.util.extensions.showBasicDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class LocationHistoryFragment :
    BaseFragment<FragmentLocationHistoryBinding>(FragmentLocationHistoryBinding::inflate) {

    private lateinit var binding: FragmentLocationHistoryBinding

    private lateinit var fireStoreListener: ListenerRegistration

    private lateinit var adapterLocation: LocationAdapter

    private val viewModel: LocationHistoryViewModel by viewModels()

    private var sessionId = ""

    override fun FragmentLocationHistoryBinding.initialize() {
        binding = this
        configRecyclerView()
        initSessionId()

    }

    override fun onResume() {
        super.onResume()
        if (sessionId.isNotEmpty()) configFireStoreRealtime()
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

    private fun configRecyclerView() {

        adapterLocation = LocationAdapter(mutableListOf()) { location ->
            val action =
                LocationHistoryFragmentDirections.actionLocationHistoryFragmentToMapsFragment(
                    latitude = location?.latitude ?: "0.0",
                    longitude = location?.longitude ?: "0.0"
                )
            findNavController().navigate(action)
        }

        binding.recyclerViewLocations.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterLocation
        }

    }

    private fun configFireStoreRealtime() {

        val db = Firebase.firestore
        val locationRef =
            db.collection(COLL_LOCATIONS).document(sessionId)
                .collection(COLL_LOCATION)

        fireStoreListener = locationRef.addSnapshotListener { value, error ->

            if (error != null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.text_error_consultar_datos),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@addSnapshotListener
            }

            for (snapshot in value!!.documentChanges) {

                val location = snapshot.document.toObject(LocationEvent::class.java)
                location.id = snapshot.document.id

                when (snapshot.type) {

                    DocumentChange.Type.ADDED -> adapterLocation.add(location)
                    DocumentChange.Type.MODIFIED -> adapterLocation.update(location)
                    DocumentChange.Type.REMOVED -> adapterLocation.delete(location)

                }

            }

        }


    }

}