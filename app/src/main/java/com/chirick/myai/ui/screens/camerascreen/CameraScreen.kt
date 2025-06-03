package com.chirick.myai.ui.screens.camerascreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chirick.myai.R
import com.chirick.myai.data.model.ProcessingState
import com.chirick.myai.ui.components.CameraPreview
import com.chirick.myai.ui.components.GifImageWithBackground
import com.chirick.myai.ui.model.InteractionStep
import com.chirick.myai.ui.model.VoiceAssistantInteractionModel
import com.chirick.myai.ui.screens.homescreen.selectDrawing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val navigationEvent = viewModel.navigationEvent.collectAsState(initial = null)

    val audioPermission = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    )

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val bitmap by viewModel.savedBitmap.collectAsState()

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
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
            navController.navigate("answer")
            viewModel.flushBitmap()
        }
    }

    fun startRecordingClick() {
        if (audioPermission.status.isGranted) {
            viewModel.startRecording()
        } else {
            audioPermission.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        MainContainer(
            bitmap,
            state,
            onPhotoTaken = { viewModel.saveBitmap(it) },
            onError = { Log.e("PhotoMaking", "Can't make a photo. Details: ", it) },
            cleanUpSelectedPhoto = { viewModel.flushBitmap() },
            startRecordingClick = { startRecordingClick() },
            stopRecordingClick = { viewModel.process() },
            modifier
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Waiting for permissions")
        }
    }


}

@Composable
fun MainContainer(
    bitmap: Bitmap?,
    state: VoiceAssistantInteractionModel,
    onPhotoTaken: (Bitmap) -> Unit,
    onError: (Exception) -> Unit,
    cleanUpSelectedPhoto: () -> Unit,
    startRecordingClick: () -> Unit,
    stopRecordingClick: () -> Unit,
    modifier: Modifier,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {

        val isProcessing =
            (state.interactionStep == InteractionStep.PROCESSING_ONE) ||
                    state.interactionStep == InteractionStep.PROCESSING_TWO

        if (isProcessing) {
            Column(
                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GifImageWithBackground(
                    background = R.drawable.background,
                    image = selectDrawing(state.interactionStep)
                )
            }
        } else if (bitmap != null) {
            DisplayImagePreview(
                bitmap,
                state,
                cleanUpSelectedPhoto,
                startRecordingClick,
                stopRecordingClick,
                Modifier
            )
        } else {
            DisplayCameraPreview(onPhotoTaken, onError, Modifier)
        }
    }
}

@Composable
fun DisplayImagePreview(
    bitmap: Bitmap,
    state: VoiceAssistantInteractionModel,
    cleanUpSelectedPhoto: () -> Unit,
    startRecordingClick: () -> Unit,
    stopRecordingClick: () -> Unit,
    modifier: Modifier
) {
    val isRecording = state.interactionStep == InteractionStep.LISTENING
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected image",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {

                IconButton(
                    onClick = { cleanUpSelectedPhoto() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (isRecording) {
                        stopRecordingClick()
                    } else {
                        startRecordingClick()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isRecording) "Stop recording" else "Start recording")
            }
        }
    }
}

@Composable
fun DisplayCameraPreview(
    onPhotoTaken: (Bitmap) -> Unit,
    onError: (Exception) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }
    var isProcessing by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(50, 50, 50, 255))
        ) {
            CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())
            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        else
                            CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier.offset(10.dp, 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if (isProcessing) {
                LinearProgressIndicator()
            } else {
                IconButton(
                    onClick = {
                        isProcessing = true
                        takePhoto(context, controller, {
                            onPhotoTaken(it)
                            isProcessing = false
                        }, {
                            onError(it)
                            isProcessing = false
                        })
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoSizeSelectActual,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    onError: (Exception) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }

                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(), 0, 0, image.width, image.height,
                    matrix, true
                )

                onPhotoTaken(cropToPreviewBox(rotatedBitmap, context))
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                onError(exception)
            }
        }
    )
}

fun cropToPreviewBox(bitmap: Bitmap, context: Context): Bitmap {
    val displayMetrics = context.resources.displayMetrics

    val boxHeightPx = (370.dp.toPx(displayMetrics))
    val boxWidthPx = displayMetrics.widthPixels

    val scaleFactor = bitmap.width.toFloat() / boxWidthPx
    val visibleHeightPx = (boxHeightPx * scaleFactor).toInt()

    val centerY = bitmap.height / 2
    val startY = (centerY - visibleHeightPx / 2).coerceAtLeast(0)
    val cropHeight = visibleHeightPx.coerceAtMost(bitmap.height - startY)

    return Bitmap.createBitmap(bitmap, 0, startY, bitmap.width, cropHeight)
}

@Stable
fun Dp.toPx(
    displayMetrics: DisplayMetrics
): Float = this.value * displayMetrics.density

@Preview
@Composable
fun MainContainerPreview() {
    MainContainer(
        null,
        VoiceAssistantInteractionModel(false, "", "", InteractionStep.NOT_PROCESSING),
        {},
        {},
        {},
        {},
        {},
        Modifier
    )
}

