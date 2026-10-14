package com.ckgin.anhance

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
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
import org.robolectric.shadows.ShadowContentResolver
import org.robolectric.annotation.Config
import java.io.File
import java.io.FileNotFoundException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class DocumentTest {
    private lateinit var context: Context
    private lateinit var provider: TestDocumentProvider

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        DocumentLoader.files.value.forEach { DocumentLoader.remove(it.filename) }
        provider = TestDocumentProvider()
        provider.attachInfo(context, ProviderInfo().apply { authority = "media" })
        ShadowContentResolver.registerProviderInternal("media", provider)
    }

    @After
    fun tearDown() {
        DocumentLoader.files.value.forEach { DocumentLoader.remove(it.filename) }
    }

    @Test
    fun creatingDocumentReturnsProviderUriAndPersistsUtf8() {
        val uri = DocumentCreator.createFile(context, Document("note.txt", "မြန်မာ")).getOrThrow()
        assertEquals(provider.uri, uri)
        assertEquals("မြန်မာ", provider.file.readText())
        assertEquals(uri, DocumentLoader.load("note.txt").uri)
        assertTrue(DocumentLoader.load("note.txt").lastModified > 0)
        assertEquals("note.txt", provider.values.getAsString(MediaStore.MediaColumns.DISPLAY_NAME))
    }

    @Test
    fun failedInsertAndFailedWriteLeaveNoCachedDocument() {
        provider.failInsert = true
        assertTrue(DocumentCreator.createFile(context, Document("note.txt", "hello")).isFailure)
        assertTrue(DocumentLoader.files.value.isEmpty())
        provider.failInsert = false
        provider.failWrite = true
        assertTrue(DocumentCreator.createFile(context, Document("note.txt", "hello")).isFailure)
        assertEquals(1, provider.deletes)
        assertTrue(DocumentLoader.files.value.isEmpty())
    }

    @Test
    fun invalidNamesDoNotReachProvider() {
        listOf("", " ", ".", "..", "../outside.txt", "nested/file.txt", "nested\\file.txt").forEach {
            assertTrue(it, DocumentCreator.createFile(context, Document(it, "hello")).isFailure)
        }
        assertEquals(0, provider.inserts)
    }

    @Test
    fun savingTruncatesExistingContentAndUpdatesCacheAfterSuccess() {
        val uri = DocumentCreator.createFile(context, Document("note.txt", "long original")).getOrThrow()
        val updated = Document("note.txt", "short", uri)
        assertTrue(DocumentCreator.saveFile(context, updated).isSuccess)
        assertEquals("short", provider.file.readText())
        assertEquals("short", DocumentLoader.load("note.txt").content)
    }

    @Test
    fun failedSavePreservesPreviouslyCachedDocument() {
        val uri = DocumentCreator.createFile(context, Document("note.txt", "original")).getOrThrow()
        val original = DocumentLoader.load("note.txt")
        provider.failWrite = true
        assertTrue(DocumentCreator.saveFile(context, original.copy(content = "new")).isFailure)
        assertEquals(original, DocumentLoader.load("note.txt"))
        assertTrue(DocumentCreator.saveFile(context, Document("empty.txt", "new")).isFailure)
    }

    @Test
    fun renameChangesProviderMetadataWithoutDeletingOriginal() {
        DocumentCreator.createFile(context, Document("old.txt", "original")).getOrThrow()
        val result = DocumentCreator.renameFile(context, DocumentLoader.load("old.txt"), "new.txt")
        assertEquals(provider.uri, result.getOrThrow().getOrThrow())
        assertEquals("new.txt", provider.values.getAsString(MediaStore.MediaColumns.DISPLAY_NAME))
        assertEquals("original", provider.file.readText())
        assertEquals(0, provider.deletes)
        assertEquals(1, provider.inserts)
        assertEquals(Document(), DocumentLoader.load("old.txt"))
        assertEquals(provider.uri, DocumentLoader.load("new.txt").uri)
    }

    @Test
    fun failedRenamePreservesOriginalAndCache() {
        DocumentCreator.createFile(context, Document("old.txt", "original")).getOrThrow()
        val original = DocumentLoader.load("old.txt")
        provider.failUpdate = true
        assertTrue(DocumentCreator.renameFile(context, original, "new.txt").isFailure)
        assertEquals(original, DocumentLoader.load("old.txt"))
        assertEquals("original", provider.file.readText())
        assertEquals(0, provider.deletes)
    }

    @Test
    fun contentDeletionUsesResolverAndOnlyUpdatesCacheOnSuccess() {
        DocumentCreator.createFile(context, Document("note.txt", "original")).getOrThrow()
        val original = DocumentLoader.load("note.txt")
        provider.failDelete = true
        assertTrue(DocumentCreator.deleteFile(context, original).isFailure)
        assertEquals(original, DocumentLoader.load("note.txt"))
        provider.failDelete = false
        assertTrue(DocumentCreator.deleteFile(context, original).isSuccess)
        assertEquals(Document(), DocumentLoader.load("note.txt"))
        assertFalse(provider.file.exists())
    }

    @Test
    fun legacyFileRenameRefusesToOverwriteAnExistingDestination() {
        val old = File(context.cacheDir, "old.txt").apply { writeText("original") }
        val existing = File(context.cacheDir, "existing.txt").apply { writeText("keep") }
        val document = Document(old.name, "original", old.toUri())
        DocumentLoader.add(document)
        assertTrue(DocumentCreator.renameFile(context, document, existing.name).isFailure)
        assertEquals("original", old.readText())
        assertEquals("keep", existing.readText())
        val renamed = DocumentCreator.renameFile(context, document, "renamed.txt").getOrThrow().getOrThrow()
        assertEquals("original", File(renamed.path!!).readText())
        DocumentCreator.deleteFile(DocumentLoader.load("renamed.txt"))
        assertEquals(Document(), DocumentLoader.load("renamed.txt"))
    }

    @Test
    fun loaderReplacesDuplicateNamesAndPublishesSortedSnapshots() {
        DocumentLoader.add(Document("a.txt", "a", lastModified = 1))
        DocumentLoader.add(Document("b.txt", "b", lastModified = 2))
        val snapshot = DocumentLoader.files.value
        DocumentLoader.add(Document("a.txt", "updated", lastModified = 3))
        assertEquals(listOf("a.txt", "b.txt"), DocumentLoader.files.value.map { it.filename })
        assertEquals("updated", DocumentLoader.load("a.txt").content)
        assertEquals(listOf("b.txt", "a.txt"), snapshot.map { it.filename })
        DocumentLoader.remove("missing.txt")
        assertEquals(2, DocumentLoader.files.value.size)
    }
}

internal class TestDocumentProvider : ContentProvider() {
    val uri: Uri = Uri.parse("content://media/external/file/1")
    lateinit var file: File
    var values = ContentValues()
    var inserts = 0
    var deletes = 0
    var failInsert = false
    var failWrite = false
    var failUpdate = false
    var failDelete = false

    override fun onCreate(): Boolean {
        file = File(context!!.cacheDir, "provider.txt")
        file.delete()
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        inserts++
        this.values = ContentValues(values)
        if (failInsert) return null
        file.writeText("")
        return this.uri
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor {
        if (failWrite) throw FileNotFoundException("write failed")
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.parseMode(mode))
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        if (failUpdate) return 0
        this.values.putAll(values)
        return 1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        deletes++
        if (failDelete) return 0
        return if (file.delete()) 1 else 0
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null
    override fun getType(uri: Uri): String = "text/plain"
}
