package com.codingwithnobody.myandroidappglanceworker

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.codingwithnobody.myandroidappglanceworker.MyWidget.Companion.KEY_QUOTE
import com.codingwithnobody.myandroidappglanceworker.MyWidget.Companion.KEY_TOPIC
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltWorker
class CustomWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {

        CoroutineScope(Dispatchers.IO).launch {
            val currentTimeMillis = System.currentTimeMillis()
            val date = Date(currentTimeMillis)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = sdf.format(date)

            val glanceIds =
                GlanceAppWidgetManager(context).getGlanceIds(MyWidget::class.java)
            glanceIds.forEach { id ->
                updateAppWidgetState(context, id) { prefs ->
                    prefs[KEY_QUOTE] = currentTimeMillis.toString()
                    prefs[KEY_TOPIC] = formattedDate
                }
                MyWidget().update(context, id)
            }
        }
        return Result.success()
    }
}