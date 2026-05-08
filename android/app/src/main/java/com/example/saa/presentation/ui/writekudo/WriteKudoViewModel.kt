package com.example.saa.presentation.ui.writekudo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saa.domain.model.Hashtag
import com.example.saa.domain.usecase.GetHashtagsUseCase
import com.example.saa.domain.usecase.SearchProfilesUseCase
import com.example.saa.domain.usecase.SubmitKudoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WriteKudoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getHashtagsUseCase: GetHashtagsUseCase,
    private val searchProfilesUseCase: SearchProfilesUseCase,
    private val submitKudoUseCase: SubmitKudoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WriteKudoUiState())
    val uiState: StateFlow<WriteKudoUiState> = _uiState.asStateFlow()

    private val _events = Channel<WriteKudoEvent>(Channel.BUFFERED)
    val events: Flow<WriteKudoEvent> = _events.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        val recipientId: String? = savedStateHandle["recipientId"]
        val recipientName: String? = savedStateHandle["recipientName"]
        if (recipientId != null) {
            _uiState.update {
                it.copy(
                    recipientId = recipientId,
                    recipientName = recipientName.orEmpty(),
                )
            }
        }
        loadHashtags()
    }

    private fun loadHashtags() {
        viewModelScope.launch {
            getHashtagsUseCase().onSuccess { hashtags ->
                _uiState.update { it.copy(availableHashtags = hashtags) }
            }.onFailure { e ->
                Timber.e(e, "Failed to load hashtags")
            }
        }
    }

    fun onRecipientSelected(id: String, name: String) {
        val currentUserId: String? = savedStateHandle["currentUserId"]
        if (id == currentUserId) {
            _uiState.update { it.copy(recipientError = "You cannot send a kudo to yourself.") }
            return
        }
        _uiState.update {
            it.copy(
                recipientId = id,
                recipientName = name,
                recipientError = null,
                showRecipientSearch = false,
                formDirty = true,
            )
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title, titleError = null, formDirty = true) }
    }

    fun onMessageChange(html: String) {
        _uiState.update { it.copy(message = html, messageError = null, formDirty = true) }
    }

    fun toggleHashtag(hashtag: Hashtag) {
        _uiState.update { state ->
            val current = state.hashtags.toMutableList()
            if (current.any { it.id == hashtag.id }) {
                current.removeAll { it.id == hashtag.id }
            } else if (state.canAddHashtag) {
                current.add(hashtag)
            }
            state.copy(hashtags = current, formDirty = true)
        }
    }

    fun removeHashtag(hashtag: Hashtag) {
        _uiState.update { state ->
            state.copy(hashtags = state.hashtags.filter { it.id != hashtag.id })
        }
    }

    fun onAnonymousToggle() {
        _uiState.update { it.copy(isAnonymous = !it.isAnonymous, formDirty = true) }
    }

    fun onImagesSelected(uriStrings: List<String>) {
        _uiState.update { state ->
            val available = 5 - state.imageIds.size
            val toAdd = uriStrings.take(available)
            state.copy(imageIds = state.imageIds + toAdd, formDirty = true)
        }
    }

    fun removeImage(uriString: String) {
        _uiState.update { it.copy(imageIds = it.imageIds.filter { id -> id != uriString }) }
    }

    fun onBackClick() {
        if (_uiState.value.formDirty) {
            _uiState.update { it.copy(showCancelDialog = true) }
        } else {
            viewModelScope.launch { _events.send(WriteKudoEvent.NavigateBack) }
        }
    }

    fun dismissCancelDialog() {
        _uiState.update { it.copy(showCancelDialog = false) }
    }

    fun confirmCancel() {
        _uiState.update { it.copy(showCancelDialog = false) }
        viewModelScope.launch { _events.send(WriteKudoEvent.NavigateBack) }
    }

    fun startRecipientSearch() {
        _uiState.update { it.copy(showRecipientSearch = true, searchResults = emptyList()) }
        // Load all profiles immediately when the sheet opens
        triggerSearch("")
    }

    fun dismissRecipientSearch() {
        _uiState.update { it.copy(showRecipientSearch = false) }
        searchJob?.cancel()
    }

    fun onSearchQueryChange(query: String) {
        searchJob?.cancel()
        triggerSearch(query)
    }

    private fun triggerSearch(query: String) {
        searchJob?.cancel()
        _uiState.update { it.copy(isSearching = true) }
        searchJob = viewModelScope.launch {
            if (query.isNotEmpty()) delay(300)
            searchProfilesUseCase(query).onSuccess { profiles ->
                _uiState.update { it.copy(searchResults = profiles, isSearching = false) }
            }.onFailure { e ->
                Timber.e(e, "Search profiles failed")
                _uiState.update { it.copy(isSearching = false) }
            }
        }
    }

    fun startHashtagPicker() {
        _uiState.update { it.copy(showHashtagPicker = true) }
    }

    fun dismissHashtagPicker() {
        _uiState.update { it.copy(showHashtagPicker = false) }
    }

    fun showLinkDialog() {
        _uiState.update { it.copy(showLinkDialog = true, pendingLinkUrl = "") }
    }

    fun dismissLinkDialog() {
        _uiState.update { it.copy(showLinkDialog = false, pendingLinkUrl = "") }
    }

    fun onPendingLinkUrlChange(url: String) {
        _uiState.update { it.copy(pendingLinkUrl = url) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onSendClick() {
        val state = _uiState.value

        var hasError = false
        var titleError: String? = null
        var messageError: String? = null
        var recipientError: String? = null

        if (state.recipientId == null) {
            recipientError = "Please select a recipient"
            hasError = true
        }
        if (state.title.isEmpty()) {
            titleError = "Please enter a title"
            hasError = true
        } else if (state.title.length > MAX_TITLE_LENGTH) {
            titleError = "Title is too long (max $MAX_TITLE_LENGTH characters)."
            hasError = true
        }
        if (state.message.isEmpty()) {
            messageError = "Please write a message"
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    titleError = titleError,
                    messageError = messageError,
                    recipientError = recipientError,
                )
            }
            return
        }

        val recipientId = state.recipientId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, error = null) }
            submitKudoUseCase(
                recipientId = recipientId,
                title = state.title,
                message = state.message,
                hashtagIds = state.hashtags.map { it.id },
                imageUris = state.imageIds,
                isAnonymous = state.isAnonymous,
            ).onSuccess {
                _events.send(WriteKudoEvent.NavigateToSuccess)
            }.onFailure { e ->
                Timber.e(e, "Submit kudo failed")
                _uiState.update {
                    it.copy(
                        isSending = false,
                        error = e.message ?: "Failed to send kudo",
                    )
                }
            }
        }
    }

    companion object {
        private const val MAX_TITLE_LENGTH = 100
    }
}

