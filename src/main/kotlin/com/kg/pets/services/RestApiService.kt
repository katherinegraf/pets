package com.kg.pets.services

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.kg.pets.models.*
import com.kg.pets.repo.TokensRepository
import com.kg.pets.utils.USERS_API_BASE_URL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.util.logging.Logger


@Service
class RestApiService {

    @Autowired
    private lateinit var tokenRepo: TokensRepository

    val logger = Logger.getLogger("logger")

    fun getUserByAPI(id: Long): User? {
//        val (_, _, result) = USERS_API_BASE_URL.plus(id)
//                .httpGet()
//                .responseObject(User.Deserializer())
//
//        return when (result) {
//            is Result.Failure -> {
//                val exception = result.getException()
//                null
//            }
//            is Result.Success -> {
//                val (user) = result
//                user
//            }
//        }
        logger.info("inside restAPI getUserByAPI")
        return User(
                111,
                "Brenda",
                "Rumin",
                "crumina@cyberchimps.com",
                "crumina",
                "7ee4Vl",
                "172.20.95.218"
        )
    }

    // TODO - 4/5 - should this take a path variable if it isn't an endpoint?
    fun validateSession(
            @PathVariable id: Long
    ): Boolean {
        val currentTokenId = tokenRepo.findCurrentTokenForUserId(id)?.tokenId
        return if (currentTokenId != null) {
            validateSessionToken(currentTokenId)
        } else {
            false
        }
    }

    fun validateSessionToken(tokenId: String): Boolean {
        val (_, _, result) = USERS_API_BASE_URL.plus("validateSessionToken")
                .httpGet()
                .header("TokenId" to tokenId)
                .responseString()

        return when (result) {
            is Result.Failure -> {
                val exception = result.getException()
                logger.warning("Exception is ${exception.toString()}")
                val token = tokenRepo.findByTokenId(tokenId)
                token!!.revoked = true
                tokenRepo.save(token)
                false
            }
            is Result.Success -> {
                val (_) = result
                true
            }
        }
    }

    fun getUserCredentialsFromLogin(): Credentials {
        // hard-coded credentials due to lack of front-end log-in feature
        return Credentials(
                username = "dshier0",
                password = "QmlAKqf"
        )
    }

    fun requestToken(id: Long, username: String, password: String): SessionToken? {
        val (_, _, result) = USERS_API_BASE_URL.plus("provideToken")
                .httpGet()
                .header("User-ID" to id,
                        "Secret1" to username,
                        "Secret2" to password)
                .responseObject(SessionToken.Deserializer())

        return when (result) {
            is Result.Failure -> {
                val exception = result.getException()
                logger.warning("Exception is ${exception.toString()}")
                null
            }
            is Result.Success -> {
                val (token) = result
                if (token != null) {
                    tokenRepo.save(token)
                }
                token
            }
        }
    }

    fun getUserCreatedPet(ownerId: Long): Pet {
        // hard-coded to fake user input from front-end
        return Pet(
                name = "Nymeria",
                ownerId = ownerId,
                age = 1,
                gender = "F",
                color = "grey",
                type = "dog"
        )
    }

    fun givenPetListReturnOwnedPets(pets: List<Pet>): MutableList<OwnedPets> {
        val listOfOwnedPets: MutableList<OwnedPets> = mutableListOf()
        pets.forEach { p ->
            val ownedPet = OwnedPets(
                    name = p.name,
                    type = p.type,
                    gender = p.gender,
                    color = p.color,
                    age = p.age,
                    petId = p.petId
            )
            listOfOwnedPets.add(ownedPet)
        }
        return listOfOwnedPets
    }

    fun requestCredentials(id: Long): Credentials? {
        val (_, _, result) = USERS_API_BASE_URL.plus("provideCredentials")
                .httpGet()
                .header("User-ID" to id)
                .responseObject(Credentials.Deserializer())

        return when (result) {
            is Result.Failure -> {
                val exception = result.getException()
                logger.warning("Exception is ${exception.toString()}")
                null
            }
            is Result.Success -> {
                val (credentials) = result
                credentials
            }
        }
    }

}
