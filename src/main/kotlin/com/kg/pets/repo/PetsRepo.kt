package com.kg.pets.repo

import com.kg.pets.controllers.Pets
import com.kg.pets.models.Pet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PetsRepository : JpaRepository<Pet, Long> {

    fun findPetByOwnerId(ownerId: Long): List<Pet>
}