package com.kg.pets.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "stored_credentials")
data class StoredCredentials(
        @Id
        @Column(name = "owner_id")
        var ownerId: Long,

        @Column(name = "username")
        var username: String,

        @Column(name = "salt")
        var salt: String,

        @Column(name = "pw_hash")
        var pwHash: String
)