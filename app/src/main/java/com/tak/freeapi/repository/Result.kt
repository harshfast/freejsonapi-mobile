package com.tak.freeapi.repository

import com.tak.freeapi.okHttp.ServiceException


sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val exception: ServiceException?) : Result<Nothing>()

    object Loading : Result<Nothing>()

}