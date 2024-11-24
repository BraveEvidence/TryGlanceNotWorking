package com.codingwithnobody.myandroidappglanceworker

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider


class MyWidget : GlanceAppWidget() {

    companion object {
        val KEY_TOPIC = stringPreferencesKey("topic")
        val KEY_QUOTE = stringPreferencesKey("quote")
    }

    override val sizeMode = SizeMode.Exact

   // override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {

            val displayText = currentState(KEY_QUOTE) ?: "Quote not found"
            val topic = currentState(KEY_TOPIC) ?: ""

            GlanceTheme {
                Scaffold(
                    titleBar = {
                        TitleBar(startIcon = ImageProvider(R.mipmap.ic_launcher), title = "Hello")
                    }, backgroundColor = GlanceTheme.colors.widgetBackground
                ) {
                    Column(
                        modifier = GlanceModifier.background(color = Color.Red)
                            .padding(30.dp)
                    ) {
                        Text(
                            text = "Display Text is $displayText && Topic is $topic",
                            style = TextStyle(
                                color = ColorProvider(
                                    color = Color(0xFF000000)
                                ),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = GlanceModifier.clickable {
                                actionStartActivity(activity = MainActivity::class.java)
                            }
                        )

                        Button(
                            "Start Activity", onClick = actionStartActivity<MainActivity>(),
                            style = TextStyle(
                                color = ColorProvider(
                                    color = Color(0x00FF0000)
                                ),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                }


            }
        }
    }


    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        super.onCompositionError(context, glanceId, appWidgetId, throwable)
        val remoteView = RemoteViews(context.packageName, R.layout.custom_error_layout)
        Log.i("errorMessageis", "${throwable.message} ${throwable.localizedMessage}")
        remoteView.setTextViewText(
            R.id.textview,
            "${throwable.message} ${throwable.localizedMessage}"
        )
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteView)
    }


}