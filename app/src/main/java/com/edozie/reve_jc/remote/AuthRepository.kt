package com.edozie.reve_jc.remote

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun signUp(email: String, pass: String): Result<FirebaseUser>
    suspend fun login(email: String, pass: String): Result<FirebaseUser>
    fun logout()
    val currentUser: FirebaseUser?
}