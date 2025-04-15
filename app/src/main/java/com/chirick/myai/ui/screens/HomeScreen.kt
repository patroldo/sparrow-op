package com.chirick.myai.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavController
import com.chirick.myai.R
import com.chirick.myai.data.model.ProcessingState
import com.chirick.myai.ui.model.HomeModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val navigationEvent = viewModel.navigationEvent.collectAsState(initial = null)

    val audioPermission = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    )

    fun startRecordingClick() {
        if (audioPermission.status.isGranted) {
            viewModel.startRecording()
        } else {
            audioPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(state) {
        if (!state.isSuccess && state.errorText != "") {
            Toast.makeText(context, state.errorText, Toast.LENGTH_LONG).show()
            viewModel.resetModel()
        }
    }

    LaunchedEffect(navigationEvent.value) {
        navigationEvent.value?.let {
            // Navigate to another screen
            navController.navigate("answer")
        }
    }

    AudioRecordComponent(
        modifier,
        state,
        ::startRecordingClick,
        viewModel
    )
}

@Composable
fun AudioRecordComponent(
    modifier: Modifier,
    state: HomeModel,
    audioPermissionClick: () -> Unit,
    viewModel: HomeViewModel?
) {
    var isRecording = state.isRecording
    var isProcessing = state.processingState != ProcessingState.NOT_PROCESSING
    var statusText =
        if (state.processingState == ProcessingState.SPEECH_TO_TEXT) "1/2 - Processing speech" else "2/2 - Processing text"

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (isProcessing) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            CircularProgressIndicator()
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = if (isRecording) Color.Red else Color.Gray,
                        shape = CircleShape
                    )
                    .clickable(
                        enabled = !isProcessing,
                        onClick = {
                            if (!isRecording) {
                                audioPermissionClick.invoke()
                            } else {
                                viewModel?.translateVoiceIntoModel()
                            }
                        }
                    )
            ) {
                Icon(
                    painter = painterResource(id = if (isRecording) R.drawable.baseline_stop_circle_24 else R.drawable.baseline_mic_24),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    fun test() {

    }
    AudioRecordComponent(
        Modifier.fillMaxSize(),
        HomeModel(false, "", "", false, ProcessingState.NOT_PROCESSING),
        ::test,
        null
    )
}