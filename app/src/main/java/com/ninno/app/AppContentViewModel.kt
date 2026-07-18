package com.ninno.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppContentViewModel(application: Application) : AndroidViewModel(application) {

    private val _content = MutableStateFlow<AppContent?>(null)
    val content: StateFlow<AppContent?> = _content.asStateFlow()

    private val _isPreloading = MutableStateFlow(true)
    val isPreloading: StateFlow<Boolean> = _isPreloading.asStateFlow()

    init {
        viewModelScope.launch {
            _content.value = AppContentRepository.load()
            val loadedContent = _content.value
            // Release loading state as soon as backend data arrives
            _isPreloading.value = false
            // Preload images in the background without blocking the UI
            if (loadedContent != null) {
                launch {
                    preloadAllUrls(application, loadedContent)
                }
            }
        }
    }

    private suspend fun preloadAllUrls(app: Application, content: AppContent) {
        withContext(Dispatchers.IO) {
            val imageLoader = ImageLoader.Builder(app)
                .build()

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
                    val request = ImageRequest.Builder(app)
                        .data(url)
                        .memoryCacheKey(url)
                        .build()
                    val result = imageLoader.execute(request)
                    if (result is SuccessResult) {
                        // Successfully cached
                    }
                } catch (_: Exception) {
                    // Ignore errors during preloading
                }
            }
        }
    }
}