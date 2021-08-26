package com.kg.pets.services

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.logging.Logger

@SpringBootTest
class EncryptionServiceTests {

    val logger = Logger.getLogger("logger")

    @Autowired
    private lateinit var encryptionService: EncryptionService

    @Test
    fun saltAndHashTest() {
        val pw = "QmlAKqf"
        val result = encryptionService.saltAndHash(pw)
        assert(result != pw)
    }

}