package dev.kavrin.spring_boot_crash_course.database.repository

import dev.kavrin.spring_boot_crash_course.database.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
}