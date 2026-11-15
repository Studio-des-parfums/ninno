package com.ninno.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ninno.app.resources.Res
import com.ninno.app.resources.anim_ambre_happy
import com.ninno.app.resources.anim_ambre_sad
import com.ninno.app.resources.anim_bambou_happy
import com.ninno.app.resources.anim_bambou_sad
import com.ninno.app.resources.anim_barbepapa_happy
import com.ninno.app.resources.anim_barbepapa_sad
import com.ninno.app.resources.anim_caramel_happy
import com.ninno.app.resources.anim_caramel_sad
import com.ninno.app.resources.anim_citron_happy
import com.ninno.app.resources.anim_citron_sad
import com.ninno.app.resources.anim_cocktail_happy
import com.ninno.app.resources.anim_cocktail_sad
import com.ninno.app.resources.anim_cola_happy
import com.ninno.app.resources.anim_cola_sad
import com.ninno.app.resources.anim_frangipane_happy
import com.ninno.app.resources.anim_frangipane_sad
import com.ninno.app.resources.anim_herbe_happy
import com.ninno.app.resources.anim_herbe_sad
import com.ninno.app.resources.anim_jasmin_happy
import com.ninno.app.resources.anim_jasmin_sad
import com.ninno.app.resources.anim_mangue_happy
import com.ninno.app.resources.anim_mangue_sad
import com.ninno.app.resources.anim_miel_happy
import com.ninno.app.resources.anim_miel_sad
import com.ninno.app.resources.anim_muguet_sad
import com.ninno.app.resources.anim_orange_happy
import com.ninno.app.resources.anim_orange_sad
import com.ninno.app.resources.anim_pomme_happy
import com.ninno.app.resources.anim_pomme_sad
import com.ninno.app.resources.anim_praline_happy
import com.ninno.app.resources.anim_praline_sad
import com.ninno.app.resources.anim_rose_happy
import com.ninno.app.resources.anim_rose_sad
import com.ninno.app.resources.anim_vanille_sad
import com.ninno.app.resources.bg_home2
import com.ninno.app.resources.ic_note_placeholder
import com.ninno.app.resources.note_ambre
import com.ninno.app.resources.note_ambre_happy
import com.ninno.app.resources.note_ambre_sad
import com.ninno.app.resources.note_bambou
import com.ninno.app.resources.note_bambou_happy
import com.ninno.app.resources.note_bambou_sad
import com.ninno.app.resources.note_barbepapa
import com.ninno.app.resources.note_barbepapa_happy
import com.ninno.app.resources.note_barbepapa_sad
import com.ninno.app.resources.note_caramel
import com.ninno.app.resources.note_caramel_happy
import com.ninno.app.resources.note_caramel_sad
import com.ninno.app.resources.note_citron
import com.ninno.app.resources.note_citron_happy
import com.ninno.app.resources.note_citron_sad
import com.ninno.app.resources.note_cocktail
import com.ninno.app.resources.note_cocktail_happy
import com.ninno.app.resources.note_cocktail_sad
import com.ninno.app.resources.note_cola
import com.ninno.app.resources.note_cola_happy
import com.ninno.app.resources.note_cola_sad
import com.ninno.app.resources.note_frangipane
import com.ninno.app.resources.note_frangipane_happy
import com.ninno.app.resources.note_frangipane_sad
import com.ninno.app.resources.note_herbe
import com.ninno.app.resources.note_herbe_happy
import com.ninno.app.resources.note_herbe_sad
import com.ninno.app.resources.note_jasmin
import com.ninno.app.resources.note_jasmin_happy
import com.ninno.app.resources.note_jasmin_sad
import com.ninno.app.resources.note_mangue
import com.ninno.app.resources.note_mangue_happy
import com.ninno.app.resources.note_mangue_sad
import com.ninno.app.resources.note_miel
import com.ninno.app.resources.note_miel_happy
import com.ninno.app.resources.note_miel_sad
import com.ninno.app.resources.note_muguet
import com.ninno.app.resources.note_muguet_happy
import com.ninno.app.resources.note_muguet_sad
import com.ninno.app.resources.note_orange
import com.ninno.app.resources.note_orange_happy
import com.ninno.app.resources.note_orange_sad
import com.ninno.app.resources.note_pomme
import com.ninno.app.resources.note_pomme_happy
import com.ninno.app.resources.note_pomme_sad
import com.ninno.app.resources.note_praline
import com.ninno.app.resources.note_praline_happy
import com.ninno.app.resources.note_praline_sad
import com.ninno.app.resources.note_rose
import com.ninno.app.resources.note_rose_happy
import com.ninno.app.resources.note_rose_sad
import com.ninno.app.resources.note_vanille
import com.ninno.app.resources.note_vanille_happy
import com.ninno.app.resources.note_vanille_sad
import com.ninno.app.ui.DeviceSize
import com.ninno.app.ui.PartnerLogo
import com.ninno.app.ui.NavBackButton
import com.ninno.app.ui.NavForwardButton
import com.ninno.app.ui.ScreenBackground
import com.ninno.app.ui.logoWidthFor
import com.ninno.app.ui.navButtonSizeFor
import com.ninno.app.ui.rememberDeviceSize
import com.ninno.app.ui.rememberIsLandscape
import com.ninno.app.ui.rememberUseStackedLayout
import com.ninno.app.ui.rememberWindowHeightDp
import com.ninno.app.ui.rememberWindowWidthDp
import com.ninno.app.ui.screenPaddingFor
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

data class PerfumeNote(
    val name: String,
    val imageRes: DrawableResource,
    val happyImageRes: DrawableResource,
    val sadImageRes: DrawableResource,
    val happyAnimRes: DrawableResource? = null,
    val sadAnimRes: DrawableResource? = null
)

val topNotes = listOf(
    PerfumeNote("Pomme", Res.drawable.note_pomme, Res.drawable.note_pomme_happy, Res.drawable.note_pomme_sad, Res.drawable.anim_pomme_happy, Res.drawable.anim_pomme_sad),
    PerfumeNote("Orange", Res.drawable.note_orange, Res.drawable.note_orange_happy, Res.drawable.note_orange_sad, Res.drawable.anim_orange_happy, Res.drawable.anim_orange_sad),
    PerfumeNote("Citron", Res.drawable.note_citron, Res.drawable.note_citron_happy, Res.drawable.note_citron_sad, Res.drawable.anim_citron_happy, Res.drawable.anim_citron_sad),
    PerfumeNote("Bambou", Res.drawable.note_bambou, Res.drawable.note_bambou_happy, Res.drawable.note_bambou_sad, Res.drawable.anim_bambou_happy, Res.drawable.anim_bambou_sad),
    PerfumeNote("Cola", Res.drawable.note_cola, Res.drawable.note_cola_happy, Res.drawable.note_cola_sad, Res.drawable.anim_cola_happy, Res.drawable.anim_cola_sad),
    PerfumeNote("Herbe", Res.drawable.note_herbe, Res.drawable.note_herbe_happy, Res.drawable.note_herbe_sad, Res.drawable.anim_herbe_happy, Res.drawable.anim_herbe_sad)
)

val heartNotes = listOf(
    PerfumeNote("Miel", Res.drawable.note_miel, Res.drawable.note_miel_happy, Res.drawable.note_miel_sad, Res.drawable.anim_miel_happy, Res.drawable.anim_miel_sad),
    PerfumeNote("Mangue", Res.drawable.note_mangue, Res.drawable.note_mangue_happy, Res.drawable.note_mangue_sad, Res.drawable.anim_mangue_happy, Res.drawable.anim_mangue_sad),
    PerfumeNote("Rose", Res.drawable.note_rose, Res.drawable.note_rose_happy, Res.drawable.note_rose_sad, Res.drawable.anim_rose_happy, Res.drawable.anim_rose_sad),
    PerfumeNote("Muguet", Res.drawable.note_muguet, Res.drawable.note_muguet_happy, Res.drawable.note_muguet_sad, null, Res.drawable.anim_muguet_sad),
    PerfumeNote("Jasmin", Res.drawable.note_jasmin, Res.drawable.note_jasmin_happy, Res.drawable.note_jasmin_sad, Res.drawable.anim_jasmin_happy, Res.drawable.anim_jasmin_sad),
    PerfumeNote("Cocktail", Res.drawable.note_cocktail, Res.drawable.note_cocktail_happy, Res.drawable.note_cocktail_sad, Res.drawable.anim_cocktail_happy, Res.drawable.anim_cocktail_sad)
)

val baseNotes = listOf(
    PerfumeNote("Caramel", Res.drawable.note_caramel, Res.drawable.note_caramel_happy, Res.drawable.note_caramel_sad, Res.drawable.anim_caramel_happy, Res.drawable.anim_caramel_sad),
    PerfumeNote("Vanille", Res.drawable.note_vanille, Res.drawable.note_vanille_happy, Res.drawable.note_vanille_sad, null, Res.drawable.anim_vanille_sad),
    PerfumeNote("Frangipane", Res.drawable.note_frangipane, Res.drawable.note_frangipane_happy, Res.drawable.note_frangipane_sad, Res.drawable.anim_frangipane_happy, Res.drawable.anim_frangipane_sad),
    PerfumeNote("Praliné", Res.drawable.note_praline, Res.drawable.note_praline_happy, Res.drawable.note_praline_sad, Res.drawable.anim_praline_happy, Res.drawable.anim_praline_sad),
    PerfumeNote("Barbe à papa", Res.drawable.note_barbepapa, Res.drawable.note_barbepapa_happy, Res.drawable.note_barbepapa_sad, Res.drawable.anim_barbepapa_happy, Res.drawable.anim_barbepapa_sad),
    PerfumeNote("Ambre", Res.drawable.note_ambre, Res.drawable.note_ambre_happy, Res.drawable.note_ambre_sad, Res.drawable.anim_ambre_happy, Res.drawable.anim_ambre_sad)
)

private data class PendingSort(val note: DisplayNote, val liked: Boolean)

private enum class DragSource { GRID, LIKED, DISLIKED }

@Composable
fun SortingScreen(
    title: String,
    notes: List<DisplayNote> = topNotes.toDisplayNotes(),
    onSelectionChanged: (liked: List<String>, disliked: List<String>) -> Unit = { _, _ -> },
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    val deviceSize = rememberDeviceSize()
    val isLandscape = rememberIsLandscape()
    val useStackedLayout = rememberUseStackedLayout()
    val buttonSize = navButtonSizeFor(deviceSize)
    val screenPadding = screenPaddingFor(deviceSize)
    val logoWidth = logoWidthFor(deviceSize, isLandscape) * 0.55f

    val titleSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 15.sp else 18.sp
        DeviceSize.MEDIUM -> 22.sp
        DeviceSize.EXPANDED -> 28.sp
    }
    val sectionSpacing = when (deviceSize) {
        DeviceSize.COMPACT -> 10.dp
        DeviceSize.MEDIUM -> 14.dp
        DeviceSize.EXPANDED -> 20.dp
    }
    val gridPadding = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 8.dp else 12.dp
        DeviceSize.MEDIUM -> 16.dp
        DeviceSize.EXPANDED -> 20.dp
    }
    val noteLabelSize = if (useStackedLayout) 12.sp else 14.sp
    val windowWidthDp = rememberWindowWidthDp()
    val windowHeightDp = rememberWindowHeightDp()
    val animationSize = minOf(
        280.dp,
        windowWidthDp * 0.5f,
        windowHeightDp * 0.7f
    )

    val maxGridImageSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 86.dp else 68.dp
        DeviceSize.MEDIUM -> 106.dp
        DeviceSize.EXPANDED -> 126.dp
    }

    val choiceImageSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 70.dp else 52.dp
        DeviceSize.MEDIUM -> 86.dp
        DeviceSize.EXPANDED -> 106.dp
    }

    var likedNotes by remember(notes) { mutableStateOf(listOf<DisplayNote>()) }
    var dislikedNotes by remember(notes) { mutableStateOf(listOf<DisplayNote>()) }
    var pendingSort by remember(notes) { mutableStateOf<PendingSort?>(null) }
    var draggedNote by remember { mutableStateOf<DisplayNote?>(null) }
    var dragSource by remember { mutableStateOf<DragSource?>(null) }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }
    var likedBounds by remember { mutableStateOf<Rect?>(null) }
    var dislikedBounds by remember { mutableStateOf<Rect?>(null) }
    val selectedNotes = likedNotes + dislikedNotes
    val pendingNote = pendingSort?.note
    val remainingNotes = notes.filterNot { it in selectedNotes || it == pendingNote }

    val soundPlayer = remember { SortSoundPlayer() }
    DisposableEffect(Unit) {
        onDispose { soundPlayer.release() }
    }

    fun notifySelectionChanged() {
        onSelectionChanged(likedNotes.map { it.name }, dislikedNotes.map { it.name })
    }

    fun handleNoteDrop(note: DisplayNote, liked: Boolean) {
        if (pendingSort == null) {
            if (liked && likedNotes.size < 2) {
                pendingSort = PendingSort(note, liked = true)
            } else if (!liked && dislikedNotes.size < 2) {
                pendingSort = PendingSort(note, liked = false)
            }
        }
    }

    fun endDrag() {
        val note = draggedNote
        if (note != null) {
            val overLiked = likedBounds?.contains(dragPosition) == true
            val overDisliked = dislikedBounds?.contains(dragPosition) == true
            when (dragSource) {
                DragSource.GRID -> when {
                    overLiked -> handleNoteDrop(note, liked = true)
                    overDisliked -> handleNoteDrop(note, liked = false)
                }
                DragSource.LIKED -> when {
                    overDisliked -> if (pendingSort == null && dislikedNotes.size < 2) {
                        likedNotes = likedNotes - note
                        pendingSort = PendingSort(note, liked = false)
                    }
                    !overLiked -> {
                        likedNotes = likedNotes - note
                        notifySelectionChanged()
                    }
                    else -> {}
                }
                DragSource.DISLIKED -> when {
                    overLiked -> if (pendingSort == null && likedNotes.size < 2) {
                        dislikedNotes = dislikedNotes - note
                        pendingSort = PendingSort(note, liked = true)
                    }
                    !overDisliked -> {
                        dislikedNotes = dislikedNotes - note
                        notifySelectionChanged()
                    }
                    else -> {}
                }
                null -> {}
            }
        }
        draggedNote = null
        dragSource = null
    }

    val isOverLiked = draggedNote != null && likedBounds?.contains(dragPosition) == true
    val isOverDisliked = draggedNote != null && dislikedBounds?.contains(dragPosition) == true

    pendingSort?.let { pending ->
        LaunchedEffect(pending.note) {
            if (pending.liked) soundPlayer.playHappy() else soundPlayer.playSad()
            delay(2500)
            if (pending.liked) {
                likedNotes = likedNotes + pending.note
            } else {
                dislikedNotes = dislikedNotes + pending.note
            }
            pendingSort = null
            notifySelectionChanged()
        }
    }

    ScreenBackground(backgroundRes = Res.drawable.bg_home2, backgroundUrl = backgroundUrl, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenPadding, vertical = screenPadding)
                .padding(bottom = buttonSize + sectionSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PartnerLogo(
                width = logoWidth,
                modifier = Modifier.padding(bottom = sectionSpacing / 2)
            )

            Text(
                text = title,
                color = Color(0xFF3B1E5C),
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(color = Color.White, blurRadius = 16f)
                ),
                fontSize = titleSize,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = sectionSpacing)
            )

            if (useStackedLayout) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(sectionSpacing)
                ) {
                    PerfumeButtonGrid(
                        notes = remainingNotes,
                        onNoteClick = { note ->
                            if (pendingSort == null) {
                                if (likedNotes.size < 2) {
                                    pendingSort = PendingSort(note, liked = true)
                                } else if (dislikedNotes.size < 2) {
                                    pendingSort = PendingSort(note, liked = false)
                                }
                            }
                        },
                        onDragStart = { note, startPosition ->
                            draggedNote = note
                            dragSource = DragSource.GRID
                            dragPosition = startPosition
                        },
                        onDrag = { dragAmount ->
                            dragPosition += dragAmount
                        },
                        onDragEnd = { endDrag() },
                        onDragCancel = {
                            draggedNote = null
                            dragSource = null
                        },
                        noteLabelSize = noteLabelSize,
                        compact = true,
                        maxImageSize = maxGridImageSize,
                        draggedNote = draggedNote,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.4f)
                            .padding(gridPadding)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = gridPadding),
                        horizontalArrangement = Arrangement.spacedBy(sectionSpacing)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "J'aime",
                                color = Color.White,
                                fontSize = (noteLabelSize.value * 1.2f).sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            ColorSection(
                                color = Color(0xFF4CAF50),
                                notes = likedNotes,
                                useHappyImage = true,
                                imageSize = choiceImageSize,
                                onNoteClick = { note ->
                                    likedNotes = likedNotes - note
                                    notifySelectionChanged()
                                },
                                isDragOver = isOverLiked,
                                onBoundsChanged = { likedBounds = it },
                                hiddenNote = if (dragSource == DragSource.LIKED) draggedNote else null,
                                onDragStart = { note, startPosition ->
                                    draggedNote = note
                                    dragSource = DragSource.LIKED
                                    dragPosition = startPosition
                                },
                                onDrag = { dragAmount -> dragPosition += dragAmount },
                                onDragEnd = { endDrag() },
                                onDragCancel = {
                                    draggedNote = null
                                    dragSource = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Je n'aime pas",
                                color = Color.White,
                                fontSize = (noteLabelSize.value * 1.2f).sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            ColorSection(
                                color = Color(0xFFE53935),
                                notes = dislikedNotes,
                                useHappyImage = false,
                                imageSize = choiceImageSize,
                                onNoteClick = { note ->
                                    dislikedNotes = dislikedNotes - note
                                    notifySelectionChanged()
                                },
                                isDragOver = isOverDisliked,
                                onBoundsChanged = { dislikedBounds = it },
                                hiddenNote = if (dragSource == DragSource.DISLIKED) draggedNote else null,
                                onDragStart = { note, startPosition ->
                                    draggedNote = note
                                    dragSource = DragSource.DISLIKED
                                    dragPosition = startPosition
                                },
                                onDrag = { dragAmount -> dragPosition += dragAmount },
                                onDragEnd = { endDrag() },
                                onDragCancel = {
                                    draggedNote = null
                                    dragSource = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(sectionSpacing)
                ) {
                    PerfumeButtonGrid(
                        notes = remainingNotes,
                        onNoteClick = { note ->
                            if (pendingSort == null) {
                                if (likedNotes.size < 2) {
                                    pendingSort = PendingSort(note, liked = true)
                                } else if (dislikedNotes.size < 2) {
                                    pendingSort = PendingSort(note, liked = false)
                                }
                            }
                        },
                        onDragStart = { note, startPosition ->
                            draggedNote = note
                            dragSource = DragSource.GRID
                            dragPosition = startPosition
                        },
                        onDrag = { dragAmount ->
                            dragPosition += dragAmount
                        },
                        onDragEnd = { endDrag() },
                        onDragCancel = {
                            draggedNote = null
                            dragSource = null
                        },
                        noteLabelSize = noteLabelSize,
                        compact = false,
                        maxImageSize = maxGridImageSize,
                        draggedNote = draggedNote,
                        modifier = Modifier
                            .weight(2f)
                            .padding(gridPadding)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(gridPadding),
                        verticalArrangement = Arrangement.spacedBy(sectionSpacing)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "J'aime",
                                color = Color.White,
                                fontSize = (noteLabelSize.value * 1.2f).sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            ColorSection(
                                color = Color(0xFF4CAF50),
                                notes = likedNotes,
                                useHappyImage = true,
                                imageSize = choiceImageSize,
                                onNoteClick = { note ->
                                    likedNotes = likedNotes - note
                                    notifySelectionChanged()
                                },
                                isDragOver = isOverLiked,
                                onBoundsChanged = { likedBounds = it },
                                hiddenNote = if (dragSource == DragSource.LIKED) draggedNote else null,
                                onDragStart = { note, startPosition ->
                                    draggedNote = note
                                    dragSource = DragSource.LIKED
                                    dragPosition = startPosition
                                },
                                onDrag = { dragAmount -> dragPosition += dragAmount },
                                onDragEnd = { endDrag() },
                                onDragCancel = {
                                    draggedNote = null
                                    dragSource = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Je n'aime pas",
                                color = Color.White,
                                fontSize = (noteLabelSize.value * 1.2f).sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            ColorSection(
                                color = Color(0xFFE53935),
                                notes = dislikedNotes,
                                useHappyImage = false,
                                imageSize = choiceImageSize,
                                onNoteClick = { note ->
                                    dislikedNotes = dislikedNotes - note
                                    notifySelectionChanged()
                                },
                                isDragOver = isOverDisliked,
                                onBoundsChanged = { dislikedBounds = it },
                                hiddenNote = if (dragSource == DragSource.DISLIKED) draggedNote else null,
                                onDragStart = { note, startPosition ->
                                    draggedNote = note
                                    dragSource = DragSource.DISLIKED
                                    dragPosition = startPosition
                                },
                                onDrag = { dragAmount -> dragPosition += dragAmount },
                                onDragEnd = { endDrag() },
                                onDragCancel = {
                                    draggedNote = null
                                    dragSource = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
        }

        draggedNote?.let { note ->
            var overlayOrigin by remember { mutableStateOf(Offset.Zero) }
            val ghostSizePx = with(androidx.compose.ui.platform.LocalDensity.current) { choiceImageSize.toPx() }
            val ghostRemoteUrl = when (dragSource) {
                DragSource.LIKED -> note.happyImageUrl
                DragSource.DISLIKED -> note.sadImageUrl
                else -> note.imageUrl
            }
            val ghostFallbackRes = when (dragSource) {
                DragSource.LIKED -> note.fallback?.happyImageRes
                DragSource.DISLIKED -> note.fallback?.sadImageRes
                else -> note.fallback?.imageRes
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { overlayOrigin = it.positionInRoot() }
            ) {
                NoteImage(
                    remoteUrl = ghostRemoteUrl,
                    fallbackRes = ghostFallbackRes,
                    contentDescription = note.name,
                    modifier = Modifier
                        .offset {
                            val topLeft = dragPosition - overlayOrigin -
                                Offset(ghostSizePx / 2f, ghostSizePx / 2f)
                            IntOffset(topLeft.x.roundToInt(), topLeft.y.roundToInt())
                        }
                        .size(choiceImageSize)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }

        pendingSort?.let { pending ->
            val note = pending.note
            val fallback = note.fallback
            val remoteAnimUrl = if (pending.liked) note.happyAnimUrl else note.sadAnimUrl
            val fallbackAnimRes = if (pending.liked) fallback?.happyAnimRes else fallback?.sadAnimRes
            val remoteImageUrl = if (pending.liked) note.happyImageUrl else note.sadImageUrl
            val fallbackImageRes = if (pending.liked) fallback?.happyImageRes else fallback?.sadImageRes

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val fallbackDrawableRes = fallbackImageRes ?: Res.drawable.ic_note_placeholder
                when {
                    remoteAnimUrl != null -> AsyncImage(
                        model = remoteAnimUrl,
                        contentDescription = note.name,
                        contentScale = ContentScale.Fit,
                        error = painterResource(fallbackDrawableRes),
                        modifier = Modifier.size(animationSize)
                    )
                    fallbackAnimRes != null -> Image(
                        painter = painterResource(fallbackAnimRes),
                        contentDescription = note.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(animationSize)
                    )
                    remoteImageUrl != null -> AsyncImage(
                        model = remoteImageUrl,
                        contentDescription = note.name,
                        contentScale = ContentScale.Fit,
                        error = painterResource(fallbackDrawableRes),
                        modifier = Modifier.size(animationSize)
                    )
                    else -> Image(
                        painter = painterResource(fallbackImageRes ?: Res.drawable.ic_note_placeholder),
                        contentDescription = note.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(animationSize)
                    )
                }
            }
        }

        NavBackButton(
            onClick = onBackClick,
            size = buttonSize,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(screenPadding)
        )

        if (selectedNotes.size >= 4) {
            NavForwardButton(
                onClick = onNextClick,
                size = buttonSize,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(screenPadding)
            )
        }
    }
}

@Composable
private fun NoteImage(
    remoteUrl: String?,
    fallbackRes: DrawableResource?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val resolvedFallback = fallbackRes ?: Res.drawable.ic_note_placeholder
    if (remoteUrl != null) {
        AsyncImage(
            model = remoteUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(resolvedFallback),
            error = painterResource(resolvedFallback),
            fallback = painterResource(resolvedFallback),
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(resolvedFallback),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

@Composable
private fun PerfumeButtonGrid(
    notes: List<DisplayNote>,
    onNoteClick: (DisplayNote) -> Unit,
    onDragStart: (DisplayNote, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    noteLabelSize: TextUnit,
    compact: Boolean,
    maxImageSize: Dp,
    draggedNote: DisplayNote?,
    modifier: Modifier = Modifier
) {
    val gridSpacing = if (compact) 8.dp else 12.dp
    val verticalPadding = if (compact) 12.dp else 28.dp
    val horizontalPadding = if (compact) 8.dp else 16.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.85f))
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(2) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gridSpacing)
            ) {
                repeat(3) { columnIndex ->
                    val index = rowIndex * 3 + columnIndex
                    val note = notes.getOrNull(index)
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (note != null) {
                            val isBeingDragged = draggedNote == note
                            var itemPosition by remember(note) { mutableStateOf(Offset.Zero) }
                            Box(
                                modifier = Modifier
                                    .widthIn(max = maxImageSize)
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .onGloballyPositioned { itemPosition = it.positionInRoot() }
                                    .pointerInput(note) {
                                        detectDragGestures(
                                            onDragStart = { startOffset ->
                                                onDragStart(note, itemPosition + startOffset)
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                onDrag(dragAmount)
                                            },
                                            onDragEnd = {
                                                onDragEnd()
                                            },
                                            onDragCancel = {
                                                onDragCancel()
                                            }
                                        )
                                    }
                            ) {
                                if (!isBeingDragged) {
                                    NoteImage(
                                        remoteUrl = note.imageUrl,
                                        fallbackRes = note.fallback?.imageRes,
                                        contentDescription = note.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .clickable { onNoteClick(note) }
                                    )
                                }
                            }
                            Text(
                                text = note.name,
                                color = Color(0xFF2B2B2B),
                                fontSize = noteLabelSize,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .widthIn(max = maxImageSize)
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSection(
    color: Color,
    notes: List<DisplayNote>,
    useHappyImage: Boolean,
    imageSize: Dp,
    onNoteClick: (DisplayNote) -> Unit,
    isDragOver: Boolean,
    onBoundsChanged: (Rect) -> Unit,
    hiddenNote: DisplayNote?,
    onDragStart: (DisplayNote, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .onGloballyPositioned { onBoundsChanged(it.boundsInRoot()) }
            .clip(RoundedCornerShape(24.dp))
            .background(color)
            // Bordure blanche, plus épaisse quand on survole la zone avec une note
            .border(if (isDragOver) 5.dp else 3.dp, Color.White, RoundedCornerShape(24.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        notes.forEach { note ->
            val remoteUrl = if (useHappyImage) note.happyImageUrl else note.sadImageUrl
            val fallbackRes = if (useHappyImage) note.fallback?.happyImageRes else note.fallback?.sadImageRes
            var itemPosition by remember(note) { mutableStateOf(Offset.Zero) }
            Box(
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(12.dp))
                    .onGloballyPositioned { itemPosition = it.positionInRoot() }
                    .pointerInput(note) {
                        detectDragGestures(
                            onDragStart = { startOffset ->
                                onDragStart(note, itemPosition + startOffset)
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                onDrag(dragAmount)
                            },
                            onDragEnd = { onDragEnd() },
                            onDragCancel = { onDragCancel() }
                        )
                    }
            ) {
                if (note != hiddenNote) {
                    NoteImage(
                        remoteUrl = remoteUrl,
                        fallbackRes = fallbackRes,
                        contentDescription = note.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onNoteClick(note) }
                    )
                }
            }
        }
    }
}
