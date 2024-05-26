package app.kotlin.snapnote

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

const val NOTIFICATION_CHANNEL_NAME = "Snapnote notification"
const val NOTIFICATION_CHANNEL_DESCRIPTION = "Show notification when reminder is set and alarm"
const val CHANNEL_ID = "SNAPNOTE_NOTIFICATION"