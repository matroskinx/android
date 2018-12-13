package com.example.vladislav.android5

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DbManager
{
    public val mDatabase = FirebaseDatabase.getInstance().getReference()
    public val mUsersRef = mDatabase.child("users")

    public fun getUserFromDb(view : IDb)
    {
        val userListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)

                view.setUserFields(user!!)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        val currentUserRef = mUsersRef.child("Vladislav")

        currentUserRef.addValueEventListener(userListener)
    }

}