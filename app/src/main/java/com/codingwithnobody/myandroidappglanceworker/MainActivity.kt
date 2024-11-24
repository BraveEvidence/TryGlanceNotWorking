package com.codingwithnobody.myandroidappglanceworker

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<CustomWorker>(
            repeatInterval = 1, repeatIntervalTimeUnit = TimeUnit.HOURS,
            flexTimeInterval = 15, //work will be executed between 45 mins to 1 hour
            flexTimeIntervalUnit = TimeUnit.MINUTES
        ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofSeconds(15)
            )
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        //new work will be ignored if name is same
        workManager.enqueueUniquePeriodicWork(
            "myWork", ExistingPeriodicWorkPolicy.KEEP, workRequest
        )
    }
}