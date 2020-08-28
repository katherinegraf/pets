package com.kg.pets.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kg.pets.models.*
import com.kg.pets.repo.PetsRepository
import com.kg.pets.services.RestApiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger

@RestController
class Pets {

    val mapper = jacksonObjectMapper()

    val logger = Logger.getLogger("logger")

    @Autowired
    val petsRepo: PetsRepository? = null

    @GetMapping("pets")
    @ResponseBody
    fun getAllPets(): ResponseEntity<MutableList<Pet>>? {
        return petsRepo?.findAll()?.let { ResponseEntity.ok(it) }
        // .let calls the RE block with the preceding 'this/it' as the argument
    }

    @GetMapping("pets/{id}")
    @ResponseBody
    fun getPetById(
            @PathVariable id: Long
    ): ResponseEntity<List<Pet?>> {
        val foundPet = petsRepo?.findByIdOrNull(id)
        return if (foundPet != null) {
            ResponseEntity.ok(listOf(foundPet))
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("pets/create")
    fun createPet(
            @RequestBody pet: Pet
    ): ResponseEntity<Pet> {
        logger.info(pet.toString())
        return if (noNullsValidator(mapOfPet(pet))) {
            petsRepo?.save(pet)
            ResponseEntity.ok(pet)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PatchMapping("pets/edit/{id}")
    fun editPet(
            @PathVariable id: Long,
            @RequestBody petEdits: Map<String, Any>
    ): ResponseEntity<List<Pet?>> {
        val editedPet = petsRepo?.findByIdOrNull(id)
        if (editedPet != null) {
            // alternate syntax: for (e in petEdits) when (e.key) {
            petEdits.forEach { e ->
                when (e.key) {
                    "name" -> editedPet.name = e.value.toString()
                    "age" -> editedPet.age = e.value.toString().toLong()
                    "gender" -> editedPet.gender = e.value.toString()
                    "color" -> editedPet.color = e.value.toString()
                    "type" -> editedPet.type = e.value.toString()
                }
                petsRepo?.save(editedPet)
                return ResponseEntity.ok(listOf(editedPet))
            }
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("pets/{id}")
    fun deletePet(
            @PathVariable("id") id: Long
    ): ResponseEntity<List<Pet>> {
        val foundPet = petsRepo?.findByIdOrNull(id)
        return if (foundPet != null) {
            petsRepo?.delete(foundPet)
            ResponseEntity.ok(listOf(foundPet))
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    fun mapOfPet(pet: Pet): Map<String, Any> {
        return mapOf(
                "name" to pet.name,
                "age" to pet.age,
                "gender" to pet.gender,
                "color" to pet.color,
                "type" to pet.type
        )
    }

    fun noNullsValidator(map: Map<String, Any?>): Boolean {
        // need to figure out how to prevent allowing 'null' in Long type - change types, perhaps?
        var result = true
        loop@ for (e in map) {
            if (e.value == null) {
                result = false
                break@loop
            }
        }
        return result;
    }

    @GetMapping("pets/ownedBy/{ownerId}")
    fun getPetsForUser(
            @PathVariable ownerId: Long
    ): ResponseEntity<Any> {
        val foundPets = petsRepo?.findPetByOwnerId(ownerId)
        var listOfOwnedPets: MutableList<OwnedPets> = mutableListOf()
        return if (foundPets != null) {
            listOfOwnedPets = givenPetListReturnOwnedPets(foundPets)
            val owner = RestApiService.getUserByAPI(ownerId)
            return if (owner != null) {
                val userAndPets = UserWithListOfOwnedPets(
                        owner = owner.firstName.plus(" ").plus(owner.lastName),
                        userId = owner.userId,
                        petsOwned = listOfOwnedPets
                )
                ResponseEntity.ok(userAndPets)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    fun givenPetListReturnOwnedPets(pets: List<Pet>): MutableList<OwnedPets> {
        val listOfOwnedPets: MutableList<OwnedPets> = mutableListOf()
        pets.forEach { e ->
            val buildOwnedPet = OwnedPets(
                    name = e.name,
                    type = e.type,
                    gender = e.gender,
                    color = e.color,
                    age = e.age,
                    petId = e.petId
            )
            listOfOwnedPets.add(buildOwnedPet)
        }
        return listOfOwnedPets
    }

}


