/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.rajdhani
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme(darkTheme = true) {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        Home()
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}

@Composable
fun Home() {

    val timer: Long by remember { mutableStateOf(60 * 1000) }
    var tempTimer by remember { mutableStateOf(timer) }

    var time by remember {
        mutableStateOf(timeToString(timer))
    }

    var onStop by remember { mutableStateOf(false) }
    var onPause by remember { mutableStateOf(false) }
    var onStarted by remember { mutableStateOf(false) }

    val countDownTimer = object : CountDownTimer(tempTimer, 1000) {
        override fun onTick(msToFinished: Long) {
            if (onPause) {
                this.cancel()
                tempTimer = msToFinished
            }

            time = timeToString(msToFinished)

            if (onStop)
                this.onFinish()
        }

        override fun onFinish() {
            time = timeToString(timer)
            tempTimer = timer
            onStarted = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        CountDownLeft(time)

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.End
        ) {
            Column(modifier = Modifier.rotate(90f)) {
                Text(
                    text = stringResource(R.string.txt_countdown), fontSize = 36.sp,
                    fontFamily = rajdhani,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.light_gray)
                )
                Text(
                    text = stringResource(R.string.txt_timer), fontSize = 36.sp,
                    color = colorResource(id = R.color.dark_gray),
                    fontFamily = rajdhani,
                    fontWeight = FontWeight.Medium,
                )
            }

            Column(modifier = Modifier.padding(end = 64.dp, top = 150.dp)) {
                if (!onStarted) {
                    Text(
                        text = stringResource(R.string.txt_play),
                        fontFamily = rajdhani,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.dark_gray),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable {
                                countDownTimer.start()
                                onStop = false
                                onStarted = true
                                onPause = false
                            }
                    )
                } else {
                    Text(

                        text = stringResource(R.string.txt_pause),
                        fontFamily = rajdhani,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.dark_gray),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable {
                                onStarted = false
                                onPause = true
                            }
                    )
                }

                Text(
                    text = stringResource(R.string.stop_txt),
                    color = colorResource(id = R.color.dark_gray),
                    fontSize = 20.sp,
                    modifier =
                    Modifier
                        .padding(bottom = 16.dp)
                        .clickable {
                            if (onPause)
                                countDownTimer.onFinish()
                            else {
                                onPause = true
                                onStop = true
                            }
                        }
                )
            }
        }
    }
}

@Composable
private fun CountDownLeft(time: String) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 52.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        val hourInArray = time.split(",").toTypedArray()
        val h = hourInArray[0]
        val m = hourInArray[1]
        val s = hourInArray[2]

        TextTimer(timer = h, timerLabel = stringResource(R.string.txt_hour_label))
        TextTimer(timer = m, timerLabel = stringResource(R.string.txt_minutes_label))
        TextTimer(timer = s, timerLabel = stringResource(R.string.txt_seconds_label))
    }
}

@Composable
fun TextTimer(timer: String, timerLabel: String) {
    Column {
        Text(
            text = timer,
            fontSize = 100.sp,
            fontFamily = rajdhani,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.light_gray)
        )
        Box(
            Modifier
                .background(color = colorResource(id = R.color.dark_gray))
                .size(width = 100.dp, height = 3.dp)
        )
        Text(
            text = timerLabel,
            fontSize = 40.sp,
            color = colorResource(id = R.color.dark_gray),
            fontFamily = rajdhani,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

fun timeToString(time: Long): String {
    val sec = (TimeUnit.MILLISECONDS.toSeconds(time) % 60).toString().padStart(2, '0')
    val min = (TimeUnit.MILLISECONDS.toMinutes(time) % 60).toString().padStart(2, '0')
    val hr = TimeUnit.MILLISECONDS.toHours(time).toString().padStart(2, '0')
    return "$hr,$min,$sec"
}
