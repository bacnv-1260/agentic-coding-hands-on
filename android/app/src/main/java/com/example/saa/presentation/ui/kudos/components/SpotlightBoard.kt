package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BoardBgGradient = listOf(Color(0xFF001828), Color(0xFF000B18))
private val BoardBorder = Color(0xFF998C5F)
private val BoardSearchBg = Color(0x1AFFEA9E)      // rgba(255,234,158, 0.10)
private val BoardHighlightColor = Color(0xFFF17676) // salmon red for highlighted names

@Composable
fun SpotlightBoard(
    totalKudos: Int,
    sunnerNames: List<String>,
    onSunnerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredNames = remember(searchQuery, sunnerNames) {
        if (searchQuery.isBlank()) sunnerNames
        else sunnerNames.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(0.3.dp, BoardBorder, RoundedCornerShape(8.dp))
            .background(Brush.verticalGradient(BoardBgGradient)),
    ) {
        // Scattered name labels – rendered behind the search overlay
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val bw = maxWidth
            val bh = maxHeight
            filteredNames.take(50).forEach { name ->
                val hash = name.hashCode()
                val fx = ((hash and 0xFFFF).toFloat() / 0xFFFF).coerceIn(0.02f, 0.85f)
                val fy = (((hash shr 16) and 0xFFFF).toFloat() / 0xFFFF).coerceIn(0.28f, 0.88f)
                val highlighted = searchQuery.isNotBlank() &&
                    name.contains(searchQuery, ignoreCase = true)
                Text(
                    text = name,
                    color = if (highlighted) BoardHighlightColor else Color.White.copy(alpha = 0.75f),
                    fontSize = 9.sp,
                    fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    modifier = Modifier
                        .offset(x = bw * fx, y = bh * fy)
                        .clickable { onSunnerClick(name) }
                        .semantics { contentDescription = name },
                )
            }
        }

        // Top overlay: search pill (left) + kudos count (right)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            // Search pill — B.7.3
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .height(22.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .border(0.3.dp, BoardBorder, RoundedCornerShape(13.dp))
                    .background(BoardSearchBg)
                    .padding(horizontal = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(10.dp),
                )
                Box(modifier = Modifier.defaultMinSize(minWidth = 60.dp)) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Tìm kiếm",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White, fontSize = 10.sp),
                    )
                }
            }

            // Total kudos — B.7.1
            if (totalKudos > 0) {
                Text(
                    text = "$totalKudos KUDOS",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}
