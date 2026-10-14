package com.ckgin.anhance

import android.content.Context
import android.content.pm.ProviderInfo
import android.graphics.Bitmap
import android.provider.MediaStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowContentResolver
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class BitmapTest {
    private lateinit var context: Context
    private lateinit var provider: TestDocumentProvider

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        provider = TestDocumentProvider()
        provider.attachInfo(context, ProviderInfo().apply { authority = "media" })
        ShadowContentResolver.registerProviderInternal("media", provider)
    }

    @Test
    fun jpegIsWrittenClosedAndPublishedBeforeSuccessToast() {
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
        bitmap.saveAsJpeg(context)
        assertTrue(provider.file.length() > 0)
        assertEquals(0, provider.values.getAsInteger(MediaStore.MediaColumns.IS_PENDING))
        assertEquals("Image Saved", ShadowToast.getTextOfLatestToast())
        assertEquals(0, provider.deletes)
        val loaded = readImage(provider.file.parent!!, provider.file.name)!!
        assertEquals(2, loaded.width)
        assertEquals(2, loaded.height)
    }

    @Test
    fun failedWritesRemovePartialMediaRows() {
        provider.failWrite = true
        Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888).saveAsJpeg(context)
        assertEquals(1, provider.deletes)
        assertTrue(ShadowToast.getTextOfLatestToast().startsWith("Image Not Saved:"))
    }

    @Test
    fun failedInsertDoesNotReportSuccess() {
        provider.failInsert = true
        Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888).saveAsJpeg(context)
        assertEquals(0, provider.deletes)
        assertTrue(ShadowToast.getTextOfLatestToast().startsWith("Image Not Saved:"))
    }

    @Test
    fun failedPublicationRemovesIncompleteImage() {
        provider.failUpdate = true
        Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888).saveAsJpeg(context)
        assertEquals(1, provider.deletes)
        assertTrue(ShadowToast.getTextOfLatestToast().startsWith("Image Not Saved:"))
    }

    @Test
    fun missingOrNonImageFilesReturnNull() {
        assertEquals(null, readImage(context.cacheDir.path, "missing.jpeg"))
        provider.file.writeText("not an image")
        assertEquals(null, readImage(provider.file.parent!!, provider.file.name))
    }
}
