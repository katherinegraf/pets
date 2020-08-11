package com.kg.pets.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kg.pets.models.Pet
import com.kg.pets.repo.PetsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.logging.Logger

@RestController
class Pets {

    val mapper = jacksonObjectMapper()

    val logger = Logger.getLogger("logger")

    @Autowired
    val petsRepo: PetsRepository? = null

    @GetMapping("pets")
    @ResponseBody
    fun getAllPets(): MutableList<Pet>? {
        return petsRepo?.findAll()
    }

    @GetMapping("pets/{id}")
    @ResponseBody
    fun getPetById(
            @PathVariable id: Long
    ): Optional<Pet>? {
        return petsRepo?.findById(id)
    }

    @PostMapping("pets/create")
    fun createPet(
            @RequestBody pet: Pet
    ): ResponseEntity<List<Pet>> {
        return if (!noNullsValidator(mapOfPet(pet))) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } else {
            petsRepo?.save(pet)
            ResponseEntity.ok(listOf(pet))
        }
    }

    @PutMapping("pets/edit/{id}")
    fun editPet(
            @PathVariable id: Long,
            @RequestBody petEdits: Map<String, Any>
    ): ResponseEntity<List<Pet?>> {
        var editedPet = petsRepo?.findByIdOrNull(id)
        if (editedPet != null) {
            for (e in petEdits) {
                when (e.key) {
                    "name" -> editedPet.name = petEdits.getValue("name").toString()
                    "age" -> editedPet.age = petEdits.getValue("age").toString().toLong()
                    "gender" -> editedPet.gender = petEdits.getValue("gender").toString()
                    "color" -> editedPet.color = petEdits.getValue("color").toString()
                    "type" -> editedPet.type = petEdits.getValue("type").toString()
                    }
                }
            petsRepo?.save(editedPet)
        }
        return ResponseEntity.ok(listOf(editedPet))
    }

    @DeleteMapping("pets/{id}")
    fun deletePet(
            @PathVariable ("id") id: Long
    ): Unit {
        return petsRepo!!.deleteById(id)
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

    fun noNullsValidator(map: Map<String,Any?>): Boolean {
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

    // "method overrides" - override a save method to add in id
    //      would apply to all saves of that object type (i think) in that repo

}
