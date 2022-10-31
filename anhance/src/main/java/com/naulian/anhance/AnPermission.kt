package com.naulian.anhance

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String) =
    AnPermission.isGranted(this , permission)

fun Context.isPermissionNotGranted(permission: String) =
    isPermissionGranted(permission).not()

fun Activity.requestPermission(permission: String) =
    ActivityCompat.requestPermissions(this, arrayOf(permission), 0)

fun Activity.isPermissionDenied(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun Activity.askForPermission(permission: String): Boolean {
    if (isPermissionNotGranted(permission)) {
        if (isPermissionDenied(permission)) {
            requestPermission(permission)
        } else requestPermission(permission)
        return false
    }
    return true
}

object AnPermission {
    const val GRANTED = PackageManager.PERMISSION_GRANTED

    const val READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    const val WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val INTERNET = Manifest.permission.INTERNET

    fun checkSelf(context: Context, permission: String): Int {
        return ContextCompat.checkSelfPermission(context, permission)
    }

    fun isGranted(context: Context, permission: String) : Boolean{
        return checkSelf(context , permission) == GRANTED
    }
}