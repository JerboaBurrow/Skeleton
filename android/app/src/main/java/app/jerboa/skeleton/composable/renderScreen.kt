package app.jerboa.skeleton.composable

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import app.jerboa.skeleton.AppInfo
import app.jerboa.skeleton.viewmodel.RenderViewModel
import app.jerboa.skeleton.viewmodel.Settings
import app.jerboa.skeleton.ui.theme.JerboaTheme
import app.jerboa.skeleton.ui.view.GLView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun renderScreen(
    renderViewModel: RenderViewModel,
    resolution: Pair<Int,Int>,
    images: Map<String,Map<String, Int>>,
    info: AppInfo
){
    val displayingAbout: Boolean by renderViewModel.displayingAbout.observeAsState(initial = false)
    val settings: Settings by renderViewModel.settings.observeAsState(renderViewModel.settings.value!!)
    val playSuccess: Boolean by renderViewModel.playLogin.observeAsState(initial = false)

    val scaffoldState = rememberScaffoldState()

    val width75Percent = info.widthDp*0.75
    val height10Percent = info.heightDp*0.1
    val menuItemHeight = height10Percent*0.66

    val themeImages: Map<String, Int> = images["default"]!!

    JerboaTheme(
        darkTheme = false
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                },
                bottomBar = {
                }
            ) {
                AndroidView(
                    factory = {
                        GLView(
                            it, null,
                            settings,
                            resolution
                        ) { v -> renderViewModel.onEvent(v) }
                    }
                ) { view ->
                    run {
                        view.settings(settings)
                    }
                }

                about(
                    displayingAbout,
                    playSuccess,
                    settings,
                    width75Percent,
                    themeImages,
                    info
                ) { v -> renderViewModel.onEvent(v) }

                menuPrompt(
                    themeImages,
                    displayingAbout,
                    settings,
                    menuItemHeight
                ) { renderViewModel.onEvent(it) }
            }
        }
    }
}