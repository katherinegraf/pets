package com.kg.pets.services

import com.github.kittinunf.fuel.core.Body
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.kg.pets.models.User
import com.kg.pets.utils.USERS_API_BASE_URL
import java.util.logging.Logger

open class RestApiService {

    val logger = Logger.getLogger("logger")

    companion object {
        fun getUserByAPI(id: Long): User? {
            val (_, _, result) = USERS_API_BASE_URL.plus(id)
                    .httpGet()
                    .responseObject(User.Deserializer())

            return when (result) {
                is Result.Failure -> {
                    val exception = result.getException()
                    null
                }
                is Result.Success -> {
                    val (user) = result
                    user
                }
            }
        }
    }
}