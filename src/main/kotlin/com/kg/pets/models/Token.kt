package com.kg.pets.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import javax.persistence.*

@Entity
@Table(name = "tokens")
data class SessionToken (
        @Id
        @Column(name = "tokenId")
        var tokenId: String,

        @Column(name = "userId")
        var userId: Long,

        @Column(name = "revoked")
        var revoked: Boolean
) {
        class Deserializer : ResponseDeserializable<SessionToken> {
                override fun deserialize(content: String): SessionToken = Gson().fromJson(content, SessionToken::class.java)
        }
}