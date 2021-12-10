package com.ahsailabs.wallpaperapp.lib

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.savedstate.SavedStateRegistryOwner
import com.google.android.material.datepicker.MaterialDatePicker
import io.ktor.client.features.*
import java.lang.Exception
import java.nio.channels.UnresolvedAddressException
import java.text.SimpleDateFormat
import java.util.*
import android.content.Context.DOWNLOAD_SERVICE
import android.os.Environment

import android.database.Cursor
import com.ahsailabs.wallpaperapp.BuildConfig
import com.ahsailabs.wallpaperapp.R


/**
 * Created by ahmad s on 13/10/21.
 */
fun <T>handleUnauthorized(processState: ProcessState<T>, navController: NavController){
    if(processState is ProcessState.Error){
        if(processState.exception != null) {
            if (processState.exception is UnAuthorizedException) {
                processState.exception = null
                //navController.navigate(NavigationItem.Login.route)
            }
        }
    }
}

//still buggy in compose
fun createWithSavedStateFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
    create: (SavedStateHandle) -> ViewModel
): ViewModelProvider.Factory {
    return object : AbstractSavedStateViewModelFactory(owner, defaultArgs){
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            @Suppress("UNCHECKED_CAST")// Casting T as ViewModel
            return create.invoke(handle) as T
        }
    }
}

fun createWithFactory(
    create: () -> ViewModel
): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")// Casting T as ViewModel
            return create.invoke() as T
        }
    }
}

fun getInfoOfException(context: Context, e: Throwable): String{
    val resources = context.resources
    val message = when {
        e is RedirectResponseException -> resources.getString(R.string.redirect_response_exception)
        e is ClientRequestException -> resources.getString(R.string.client_request_exception)
        e is ServerResponseException -> resources.getString(R.string.server_response_exception)
        e is HttpRequestTimeoutException -> resources.getString(R.string.request_timeout_exception)
        e is UnresolvedAddressException -> resources.getString(R.string.unresolved_address_exception)
        e.message != null -> e.message
        e.localizedMessage != null -> e.localizedMessage
        e.cause != null && e.cause!!.message != null -> e.cause!!.message
        e.cause != null && e.cause!!.localizedMessage != null -> e.cause!!.localizedMessage
        else -> e.toString()
    }
    return message.toString()
}


fun showDatePicker(
    activity : AppCompatActivity,
    updatedDate: (String) -> Unit)
{
    val picker = MaterialDatePicker.Builder.datePicker().build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        val result = dateFormatter(it)
        updatedDate(result)
    }
}

fun dateFormatter(milliseconds : Long) : String{
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliseconds
    return formatter.format(calendar.time)
}

fun <T>handleException(processState: UseProcessStateFlow<T>, e: Exception){
    when (e) {
        is ClientRequestException -> {
            if (e.response.status.value == 401) {
                processState.setStateError(UnAuthorizedException(e))
            } else {
                processState.setStateError(e)
            }
        }
        else -> {
            processState.setStateError(e)
        }
    }
}

fun <T,U>handleException(process2State: UseProcess2StateFlow<T,U>, exception: Exception, error: U){
    when (exception) {
        is ClientRequestException -> {
            if (exception.response.status.value == 401) {
                process2State.setStateError(UnAuthorizedException(exception), error)
            } else {
                process2State.setStateError(exception, error)
            }
        }
        else -> {
            process2State.setStateError(exception, error)
        }
    }
}


suspend fun <T,U>handleProcess(useProcessState: UseProcessStateFlow<T>
                               , process: suspend ()->U
                               , isSuccess: suspend (U)->Boolean
                               , successData: suspend (U)->Pair<T,String?>
                               , afterSuccess: (suspend (U)->Unit)? = null
                               , errorMessage: suspend (U)->String){
    try {
        useProcessState.setStateLoading()
        val response: U = process()
        if(isSuccess(response)){
            val data = successData(response)
            useProcessState.setStateSuccess(data.first, LimitedMessage(data.second))
            afterSuccess?.let { it(response) }
        } else {
            useProcessState.setStateError(errorMessage(response))
        }
    } catch (e: Exception){
        useProcessState.handleException(e)
    }
}

suspend fun <T,U,V>handleProcess(useProcess2State: UseProcess2StateFlow<T,U?>
                               , process: suspend ()->V
                               , isSuccess: suspend (V)->Boolean
                               , successData: suspend (V)->Pair<T,String?>
                               , afterSuccess: (suspend (V)->Unit)? = null
                               , errorMessage: suspend (V)->Pair<String,U>){
    try {
        useProcess2State.setStateLoading()
        val response: V = process()
        if(isSuccess(response)){
            val data = successData(response)
            useProcess2State.setStateSuccess(data.first, LimitedMessage(data.second,1))
            afterSuccess?.let { it(response) }
        } else {
            val error = errorMessage(response)
            useProcess2State.setStateError(error.first, error.second)
        }
    } catch (e: Exception){
        useProcess2State.handleException(e, null)
    }
}

fun downloadFile(
    context: Context,
    url: String,
    fileName: String,
    title: String,
    description: String = "Downloading",
    headerAuthorization: String
): Long {
    val request = DownloadManager.Request(Uri.parse(url))
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // Visibility of the download Notification
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        .setTitle(title) // Title of the Download Notification
        .setDescription(description) // Description of the Download Notification
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setAllowedOverMetered(true) // Set if download is allowed on Mobile network
        .setAllowedOverRoaming(true) // Set if download is allowed on roaming network
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        request.setRequiresCharging(false) // Set if charging is required to begin the download
    }
    request.addRequestHeader("Authorization", headerAuthorization)
    val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    return downloadManager.enqueue(request) // enqueue puts the download request in the queue.
}

fun getVersion(): String{
    return "Versi ${BuildConfig.VERSION_NAME}"
}