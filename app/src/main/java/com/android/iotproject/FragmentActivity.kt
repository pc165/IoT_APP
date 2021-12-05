package com.android.iotproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.iotproject.adapter.DiscoveredBluetoothDevice
import com.android.iotproject.databinding.ActivityFragmentBinding
import com.android.iotproject.utils.DeviceSingleton
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator


class FragmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFragmentBinding
    private val adapter by lazy { ViewPagerAdapter(this) }
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

        binding.pager.adapter = adapter
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
                when (position + 1) {
                    1 -> {
                        tab.text = R.string.product.toString()
                    }
                    2 -> {
                        tab.text = R.string.basket.toString()
                        val badge: BadgeDrawable = tab.orCreateBadge
                        badge.backgroundColor =
                            ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                        badge.number = 10
                        badge.isVisible = true
                    }
                }
            }
        tabLayoutMediator.attach()
    }

    companion object {
        const val EXTRA_DEVICE = "com.android.iotproject.EXTRA_DEVICE"
    }
}

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ProductFragment()
            }
            1 -> {
                BasketFragment()
            }
            else -> ProductFragment()
        }
    }
}