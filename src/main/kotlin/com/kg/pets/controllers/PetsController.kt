package com.kg.pets.controllers

import com.kg.pets.models.Pets
import com.kg.pets.repo.PetsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Pets {

    @Autowired
    val petsRepo: PetsRepository? = null

    @GetMapping("pets")
    @ResponseBody
    fun getAllPets(): MutableList<Pets>? {
        return petsRepo?.findAll()
    }



}