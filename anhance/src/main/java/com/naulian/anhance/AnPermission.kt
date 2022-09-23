package com.naulian.anhance

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val GRANTED = PackageManager.PERMISSION_GRANTED

fun Context.checkPermission(permission : String) =
    ContextCompat.checkSelfPermission(this , permission)

fun Context.isPermissionGranted(permission: String) =
    checkPermission(permission) == GRANTED

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