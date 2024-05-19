package app.kotlin.snapnote.ui.views

enum class Destination(val route: String) {
    WelcomeScreen(route = "WelcomeScreen"),
    PermissionRequestScreen(route = "PermissionRequestScreen"),
    MainScreen(route = "MainScreen"),
    CreateNoteScreen(route = "CreateNoteScreen"),
    SearchingScreen(route = "SearchingScreen")
}

const val MAX_TITLE_LENGTH = 100
const val MAX_BODY_LENGTH = 1000
const val MAX_FOLDER_NAME_LENGTH = 50