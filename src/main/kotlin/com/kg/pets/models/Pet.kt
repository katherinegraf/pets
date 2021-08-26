package com.kg.pets.models

import javax.persistence.*

@Entity
@Table(name = "pets")
data class Pet(

        @Column(name = "name")
        var name: String,
        @Column(name = "owner_id")
        var ownerId: Long = 9000,
        @Column(name = "age")
        var age: Long = 9000,
        @Column(name = "gender")
        var gender: String,
        @Column(name = "color")
        var color: String,
        @Column(name = "type")
        var type: String
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var petId: Long = 0;
}

data class OwnedPets (
        var name: String,
        var type: String,
        var gender: String,
        var color: String,
        var age: Long,
        var petId: Long
)

