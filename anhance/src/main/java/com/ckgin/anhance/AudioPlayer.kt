@file:Suppress("unused")

package com.ckgin.anhance

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File
import java.io.IOException

fun Context.playAudio(resId: Int) = AudioPlayer.play(this, resId)
fun Context.playAudio(fileName: String) = AudioPlayer.play(this, fileName)

object AudioPlayer {
    private val keyMap = hashMapOf<String, AnMediaPlayer>()
    private const val DEFAULT_PLAYER = "default_player"

    fun load(context: Context, resId: Int, key: String = DEFAULT_PLAYER) {
        replace(key, AnMediaPlayer(context.applicationContext).apply { createFromRes(resId) })
    }

    fun load(context: Context, file: File, key: String = DEFAULT_PLAYER) {
        replace(key, AnMediaPlayer(context.applicationContext).apply { createFromFile(file) })
    }

    fun load(context: Context, fileName: String, key: String = DEFAULT_PLAYER) {
        val player = AnMediaPlayer(context.applicationContext).apply {
            if (fileName.startsWith("https://", ignoreCase = true) ||
                fileName.startsWith("http://", ignoreCase = true)
            ) {
                createFromUrl(fileName)
            } else {
                createFromAsset(fileName)
            }
        }
        replace(key, player)
    }

    private fun replace(key: String, player: AnMediaPlayer) {
        keyMap.put(key, player)?.release()
    }

    fun play(context: Context, resId: Int, onComplete: () -> Unit = {}) {
        load(context, resId)
        play(onComplete = onComplete)
    }

    fun play(context: Context, fileName: String, onComplete: () -> Unit = {}) {
        load(context, fileName)
        play(onComplete = onComplete)
    }

    fun play(key: String = DEFAULT_PLAYER, onComplete: () -> Unit = {}) {
        keyMap[key]?.apply {
            setOnCompletionListener(onComplete)
            play()
        }
    }

    fun stop(key: String = DEFAULT_PLAYER) {
        keyMap[key]?.stop()
    }

    fun remove(key: String = DEFAULT_PLAYER) {
        keyMap.remove(key)?.release()
    }

    fun pause(key: String = DEFAULT_PLAYER) {
        keyMap[key]?.pause()
    }

    fun clear() {
        keyMap.values.forEach { it.release() }
        keyMap.clear()
    }
}

private class AnMediaPlayer(private val context: Context) {
    private var player: MediaPlayer? = null
    private var prepared = false
    private var playWhenPrepared = false

    fun createFromRes(resId: Int) {
        player = MediaPlayer.create(context, resId) ?: throw IOException("Cannot open audio resource")
        prepared = true
        listenForErrors()
    }

    fun createFromFile(file: File) {
        player = MediaPlayer.create(context, file.toUri()) ?: throw IOException("Cannot open audio file")
        prepared = true
        listenForErrors()
    }

    fun createFromUrl(url: String) {
        create { media ->
            media.setOnPreparedListener {
                prepared = true
                if (playWhenPrepared) {
                    playWhenPrepared = false
                    it.start()
                }
            }
            media.setDataSource(url)
            media.prepareAsync()
        }
    }

    fun createFromAsset(fileName: String) {
        create { media ->
            context.assets.openFd(fileName).use { asset ->
                media.setDataSource(asset.fileDescriptor, asset.startOffset, asset.length)
            }
            media.prepare()
            prepared = true
        }
    }

    private fun create(setup: (MediaPlayer) -> Unit) {
        val media = MediaPlayer()
        player = media
        listenForErrors()
        try {
            setup(media)
        } catch (exception: Exception) {
            release()
            throw exception
        }
    }

    private fun listenForErrors() {
        player?.setOnErrorListener { _, _, _ ->
            prepared = false
            playWhenPrepared = false
            false
        }
    }

    fun setOnCompletionListener(action: () -> Unit) {
        player?.setOnCompletionListener { action() }
    }

    fun play() {
        val media = player ?: return
        if (!prepared) {
            playWhenPrepared = true
            return
        }
        if (media.isPlaying) media.seekTo(0)
        media.start()
    }

    fun stop() {
        playWhenPrepared = false
        if (!prepared) return
        player?.let {
            if (it.isPlaying) it.pause()
            it.seekTo(0)
        }
    }

    fun pause() {
        playWhenPrepared = false
        if (prepared && player?.isPlaying == true) player?.pause()
    }

    fun release() {
        playWhenPrepared = false
        prepared = false
        player?.release()
        player = null
    }
}
