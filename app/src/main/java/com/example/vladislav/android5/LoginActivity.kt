package com.example.vladislav.android5

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        login_button.setOnClickListener()
        {
            signIn()
        }
        register_button.setOnClickListener()
        {
            signUp()
        }

    }

    public override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            onAuthSuccess(it)
        }
    }

    private fun signIn()
    {
        showProgressDialog()

        val email = username_textview.text.toString()
        val password = password_textview.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                hideProgressDialog()

                if (task.isSuccessful) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                } else {
                    Toast.makeText(baseContext, "Sign In Failed",
                            Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp()
    {
        val email = username_textview.text.toString()
        val password = password_textview.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    onAuthSuccess(task.result?.user!!)
                } else {
                    Toast.makeText(baseContext, "Sign Up Failed",
                            Toast.LENGTH_SHORT).show()
                }
            }
    }
//
    private fun onAuthSuccess(user: FirebaseUser)
    {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private var progressDialog: ProgressDialog? = null


    fun showProgressDialog()
    {
        if (progressDialog == null)
        {
            val pd = ProgressDialog(this)
            pd.setCancelable(false)
            pd.setMessage("Loading...")

            progressDialog = pd
        }

        progressDialog?.show()
    }

    fun hideProgressDialog()
    {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}
