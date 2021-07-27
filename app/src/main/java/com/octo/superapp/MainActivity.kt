package com.octo.superapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus.*
import com.octo.superapp.databinding.ActivityMainLayoutBinding
import com.octo.superlibrary.SuperLibraryActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.simpleName

        private const val MODULE_ONDEMAND_NAME = "ondemand"
        private const val ACTIVITY_ONDEMAND_NAME = "com.octo.ondemandfeature.OnDemandActivity"

        private const val FLIPPER_BUTTON = 0
        private const val FLIPPER_PROGRESS_BAR = 1
    }

    private lateinit var binding: ActivityMainLayoutBinding
    private lateinit var splitInstallManager: SplitInstallManager
    private var mySessionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splitInstallManager = SplitInstallManagerFactory.create(this)

        binding.mainButton.setOnClickListener {
            if (splitInstallManager.installedModules.contains(MODULE_ONDEMAND_NAME).not()) {
                Log.d(TAG, "$MODULE_ONDEMAND_NAME feature module not yet installed")
                loadFeature()
            } else {
                Log.d(TAG, "$MODULE_ONDEMAND_NAME already installed")
                makeText(this, "$MODULE_ONDEMAND_NAME already installed", LENGTH_SHORT).show()
            }
        }

        binding.secondButton.setOnClickListener {
            try {
                startActivity(
                    Intent(this, SuperLibraryActivity::class.java)
                )
            } catch (e: ActivityNotFoundException) {
                Log.e(TAG, "Cannot start SuperLibraryActivity Activity")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        splitInstallManager.unregisterListener(splitInstallListener)
    }

    private fun loadFeature() {
        val request = SplitInstallRequest
            .newBuilder()
            .addModule(MODULE_ONDEMAND_NAME)
            .build()

        splitInstallManager.registerListener(splitInstallListener)

        splitInstallManager
            .startInstall(request)
            .addOnSuccessListener { sessionId ->
                mySessionId = sessionId
                Log.d(TAG, "Feature request successful (${mySessionId})")
                binding.flipper.displayedChild = FLIPPER_PROGRESS_BAR
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Feature request failed (${exception})")
                binding.flipper.displayedChild = FLIPPER_BUTTON
            }
    }

    private val splitInstallListener = SplitInstallStateUpdatedListener { state ->
        if (state.sessionId() == mySessionId) {
            when (state.status()) {
                DOWNLOADING -> {
                    Log.d(TAG, "Feature installation downloading ...")
                }
                CANCELED -> {
                    Log.d(TAG, "Feature installation canceled")
                    binding.flipper.displayedChild = FLIPPER_BUTTON
                }
                INSTALLED -> {
                    Log.d(TAG, "Feature installation successful")
                    if (splitInstallManager.installedModules.contains(MODULE_ONDEMAND_NAME)) {
                        Log.d(TAG, "$MODULE_ONDEMAND_NAME feature module is present")
                        binding.flipper.displayedChild = FLIPPER_BUTTON

                        val intent = Intent().setClassName(packageName, ACTIVITY_ONDEMAND_NAME)

                        try {
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Log.e(TAG, "Cannot start Activity (${intent})")
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "Install status is : ${state.status()}")
                }
            }
        }
    }
}