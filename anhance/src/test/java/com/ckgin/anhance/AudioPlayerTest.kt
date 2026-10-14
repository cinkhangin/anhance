package com.ckgin.anhance

import android.content.Context
import android.media.MediaPlayer
import android.os.Looper
import androidx.core.net.toUri
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowMediaPlayer
import org.robolectric.shadows.ShadowMediaPlayer.InvalidStateBehavior
import org.robolectric.shadows.ShadowMediaPlayer.MediaInfo
import org.robolectric.shadows.ShadowMediaPlayer.State
import org.robolectric.shadows.util.DataSource
import java.io.File
import java.time.Duration

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28, 35])
class AudioPlayerTest {
    private lateinit var context: Context
    private val players = mutableListOf<MediaPlayer>()

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        AudioPlayer.clear()
        ShadowMediaPlayer.setMediaInfoProvider { MediaInfo(10000, 100) }
        ShadowMediaPlayer.setCreateListener { player, shadow ->
            players.add(player)
            shadow.setInvalidStateBehavior(InvalidStateBehavior.ASSERT)
        }
    }

    @After
    fun tearDown() {
        AudioPlayer.clear()
    }

    @Test
    fun urlsUseTheirActualSourceAndWaitForPreparation() {
        val url = "https://example.com/audio.mp3"
        AudioPlayer.load(context, url)
        val player = players.last()
        assertEquals(DataSource.toDataSource(url), shadowOf(player).dataSource)
        AudioPlayer.play()
        assertFalse(player.isPlaying)
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofMillis(100))
        assertTrue(player.isPlaying)
    }

    @Test
    fun httpUrlsAreAlsoRecognized() {
        val url = "http://example.com/audio.mp3"
        AudioPlayer.load(context, url)
        assertEquals(DataSource.toDataSource(url), shadowOf(players.last()).dataSource)
    }

    @Test
    fun stopDuringPreparationCancelsPendingPlayback() {
        AudioPlayer.load(context, "https://example.com/audio.mp3")
        AudioPlayer.play()
        AudioPlayer.stop()
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofMillis(100))
        assertFalse(players.last().isPlaying)
        AudioPlayer.play()
        assertTrue(players.last().isPlaying)
    }

    @Test
    fun preparedFileCanPlayPauseStopAndReplay() {
        AudioPlayer.load(context, File(context.cacheDir, "audio.mp3"))
        val player = players.last()
        AudioPlayer.pause()
        AudioPlayer.play()
        assertTrue(player.isPlaying)
        AudioPlayer.play()
        assertTrue(player.isPlaying)
        AudioPlayer.pause()
        assertFalse(player.isPlaying)
        AudioPlayer.play()
        assertTrue(player.isPlaying)
        AudioPlayer.stop()
        AudioPlayer.stop()
        assertFalse(player.isPlaying)
        AudioPlayer.play()
        assertTrue(players.last().isPlaying)
    }

    @Test
    fun replacementRemovalAndClearReleaseOwnedPlayers() {
        AudioPlayer.load(context, File(context.cacheDir, "one.mp3"), "first")
        val replaced = players.last()
        AudioPlayer.load(context, File(context.cacheDir, "two.mp3"), "first")
        assertEquals(State.END, shadowOf(replaced).state)
        val removed = players.last()
        AudioPlayer.remove("first")
        assertEquals(State.END, shadowOf(removed).state)
        AudioPlayer.load(context, File(context.cacheDir, "three.mp3"), "third")
        val cleared = players.last()
        AudioPlayer.clear()
        assertEquals(State.END, shadowOf(cleared).state)
    }

    @Test
    fun keysAndCompletionCallbacksAreIndependent() {
        AudioPlayer.load(context, File(context.cacheDir, "one.mp3"), "first")
        val first = players.last()
        AudioPlayer.load(context, File(context.cacheDir, "two.mp3"), "second")
        val second = players.last()
        var completions = 0
        AudioPlayer.play("first") { completions++ }
        assertTrue(first.isPlaying)
        assertFalse(second.isPlaying)
        shadowOf(first).invokeCompletionListener()
        assertEquals(1, completions)
        AudioPlayer.play("missing")
        AudioPlayer.pause("missing")
        AudioPlayer.stop("missing")
        AudioPlayer.remove("missing")
    }
}
