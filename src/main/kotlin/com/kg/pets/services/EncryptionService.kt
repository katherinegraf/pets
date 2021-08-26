package com.kg.pets.services

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class EncryptionService {
    fun saltAndHash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun hashPW(password: String, salt: String): String {
        return BCrypt.hashpw(password, salt)
    }

}