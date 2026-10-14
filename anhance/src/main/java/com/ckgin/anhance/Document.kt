package com.ckgin.anhance

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.IOException

data class Document(
    val filename: String = "",
    val content: String = "",
    val uri: Uri = Uri.EMPTY,
    val lastModified: Long = 0L,
)

fun Document.save(context: Context) {
    if (uri == Uri.EMPTY) {
        DocumentCreator.createFile(context, this)
    } else {
        DocumentCreator.saveFile(context, this)
    }
}

object DocumentLoader {
    private val fileMap = hashMapOf<String, Document>()
    private val _files = MutableStateFlow(emptyList<Document>())
    val files = _files.asStateFlow()

    @Synchronized
    fun add(document: Document) {
        fileMap[document.filename] = document
        updateData()
    }

    @Synchronized
    fun remove(filename: String) {
        fileMap.remove(filename)
        updateData()
    }

    @Synchronized
    fun load() {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val documents = directory.listFiles { file -> file.isFile && file.name.endsWith(".txt") }
        val loaded = documents.orEmpty().associate { file ->
            file.name to Document(
                filename = file.name,
                content = file.bufferedReader().use { it.readText() },
                lastModified = file.lastModified(),
                uri = file.toUri()
            )
        }
        fileMap.clear()
        fileMap.putAll(loaded)
        updateData()
    }

    private fun updateData() {
        _files.value = fileMap.values.sortedByDescending { it.lastModified }
    }

    @Synchronized
    fun load(filename: String): Document = fileMap[filename] ?: Document()
}

object DocumentCreator {
    private val TAG = DocumentCreator::class.java.simpleName

    fun createFile(context: Context, document: Document): Result<Uri> {
        return create(context, document).also { result ->
            result.onSuccess { uri ->
                DocumentLoader.add(document.copy(uri = uri, lastModified = millisOfNow))
            }
            result.onFailure { Log.e(TAG, "Cannot create document", it) }
        }
    }

    fun saveFile(context: Context, document: Document): Result<Unit> = runCatching {
        require(document.uri != Uri.EMPTY) { "Document URI is empty" }
        val stream = context.contentResolver.openOutputStream(document.uri, "rwt")
            ?: throw IOException("Cannot open document output stream")
        stream.use { it.write(document.content.toByteArray()) }
        DocumentLoader.add(document.copy(lastModified = millisOfNow))
    }

    private fun validateFilename(filename: String) {
        require(filename.isNotBlank() && filename != "." && filename != ".." &&
            '/' !in filename && '\\' !in filename
        ) { "Document filename must be a single non-empty name" }
    }

    private fun create(context: Context, document: Document): Result<Uri> = runCatching {
        validateFilename(document.filename)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            if (!directory.isDirectory && !directory.mkdirs()) {
                throw IOException("Cannot create document directory")
            }
            val file = File(directory, document.filename)
            if (!file.createNewFile()) throw IOException("Document already exists")
            try {
                file.outputStream().use { it.write(document.content.toByteArray()) }
            } catch (exception: Exception) {
                file.delete()
                throw exception
            }
            return@runCatching file.toUri()
        }

        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, document.filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
            ?: throw IOException("Cannot create document")
        try {
            val stream = resolver.openOutputStream(uri)
                ?: throw IOException("Cannot open document output stream")
            stream.use { it.write(document.content.toByteArray()) }
        } catch (exception: Exception) {
            try {
                resolver.delete(uri, null, null)
            } catch (cleanup: Exception) {
                exception.addSuppressed(cleanup)
            }
            throw exception
        }
        uri
    }

    // File URIs can be deleted without a resolver. Use the Context overload for content URIs.
    fun deleteFile(document: Document) {
        if (document.uri == Uri.EMPTY) return
        val file = document.uri.toFile()
        if (!file.delete()) throw IOException("Cannot delete document")
        DocumentLoader.remove(document.filename)
    }

    fun deleteFile(context: Context, document: Document): Result<Unit> = runCatching {
        require(document.uri != Uri.EMPTY) { "Document URI is empty" }
        if (document.uri.scheme == "file") {
            deleteFile(document)
        } else {
            if (context.contentResolver.delete(document.uri, null, null) <= 0) {
                throw IOException("Cannot delete document")
            }
            DocumentLoader.remove(document.filename)
        }
    }

    // Preserve the existing nested Result API while renaming in place.
    fun renameFile(context: Context, document: Document, newName: String): Result<Result<Uri>> = runCatching {
        validateFilename(newName)
        require(document.uri != Uri.EMPTY) { "Document URI is empty" }
        val renamedUri = if (document.uri.scheme == "file") {
            val original = document.uri.toFile()
            val renamed = File(original.parentFile, newName)
            if (original != renamed && (renamed.exists() || !original.renameTo(renamed))) {
                throw IOException("Cannot rename document")
            }
            if (!renamed.isFile) throw IOException("Document does not exist")
            renamed.toUri()
        } else {
            val values = ContentValues().apply { put(MediaStore.MediaColumns.DISPLAY_NAME, newName) }
            if (context.contentResolver.update(document.uri, values, null, null) <= 0) {
                throw IOException("Cannot rename document")
            }
            document.uri
        }
        DocumentLoader.remove(document.filename)
        DocumentLoader.add(document.copy(filename = newName, uri = renamedUri, lastModified = millisOfNow))
        Result.success(renamedUri)
    }
}
