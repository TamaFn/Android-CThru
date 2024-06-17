package com.example.finalproject_cthru.data.local.auth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.PropertyName

data class User(
    val id : String,
    val fullName : String,
    val email : String
)

// Penyesuaian local data ke network data
fun FirebaseUser?.toUser() = this?.let {
    User(
        id = this.uid,
        fullName = this.displayName.orEmpty(),
        email = this.email.orEmpty()
    )
}


data class Users(
    @PropertyName("fullNameUser") var fullNameUser: String? = null,
    @PropertyName("phoneNumber") var phoneNumber: String? = null,
    @PropertyName("age") var age: String? = null,
    @PropertyName("imageUrl") var imageUrl: String? = null,
)