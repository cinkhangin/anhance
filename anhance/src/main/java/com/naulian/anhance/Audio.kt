@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.media.MediaPlayer

fun Context.playAudio(resId : Int) = AnAudio.playNow(this ,resId)
fun Context.playAudio(fileName: String) = AnAudio.playNow(this , fileName)

object AnAudio {
    private val keyMap = hashMapOf<String, AnMediaPlayer>()

    fun load(context: Context, resId: Int, key: String) {
         AnMediaPlayer(context).apply {
             loadResource(resId)
             keyMap[key] = this
         }
    }

    fun load(context: Context, fileName: String, key: String) {
        AnMediaPlayer(context).apply {
            loadAsset(fileName)
            keyMap[key] = this
        }
    }

    fun playNow(context: Context, resId: Int) {
        AnMediaPlayer(context).apply {
            loadResource(resId)
            play()
        }
    }

    fun playNow(context: Context, fileName: String) {
        AnMediaPlayer(context).apply {
            loadAsset(fileName)
            play()
        }
    }

    fun play(key: String){
        keyMap[key]?.apply { play() }
    }
}

private class AnMediaPlayer(val context: Context) {
    private var player: MediaPlayer? = null

    fun loadResource(res: Int) {
        player = MediaPlayer.create(context, res)
    }

    fun loadAsset(fileName: String) {
        player = MediaPlayer().also {
            val file = context.assets.openFd(fileName)
            it.setDataSource(file.fileDescriptor, file.startOffset, file.length)
            file.close()
            it.prepare()
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

}