@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import com.naulian.anhance.objects.AnStore

suspend fun Context.readInt(key: String, defValue: Int): Int {
    return AnStore(this).readInt(key, defValue)
}

suspend fun Context.readString(key: String, defValue: String): String {
    return AnStore(this).readString(key, defValue)
}

suspend fun Context.readFloat(key: String, defValue: Float): Float {
    return AnStore(this).readFloat(key, defValue)
}

suspend fun Context.readBoolean(key: String, defValue: Boolean): Boolean {
    return AnStore(this).readBoolean(key, defValue)
}

suspend fun Context.readLong(key: String, defValue: Long): Long {
    return AnStore(this).readLong(key, defValue)
}

suspend fun Context.writeInt(key: String , value : Int){
    AnStore(this).writeInt(key , value)
}

suspend fun Context.writeString(key: String , value : String){
    AnStore(this).writeString(key , value)
}

suspend fun Context.writeFloat(key: String , value : Float){
    AnStore(this).writeFloat(key , value)
}

suspend fun Context.writeBoolean(key: String , value : Boolean){
    AnStore(this).writeBoolean(key , value)
}

suspend fun Context.writeLong(key: String , value : Long){
    AnStore(this).writeLong(key , value)
}