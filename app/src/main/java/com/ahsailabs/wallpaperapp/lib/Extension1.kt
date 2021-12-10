package com.ahsailabs.wallpaperapp.lib

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.navigation.NavController
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


/**
 * Created by ahmad s on 13/10/21.
 */

fun <T>ProcessState<T>.handleUnauthorized(navController: NavController){
    handleUnauthorized(this, navController)
}
fun <T>UseProcessStateFlow<T>.handleException(e: Exception){
    handleException(this, e)
}

fun <T,U>UseProcess2StateFlow<T,U>.handleException(exception: Exception, error: U){
    handleException(this, exception, error)
}

suspend fun <T,U>UseProcessStateFlow<T>.handleProcess(process: suspend ()->U
                                                      , isSuccess: suspend (U)->Boolean
                                                      , successData: suspend (U)->Pair<T,String?>
                                                      , afterSuccess: (suspend (U)->Unit)? = null
                                                      , errorMessage: suspend (U)->String){
    handleProcess(
        useProcessState = this,
        process = process,
        isSuccess = isSuccess,
        successData = successData,
        afterSuccess = afterSuccess,
        errorMessage = errorMessage
    )
}

suspend fun <T,U,V>UseProcess2StateFlow<T,U?>.handleProcess(process: suspend ()->V
                                                      , isSuccess: suspend (V)->Boolean
                                                      , successData: suspend (V)->Pair<T,String?>
                                                      , afterSuccess: (suspend (V)->Unit)? = null
                                                      , errorMessage: suspend (V)->Pair<String, U>){
    handleProcess(
        useProcess2State = this,
        process = process,
        isSuccess = isSuccess,
        successData = successData,
        afterSuccess = afterSuccess,
        errorMessage = errorMessage
    )
}



fun String.encodeUrl(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}

fun String.decodeUrl(): String {
    return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
}

fun Context.openSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = Uri.fromParts("package",packageName,null)
    intent.data = uri
    startActivity(intent)
}

fun Context.openPlaystore(){
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + this.packageName)
            )
        )
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + this.packageName)
            )
        )
    }
}


fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}


fun String?.letterCase(): String? {
    return if(this != null) {
        val words = this.split(" ").toMutableList()
        var output = ""
        for (word in words) {
            output += word.replaceFirstChar { it.uppercaseChar() } + " "
        }
        output.trim()
    } else {
        null
    }
}
