package com.tak.freeapi.worker

interface WorkerTask {
    fun execute(): Any
}