package com.naulian.anhance

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Document(
    val filename: String = "",
    val content: String = "",

    val uri: Uri = Uri.EMPTY,
    val lastModified: Long = 0L,
)

object DocumentLoader {

    private val fileMap = hashMapOf<String, Document>()

    private val _files = MutableStateFlow(emptyList<Document>())
    val files = _files.asStateFlow()

    fun add(document: Document) {
        fileMap[document.filename]?.let {
            load()
            return
        }

        fileMap[document.filename] = document
        updateData()
    }

    fun remove(filename: String) {
        fileMap.remove(filename)
        updateData()
    }

    fun load() {
        val directory = Environment.DIRECTORY_DOCUMENTS
        val docFiles = Environment.getExternalStoragePublicDirectory(directory)
        val documents = docFiles.listFiles { _, name -> name.endsWith(".txt") }
        fileMap.clear()
        documents?.forEach {
            fileMap[it.name] = Document(
                filename = it.name,
                content = it.bufferedReader().readText(),
                lastModified = it.lastModified(),
                uri = it.toUri()
            )
        }
        updateData()
    }

    private fun updateData() {
        _files.update {
            fileMap.values.sortedByDescending { it.lastModified }
        }
    }

    fun load(filename: String): Document {
        return fileMap[filename] ?: Document()
    }
}

object DocumentCreator {
    private val TAG = DocumentCreator::class.java.simpleName

    fun createFile(context: Context, document: Document): Result<Uri> {
        return create(context, document).also { result ->
            result.onSuccess {
                val newFile = document.copy(uri = it)
                DocumentLoader.add(newFile)
                Log.d(TAG, "createFile: $it")
            }
            result.onFailure { Log.e(TAG, "createFile: $it") }
        }
    }

    fun saveFile(context: Context, document: Document): Result<Unit> {
        DocumentLoader.add(document)
        return save(context, document)
    }

    private fun create(context: Context, document: Document): Result<Uri> {
        val resolver = context.contentResolver
        val content = document.content.toByteArray()

        ContentValues().apply {
            val directory = Environment.DIRECTORY_DOCUMENTS
            val contentUri = MediaStore.Files.getContentUri("external")
            put(MediaStore.MediaColumns.DISPLAY_NAME, document.filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            put(MediaStore.MediaColumns.RELATIVE_PATH, directory)

            resolver.insert(contentUri, this)?.let { fileUri ->
                return runCatching {
                    resolver.openOutputStream(fileUri)?.use { outputStream ->
                        outputStream.write(content)
                        return@runCatching "file:///storage/emulated/0/Documents/${document.filename}".toUri()
                    }
                    throw Exception("Failed to open output stream for $fileUri")
                }
            }

            return Result.failure(Exception("Cannot create file"))
        }
    }

    private fun save(context: Context, document: Document): Result<Unit> = runCatching {
        if (document.uri == Uri.EMPTY) return@runCatching
        val os = context.contentResolver.openOutputStream(document.uri, "rwt")
        os?.use { it.write(document.content.toByteArray()) }
    }


    fun deleteFile(document: Document) {
        if (document.uri == Uri.EMPTY) {
            Log.d(TAG, "deleteFile: Empty")
            return
        }

        document.uri.toFile().delete()
        DocumentLoader.remove(document.filename)
        Log.d(TAG, "deleteFile: File Removed")
    }

    fun renameFile(context: Context, document: Document, newName: String) = runCatching {
        val newdocument = Document(
            filename = newName,
            content = document.content,
            lastModified = document.lastModified,
        )
        Log.d(TAG, "renameFile: ${document.uri}")
        deleteFile(document)
        createFile(context, newdocument)
    }
}