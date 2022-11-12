@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.media.MediaPlayer

object AnAudio {

    private var player: MediaPlayer? = null

    fun load(context: Context, res: Int) {
        player = MediaPlayer.create(context, res)
    }

    fun loadAndPlay(context: Context , res : Int){
        load(context, res)
        play()
    }

    fun play() {
        player?.start()
    }

    fun stop() {
        player?.stop()
    }

    fun playAssets(context: Context, name : String) {
        try {
            player?.apply {
                if(isPlaying){
                    stop()
                    release()
                }
            }

            player = MediaPlayer()
            player?.apply {
                val file = context.assets.openFd(name)
                setDataSource(file.fileDescriptor , file.startOffset , file.length)
                file.close()

                prepare()
                start()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
}