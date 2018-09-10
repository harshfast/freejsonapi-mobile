package com.tak.freeapi.model

import java.io.Serializable

/**
 * Data class that represent Api attributes.
 */
data class Api(

        /**
         * Api name.
         */
        val API: String?,

        /**
         * Api description.
         */
        val Description: String?,

        /**
         * Api auth required or not
         */
        val Auth: String?,

        /**
         * Is api https or not.
         */
        val HTTPS: Boolean?,

        /**
         *
         */
        val Cors: String?,

        /**
         * Api url to connect
         */
        val Link: String?,

        /**
         * Api category
         */
        val Category: String?

) : Serializable