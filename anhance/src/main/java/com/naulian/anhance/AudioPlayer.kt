@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.media.MediaPlayer

fun Context.playAudio(resId: Int) = AudioPlayer.play(this, resId)
fun Context.playAudio(fileName: String) = AudioPlayer.play(this, fileName)

object AudioPlayer {
    private val keyMap = hashMapOf<String, AnMediaPlayer>()

    private const val DEFAULT_PLAYER = "default_player"

    fun load(context: Context, resId: Int, key: String = DEFAULT_PLAYER) {
        AnMediaPlayer(context).apply {
            createFromRes(resId)
            keyMap[key] = this
        }
    }

    fun load(context: Context, fileName: String, key: String = DEFAULT_PLAYER) {
        AnMediaPlayer(context).apply {
            createFromAsset(fileName)
            keyMap[key] = this
        }
    }

    fun play(context: Context, resId: Int, onComplete: () -> Unit = {}) {
        load(context = context, resId = resId, key = DEFAULT_PLAYER)
        play(DEFAULT_PLAYER, onComplete)
    }

    fun play(context: Context, fileName: String, onComplete: () -> Unit = {}) {
        load(context = context, fileName = fileName, key = DEFAULT_PLAYER)
        play(DEFAULT_PLAYER, onComplete)
    }

    fun play(key: String = DEFAULT_PLAYER, onComplete: () -> Unit = {}) {
        keyMap[key]?.apply {
            play()
            setOnCompletionListener(onComplete)
        }
    }

    fun stop(key: String = DEFAULT_PLAYER){
        keyMap[key]?.stop()
    }

    fun remove(key: String = DEFAULT_PLAYER) {
        keyMap.remove(key)
    }

    //clear all player
    fun clear() {
        keyMap.clear()
    }
}

private class AnMediaPlayer(val context: Context) {
    private var player: MediaPlayer? = null

    fun createFromRes(res: Int): MediaPlayer {
        player?.release()
        return MediaPlayer.create(context, res).also { player = it }
    }

    fun setOnCompletionListener(action: () -> Unit) {
        player?.setOnCompletionListener { action() }
    }

    fun createFromAsset(fileName: String): MediaPlayer {
        return MediaPlayer().also {
            val file = context.assets.openFd(fileName)
            it.setDataSource(file.fileDescriptor, file.startOffset, file.length)
            file.close()
            it.prepare()
            player = it
        }
    }

    fun play() {
        if (player?.isPlaying == true) {
            player?.stop()
        }

        player?.start()
    }

    fun stop() {
        player?.stop()
    }

    fun pause(){
        player?.pause()
    }
}