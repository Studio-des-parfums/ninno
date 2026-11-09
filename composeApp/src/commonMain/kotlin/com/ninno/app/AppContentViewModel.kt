package com.ninno.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppContentViewModel : ViewModel() {

    private val _content = MutableStateFlow<AppContent?>(null)
    val content: StateFlow<AppContent?> = _content.asStateFlow()

    private val _isPreloading = MutableStateFlow(true)
    val isPreloading: StateFlow<Boolean> = _isPreloading.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _content.value = AppContentRepository.load()
            } finally {
                // Always release loading state, even if the backend is unreachable
                _isPreloading.value = false
            }
            val loadedContent = _content.value
            // Preload images in the background without blocking the UI
            if (loadedContent != null) {
                launch {
                    preloadAllUrls(loadedContent)
                }
            }
        }
    }

    private suspend fun preloadAllUrls(content: AppContent) {
        withContext(Dispatchers.Default) {
            CoilSetup.ensureInitialized()
            val imageLoader = SingletonImageLoader.get(PlatformContext.INSTANCE)

            val urls = mutableListOf<String>()

            // Background URLs
            content.backgroundUrl?.let { urls.add(it) }
            content.backgroundOtherUrl?.let { urls.add(it) }
            content.logoUrl?.let { urls.add(it) }

            // Note images & animations
            val allNotes = content.topNotes + content.heartNotes + content.baseNotes
            for (note in allNotes) {
                note.imageUrl?.let { urls.add(it) }
                note.happyImageUrl?.let { urls.add(it) }
                note.sadImageUrl?.let { urls.add(it) }
                note.happyAnimUrl?.let { urls.add(it) }
                note.sadAnimUrl?.let { urls.add(it) }
            }

            // Preload each URL into the Coil cache
            for (url in urls) {
                try {
                    val request = ImageRequest.Builder(PlatformContext.INSTANCE)
                        .data(url)
                        .memoryCacheKey(url)
                        .build()
                    val result = imageLoader.execute(request)
                    if (result is SuccessResult) {
                        // Successfully cached
                    }
                } catch (_: Throwable) {
                    // Ignore errors during preloading
                }
            }
        }
    }
}
