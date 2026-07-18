package com.ninno.app

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.ninno.app.ui.DeviceSize
import com.ninno.app.ui.NavBackButton
import com.ninno.app.ui.NavForwardButton
import com.ninno.app.ui.FamilyDayLogo
import com.ninno.app.ui.ScreenBackground
import com.ninno.app.ui.navButtonSizeFor
import com.ninno.app.ui.logoWidthFor
import com.ninno.app.ui.rememberDeviceSize
import com.ninno.app.ui.rememberIsLandscape
import com.ninno.app.ui.rememberUseStackedLayout
import com.ninno.app.ui.screenPaddingFor
import kotlinx.coroutines.delay

data class PerfumeNote(
    val name: String,
    val imageRes: Int,
    val happyImageRes: Int,
    val sadImageRes: Int,
    val happyAnimRes: Int? = null,
    val sadAnimRes: Int? = null
)

val topNotes = listOf(
    PerfumeNote("Pomme", R.drawable.note_pomme, R.drawable.note_pomme_happy, R.drawable.note_pomme_sad, R.drawable.anim_pomme_happy, R.drawable.anim_pomme_sad),
    PerfumeNote("Orange", R.drawable.note_orange, R.drawable.note_orange_happy, R.drawable.note_orange_sad, R.drawable.anim_orange_happy, R.drawable.anim_orange_sad),
    PerfumeNote("Citron", R.drawable.note_citron, R.drawable.note_citron_happy, R.drawable.note_citron_sad, R.drawable.anim_citron_happy, R.drawable.anim_citron_sad),
    PerfumeNote("Bambou", R.drawable.note_bambou, R.drawable.note_bambou_happy, R.drawable.note_bambou_sad, R.drawable.anim_bambou_happy, R.drawable.anim_bambou_sad),
    PerfumeNote("Cola", R.drawable.note_cola, R.drawable.note_cola_happy, R.drawable.note_cola_sad, R.drawable.anim_cola_happy, R.drawable.anim_cola_sad),
    PerfumeNote("Herbe", R.drawable.note_herbe, R.drawable.note_herbe_happy, R.drawable.note_herbe_sad, R.drawable.anim_herbe_happy, R.drawable.anim_herbe_sad)
)

val heartNotes = listOf(
    PerfumeNote("Miel", R.drawable.note_miel, R.drawable.note_miel_happy, R.drawable.note_miel_sad, R.drawable.anim_miel_happy, R.drawable.anim_miel_sad),
    PerfumeNote("Mangue", R.drawable.note_mangue, R.drawable.note_mangue_happy, R.drawable.note_mangue_sad, R.drawable.anim_mangue_happy, R.drawable.anim_mangue_sad),
    PerfumeNote("Rose", R.drawable.note_rose, R.drawable.note_rose_happy, R.drawable.note_rose_sad, R.drawable.anim_rose_happy, R.drawable.anim_rose_sad),
    PerfumeNote("Muguet", R.drawable.note_muguet, R.drawable.note_muguet_happy, R.drawable.note_muguet_sad, null, R.drawable.anim_muguet_sad),
    PerfumeNote("Jasmin", R.drawable.note_jasmin, R.drawable.note_jasmin_happy, R.drawable.note_jasmin_sad, R.drawable.anim_jasmin_happy, R.drawable.anim_jasmin_sad),
    PerfumeNote("Cocktail", R.drawable.note_cocktail, R.drawable.note_cocktail_happy, R.drawable.note_cocktail_sad, R.drawable.anim_cocktail_happy, R.drawable.anim_cocktail_sad)
)

val baseNotes = listOf(
    PerfumeNote("Caramel", R.drawable.note_caramel, R.drawable.note_caramel_happy, R.drawable.note_caramel_sad, R.drawable.anim_caramel_happy, R.drawable.anim_caramel_sad),
    PerfumeNote("Vanille", R.drawable.note_vanille, R.drawable.note_vanille_happy, R.drawable.note_vanille_sad, null, R.drawable.anim_vanille_sad),
    PerfumeNote("Frangipane", R.drawable.note_frangipane, R.drawable.note_frangipane_happy, R.drawable.note_frangipane_sad, R.drawable.anim_frangipane_happy, R.drawable.anim_frangipane_sad),
    PerfumeNote("Praliné", R.drawable.note_praline, R.drawable.note_praline_happy, R.drawable.note_praline_sad, R.drawable.anim_praline_happy, R.drawable.anim_praline_sad),
    PerfumeNote("Barbe à papa", R.drawable.note_barbepapa, R.drawable.note_barbepapa_happy, R.drawable.note_barbepapa_sad, R.drawable.anim_barbepapa_happy, R.drawable.anim_barbepapa_sad),
    PerfumeNote("Ambre", R.drawable.note_ambre, R.drawable.note_ambre_happy, R.drawable.note_ambre_sad, R.drawable.anim_ambre_happy, R.drawable.anim_ambre_sad)
)

private data class PendingSort(val note: DisplayNote, val liked: Boolean)

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

    val titleSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 20.sp else 24.sp
        DeviceSize.MEDIUM -> 30.sp
        DeviceSize.EXPANDED -> 38.sp
    }
    val logoWidth = logoWidthFor(deviceSize, isLandscape)
    val sectionSpacing = when (deviceSize) {
        DeviceSize.COMPACT -> 16.dp
        DeviceSize.MEDIUM -> 20.dp
        DeviceSize.EXPANDED -> 28.dp
    }
    val gridPadding = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 8.dp else 12.dp
        DeviceSize.MEDIUM -> 16.dp
        DeviceSize.EXPANDED -> 20.dp
    }
    val noteLabelSize = if (useStackedLayout) 12.sp else 14.sp
    val configuration = LocalConfiguration.current
    val animationSize = minOf(
        280.dp,
        (configuration.screenWidthDp * 0.5f).dp,
        (configuration.screenHeightDp * 0.7f).dp
    )

    val maxGridImageSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 90.dp else 70.dp
        DeviceSize.MEDIUM -> 110.dp
        DeviceSize.EXPANDED -> 130.dp
    }

    val choiceImageSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 75.dp else 55.dp
        DeviceSize.MEDIUM -> 90.dp
        DeviceSize.EXPANDED -> 110.dp
    }

    var likedNotes by remember(notes) { mutableStateOf(listOf<DisplayNote>()) }
    var dislikedNotes by remember(notes) { mutableStateOf(listOf<DisplayNote>()) }
    var pendingSort by remember(notes) { mutableStateOf<PendingSort?>(null) }
    val selectedNotes = likedNotes + dislikedNotes
    val pendingNote = pendingSort?.note
    val remainingNotes = notes.filterNot { it in selectedNotes || it == pendingNote }

    val context = LocalContext.current
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }
    val happySoundId = remember { soundPool.load(context, R.raw.sound_happy, 1) }
    val sadSoundId = remember { soundPool.load(context, R.raw.sound_sad, 1) }
    DisposableEffect(Unit) {
        onDispose { soundPool.release() }
    }

    fun notifySelectionChanged() {
        onSelectionChanged(likedNotes.map { it.name }, dislikedNotes.map { it.name })
    }

    pendingSort?.let { pending ->
        LaunchedEffect(pending.note) {
            soundPool.play(if (pending.liked) happySoundId else sadSoundId, 1f, 1f, 1, 0, 1f)
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

    ScreenBackground(backgroundRes = R.drawable.bg_home2, backgroundUrl = backgroundUrl, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenPadding, vertical = screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = sectionSpacing),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavBackButton(
                    onClick = onBackClick,
                    size = buttonSize
                )

                FamilyDayLogo(width = logoWidth, logoUrl = logoUrl)

                if (selectedNotes.size >= 4) {
                    NavForwardButton(
                        onClick = onNextClick,
                        size = buttonSize
                    )
                } else {
                    Spacer(modifier = Modifier.size(buttonSize))
                }
            }

            Text(
                text = title,
                color = Color.White,
                fontSize = titleSize,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
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
                        noteLabelSize = noteLabelSize,
                        compact = true,
                        maxImageSize = maxGridImageSize,
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
                        ColorSection(
                            color = Color(0xFF4CAF50).copy(alpha = 0.75f),
                            notes = likedNotes,
                            useHappyImage = true,
                            imageSize = choiceImageSize,
                            onNoteClick = { note ->
                                likedNotes = likedNotes - note
                                notifySelectionChanged()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        ColorSection(
                            color = Color(0xFFE53935).copy(alpha = 0.75f),
                            notes = dislikedNotes,
                            useHappyImage = false,
                            imageSize = choiceImageSize,
                            onNoteClick = { note ->
                                dislikedNotes = dislikedNotes - note
                                notifySelectionChanged()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
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
                        noteLabelSize = noteLabelSize,
                        compact = false,
                        maxImageSize = maxGridImageSize,
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
                        ColorSection(
                            color = Color(0xFF4CAF50).copy(alpha = 0.75f),
                            notes = likedNotes,
                            useHappyImage = true,
                            imageSize = choiceImageSize,
                            onNoteClick = { note ->
                                likedNotes = likedNotes - note
                                notifySelectionChanged()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        ColorSection(
                            color = Color(0xFFE53935).copy(alpha = 0.75f),
                            notes = dislikedNotes,
                            useHappyImage = false,
                            imageSize = choiceImageSize,
                            onNoteClick = { note ->
                                dislikedNotes = dislikedNotes - note
                                notifySelectionChanged()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                }
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
                val imageLoader = remember(context) {
                    ImageLoader.Builder(context)
                        .components { add(ImageDecoderDecoder.Factory()) }
                        .build()
                }
                val fallbackDrawableRes = fallbackImageRes ?: R.drawable.ic_note_placeholder
                when {
                    remoteAnimUrl != null -> AsyncImage(
                        model = ImageRequest.Builder(context).data(remoteAnimUrl).build(),
                        contentDescription = note.name,
                        imageLoader = imageLoader,
                        contentScale = ContentScale.Fit,
                        error = painterResource(id = fallbackDrawableRes),
                        modifier = Modifier.size(animationSize)
                    )
                    fallbackAnimRes != null -> AsyncImage(
                        model = fallbackAnimRes,
                        contentDescription = note.name,
                        imageLoader = imageLoader,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(animationSize)
                    )
                    remoteImageUrl != null -> AsyncImage(
                        model = ImageRequest.Builder(context).data(remoteImageUrl).build(),
                        contentDescription = note.name,
                        contentScale = ContentScale.Fit,
                        error = painterResource(id = fallbackDrawableRes),
                        modifier = Modifier.size(animationSize)
                    )
                    else -> Image(
                        painter = painterResource(id = fallbackImageRes ?: R.drawable.ic_note_placeholder),
                        contentDescription = note.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(animationSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteImage(
    remoteUrl: String?,
    fallbackRes: Int?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val resolvedFallback = fallbackRes ?: R.drawable.ic_note_placeholder
    if (remoteUrl != null) {
        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context).data(remoteUrl).crossfade(true).build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = resolvedFallback),
            error = painterResource(id = resolvedFallback),
            fallback = painterResource(id = resolvedFallback),
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = resolvedFallback),
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
    noteLabelSize: TextUnit,
    compact: Boolean,
    maxImageSize: Dp,
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
                            NoteImage(
                                remoteUrl = note.imageUrl,
                                fallbackRes = note.fallback?.imageRes,
                                contentDescription = note.name,
                                modifier = Modifier
                                    .widthIn(max = maxImageSize)
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onNoteClick(note) }
                            )
                            Text(
                                text = note.name,
                                color = Color(0xFF2B2B2B),
                                fontSize = noteLabelSize,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif,
                                textAlign = TextAlign.Center,
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(color)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        notes.forEach { note ->
            val remoteUrl = if (useHappyImage) note.happyImageUrl else note.sadImageUrl
            val fallbackRes = if (useHappyImage) note.fallback?.happyImageRes else note.fallback?.sadImageRes
            NoteImage(
                remoteUrl = remoteUrl,
                fallbackRes = fallbackRes,
                contentDescription = note.name,
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNoteClick(note) }
            )
        }
    }
}
