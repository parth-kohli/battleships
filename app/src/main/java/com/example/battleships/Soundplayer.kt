package com.example.battleships
import android.content.Context
import android.media.MediaPlayer
object Soundplayer {
    private var mediaPlayer: MediaPlayer? = null

    fun fireball(context: Context) {

        mediaPlayer = MediaPlayer.create(context, R.raw.fireball)
        mediaPlayer?.start()
    }
    fun pop(context: Context, sounds: Boolean) {
        if (sounds) {
            mediaPlayer = MediaPlayer.create(context, R.raw.pop)
            mediaPlayer?.start()
        }

    }
    fun click(context: Context, sounds: Boolean){
        if (sounds) {
            mediaPlayer = MediaPlayer.create(context, R.raw.click)
            mediaPlayer?.start()
        }
    }
    fun rotate(context: Context, sounds: Boolean){
        if (sounds) {
            mediaPlayer = MediaPlayer.create(context, R.raw.rotate)
            mediaPlayer?.start()
        }
    }
    fun error(context: Context, sounds: Boolean){
        if (sounds) {
            mediaPlayer = MediaPlayer.create(context, R.raw.error)
            mediaPlayer?.start()
        }
    }

}