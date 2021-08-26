package com.kg.pets.repo

import com.kg.pets.models.StoredCredentials
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StoredCredentialsRepo : JpaRepository<StoredCredentials, Long> {

    @Query(value = "SELECT OWNER_ID FROM STORED_CREDENTIALS", nativeQuery = true)
    fun findAllOwnerIds(): List<Long>

}