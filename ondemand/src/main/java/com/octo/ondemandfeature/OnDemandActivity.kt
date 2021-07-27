package com.octo.ondemandfeature

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitcompat.SplitCompat
import com.octo.ondemandfeature.databinding.ActivityOndemandLayoutBinding

class OnDemandActivity : AppCompatActivity() {

    companion object {
        private val TAG = OnDemandActivity::class.java.simpleName
        private const val ACTIVITY_OTHER_NAME = "com.octo.superapp.OtherActivity"
    }

    private lateinit var binding: ActivityOndemandLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOndemandLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val intent = Intent().setClassName(packageName, ACTIVITY_OTHER_NAME)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Log.e(TAG, "Cannot start Activity (${intent})")
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }
}