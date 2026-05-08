package com.example.saa.presentation.ui.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.saa.R
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.homeNoteBody

@Composable
fun NoteSection(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.home_note_text),
        style = MaterialTheme.typography.homeNoteBody,
        color = TextOnDark,
        modifier = modifier,
    )
}
