package app.jerboa.skeleton.viewmodel

abstract class Event {}

class DisplayingAboutChanged (val newValue: Boolean) : Event()

enum class SOCIAL {NOTHING, WEB, PLAY, YOUTUBE, GITHUB}
class RequestingSocial(val request: SOCIAL) : Event()

class RequestingLicenses() : Event()

data class Settings(val firstLaunch: Boolean)

class SettingsChanged(val newSettings: Settings) : Event()

class PlayLogin(val success: Boolean) : Event()