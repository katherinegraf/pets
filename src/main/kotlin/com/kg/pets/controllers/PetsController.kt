package com.kg.pets.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kg.pets.models.*
import com.kg.pets.repo.PetsRepository
import com.kg.pets.repo.StoredCredentialsRepo
import com.kg.pets.repo.TokensRepository
import com.kg.pets.services.EncryptionService
import com.kg.pets.services.RestApiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger
import kotlin.system.exitProcess

@RestController
class Pets (){
    val mapper = jacksonObjectMapper()

    val logger = Logger.getLogger("logger")

    @Autowired
    private lateinit var petsRepo: PetsRepository
    @Autowired
    private lateinit var restApiService: RestApiService
    @Autowired
    private lateinit var tokenRepo: TokensRepository
    @Autowired
    private lateinit var encryptionService: EncryptionService
    @Autowired
    private lateinit var storedCredentialsRepo: StoredCredentialsRepo

    @GetMapping("pets")
    @ResponseBody
    fun getAllPets(): ResponseEntity<List<Pet>> {
        val petList = petsRepo.findAllByOrderByPetId()
        return ResponseEntity.ok(petList)
    }

    @GetMapping("pets/{id}")
    @ResponseBody
    fun getPetById(
            @PathVariable id: Long
    ): ResponseEntity<Pet> {
        val foundPet = petsRepo.findByIdOrNull(id)
        return if (foundPet != null) {
            ResponseEntity.ok(foundPet)
        } else {
            // TODO throw Exception("Requested pet was not found")
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("pets/getUser")
    fun getUser(): User? {
        return restApiService.getUserByAPI(111)
    }

    @PostMapping("pets/create")
    fun createPet(
            @RequestBody pet: Pet
    ): ResponseEntity<Pet> {
        return if (containsNoDefaultVals(pet)) {
            petsRepo.save(pet)
            ResponseEntity.ok(pet)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PatchMapping("pets/edit/{id}")
    fun editPet(
            @PathVariable id: Long,
            @RequestBody petEdits: Map<String, Any>
    ): ResponseEntity<Pet?> {
        val editedPet = petsRepo.findByIdOrNull(id)
        if (editedPet != null) {
            petEdits.forEach { e ->
                when (e.key) {
                    "name" -> editedPet.name = e.value.toString()
                    "age" -> editedPet.age = e.value.toString().toLong()
                    "gender" -> editedPet.gender = e.value.toString()
                    "color" -> editedPet.color = e.value.toString()
                    "type" -> editedPet.type = e.value.toString()
                }
            }
            petsRepo.save(editedPet)
            return ResponseEntity.ok(editedPet)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("pets/{id}")
    fun deletePet(
            @PathVariable("id") id: Long
    ): ResponseEntity<Pet> {
        val foundPet = petsRepo.findByIdOrNull(id)
        return if (foundPet != null) {
            petsRepo.delete(foundPet)
            ResponseEntity.ok(foundPet)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("pets/ownedBy/{ownerId}")
    fun getPetsForUser(
            @PathVariable ownerId: Long
    ): ResponseEntity<UserWithListOfOwnedPets> {
        val foundPets = petsRepo.findPetByOwnerId(ownerId)
        val owner = restApiService.getUserByAPI(ownerId)
        return if (owner != null) {
            ResponseEntity.ok(givenUserAndPetListReturnUserWithPets(owner, foundPets))
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    fun givenUserAndPetListReturnUserWithPets(user: User, pets: List<Pet>): UserWithListOfOwnedPets {
        return UserWithListOfOwnedPets(
                owner = user.firstName.plus(" ").plus(user.lastName),
                petsOwned = restApiService.givenPetListReturnOwnedPets(pets)
        )
    }

    @PostMapping("pets/users/{id}/savePet")
    fun saveUserCreatedPet(
            @PathVariable id: Long
    ): ResponseEntity<Pet> {
        val pet = restApiService.getUserCreatedPet(id)
        return if (restApiService.validateSession(id)) {
            logger.info("session confirmed")
            petsRepo.save(pet)
            ResponseEntity.ok(pet)
        } else {
            val credentials = restApiService.getUserCredentialsFromLogin()
            val username = credentials.username
            val password = credentials.password

            val newToken = restApiService.requestToken(id, username, password)
            if (newToken != null) {
                logger.info("received new token")
                petsRepo.save(pet)
                ResponseEntity.ok(pet)
            } else {
                logger.info("unable to get token")
                ResponseEntity(HttpStatus.UNAUTHORIZED)
            }
        }
    }

    @GetMapping("pets/checkHashedPW")
    fun checkHashedPW(): String {
        val pw = restApiService.getUserCredentialsFromLogin().password
        return encryptionService.saltAndHash(pw)
    }

    // TODO use hashed pw throughout app; use BCrypt's checkpw method

    fun containsNoDefaultVals(pet: Pet): Boolean {
        val defaultVal: Long = 9000
        return if (pet.age == defaultVal) {
            false
        } else pet.ownerId != defaultVal
    }

    // ONE-TIME-USE ENDPOINT TO POPULATE STORED_CREDENTIALS TABLE
    // substitute for processing credentials input during non-existent login
    @GetMapping("pets/saltHash")
    fun addSaltHashToDb() {
        val petOwnerIds = petsRepo.findAllOwnerIds()
        petOwnerIds.forEach{ o ->
            val storedOwnerIds = storedCredentialsRepo.findAllOwnerIds()
            if (storedOwnerIds.contains(o)) {
            } else {
                val credentials = restApiService.requestCredentials(o)
                val salt = BCrypt.gensalt()
                val credentialsToStore = StoredCredentials(
                        ownerId = o,
                        username = credentials!!.username,
                        salt = salt,
                        pwHash = encryptionService.hashPW(credentials.password, salt)
                )
                storedCredentialsRepo.save(credentialsToStore)
            }
        }

    }

}
