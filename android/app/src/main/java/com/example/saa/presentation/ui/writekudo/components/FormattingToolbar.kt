package com.example.saa.presentation.ui.writekudo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ToolbarBg = Color(0xFFF0ECD6)
private val ToolbarBorder = Color(0xFF998C5F)
private val IconDefault = Color(0xFF333333)
private val IconActive = Color(0xFF00101A)
private val CommunityLinkRed = Color(0xFFE73928)

@Composable
fun FormattingToolbar(
    isBold: Boolean,
    isItalic: Boolean,
    isStrikethrough: Boolean,
    isOrderedList: Boolean,
    isQuote: Boolean,
    onBoldClick: () -> Unit,
    onItalicClick: () -> Unit,
    onStrikethroughClick: () -> Unit,
    onOrderedListClick: () -> Unit,
    onLinkClick: () -> Unit,
    onQuoteClick: () -> Unit,
    onCommunityStandardsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .background(ToolbarBg),
    ) {
        ToolbarIconBtn(
            imageVector = Icons.Default.FormatBold,
            isActive = isBold,
            onClick = onBoldClick,
            contentDesc = "Bold",
            isFirst = true,
        )
        ToolbarIconBtn(
            imageVector = Icons.Default.FormatItalic,
            isActive = isItalic,
            onClick = onItalicClick,
            contentDesc = "Italic",
        )
        ToolbarIconBtn(
            imageVector = Icons.Default.FormatStrikethrough,
            isActive = isStrikethrough,
            onClick = onStrikethroughClick,
            contentDesc = "Strikethrough",
        )
        ToolbarIconBtn(
            imageVector = Icons.Default.FormatListNumbered,
            isActive = isOrderedList,
            onClick = onOrderedListClick,
            contentDesc = "Ordered list",
        )
        ToolbarIconBtn(
            imageVector = Icons.Default.Link,
            isActive = false,
            onClick = onLinkClick,
            contentDesc = "Insert link",
        )
        ToolbarIconBtn(
            imageVector = Icons.Default.FormatQuote,
            isActive = isQuote,
            onClick = onQuoteClick,
            contentDesc = "Quote",
        )
        // Community standards link fills remaining space
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(width = 0.5.dp, color = ToolbarBorder)
                .clickable(onClick = onCommunityStandardsClick)
                .padding(horizontal = 7.dp)
                .semantics { contentDescription = "Community standards" },
        ) {
            Text(
                text = "Tiêu chuẩn cộng đồng",
                color = CommunityLinkRed,
                fontSize = 10.sp,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun ToolbarIconBtn(
    imageVector: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    contentDesc: String,
    isFirst: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(24.dp)
            .border(width = 0.5.dp, color = ToolbarBorder)
            .clickable(onClick = onClick)
            .padding(4.dp)
            .semantics { contentDescription = contentDesc },
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = if (isActive) IconActive else IconDefault,
            modifier = Modifier.size(16.dp),
        )
    }
}
