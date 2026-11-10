package com.ninno.app

import kotlinx.coroutines.withTimeout

import com.ninno.app.network.ContentApiClient
import com.ninno.app.network.RemoteNote

data class DisplayNote(
    val name: String,
    val imageUrl: String?,
    val happyImageUrl: String?,
    val sadImageUrl: String?,
    val happyAnimUrl: String?,
    val sadAnimUrl: String?,
    val fallback: PerfumeNote?
)

data class AppContent(
    val backgroundUrl: String?,
    val backgroundOtherUrl: String?,
    val logoUrl: String?,
    /** Logo partenaire en haut à droite (null = rien n'est affiché). */
    val partnerLogoUrl: String?,
    val topNotes: List<DisplayNote>,
    val heartNotes: List<DisplayNote>,
    val baseNotes: List<DisplayNote>
)

private fun localFallbackFor(name: String): PerfumeNote? {
    return (topNotes + heartNotes + baseNotes).firstOrNull {
        it.name.equals(name, ignoreCase = true)
    }
}

private fun List<RemoteNote>.toDisplayNotes(type: String): List<DisplayNote> {
    return filter { it.type == type }.map { note ->
        DisplayNote(
            name = note.name,
            imageUrl = note.imageUrl,
            happyImageUrl = note.happyImageUrl,
            sadImageUrl = note.sadImageUrl,
            happyAnimUrl = note.happyAnimUrl,
            sadAnimUrl = note.sadAnimUrl,
            fallback = localFallbackFor(note.name)
        )
    }
}

fun List<PerfumeNote>.toDisplayNotes(): List<DisplayNote> = map { note ->
    DisplayNote(
        name = note.name,
        imageUrl = null,
        happyImageUrl = null,
        sadImageUrl = null,
        happyAnimUrl = null,
        sadAnimUrl = null,
        fallback = note
    )
}

private val localFallbackContent = AppContent(
    backgroundUrl = null,
    backgroundOtherUrl = null,
    logoUrl = null,
    partnerLogoUrl = null,
    topNotes = topNotes.toDisplayNotes(),
    heartNotes = heartNotes.toDisplayNotes(),
    baseNotes = baseNotes.toDisplayNotes()
)

object AppContentRepository {
    suspend fun load(): AppContent {
        return try {
            withTimeout(8_000) { loadRemote() }
        } catch (e: Throwable) {
            localFallbackContent
        }
    }

    private suspend fun loadRemote(): AppContent {
        return run {
            val config = ContentApiClient.getConfig()
            val notes = ContentApiClient.getNotes()

            val remoteTop = notes.toDisplayNotes("top")
            val remoteHeart = notes.toDisplayNotes("heart")
            val remoteBase = notes.toDisplayNotes("base")

            AppContent(
                backgroundUrl = config.backgroundImageUrl?.takeIf { it.isNotBlank() },
                backgroundOtherUrl = config.backgroundOtherImageUrl?.takeIf { it.isNotBlank() },
                logoUrl = config.logoImageUrl?.takeIf { it.isNotBlank() },
                partnerLogoUrl = config.partnerLogoImageUrl?.takeIf { it.isNotBlank() },
                topNotes = remoteTop.ifEmpty { localFallbackContent.topNotes },
                heartNotes = remoteHeart.ifEmpty { localFallbackContent.heartNotes },
                baseNotes = remoteBase.ifEmpty { localFallbackContent.baseNotes }
            )
        }
    }
}
