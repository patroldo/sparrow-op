package com.chirick.myai.ui.components

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon

@Composable
fun MarkdownText(
    modifier: Modifier = Modifier,
    markdown: String,
    fontSize: TextUnit = 16.sp,
    lineHeight: TextUnit = 24.sp,
    textColor: Color = Color.White // <-- default color
) {
    val context = LocalContext.current
    val markwon = Markwon.create(context)

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            TextView(ctx).apply {
                textSize = fontSize.value // font size in SP
                setLineSpacing(lineHeight.value, 1f)
                setTextColor(textColor.toArgb()) // <-- set color
            }
        },
        update = { textView ->
            markwon.setMarkdown(textView, markdown)
        }
    )
}