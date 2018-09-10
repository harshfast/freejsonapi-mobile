package com.tak.freeapi.worker

import kotlinx.coroutines.experimental.launch

/**
 * Worker object is single instance and performs the task in parallel thread.
 */
object Worker {

    /**
     * Executes the provided higher order function in parallel thread.
     * It uses coroutine launch{} for parallel execution.
     *
     * @param backgroundTask: higher order function which would be executed in launch{}
     */
    fun execute(backgroundTask: () -> Unit) {
        launch {
            backgroundTask()
        }
    }
}