package app.kotlin.snapnote.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface AppContainer {
    val snapnoteRepository: SnapnoteRepository
}

class DefaultAppContainer(
    private val context: Context,
) : AppContainer {
    override val snapnoteRepository: SnapnoteRepository by lazy {
        LocalSnapnoteRepository(
            noteDao = AppDatabase.getDatabase(context = context).noteDao()
        )
    }
}