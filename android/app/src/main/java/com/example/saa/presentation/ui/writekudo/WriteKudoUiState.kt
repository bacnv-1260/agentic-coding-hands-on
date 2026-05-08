package com.example.saa.presentation.ui.writekudo

import com.example.saa.domain.model.Hashtag
import com.example.saa.domain.model.Profile

data class WriteKudoUiState(
    val recipientId: String? = null,
    val recipientName: String = "",
    val title: String = "",
    val message: String = "",
    val hashtags: List<Hashtag> = emptyList(),
    val imageIds: List<String> = emptyList(),
    val isAnonymous: Boolean = false,
    val isSending: Boolean = false,
    val formDirty: Boolean = false,
    val error: String? = null,
    val titleError: String? = null,
    val messageError: String? = null,
    val recipientError: String? = null,
    val showCancelDialog: Boolean = false,
    val showLinkDialog: Boolean = false,
    val showHashtagPicker: Boolean = false,
    val showRecipientSearch: Boolean = false,
    val pendingLinkUrl: String = "",
    val availableHashtags: List<Hashtag> = emptyList(),
    val searchResults: List<Profile> = emptyList(),
    val isSearching: Boolean = false,
) {
    val isSubmitEnabled: Boolean
        get() = recipientId != null && title.isNotEmpty() &&
            message.isNotEmpty() && hashtags.isNotEmpty() && !isSending

    val canAddImage: Boolean get() = imageIds.size < 5
    val canAddHashtag: Boolean get() = hashtags.size < 5
    val messageCharCount: Int get() = message.length
}

sealed class WriteKudoEvent {
    data object NavigateToSuccess : WriteKudoEvent()
    data object NavigateBack : WriteKudoEvent()
    data class ShowError(val message: String) : WriteKudoEvent()
}
