package com.kg.pets.repo

import com.kg.pets.controllers.Pets
import com.kg.pets.models.Pet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PetsRepository : JpaRepository<Pet, Long> {

    fun findAllByOrderByPetId(): List<Pet>

    fun findPetByOwnerId(ownerId: Long): List<Pet>

    @Query(value = "SELECT OWNER_ID FROM PETS", nativeQuery = true)
    fun findAllOwnerIds(): List<Long>
}