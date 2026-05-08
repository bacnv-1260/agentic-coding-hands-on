package com.example.saa.presentation.ui.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.saa.R

private data class LanguageOption(
    val code: String,
    val flag: String,
    val contentDescRes: Int,
)

private val SUPPORTED_LANGUAGES = listOf(
    LanguageOption("VN", "🇻🇳", R.string.language_option_vn),
    LanguageOption("EN", "🇬🇧", R.string.language_option_en),
)

@Composable
fun LanguageDropdownMenu(
    expanded: Boolean,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        SUPPORTED_LANGUAGES.forEach { lang ->
            val isSelected = lang.code == selectedLanguage
            val optionDescription = stringResource(lang.contentDescRes)
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = lang.flag)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = lang.code,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                onClick = {
                    onLanguageSelected(lang.code)
                    onDismiss()
                },
                modifier = Modifier
                    .then(
                        if (isSelected) {
                            Modifier.background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .semantics { contentDescription = optionDescription },
            )
        }
    }
}
