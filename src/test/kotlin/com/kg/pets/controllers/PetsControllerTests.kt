package com.kg.pets.controllers

import com.kg.pets.mocks.*
import com.kg.pets.models.Credentials
import com.kg.pets.models.Pet
import com.kg.pets.models.User
import com.kg.pets.models.UserWithListOfOwnedPets
import com.kg.pets.services.EncryptionService
import com.kg.pets.services.RestApiService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.apiguardian.api.API
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.mockito.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

var createdPetId: Long = 0

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PetsControllerTests {

    val mockApiService = Mockito.mock(RestApiService::class.java)

    val logger: Logger = Logger.getLogger("logger")

    @Autowired
    private lateinit var pets: Pets
    //@Mock
    //private lateinit var mockApiService : RestApiService

    @Test
    @Order(1)
    fun getAllPetsTest() {
        val result = pets.getAllPets()
        val resultBody = result.body as List<Pet>
        assert(result.statusCodeValue == 200)
        assert(resultBody[0].name == petMock.name)
    }

    @Test
    @Order(2)
    fun getPetById_success() {
        val result = pets.getPetById(17)
        val resultBody = result.body as Pet
        assert(result.statusCodeValue == 200)
        assert(resultBody.name == petMock.name)
    }

    @Test
    @Order(3)
    fun getPetById_failure() {
        val result = pets.getPetById(300)
        assert(result.statusCodeValue == 404)
    }

    @Test
    @Order(4)
    fun returnUserWithPets() {
        val result = pets.givenUserAndPetListReturnUserWithPets(userMock, listOf(petMock))
        assert(result.owner == userMock.firstName.plus(" ").plus(userMock.lastName))
    }

    @Test
    @Order(5)
    fun createPetTest() {
        val result = pets.createPet(petMock)
        val resultBody = result.body as Pet
        createdPetId = resultBody.petId
        assert(result.statusCodeValue == 200)
        assert(result.body is Pet)
        assert(resultBody.name == petMock.name)
    }

    @Test
    @Order(6)
    fun editPetTest() {
        val result = pets.editPet(createdPetId, petEditsMock)
        val resultBody = result.body
        assert(result.statusCodeValue == 200)
        assert(result.body is Pet)
        assert(resultBody?.name != petMock.name)
        assert(resultBody?.name == petEditsMock.getValue("name"))
        assert(resultBody?.gender != petMock.gender)
    }

    @Test
    @Order(7)
    fun deletePetTest() {
        val result = pets.deletePet(createdPetId)
        val resultBody = result.body
        assert(result.statusCodeValue == 200)
        assert(result.body is Pet)
        assert(resultBody?.name == petEditsMock.getValue("name"))
        val findAfterDelete = pets.getPetById(createdPetId)
        assert(findAfterDelete.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    @Order(8)
    fun checkHashedPWTest() {
        val result = pets.checkHashedPW()
        whenever(mockApiService.getUserCredentialsFromLogin()).thenReturn(credentialsMock)
        val pw = mockApiService.getUserCredentialsFromLogin().password
        logger.info("mocked pw is $pw; standard is QmlAKqf")
        assert(result != pw)
    }

    @Test
    @Order(9)
    fun saveUserCreatedPetTest_withActiveToken() {

        // TODO don't do integration testing!

        whenever(mockApiService.validateSession(101)).thenReturn(true)
        val result = pets.saveUserCreatedPet(101)
        val resultBody = result.body
        logger.info(result.statusCodeValue.toString())
        assert(result.statusCodeValue == 200)
        assert(resultBody is Pet)
        val findAfterSave = pets.getPetById(resultBody!!.petId)
        assert(findAfterSave.statusCodeValue == 200)
    }

    @Test
    @Order(10)
    fun saveUserCreatedPetTest_requestNewToken() {
        whenever(mockApiService.validateSession(101)).thenReturn(false)
        whenever(mockApiService.requestToken(101, credentialsMock.username, credentialsMock.password))
                .thenReturn(tokenMock)
        val result = pets.saveUserCreatedPet(101)
        val resultBody = result.body
        assert(result.statusCodeValue == 200)
        assert(resultBody is Pet)
        val findAfterSave = pets.getPetById(resultBody!!.petId)
        assert(findAfterSave.statusCodeValue == 200)
    }

    @Test
    @Order(11)
    fun saveUserCreatedPetTest_unauthorized() {
        whenever(mockApiService.validateSession(101)).thenReturn(false)
        whenever(mockApiService.requestToken(
                101, credentialsMock.username, credentialsMock.password))
                .thenReturn(null)
        val result = pets.saveUserCreatedPet(101)
        assert(result.statusCode == HttpStatus.UNAUTHORIZED)
    }


}