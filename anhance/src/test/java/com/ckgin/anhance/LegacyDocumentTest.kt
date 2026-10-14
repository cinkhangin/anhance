package com.ckgin.anhance

import android.content.Context
import android.os.Environment
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
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [26])
class LegacyDocumentTest {
    private lateinit var context: Context
    private lateinit var directory: File

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        directory.mkdirs()
        DocumentLoader.files.value.forEach { DocumentLoader.remove(it.filename) }
    }

    @After
    fun tearDown() {
        directory.listFiles()?.forEach { it.delete() }
        DocumentLoader.files.value.forEach { DocumentLoader.remove(it.filename) }
    }

    @Test
    fun creationOnMinimumSdkUsesAFileUriAndRefusesToOverwrite() {
        val uri = DocumentCreator.createFile(context, Document("note.txt", "original")).getOrThrow()
        assertEquals("file", uri.scheme)
        assertEquals("original", File(uri.path!!).readText())
        assertTrue(DocumentCreator.createFile(context, Document("note.txt", "replacement")).isFailure)
        assertEquals("original", File(uri.path!!).readText())
    }

    @Test
    fun loaderReadsOnlyTextFilesAndSortsByModificationTime() {
        val old = File(directory, "old.txt").apply {
            writeText("old")
            setLastModified(1000)
        }
        val recent = File(directory, "new.txt").apply {
            writeText("new")
            setLastModified(2000)
        }
        File(directory, "ignored.jpg").writeText("image")
        File(directory, "folder.txt").mkdirs()
        DocumentLoader.load()
        assertEquals(listOf("new.txt", "old.txt"), DocumentLoader.files.value.map { it.filename })
        assertEquals("old", DocumentLoader.load("old.txt").content)
        assertEquals(recent.toUri(), DocumentLoader.load("new.txt").uri)
        assertTrue(old.exists())
    }

    @Test
    fun unsuccessfulFileDeletionRetainsCache() {
        val document = Document("missing.txt", "original", File(directory, "missing.txt").toUri())
        DocumentLoader.add(document)
        assertTrue(DocumentCreator.deleteFile(context, document).isFailure)
        assertEquals(document, DocumentLoader.load("missing.txt"))
        assertFalse(File(directory, "missing.txt").exists())
    }
}
