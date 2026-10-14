package com.ckgin.anhance

import android.os.Build
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class FrameworkIntegrationTest {
    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    private val context get() = instrumentation.targetContext

    @Test
    fun privateDocumentSavesRenamesAndDeletesThroughRealResolver() {
        val file = File.createTempFile("document", ".txt", context.cacheDir)
        val document = Document(file.name, "hello မြန်မာ", file.toUri())
        try {
            DocumentCreator.saveFile(context, document).getOrThrow()
            assertEquals(document.content, file.readText())
            val renamed = DocumentCreator.renameFile(context, document, "${file.name}.renamed")
                .getOrThrow().getOrThrow()
            assertFalse(file.exists())
            val saved = document.copy(filename = "${file.name}.renamed", uri = renamed)
            try {
                assertEquals(document.content, context.contentResolver.openInputStream(renamed)!!
                    .bufferedReader().use { it.readText() })
            } finally {
                DocumentCreator.deleteFile(context, saved).getOrThrow()
            }
        } finally {
            file.delete()
            DocumentLoader.remove(document.filename)
            DocumentLoader.remove("${file.name}.renamed")
        }
    }

    @Test
    fun mediaStoreDocumentRetainsItsContentUriAcrossSaveAndRename() {
        assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        val name = "anhance-test-${System.nanoTime()}.txt"
        val uri = DocumentCreator.createFile(context, Document(name, "original")).getOrThrow()
        var document = DocumentLoader.load(name)
        try {
            assertEquals("content", uri.scheme)
            document = document.copy(content = "short")
            DocumentCreator.saveFile(context, document).getOrThrow()
            assertEquals("short", context.contentResolver.openInputStream(uri)!!
                .bufferedReader().use { it.readText() })
            val renamedName = "renamed-$name"
            val renamed = DocumentCreator.renameFile(context, document, renamedName).getOrThrow().getOrThrow()
            assertEquals(uri, renamed)
            document = document.copy(filename = renamedName)
        } finally {
            DocumentCreator.deleteFile(context, document).getOrThrow()
            DocumentLoader.remove(name)
        }
    }

    @Test
    fun localAudioCanRepeatPlayPauseStopAndReleaseWithoutStateErrors() {
        val file = File.createTempFile("audio", ".mp3", context.cacheDir)
        instrumentation.context.assets.open("audio.mp3").use { input ->
            file.outputStream().use { input.copyTo(it) }
        }
        try {
            instrumentation.runOnMainSync {
                try {
                    AudioPlayer.load(context, file, "test")
                    AudioPlayer.play("test")
                    AudioPlayer.play("test")
                    AudioPlayer.pause("test")
                    AudioPlayer.play("test")
                    AudioPlayer.stop("test")
                    AudioPlayer.play("test")
                } finally {
                    AudioPlayer.remove("test")
                }
            }
        } finally {
            file.delete()
        }
    }

    @Test
    fun missingAssetReturnsFailureWithoutCrashing() {
        assertEquals("", context.readStringAsset("missing.txt"))
        assertTrue(readStringAsset(context, "missing.txt").isFailure)
    }
}
