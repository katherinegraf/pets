package com.kg.pets.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Credentials(
        var username: String,
        var password: String
) {
    class Deserializer : ResponseDeserializable<Credentials> {
        override fun deserialize(content: String): Credentials = Gson().fromJson(content, Credentials::class.java)
    }
}