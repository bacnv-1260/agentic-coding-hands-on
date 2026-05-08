package com.example.saa.presentation.ui.writekudo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FieldBorder = Color(0xFF998C5F)
private val LabelDark = Color(0xFF00101A)
private val PlaceholderGray = Color(0xFF999999)
private val HintGray = Color(0xFF999999)
private val RequiredRed = Color(0xFFCF1322)

@Composable
fun TitleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Danh hiệu")
                    withStyle(SpanStyle(color = RequiredRed, fontWeight = FontWeight.Bold)) {
                        append("*")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = LabelDark,
                modifier = Modifier.width(96.dp),
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = LabelDark),
                cursorBrush = SolidColor(LabelDark),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .semantics { contentDescription = "Title field" },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .border(
                                width = 1.dp,
                                color = if (errorMessage != null) MaterialTheme.colorScheme.error else FieldBorder,
                                shape = RoundedCornerShape(4.dp),
                            )
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = "Dành tặng một danh hiệu cho...",
                                style = MaterialTheme.typography.bodySmall,
                                color = PlaceholderGray,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp),
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ví dụ: Người truyền động lực cho tôi.\nDanh hiệu sẽ hiển thị làm tiêu đề của Kudos của bạn.",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = HintGray,
            )
        }
    }
}
