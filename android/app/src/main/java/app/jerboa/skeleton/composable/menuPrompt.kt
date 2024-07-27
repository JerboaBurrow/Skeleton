package app.jerboa.skeleton.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.jerboa.skeleton.viewmodel.DisplayingAboutChanged
import app.jerboa.skeleton.viewmodel.Event
import app.jerboa.skeleton.viewmodel.Settings

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun menuPrompt(
    images: Map<String,Int>,
    displayingMenu: Boolean,
    settings: Settings,
    menuItemHeight: Double,
    onEvent: (Event) -> Unit
){

    val alphaM1: Float by animateFloatAsState(
        targetValue = if (!displayingMenu) 0.66f else 1.0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        )
    )

    val alphaM2: Float by animateFloatAsState(
        targetValue = if (displayingMenu) 0.66f else 1.0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        )
    )


    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
        ) {
        Box(
            modifier = Modifier
                .width(menuItemHeight.dp)
                .height((menuItemHeight * 2.0).dp)
                .padding((menuItemHeight * 0.1).dp)
                .align(alignment = Alignment.BottomStart)
        ) {
            AnimatedVisibility(
                visible = !displayingMenu,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Image(
                    painter = painterResource(id = images["burger"]!!),
                    contentDescription = "Menu icon, click to open the menu",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = { onEvent(DisplayingAboutChanged(true)) }
                        )
                        .alpha(alphaM1)
                )
            }
            AnimatedVisibility(
                visible = displayingMenu,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Image(
                    painter = painterResource(id = images["dismiss"]!!),
                    contentDescription = "Menu icon, click to close the menu",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = { onEvent(DisplayingAboutChanged(false)) }
                        )
                        .alpha(alphaM2)
                )
            }
        }
    }
}