package com.chirick.myai.ui.screens.answerscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.chirick.myai.ui.components.MarkdownText

@Composable
fun AnswerScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AnswerViewModel = hiltViewModel()
) {

    val text by viewModel.answerText.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MarkdownText(
            markdown = text,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 10.dp),
            fontSize = 40.sp,
            lineHeight = 48.sp,
            textColor = Color.White
        )
    }
}