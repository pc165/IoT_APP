package com.android.iotproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.iotproject.adapter.DiscoveredBluetoothDevice
import com.android.iotproject.databinding.ActivityFragmentBinding
import com.android.iotproject.utils.DeviceSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView


class FragmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val device = intent.getParcelableExtra<DiscoveredBluetoothDevice>(EXTRA_DEVICE)!!
        DeviceSingleton.device = device
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val deviceName = device.name
        val deviceAddress = device.address
        val toolbar = binding.toolbar
        toolbar.subtitle = "${deviceName ?: getString(R.string.unknown_device)} $deviceAddress"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //setup navigation bottom bar
        val navView: BottomNavigationView = binding.navView
        val navController: NavController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_basket, R.id.navigation_product
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    companion object {
        const val EXTRA_DEVICE = "com.android.iotproject.EXTRA_DEVICE"
    }
}
