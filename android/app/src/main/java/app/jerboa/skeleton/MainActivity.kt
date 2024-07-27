package app.jerboa.skeleton

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.jerboa.skeleton.viewmodel.PlayLogin
import app.jerboa.skeleton.viewmodel.RenderViewModel
import app.jerboa.skeleton.viewmodel.SOCIAL
import app.jerboa.skeleton.viewmodel.Settings
import app.jerboa.skeleton.viewmodel.SettingsChanged
import app.jerboa.skeleton.composable.renderScreen
import app.jerboa.skeleton.onlineServices.Client
import app.jerboa.skeleton.onlineServices.InAppReview
import com.google.android.gms.games.PlayGamesSdk
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


data class AppInfo(
    val versionString: String,
    val density: Float,
    val heightDp: Float,
    val widthDp: Float
)

class MainActivity : AppCompatActivity() {

    private val renderViewModel by viewModels<RenderViewModel>()

    private lateinit var client: Client

    private val startTimeMillis: Long = System.currentTimeMillis()

    private val imageResources: Map<String,Map<String, Int>> = mapOf(
        "default" to mapOf(
            "logo" to R.drawable.logo,
            "play-ach" to R.drawable.games_achievements,
            "play-lead" to R.drawable.games_leaderboards,
            "play-logo" to R.drawable.play_,
            "yt" to R.drawable.ic_yt,
            "github" to R.drawable.github_mark,
            "burger" to R.drawable.menu,
            "dismiss" to R.drawable.menu_dimiss
        )
    )

    private fun tryStartActivity(intent: Intent, failInfoToast: String) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("tryStartActivity", failInfoToast)
            val toast = Toast.makeText(this, failInfoToast, Toast.LENGTH_SHORT) // in Activity
            toast.show()
        }
    }
    private fun playRate() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                "https://play.google.com/store/apps/details?id=app.jerboa.spp"
            )
            setPackage("com.android.vending")
        }
        tryStartActivity(intent, "Could not open Play Store")
    }

    private fun youtube() {
        val uri = Uri.parse("https://www.youtube.com/channel/UCP3KhLhmG3Z1CMWyLkn7pbQ")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        tryStartActivity(intent, "Could not open Youtube")
    }

    private fun web() {
        val uri = Uri.parse("https://jerboa.app")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        tryStartActivity(intent, "Could not open https://jerboa.app")
    }

    private fun github() {
        val uri = Uri.parse("https://github.com/JerboaBurrow/")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        tryStartActivity(intent, "Could not open Github")
    }

    private fun showLicenses() {
        val intent = Intent(this.applicationContext, OssLicensesMenuActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //on opening OSS sometimes there is a crash..
        //https://github.com/google/play-services-plugins/issues/100
        //com.google.android.gms.internal.oss_licenses.zzf.dummy_placeholder = getResources().getIdentifier("third_party_license_metadata", "raw", getPackageName());
        tryStartActivity(intent, "Could not show licenses")
    }

    private fun installPGS() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                "https://play.google.com/store/apps/details?id=com.google.android.play.games"
            )
            setPackage("com.android.vending")
        }
        tryStartActivity(intent, "Could not open Play Store")
    }

    private fun saveSettings()
    {
        val gson = Gson()
        val settings = gson.toJson(renderViewModel.settings.value)
        val prefs = getSharedPreferences(resources.getString(R.string.app_prefs), MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.putString("settings", settings)
        Log.d("saveSettings", settings)
        val playTime = System.currentTimeMillis() - startTimeMillis
        val lastPlayTime = prefs.getLong("playTime", 0L)
        prefsEdit.putLong("playTime", playTime+lastPlayTime)
        prefsEdit.apply()
    }

    private suspend fun checkPGS()
    {
        renderViewModel.onEvent(PlayLogin(client.loginSuccessful()))

        while (true)
        {
            val loggedIn = client.loginSuccessful()
            if (loggedIn != renderViewModel.playLogin.value)
            {
                renderViewModel.onEvent(PlayLogin(loggedIn))
            }
            delay(1000*3)
        }

    }

    override fun onDestroy() {
        saveSettings()
        super.onDestroy()
    }

    override fun onPause() {
        saveSettings()
        super.onPause()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // play game services
        PlayGamesSdk.initialize(this)

        // play game services
        PlayGamesSdk.initialize(this)

        val prefs = getSharedPreferences(resources.getString(R.string.app_prefs), MODE_PRIVATE)

        client = Client(resources, getSharedPreferences(resources.getString(R.string.app_prefs), MODE_PRIVATE))
        client.playGamesServicesLogin(this)
        client.sync(this)
        renderViewModel.requestingLicenses.observe(
            this, androidx.lifecycle.Observer { request -> if(request){ showLicenses() }}
        )

        renderViewModel.requestingSocial.observe(
            this, androidx.lifecycle.Observer { request ->
                when (request){
                    SOCIAL.WEB -> web()
                    SOCIAL.PLAY -> playRate()
                    SOCIAL.YOUTUBE -> youtube()
                    SOCIAL.GITHUB -> github()
                    SOCIAL.NOTHING -> {}
                }
            }
        )


//        if (BuildConfig.DEBUG){
//            prefs.edit().clear().apply()
//        }

        if (!prefs.contains("settings"))
        {
            renderViewModel.onEvent(SettingsChanged(Settings(firstLaunch = true)))
        }
        else
        {
            val gson = Gson()
            try {
                renderViewModel.onEvent(SettingsChanged(gson.fromJson(prefs.getString("settings", ""), Settings::class.java)))
            }
            catch (e: Error)
            {
                Log.d("load", "$e")
                val prefsEdit = prefs.edit()
                prefsEdit.remove("settings")
                prefsEdit.apply()
                renderViewModel.onEvent(SettingsChanged(Settings(firstLaunch = false)))
            }
        }

        InAppReview().requestUserReviewPrompt(this)

        val versionString = BuildConfig.VERSION_NAME + ": " + Date(BuildConfig.TIMESTAMP)

        val displayInfo = resources.displayMetrics
        val dpHeight = displayInfo.heightPixels / displayInfo.density
        val dpWidth = displayInfo.widthPixels / displayInfo.density
        val appInfo = AppInfo(
            versionString,
            if (resources.getBoolean(R.bool.isTablet)){displayInfo.density}else{1f},
            dpHeight,
            dpWidth
        )

        Log.d("density",""+ resources.displayMetrics.density)

        GlobalScope.launch {
            checkPGS()
        }


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        setContent {
            // A surface container using the 'background' color from the theme
            renderScreen(
                renderViewModel,
                Pair(width,height),
                imageResources,
                appInfo
            )
        }
    }
}