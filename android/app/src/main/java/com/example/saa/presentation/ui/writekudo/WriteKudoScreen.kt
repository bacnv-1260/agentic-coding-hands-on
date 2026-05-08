package com.example.saa.presentation.ui.writekudo

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.saa.R
import com.example.saa.presentation.ui.writekudo.components.AnonymousToggle
import com.example.saa.presentation.ui.writekudo.components.FormattingToolbar
import com.example.saa.presentation.ui.writekudo.components.HashtagPickerSheet
import com.example.saa.presentation.ui.writekudo.components.HashtagSection
import com.example.saa.presentation.ui.writekudo.components.ImageSection
import com.example.saa.presentation.ui.writekudo.components.MessageCharCounter
import com.example.saa.presentation.ui.writekudo.components.RecipientDropdownField
import com.example.saa.presentation.ui.writekudo.components.RichTextEditorField
import com.example.saa.presentation.ui.writekudo.components.TitleInputField
import com.example.saa.presentation.ui.writekudo.components.WriteKudoBottomBar
import com.example.saa.presentation.ui.writekudo.components.WriteKudoFormCard
import com.example.saa.presentation.ui.writekudo.search.RecipientSearchSheet
import com.mohamedrejeb.richeditor.model.rememberRichTextState

private val ScreenBg = Color(0xFF00101A)
private val TopBarBg = Color.Transparent
private val HintTextColor = Color(0xFF999999)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteKudoScreen(
    recipientId: String?,
    recipientName: String?,
    onNavigateBack: () -> Unit,
    onNavigateToCommunityStandards: () -> Unit,
    viewModel: WriteKudoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val richTextState = rememberRichTextState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Sync rich text state changes to ViewModel
    LaunchedEffect(richTextState.toHtml()) {
        viewModel.onMessageChange(richTextState.toHtml())
    }

    // Consume one-shot events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is WriteKudoEvent.NavigateBack -> onNavigateBack()
                is WriteKudoEvent.NavigateToSuccess -> onNavigateBack()
                is WriteKudoEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    // Show error as snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            snackbarHostState.showSnackbar(err)
            viewModel.clearError()
        }
    }

    BackHandler { viewModel.onBackClick() }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
    ) { uris: List<Uri> ->
        viewModel.onImagesSelected(uris.map { it.toString() })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg),
    ) {
        // Full-screen keyvisual background
        Image(
            painter = painterResource(R.drawable.img_keyvisual_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp),
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "New Kudo",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { viewModel.onBackClick() },
                            modifier = Modifier.semantics { contentDescription = "Back" },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TopBarBg,
                        scrolledContainerColor = ScreenBg,
                    ),
                )
            },
            bottomBar = {
                WriteKudoBottomBar(
                    isSubmitEnabled = uiState.isSubmitEnabled,
                    isSending = uiState.isSending,
                    onCancelClick = { viewModel.onBackClick() },
                    onSendClick = { viewModel.onSendClick() },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
            ) {
                WriteKudoFormCard {
                    // Recipient
                    RecipientDropdownField(
                        recipientName = uiState.recipientName,
                        errorMessage = uiState.recipientError,
                        onClick = { viewModel.startRecipientSearch() },
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    TitleInputField(
                        value = uiState.title,
                        onValueChange = viewModel::onTitleChange,
                        errorMessage = uiState.titleError,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Formatting toolbar + Rich text editor
                    FormattingToolbar(
                        isBold = richTextState.currentSpanStyle.fontWeight?.weight == 700,
                        isItalic = richTextState.currentSpanStyle.fontStyle == FontStyle.Italic,
                        isStrikethrough = richTextState.currentSpanStyle.textDecoration
                            ?.contains(TextDecoration.LineThrough) == true,
                        isOrderedList = richTextState.isOrderedList,
                        isQuote = false,
                        onBoldClick = {
                            richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        },
                        onItalicClick = {
                            richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                        },
                        onStrikethroughClick = {
                            richTextState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                        },
                        onOrderedListClick = {
                            richTextState.toggleOrderedList()
                        },
                        onLinkClick = { viewModel.showLinkDialog() },
                        onQuoteClick = { /* not yet supported by richeditor */ },
                        onCommunityStandardsClick = onNavigateToCommunityStandards,
                    )
                    RichTextEditorField(
                        state = richTextState,
                        isError = uiState.messageError != null,
                        errorMessage = uiState.messageError,
                    )
                    if (uiState.messageError != null) {
                        Text(
                            text = uiState.messageError.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                        )
                    }
                    // Mention hint
                    Text(
                        text = "Bạn có thể \"@ + tên\" để nhắc tới đồng nghiệp khác",
                        style = MaterialTheme.typography.bodySmall,
                        color = HintTextColor,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    MessageCharCounter(charCount = uiState.messageCharCount)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Hashtags
                    HashtagSection(
                        selectedHashtags = uiState.hashtags,
                        canAddMore = uiState.canAddHashtag,
                        onAddClick = { viewModel.startHashtagPicker() },
                        onRemoveHashtag = viewModel::removeHashtag,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Images
                    ImageSection(
                        imageUris = uiState.imageIds,
                        canAddMore = uiState.canAddImage,
                        onAddImageClick = { imagePickerLauncher.launch("image/*") },
                        onRemoveImage = viewModel::removeImage,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Anonymous toggle
                    AnonymousToggle(
                        isAnonymous = uiState.isAnonymous,
                        onToggle = { viewModel.onAnonymousToggle() },
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Cancel confirmation dialog
    if (uiState.showCancelDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCancelDialog() },
            title = { Text("Bạn có chắc muốn hủy không?") },
            text = { Text("Dữ liệu đã nhập sẽ bị mất.") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.confirmCancel() },
                    modifier = Modifier.semantics { contentDescription = "Confirm cancel" },
                ) {
                    Text("Hủy bỏ", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.dismissCancelDialog() },
                    modifier = Modifier.semantics { contentDescription = "Keep editing" },
                ) {
                    Text("Tiếp tục chỉnh sửa")
                }
            },
        )
    }

    // Link dialog
    if (uiState.showLinkDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLinkDialog() },
            title = { Text("Chèn liên kết") },
            text = {
                OutlinedTextField(
                    value = uiState.pendingLinkUrl,
                    onValueChange = viewModel::onPendingLinkUrlChange,
                    label = { Text("URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val url = uiState.pendingLinkUrl
                        if (url.isNotBlank()) {
                            richTextState.addLink(text = url, url = url)
                        }
                        viewModel.dismissLinkDialog()
                    },
                ) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissLinkDialog() }) {
                    Text("Hủy")
                }
            },
        )
    }

    // Recipient search sheet
    if (uiState.showRecipientSearch) {
        RecipientSearchSheet(
            searchResults = uiState.searchResults,
            isSearching = uiState.isSearching,
            onQueryChange = viewModel::onSearchQueryChange,
            onProfileSelected = viewModel::onRecipientSelected,
            onDismiss = { viewModel.dismissRecipientSearch() },
        )
    }

    // Hashtag picker sheet
    if (uiState.showHashtagPicker) {
        HashtagPickerSheet(
            availableHashtags = uiState.availableHashtags,
            selectedHashtags = uiState.hashtags,
            onToggleHashtag = viewModel::toggleHashtag,
            onDismiss = { viewModel.dismissHashtagPicker() },
        )
    }
}

