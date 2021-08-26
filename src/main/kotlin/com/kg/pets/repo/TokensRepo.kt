package com.kg.pets.repo

import com.kg.pets.models.SessionToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TokensRepository : JpaRepository<SessionToken, Long> {

    fun findByTokenId(tokenId: String): SessionToken?

    @Query("FROM SessionToken WHERE revoked = false AND userId = :userId")
    fun findCurrentTokenForUserId(@Param("userId") userId: Long): SessionToken?
}