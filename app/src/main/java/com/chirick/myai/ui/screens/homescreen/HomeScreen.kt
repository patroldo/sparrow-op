package com.chirick.myai.ui.screens.homescreen

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavController
import com.chirick.myai.R
import com.chirick.myai.ui.components.GifImageWithBackground
import com.chirick.myai.ui.model.InteractionStep
import com.chirick.myai.ui.model.VoiceAssistantInteractionModel
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
        Manifest.permission.RECORD_AUDIO
    )

    fun startRecordingClick() {
        if (audioPermission.status.isGranted) {
            viewModel.startRecording()
        } else {
            audioPermission.launchPermissionRequest()
        }
    }

    fun translateVoiceIntoModelClick() {
        viewModel.startTranslateVoiceIntoModel()
    }

    LaunchedEffect(state) {
        if (!state.isSuccess && state.errorText != "") {
            Toast.makeText(context, state.errorText, Toast.LENGTH_LONG).show()
            viewModel.resetModel()
        }
    }

    LaunchedEffect(navigationEvent.value) {
        navigationEvent.value?.let {
            navController.navigate("answer")
        }
    }

    AudioRecordComponent(
        modifier, state, ::startRecordingClick, ::translateVoiceIntoModelClick
    )
}

@Composable
fun AudioRecordComponent(
    modifier: Modifier,
    state: VoiceAssistantInteractionModel,
    startRecordingClick: () -> Unit,
    translateVoiceIntoModelClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            GifImageWithBackground(
                background = R.drawable.background, image = selectDrawing(state.interactionStep)
            )
            Button(
                onClick = {
                    if (state.interactionStep != InteractionStep.LISTENING) {
                        startRecordingClick.invoke()
                    } else {
                        translateVoiceIntoModelClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.interactionStep == InteractionStep.NOT_PROCESSING || state.interactionStep == InteractionStep.LISTENING
            ) {
                Text(if (state.interactionStep == InteractionStep.LISTENING) "Stop recording" else "Start recording")
            }
        }
    }
}

fun selectDrawing(interactionStep: InteractionStep): Int {
    return when (interactionStep) {
        InteractionStep.NOT_PROCESSING -> R.drawable.waiting
        InteractionStep.LISTENING -> R.drawable.microphone
        InteractionStep.PROCESSING_ONE -> R.drawable.processing
        InteractionStep.PROCESSING_TWO -> R.drawable.chatting
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    AudioRecordComponent(
        Modifier.fillMaxSize(),
        VoiceAssistantInteractionModel(false, "", "", InteractionStep.NOT_PROCESSING),
        {},
        {})
}