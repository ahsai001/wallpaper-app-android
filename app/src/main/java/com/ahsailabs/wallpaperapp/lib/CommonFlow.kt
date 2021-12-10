package com.ahsailabs.wallpaperapp.lib

import kotlinx.coroutines.flow.*
import java.lang.RuntimeException

/**
 * Created by ahmad s on 06/10/21.
 */

class LimitedMessage(message: String?, private var useLimit: Int? = null){
    val message: String? = message
        get() {
            if(useLimit == null){
                return field
            } else {
                if (useLimit!! > 0) {
                    useLimit = useLimit!! - 1
                    return field
                }
                return null
            }
        }
}

//======================== StateFlow ===================================
sealed class ProcessState<out T> {
    data class Initial(var message: LimitedMessage? = null): ProcessState<Nothing>()
    data class Loading(var message: LimitedMessage? = null): ProcessState<Nothing>()
    data class Success<T>(val data: T, var message: LimitedMessage? = null): ProcessState<T>()
    data class Error(var exception: Throwable?=null, var message: LimitedMessage? = null): ProcessState<Nothing>()
}

abstract class UseProcessStateFlow<T>(private val initialState: ProcessState<T>) {
    private var _processStateFlow = MutableStateFlow(initialState)
    private val processStateFlow: StateFlow<ProcessState<T>> = _processStateFlow.asStateFlow()

    fun setStateFlowValue(newState: ProcessState<T>){
        _processStateFlow.value = newState
    }

    fun resetState(){
        _processStateFlow.value = initialState
    }

    fun getStateFlowValue(): ProcessState<T> {
        return processStateFlow.value
    }

    fun getStateFlow(): StateFlow<ProcessState<T>> {
        return processStateFlow
    }

    fun setStateLoading(message: LimitedMessage? = null){
        setStateFlowValue(ProcessState.Loading(message))
    }

    fun setStateSuccess(data: T,message: LimitedMessage? = null){
        setStateFlowValue(ProcessState.Success(data,message))
    }

    fun setStateError(data: Throwable? = null, message: LimitedMessage? = null){
        setStateFlowValue(ProcessState.Error(data, message))
    }

    fun setStateError(message: String){
        setStateFlowValue(ProcessState.Error(RuntimeException(message)))
    }
}


sealed class Process2State<out T, out U> {
    data class Initial<T>(var data: T, var message: LimitedMessage? = null): Process2State<T, Nothing>()
    data class Loading(var message: LimitedMessage? = null): Process2State<Nothing, Nothing>()
    data class Success<T>(var data: T, var message: LimitedMessage? = null): Process2State<T, Nothing>()
    data class Error<U>(var exception: Throwable?=null, var error: U, var message: LimitedMessage? = null): Process2State<Nothing, U>()
}

abstract class UseProcess2StateFlow<T, U>(private val initialState: Process2State<T, U>) {
    private var _processStateFlow = MutableStateFlow(initialState)
    private val processStateFlow: StateFlow<Process2State<T, U>> = _processStateFlow.asStateFlow()

    fun setStateFlowValue(newState: Process2State<T, U>){
        _processStateFlow.value = newState
    }

    fun resetState(){
        _processStateFlow.value = initialState
    }

    fun getStateFlowValue(): Process2State<T, U> {
        return processStateFlow.value
    }

    fun getStateFlow(): StateFlow<Process2State<T, U>> {
        return processStateFlow
    }

    fun setStateLoading(message: LimitedMessage? = null){
        setStateFlowValue(Process2State.Loading(message))
    }

    fun setStateSuccess(data: T,message: LimitedMessage? = null){
        setStateFlowValue(Process2State.Success(data,message))
    }

    fun setStateError(data: Throwable? = null, error: U, message: LimitedMessage? = null){
        setStateFlowValue(Process2State.Error(data, error, message))
    }

    fun setStateError(message: String, error: U){
        setStateFlowValue(Process2State.Error(RuntimeException(message),error))
    }
}

abstract class UseSimpleStateFlow<T>(private val initialState: T) {
    private var _simpleStateFlow = MutableStateFlow(initialState)
    private val simpleStateFlow: StateFlow<T> = _simpleStateFlow

    fun setStateFlowValue(newState: T){
        _simpleStateFlow.value = newState
    }

    fun resetState(){
        _simpleStateFlow.value = initialState
    }

    fun getStateFlowValue(): T{
        return simpleStateFlow.value
    }

    fun getStateFlow(): StateFlow<T> {
        return simpleStateFlow
    }

}




//====================== SharedFlow ==================================
sealed class ProcessShared<out T> {
    data class Initial(var message: LimitedMessage? = null): ProcessShared<Nothing>()
    data class Loading(var message: LimitedMessage? = null): ProcessShared<Nothing>()
    data class Success<T>(var data: T, var message: LimitedMessage? = null): ProcessShared<T>()
    data class Error(var exception: Throwable?=null, var message: LimitedMessage? = null): ProcessShared<Nothing>()
}

abstract class UseProcessSharedFlow<T>() {
    private var _processSharedFlow = MutableSharedFlow<ProcessShared<T>>()
    private val processSharedFlow: SharedFlow<ProcessShared<T>> = _processSharedFlow.asSharedFlow()

    suspend fun setSharedFlowValue(newShared: ProcessShared<T>){
        _processSharedFlow.emit(newShared)
    }

    suspend fun getSharedFlowLast(): ProcessShared<T> {
        return processSharedFlow.last()
    }

    fun getSharedFlow(): SharedFlow<ProcessShared<T>> {
        return processSharedFlow
    }


    suspend fun setSharedLoading(message: LimitedMessage? = null){
        setSharedFlowValue(ProcessShared.Loading(message))
    }

    suspend fun setSharedSuccess(data: T,message: LimitedMessage? = null){
        setSharedFlowValue(ProcessShared.Success(data,message))
    }

    suspend fun setSharedError(data: Throwable? = null, message: LimitedMessage? = null){
        setSharedFlowValue(ProcessShared.Error(data, message))
    }

    suspend fun setSharedError(message: String){
        setSharedFlowValue(ProcessShared.Error(RuntimeException(message)))
    }
}


sealed class Process2Shared<out T, out U> {
    data class Initial<T>(var data: T, var message: LimitedMessage? = null): Process2Shared<T, Nothing>()
    data class Loading(var message: LimitedMessage? = null): Process2Shared<Nothing, Nothing>()
    data class Success<T>(var data: T, var message: LimitedMessage? = null): Process2Shared<T, Nothing>()
    data class Error<U>(var exception: Throwable? = null, var error: U, var message: LimitedMessage? = null): Process2Shared<Nothing, U>()
}

abstract class UseProcess2SharedFlow<T, U> {
    private var _processSharedFlow = MutableSharedFlow<Process2Shared<T, U>>()
    private val processSharedFlow: SharedFlow<Process2Shared<T, U>> = _processSharedFlow.asSharedFlow()

    suspend fun setSharedFlowValue(newShared: Process2Shared<T, U>){
        _processSharedFlow.emit(newShared)
    }

    suspend fun getSharedFlowLast(): Process2Shared<T, U> {
        return processSharedFlow.last()
    }

    fun getSharedFlow(): SharedFlow<Process2Shared<T, U>> {
        return processSharedFlow
    }


    suspend fun setSharedLoading(message: LimitedMessage? = null){
        setSharedFlowValue(Process2Shared.Loading(message))
    }

    suspend fun setSharedSuccess(data: T,message: LimitedMessage? = null){
        setSharedFlowValue(Process2Shared.Success(data,message))
    }

    suspend fun setSharedError(data: Throwable? = null, error: U, message: LimitedMessage? = null){
        setSharedFlowValue(Process2Shared.Error(data, error, message))
    }

    suspend fun setSharedError(message: String, error: U){
        setSharedFlowValue(Process2Shared.Error(RuntimeException(message),error))
    }
}


abstract class UseSimpleSharedFlow<T> {
    private var _simpleSharedFlow = MutableSharedFlow<T>()
    private val simpleSharedFlow: SharedFlow<T> = _simpleSharedFlow

    suspend fun setSharedFlowValue(newShared: T){
        _simpleSharedFlow.emit(newShared)
    }
    suspend fun getSharedFlowLast(): T{
        return simpleSharedFlow.last()
    }

    fun getSharedFlow(): SharedFlow<T> {
        return simpleSharedFlow
    }

}