package com.android.iotproject

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.iotproject.adapter.DiscoveredBluetoothDevice
import com.android.iotproject.databinding.ActivityCameraBinding
import com.android.iotproject.viewmodels.ProductViewModel
import no.nordicsemi.android.ble.livedata.state.ConnectionState
import no.nordicsemi.android.ble.observer.ConnectionObserver

class ProductActivity : AppCompatActivity() {
    private lateinit var viewModel: ProductViewModel
    private lateinit var binding: ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val device = intent.getParcelableExtra<DiscoveredBluetoothDevice>(EXTRA_DEVICE)!!
        val deviceName = device.name
        val deviceAddress = device.address
        val toolbar = binding.toolbar
        toolbar.title = deviceName ?: getString(R.string.unknown_device)
        toolbar.subtitle = deviceAddress
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Configure the view model.
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModel.connect(device)

        // Set up views.
        binding.infoNotSupported.actionRetry.setOnClickListener { viewModel.reconnect() }
        binding.infoTimeout.actionRetry.setOnClickListener { viewModel.reconnect() }
        binding.btnGetData.setOnClickListener {}
        binding.btnGetImage.setOnClickListener { viewModel.getPicture()}

        viewModel.connectionState.observe(this, { state: ConnectionState ->
            when (state.state!!) {
                ConnectionState.State.CONNECTING -> {
                    binding.progressContainer.visibility = View.VISIBLE
                    binding.infoNotSupported.container.visibility = View.GONE
                    binding.infoTimeout.container.visibility = View.GONE
                    binding.connectionState.setText(R.string.state_connecting)
                }
                ConnectionState.State.INITIALIZING -> binding.connectionState.setText(R.string.state_initializing)
                ConnectionState.State.READY -> {
                    binding.progressContainer.visibility = View.GONE
                    binding.deviceContainer.visibility = View.VISIBLE
                }
                ConnectionState.State.DISCONNECTED -> {
                    if (state is ConnectionState.Disconnected) {
                        binding.deviceContainer.visibility = View.GONE
                        binding.progressContainer.visibility = View.GONE
                        if (state.reason == ConnectionObserver.REASON_NOT_SUPPORTED) {
                            binding.infoNotSupported.container.visibility = View.VISIBLE
                        } else {
                            binding.infoTimeout.container.visibility = View.VISIBLE
                        }
                    }
                }
                ConnectionState.State.DISCONNECTING -> {}
            }
        })

    }

    companion object {
        const val EXTRA_DEVICE = "com.android.iotproject.EXTRA_DEVICE"
    }
}
