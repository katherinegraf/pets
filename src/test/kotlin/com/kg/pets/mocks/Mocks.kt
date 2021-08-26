package com.kg.pets.mocks

import com.kg.pets.models.*

val petMock = Pet(
         "Perrin",
         17,
         3,
         "M",
         "brown",
         "dog"
)

val petEditsMock = mapOf<String,Any>(
        "name" to "Anjelique",
        "gender" to "F"
)

val userMock = User(
         111,
         "Claudia",
         "Rumin",
         "crumina@cyberchimps.com",
         "crumina",
         "7ee4Vl",
         "172.20.95.218"
)

val userWithPetMock = UserWithListOfOwnedPets(
        owner = "Claudia Rumin",
        petsOwned = listOf(
                OwnedPets(
                        "JerBear",
                        "dog",
                        "M",
                        "brown",
                        9,
                        342
                ),
                OwnedPets(
                        "Stella",
                        "cat",
                        "F",
                        "gray",
                        4,
                        337
                )
        )
)

val credentialsMock = Credentials(
        username = "kfarber",
        password = "Bfdsk4567"
)

val tokenMock = SessionToken(
        tokenId = "jcNDkgU4589",
        userId = 101,
        revoked = false
)
