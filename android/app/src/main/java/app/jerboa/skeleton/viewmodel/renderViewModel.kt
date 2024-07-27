package app.jerboa.skeleton.viewmodel

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*

class RenderViewModel : ViewModel() {

    private val _displayingAbout = MutableLiveData(false)
    val displayingAbout: MutableLiveData<Boolean> = _displayingAbout

    private val _requestingSocial = MutableLiveData(SOCIAL.NOTHING)
    val requestingSocial: MutableLiveData<SOCIAL> = _requestingSocial

    private val _requestingLicenses = MutableLiveData(false)
    val requestingLicenses: MutableLiveData<Boolean> = _requestingLicenses

    private val _settings = MutableLiveData(Settings(firstLaunch = false))
    val settings: MutableLiveData<Settings> = _settings

    private val _paused = MutableLiveData(false)
    val paused: MutableLiveData<Boolean> = _paused

    private val _playLogin = MutableLiveData(false)
    val playLogin: MutableLiveData<Boolean> = _playLogin

    fun onEvent(e: Event)
    {
        Log.d("renderViewModel", "$e")
        when (e)
        {
            is DisplayingAboutChanged -> onDisplayingAboutChanged(e.newValue)
            is RequestingSocial -> onRequestingSocial(e.request)
            is RequestingLicenses -> onRequestingLicenses()
            is SettingsChanged -> onSettingsChanged(e.newSettings)
            is PlayLogin -> onPlayLogin(e.success)
        }
    }

    private fun onDisplayingAboutChanged(newVal: Boolean){
        _displayingAbout.value = !_displayingAbout.value!!
        _paused.value = _displayingAbout.value!!
    }

    private fun onRequestingSocial(v: SOCIAL){
        _requestingSocial.value = v
    }

    private fun onRequestingLicenses() {
        requestingLicenses.value = true
    }

    private fun onSettingsChanged(s: Settings)
    {
        Log.d("renderViewModel", "$s")
        _settings.value = s
    }

    private fun onPlayLogin(success: Boolean)
    {
        _playLogin.postValue(success)
    }
}