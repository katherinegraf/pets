package com.kg.pets.services

import com.kg.pets.models.Pet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RestApiServiceTests {
    @Autowired
    private lateinit var restApiService: RestApiService

    fun getUserCreatedPetTest() {
        val ownerId: Long = 72
        val result = restApiService.getUserCreatedPet(ownerId)
        assert(result.name == "Nymeria")
        assert(result.ownerId == ownerId)
    }

    // TODO - not well written; could easily fail
    fun validateSessionTest() {
        val userId: Long = 101
        val result = restApiService.validateSession(userId)
        assert(result)
    }

    fun validateSessionTokenTest() {

    }
}