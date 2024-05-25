package app.kotlin.snapnote

import android.app.Application
import app.kotlin.snapnote.data.AppContainer
import app.kotlin.snapnote.data.DefaultAppContainer


class SnapnoteApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(
            context = applicationContext,
        )
    }
}