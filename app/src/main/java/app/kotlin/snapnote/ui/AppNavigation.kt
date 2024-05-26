package app.kotlin.snapnote.ui

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.kotlin.snapnote.ui.views.CreateNoteScreen
import app.kotlin.snapnote.Destination
import app.kotlin.snapnote.ui.views.MainScreen
import app.kotlin.snapnote.ui.views.PermissionRequestScreen
import app.kotlin.snapnote.ui.views.SearchingScreen
import app.kotlin.snapnote.ui.views.WelcomeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

private const val USER_PREFERENCE_NAME = "user_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCE_NAME
)

object PreferencesKeys {
    val IS_DARK_MODE = booleanPreferencesKey(name = "is_dark_mode")
    val IS_NEW_INSTALL = booleanPreferencesKey(name = "is_new_install")
}

fun detectIsNewInstallAndIsDarkMode(context: Context): Flow<List<Boolean>> {
    return context
        .dataStore
        .data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->
            listOf(
                preference[PreferencesKeys.IS_NEW_INSTALL] ?: true,
                preference[PreferencesKeys.IS_DARK_MODE] ?: false
            )
        }
}

suspend fun saveUiMode(
    context: Context,
    isDarkMode: Boolean
) {
    context.dataStore.edit { preference ->
        preference[PreferencesKeys.IS_DARK_MODE] = isDarkMode
    }
}

suspend fun saveFirstUse(
    context: Context,
    isNewInstall: Boolean
) {
    context.dataStore.edit { preference ->
        preference[PreferencesKeys.IS_NEW_INSTALL] = isNewInstall
    }
}

@Composable
fun AppNavigation(context: Context) {
    val coroutine: CoroutineScope = rememberCoroutineScope()

    val navController: NavHostController = rememberNavController()

    val preferences: State<List<Boolean>> = detectIsNewInstallAndIsDarkMode(context = context)
        .collectAsState(initial = listOf(true, false))


    val switchAndSaveUiModeLambda: (Context, Boolean) -> Unit = { _, _ ->
        coroutine.launch {
            withContext(Dispatchers.IO) {
                saveUiMode(context = context, isDarkMode = !preferences.value[1])
            }
        }
    }

    val saveFirstUseLambda: (Context, Boolean) -> Unit = { _, _ ->
        coroutine.launch {
            withContext(Dispatchers.IO) {
                saveFirstUse(context = context, isNewInstall = false)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (preferences.value[0])
            Destination.WelcomeScreen.route
        else
            Destination.MainScreen.route
    ) {
        composable(
            route = Destination.WelcomeScreen.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { -it }
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { -it }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { it }
                )
            }
        ) {
            WelcomeScreen(
                navController = navController,
                isDarkMode = preferences.value[1]
            )
        }
        composable(
            route = Destination.PermissionRequestScreen.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { -it }
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { -it }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { it }
                )
            }
        ) {
            PermissionRequestScreen(
                navController = navController,
                isDarkMode = preferences.value[1],
                saveFirstUse = { saveFirstUseLambda(context, false) }
            )
        }
        composable(
            route = Destination.MainScreen.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { -it }
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { -it }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { it }
                )
            }
        ) {
            MainScreen(
                navController = navController,
                isDarkMode = preferences.value[1],
                saveAndSwitchUiMode = { switchAndSaveUiModeLambda(context, false) }
            )
        }
        composable(
            route = Destination.SearchingScreen.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { -it }
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { -it }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { it }
                )
            }
        ) {
            SearchingScreen(
                navController = navController,
                isDarkMode = preferences.value[1]
            )
        }
        composable(
            route = Destination.CreateNoteScreen.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { -it }
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetX = { -it }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetX = { it }
                )
            }
        ) {
            CreateNoteScreen(
                navController = navController,
                isDarkMode = preferences.value[1]
            )
        }
    }
}