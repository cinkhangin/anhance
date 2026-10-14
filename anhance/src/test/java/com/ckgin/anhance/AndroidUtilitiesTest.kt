package com.ckgin.anhance

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.NetworkCapabilities
import android.os.Looper
import android.text.style.AbsoluteSizeSpan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.io.File
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28, 35])
class AndroidUtilitiesTest {
    private val context: Context get() = RuntimeEnvironment.getApplication()

    @Test
    fun externalAppsCanOpenFromApplicationContextAndIdentifiersAreEncoded() {
        context.openTelegramUser("name&other=value")
        val telegram = shadowOf(context as Application).nextStartedActivity
        assertTrue(telegram.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
        assertEquals("org.telegram.messenger", telegram.`package`)
        assertEquals("name&other=value", telegram.data!!.getQueryParameter("domain"))
        context.openMessenger("user/id")
        val messenger = shadowOf(context as Application).nextStartedActivity
        assertTrue(messenger.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
        assertEquals("user/id", messenger.data!!.lastPathSegment)
    }

    @Test
    fun permissionChecksReflectGrantAndDenial() {
        val app = shadowOf(context as Application)
        app.denyPermissions(Manifest.permission.RECORD_AUDIO)
        assertFalse(context.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
        assertTrue(context.isPermissionNotGranted(Manifest.permission.RECORD_AUDIO))
        app.grantPermissions(Manifest.permission.RECORD_AUDIO)
        assertTrue(context.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
        assertFalse(context.isPermissionNotGranted(Manifest.permission.RECORD_AUDIO))
    }

    @Test
    fun assetsReturnContentOrFailureAndCallbackRunsOnce() {
        assertEquals("hello\nမြန်မာ\n", context.readStringAsset("nested/sample.txt"))
        assertEquals("hello\nမြန်မာ\n", readStringAsset(context, "nested/sample.txt").getOrThrow())
        assertEquals("", context.readStringAsset("missing.txt"))
        assertTrue(readStringAsset(context, "missing.txt").isFailure)
        var calls = 0
        context.readStringAsset("missing.txt") {
            calls++
            assertTrue(it.isFailure)
        }
        assertEquals(1, calls)
    }

    @Test
    fun descriptorCallbackExceptionsAreNotRedeliveredAsAssetFailures() {
        var calls = 0
        val error = IOException("callback")
        val thrown = org.junit.Assert.assertThrows(IOException::class.java) {
            context.readAssetFd("sample.mp3") {
                calls++
                it.getOrThrow().close()
                throw error
            }
        }
        assertTrue(thrown === error)
        assertEquals(1, calls)
        assertEquals(null, context.readAsset("missing.mp3"))
    }

    @Test
    fun nestedAssetsCopyIntoPrivateStorage() {
        val path = context.assetFilePath("nested/sample.txt")
        assertTrue(path.isNotEmpty())
        assertEquals("hello\nမြန်မာ\n", File(path).readText())
        assertEquals("", context.assetFilePath("missing.txt"))
        assertEquals("", context.assetFilePath("../outside.txt"))
    }

    @Test
    fun colorsSupportShorthandAndInvalidInputFallback() {
        assertEquals(Color.RED, "#ff0000".toColorInt())
        assertEquals(Color.RED, "#f00".toColorInt())
        assertEquals(0x88ff0000.toInt(), "#8f00".toColorInt())
        assertEquals(Color.WHITE, "#gggggg".toColorInt())
        assertEquals(Color.WHITE, "red".toColorInt())
        assertEquals(Color.WHITE, "#12345".toColorInt())
    }

    @Test
    fun ethernetAndVpnConnectionsAreDetectedAndOfflineNetworksAreRejected() {
        val manager = context.connectivityManager
        val shadow = shadowOf(manager)
        val network = manager.activeNetwork!!
        listOf(NetworkCapabilities.TRANSPORT_ETHERNET, NetworkCapabilities.TRANSPORT_WIFI,
            NetworkCapabilities.TRANSPORT_CELLULAR, NetworkCapabilities.TRANSPORT_VPN).forEach { transport ->
            val capabilities = NetworkCapabilities()
            shadowOf(capabilities).addTransportType(transport)
            shadowOf(capabilities).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            shadowOf(capabilities).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadow.setNetworkCapabilities(network, capabilities)
            assertTrue("transport $transport", context.isInternetAvailable)
        }
        val offline = NetworkCapabilities()
        shadowOf(offline).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadow.setNetworkCapabilities(network, offline)
        assertFalse(context.isInternetAvailable)
        shadow.setActiveNetworkInfo(null)
        assertFalse(context.isInternetAvailable)
    }

    @Test
    fun clipboardAndResourceUrisUseContext() {
        context.copyString("copied")
        assertEquals("copied", context.clipboardManager.primaryClip!!.getItemAt(0).text)
        assertEquals("android.resource://${context.packageName}/123", 123.toUri(context).toString())
        shadowOf(Looper.getMainLooper()).idle()
    }

    @Test
    fun htmlAndDimensionsPreserveTheirContracts() {
        assertEquals("red", "red".colorHtml("#ff0000").toString())
        assertEquals("<font color=#ff0000>red</font>", "red".color("#ff0000"))
        assertEquals("bold", "<b>bold</b>".html().toString())
        assertEquals("bold", htmlSpanned("<b>bold</b>").toString())
        assertEquals(16f, 16.pxValueOfDp().dpValueOfPx(), 0.001f)
        assertEquals(16f, dpOf(pxOf(16)), 0.001f)
        val sized = "text".size(16)
        assertEquals(16.pxValueOfDp().toInt(), sized.getSpans(0, 4, AbsoluteSizeSpan::class.java).single().size)
    }
}
