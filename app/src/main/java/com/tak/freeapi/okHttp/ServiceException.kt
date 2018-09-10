package com.tak.freeapi.okHttp

/**
 * Wrapper class for all the exceptions and errors.
 */
data class ServiceException(val errorCode: String) : Exception()