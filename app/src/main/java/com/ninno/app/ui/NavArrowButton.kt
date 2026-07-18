package com.ninno.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun navButtonSizeFor(deviceSize: DeviceSize): Dp = when (deviceSize) {
    DeviceSize.COMPACT -> 48.dp
    DeviceSize.MEDIUM -> 64.dp
    DeviceSize.EXPANDED -> 72.dp
}

@Composable
fun NavForwardButton(onClick: () -> Unit, size: Dp, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Suivant",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun NavBackButton(onClick: () -> Unit, size: Dp, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Précédent",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun NavHomeButton(onClick: () -> Unit, size: Dp, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "Accueil",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
