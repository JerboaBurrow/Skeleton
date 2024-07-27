package app.jerboa.skeleton.composable

import app.jerboa.skeleton.AppInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.jerboa.skeleton.viewmodel.SOCIAL
import app.jerboa.skeleton.viewmodel.Event
import app.jerboa.skeleton.viewmodel.RequestingSocial
import app.jerboa.skeleton.R

@Composable
fun socials(
    images: Map<String,Int>,
    info: AppInfo,
    onEvent: (Event) -> Unit
){
    val width75Percent = info.widthDp*0.75
    val height25Percent = info.heightDp*0.25
    val height20Percent = info.heightDp*0.2
    val menuItemHeight = height20Percent*0.75

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            Modifier
                .width(width75Percent.dp)
                .height(height25Percent.dp)
                .background(
                    color = Color(0.0f,0.0f,0.0f,0.0f),
                    shape = RoundedCornerShape(5)
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.height((0.25 * menuItemHeight).dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onEvent(RequestingSocial(SOCIAL.PLAY)) }) {
                        Image(
                            painter = painterResource(id = images["play-logo"]!!),
                            contentDescription = "Play Logo, click to browse to the Google Play store page for JellyCram",
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                    IconButton(onClick = { onEvent(RequestingSocial(SOCIAL.YOUTUBE)) }) {
                        Image(
                            painter = painterResource(id = images["yt"]!!),
                            contentDescription = "Youtube logo, click to browse to Jerboa.app's youtube channel",
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                    IconButton(onClick = { onEvent(RequestingSocial(SOCIAL.WEB)) }) {
                        Image(
                            painter = painterResource(id = images["logo"]!!),
                            contentDescription = "Jerboa.app's logo click to browse to our website",
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                    IconButton(onClick = { onEvent(RequestingSocial(SOCIAL.GITHUB)) }) {
                        Image(
                            painter = painterResource(id = images["github"]!!),
                            contentDescription = "Github logo, click to browse to this games source code on Github",
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(id = R.string.rate),
                    color = MaterialTheme.colors.primary,
                    fontSize = MaterialTheme.typography.body1.fontSize*info.density,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}