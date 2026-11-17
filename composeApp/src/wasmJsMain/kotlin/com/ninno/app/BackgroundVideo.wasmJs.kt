package com.ninno.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * Le canvas Compose ne sait pas dessiner une vidéo : on place un <video> HTML plein écran
 * derrière le canvas (transparent), et Compose dessine son contenu par-dessus.
 */
@Composable
actual fun BackgroundVideo(url: String): Boolean {
    DisposableEffect(url) {
        showBackgroundVideo(url)
        onDispose { hideBackgroundVideo() }
    }
    return true
}

private fun showBackgroundVideo(url: String): Unit = js(
    """
    (function(url) {
        var video = document.getElementById("ninno-bg-video");
        if (!video) {
            video = document.createElement("video");
            video.id = "ninno-bg-video";
            video.muted = true;
            video.loop = true;
            video.autoplay = true;
            video.playsInline = true;
            video.setAttribute("muted", "");
            video.setAttribute("playsinline", "");
            video.setAttribute("aria-hidden", "true");
            video.style.cssText = "position:fixed;inset:0;width:100%;height:100%;object-fit:cover;z-index:-1;pointer-events:none;";
            document.body.insertBefore(video, document.body.firstChild);
        }
        if (video.getAttribute("src") !== url) video.src = url;
        video.style.display = "block";
        var p = video.play();
        if (p && p.catch) p.catch(function() {});
    })(url)
    """
)

private fun hideBackgroundVideo(): Unit = js(
    """
    (function() {
        var video = document.getElementById("ninno-bg-video");
        if (video) { video.pause(); video.style.display = "none"; }
    })()
    """
)
