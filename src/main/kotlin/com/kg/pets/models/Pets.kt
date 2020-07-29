package com.kg.pets.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "pets")
data class Pets (
    @Id
    @Column(name = "pet_id")
    var petId: Long,

    @Column(name = "name")
    var name: String,

    @Column(name = "owner_id")
    var ownerId: Long,

    @Column(name = "age")
    var age: Long,

    @Column(name = "gender")
    var gender: String,

    @Column(name = "color")
    var color: String
)