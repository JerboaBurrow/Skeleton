package app.jerboa.skeleton.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.jerboa.skeleton.AppInfo
import app.jerboa.skeleton.viewmodel.Event
import app.jerboa.skeleton.viewmodel.RequestingLicenses
import app.jerboa.skeleton.viewmodel.Settings
import app.jerboa.skeleton.R

@Composable
fun myCheckBoxColors(): CheckboxColors {
    return CheckboxDefaults.colors(
        checkedColor = MaterialTheme.colors.primary,
        uncheckedColor = MaterialTheme.colors.primary
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun about(
    displayingAbout: Boolean,
    playSuccess: Boolean,
    settings: Settings,
    width75Percent: Double,
    images: Map<String,Int>,
    info: AppInfo,
    onEvent: (e: Event) -> Unit
){

    val notPGSAlpha = 0.1f

    AnimatedVisibility(
        visible = displayingAbout,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.size(2.dp))
            Box(
                Modifier
                    .width(width75Percent.dp)
                    .height((width75Percent * 1.1).dp)
                    .background(
                        color = MaterialTheme.colors.background,
                        shape = RoundedCornerShape(5)
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            stringResource(id = R.string.tagline),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.5f),
                            fontSize = MaterialTheme.typography.body1.fontSize * info.density
                        )
                        Text(
                            stringResource(id = R.string.description),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(2f),
                            fontSize = MaterialTheme.typography.body1.fontSize * info.density
                        )
                        TextButton(onClick = { onEvent(RequestingLicenses()) }) {
                            Text(
                                stringResource(id = R.string.OSSprompt),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.5f),
                                fontSize = MaterialTheme.typography.caption.fontSize * info.density
                            )
                        }
                        Text(
                            stringResource(R.string.attrib) + " version: " + info.versionString,
                            modifier = Modifier.weight(0.25f),
                            fontSize = MaterialTheme.typography.overline.fontSize * info.density,
                            textAlign = TextAlign.Center
                        )
                        Row(
                            modifier = Modifier.weight(1f).fillMaxWidth(0.75f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            IconButton(onClick = { }) {
                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .alpha(
                                            if (!playSuccess) {
                                                notPGSAlpha
                                            } else {
                                                1f
                                            }
                                        )
                                        .padding(horizontal = 1.dp),
                                    painter = painterResource(id = images["play-lead"]!!),
                                    contentDescription = "button for high score leaderboards"
                                )
                            }
                            IconButton(onClick = {  }) {
                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .alpha(
                                            if (!playSuccess) {
                                                notPGSAlpha
                                            } else {
                                                1f
                                            }
                                        )
                                        .padding(horizontal = 1.dp),
                                    painter = painterResource(id = images["play-ach"]!!),
                                    contentDescription = "button for achievements"
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(2.dp))
            socials(images, info) { onEvent(it) }
        }
    }
}