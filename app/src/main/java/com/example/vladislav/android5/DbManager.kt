package com.example.vladislav.android5

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DbManager
{

    val mDatabase = FirebaseDatabase.getInstance().getReference()
    val mUsersRef = mDatabase.child("users")

    val user_id: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var current_user : User

    fun getUserFromDb(view : IDb)
    {
        val userListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                current_user = p0.getValue(User::class.java)!!

                view.setUserFields(current_user)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        val currentUserRef = mUsersRef.child(user_id)

        currentUserRef.addValueEventListener(userListener)
    }

    fun saveUserToDb(user : User)
    {
        mUsersRef.child(user_id).setValue(user)
    }

}