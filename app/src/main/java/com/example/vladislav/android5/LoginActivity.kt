package com.example.vladislav.android5

import android.app.ProgressDialog
import android.app.ProgressDialog.STYLE_HORIZONTAL
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.vladislav.android5.Managers.DbManager
import com.example.vladislav.android5.Models.User
import com.google.firebase.auth.FirebaseAuth
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
            onAuthSuccess()
        }
    }

    private fun signIn()
    {
        if(validateForm() != true)
            return

        showProgressDialog()

        val email = username_textview.text.toString()
        val password = password_textview.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                hideProgressDialog()

                if (task.isSuccessful)
                {
                    onAuthSuccess()

                } else {
                    Toast.makeText(baseContext, "Sign In Failed",
                            Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp()
    {
        if(validateForm() != true)
            return

        showProgressDialog()

        val email = username_textview.text.toString()
        val password = password_textview.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                hideProgressDialog()

                if (task.isSuccessful)
                {
                    val uid = task.result?.user!!.uid
                    createNewUser(uid = uid, email = email)
                    onAuthSuccess()
                }
                else
                {
                    Toast.makeText(baseContext, "Sign Up Failed",
                            Toast.LENGTH_SHORT).show()
                }
            }
    }
//
    private fun onAuthSuccess()
    {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun createNewUser(uid : String, email : String)
    {
        val dbMan = DbManager()
        val user = User(email = email)

        dbMan.saveUserToDb(uid, user)

    }


    private fun validateForm(): Boolean {
        var isValid = true
        if (TextUtils.isEmpty(username_textview.text.toString())) {
            username_textview.error = "Required"
            isValid = false
        } else
        {
            username_textview.error = null
        }

        if (TextUtils.isEmpty(password_textview.text.toString())) {
            password_textview.error = "Required"
            isValid = false
        } else {
            password_textview.error = null
        }

        return isValid
    }

    private var progressDialog: ProgressDialog? = null


    fun showProgressDialog()
    {
        if (progressDialog == null)
        {
            val pd = ProgressDialog(this)
            pd.isIndeterminate = true
            pd.setProgressStyle(STYLE_HORIZONTAL)
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
