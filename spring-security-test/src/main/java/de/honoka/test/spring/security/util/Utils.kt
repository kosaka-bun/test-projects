package de.honoka.test.spring.security.util

object Utils {

    fun getTokenByAuthorization(authorization: String?): String? {
        if(authorization.isNullOrEmpty()) return null
        val parts = authorization.split(" ")
        if(parts[0].lowercase() != "token") throw UnsupportedOperationException()
        return parts[parts.lastIndex]
    }
}