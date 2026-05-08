package com.example.saa.presentation.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.saa.domain.model.Profile

private val ProfileNameColor = Color(0xFFFFEA9E)
private val ProfileInfoColor = Color(0xFFFFFFFF)
private val BadgeBgColor = Color(0xFFE73928)

@Composable
fun ProfileInfoSection(
    profile: Profile?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Avatar — centered at top
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF2E3940)),
            contentAlignment = Alignment.Center,
        ) {
            if (profile?.avatarUrl != null) {
                AsyncImage(
                    model = profile.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    error = null,
                    fallback = null,
                )
            }
            if (profile?.avatarUrl == null) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF999999),
                    modifier = Modifier.size(44.dp),
                )
            }
        }

        // Name + team code + badge chip — centered below avatar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // Full name
            Text(
                text = profile?.fullName ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ProfileNameColor,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            // Employee code + badge chip row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = profile?.employeeCode ?: "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = ProfileInfoColor,
                    maxLines = 1,
                )

                if (profile?.badgeType?.isNotBlank() == true) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = BadgeBgColor,
                                shape = RoundedCornerShape(50),
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                    ) {
                        Text(
                            text = profile.badgeType,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProfileInfoColor,
                        )
                    }
                }
            }
        }
    }
}
