@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

fun Context.playAudio(resId: Int) = AudioPlayer.play(this, resId)
fun Context.playAudio(fileName: String) = AudioPlayer.play(this, fileName)

object AudioPlayer {
    private val keyMap = hashMapOf<String, AnMediaPlayer>()

    private const val DEFAULT_PLAYER = "default_player"

    private val defaultPlayer get() = keyMap[DEFAULT_PLAYER]

    fun load(context: Context, resId: Int, key: String = DEFAULT_PLAYER) {
        AnMediaPlayer(context).apply {
            createFromRes(resId)
            keyMap[key] = this
        }
    }

    fun load(context: Context, file: File, key: String = DEFAULT_PLAYER) {
        AnMediaPlayer(context).apply {
            createFromFile(file)
            keyMap[key] = this
        }
    }

    fun load(context: Context, fileName: String, key: String = DEFAULT_PLAYER) {
        if(fileName.startsWith("https")){
            AnMediaPlayer(context).apply {
                createFromUrl(url)
                keyMap[key] = this
            }
            return
        }

        AnMediaPlayer(context).apply {
            createFromAsset(fileName)
            keyMap[key] = this
        }
    }

    fun play(context: Context, resId: Int, onComplete: () -> Unit = {}) {
        if (resId == defaultPlayer?.resId) {
            stop()
        }

        load(context = context, resId = resId)
        play(onComplete = onComplete)
    }

    fun play(context: Context, fileName: String, onComplete: () -> Unit = {}) {
        if (fileName == defaultPlayer?.fileName) {
            stop()
        }
        load(context = context, fileName = fileName)
        play(onComplete = onComplete)
    }

    fun play(key: String = DEFAULT_PLAYER, onComplete: () -> Unit = {}) {
        keyMap[key]?.let { player ->
            player.play()
            player.setOnCompletionListener(onComplete)
        }
    }

    fun stop(key: String = DEFAULT_PLAYER) {
        keyMap[key]?.stop()
    }

    fun remove(key: String = DEFAULT_PLAYER) {
        keyMap.remove(key)
    }

    fun pause(key: String = DEFAULT_PLAYER) {
        keyMap[key]?.pause()
    }

    //clear all player
    fun clear() {
        keyMap.clear()
    }
}

private class AnMediaPlayer(val context: Context) {
    private var player: MediaPlayer? = null

    var url = ""
        private set

    var file: File? = null
        private set

    var resId = 0
        private set

    var fileName = ""
        private set

    fun createFromRes(res: Int): MediaPlayer {
        resId = res
        player?.release()
        return MediaPlayer.create(context, res).also { player = it }
    }

    fun createFromUrl(url: String): MediaPlayer {
        this.url = url
        player?.release()
        return MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            player = this
        }
    }

    fun setOnCompletionListener(action: () -> Unit) {
        player?.setOnCompletionListener { action() }
    }

    fun createFromAsset(fileName: String): MediaPlayer {
        this.fileName = fileName
        return MediaPlayer().also {
            val file = context.assets.openFd(fileName)
            it.setDataSource(file.fileDescriptor, file.startOffset, file.length)
            file.close()
            it.prepare()
            player = it
        }
    }

    fun createFromFile(file: File): MediaPlayer {
        this.file = file
        return MediaPlayer.create(
            context,
            file.toUri()
        ).also { player = it }
    }

    fun play() {
        if (player?.isPlaying == true) {
            player?.stop()
        }

        player?.start()
    }

    fun stop() {
        player?.stop()

        reset()
    }

    fun pause() {
        player?.pause()
    }

    private fun reset() {
        player?.reset()
        if (resId != 0) {
            createFromRes(resId)
        }

        if (fileName != "") {
            createFromAsset(fileName)
        }
    }
}