package com.kg.pets.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import javax.persistence.*

data class User (
        var userId: Long,
        var firstName: String? = null,
        var lastName: String? = null,
        var email: String,
        var username: String,
        var password: String,
        var ipAddress: String? = null
) {
class Deserializer : ResponseDeserializable<User> {
    override fun deserialize(content: String): User = Gson().fromJson(content, User::class.java)
    }
}

data class UserList (
        val users: List<User>
// can use this if expecting a list of users from user app
)

data class UserWithListOfOwnedPets (
        var owner: String? = null,
        var petsOwned: List<OwnedPets>?
)
