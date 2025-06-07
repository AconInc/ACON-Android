package com.acon.acon.feature.signin.utils

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import com.acon.acon.feature.signin.R

class SplashAudioManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playSplashSoundIfAllowed(): MediaPlayer? {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.isMusicActive) return null

        val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT).build()
        val result = audioManager.requestAudioFocus(focusRequest)
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) return null

        mediaPlayer = MediaPlayer.create(context, R.raw.splash_lottie).apply {
            setVolume(0.8f, 0.8f)
            setOnCompletionListener {
                release()
            }
            start()
        }
        return mediaPlayer
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}