package com.example.vladislav.android5

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.user_fragment.*
import java.io.File
import java.io.IOException
import java.util.*


class UserFragment : Fragment(), IDb {

    val uid: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

//    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY_PICK = 9

    var mCurrentPhotoPath : String = ""

    lateinit var currentUser : User

    val dbMan = DbManager()

    var mNewPhotoUri : String? = null


    //lateinit var actvt : MainActivity


    fun saveToDb()
    {
        val user = User(
                username = name_text_e?.text.toString(),
                last_name = last_name_text_e?.text.toString(),
                email = email_text_e?.text.toString(),
                phone = phone_text_e?.text.toString()
        )
//                currentUser.username = name_text_e?.text.toString()
//                currentUser.last_name = last_name_text_e?.text.toString()
//                currentUser.email = email_text_e?.text.toString()
//                currentUser.phone = phone_text_e?.text.toString()

        if(mNewPhotoUri != null)
        {
            imageView?.setImageURI(Uri.parse(mNewPhotoUri))
            user.image_uri = mNewPhotoUri
        }

        dbMan.saveUserToDb(user)
    }


    val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which)
        {
            DialogInterface.BUTTON_POSITIVE ->
            {
                saveToDb()
            }

            DialogInterface.BUTTON_NEGATIVE ->
            {
                name_text_e.setText(name_text.text)
                last_name_text_e.setText(last_name_text.text)
                email_text_e.setText(email_text.text)
                phone_text_e.setText(phone_text.text)
                imageView_e.setImageURI(Uri.parse(dbMan.current_user.image_uri))
            }
        }
        view_switcher_x.showNext()
    }

/*    val bottomNavClickListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        val builder = AlertDialog.Builder(context)

        builder.setMessage("Do you want to save information?")
                .setPositiveButton("YES", dialogClickListener)
                .setNegativeButton("NO", dialogClickListener)
                .show()

        when (item.itemId) {
            R.id.home_dest -> {
                actvt.navController.navigate(R.id.home_dest)
                return@OnNavigationItemSelectedListener true
            }
            R.id.status_dest -> {
                // TODO
                actvt.navController.navigate(R.id.status_dest)
                return@OnNavigationItemSelectedListener true
            }
            R.id.user_dest -> {
                actvt.navController.navigate(R.id.user_dest)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }*/

    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.user_fragment, container, false)
    }

    override fun setUserFields(user: User)
    {
        name_text?.text = user.username
        name_text_e?.setText(user.username)

        last_name_text?.text = user.last_name
        last_name_text_e?.setText(user.last_name)

        phone_text?.text = user.phone
        phone_text_e?.setText(user.phone)

        email_text?.text = user.email
        email_text_e?.setText(user.email)

        imageView.setImageURI(Uri.parse(user.image_uri))
        imageView_e.setImageURI(Uri.parse(user.image_uri))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val user = User(
                username = "Vlad",
                last_name = "Kirianov",
                email = "matroskinx@gmail.com",
                phone = "+375291954890"
        )


        dbMan.getUserFromDb(this)


        noedit_switch_btn.setOnClickListener{
            //actvt.bottom_bar.setOnNavigationItemSelectedListener(bottomNavClickListener)
            view_switcher_x.showNext()
        }

        noedit_switch_btn_e.setOnClickListener{
            //switch back button

            val builder = AlertDialog.Builder(context)

            builder.setMessage("Do you want to save information?")
                    .setPositiveButton("YES", dialogClickListener)
                    .setNegativeButton("NO", dialogClickListener)
                    .show()
        }

        camera_button_e.setOnClickListener{
            dispatchTakePictureIntent()
        }

        pick_button_e.setOnClickListener{
            dispatchPickPictureIntent()
        }


/*        val userListener = object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)
                if(user != null) {
                    currentUser = user
                }
                if(user != null)
                {
                    name_text?.text = currentUser.username
                    name_text_e?.setText(currentUser.username)

                    last_name_text?.text = currentUser.last_name
                    last_name_text_e?.setText(currentUser.last_name)

                    phone_text?.text = currentUser.phone
                    phone_text_e?.setText(currentUser.phone)

                    email_text?.text = currentUser.email
                    email_text_e?.setText(currentUser.email)

                    val a = currentUser.image_uri
                    val photoUri = Uri.parse(currentUser.image_uri)
                    imageView?.setImageURI(photoUri)
                    imageView_e?.setImageURI(photoUri)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }*/

/*        val currentUserRef = mUsersRef.child("Vladislav")

        currentUserRef.addValueEventListener(userListener)*/

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        noedit_switch_btn.setOnClickListener{
            //actvt.bottom_bar.setOnNavigationItemSelectedListener(bottomNavClickListener)
            view_switcher_x.showNext()
        }

        noedit_switch_btn_e.setOnClickListener{
            //switch back button

            val builder = AlertDialog.Builder(context)

            builder.setMessage("Do you want to save information?")
                    .setPositiveButton("YES", dialogClickListener)
                    .setNegativeButton("NO", dialogClickListener)
                    .show()
        }

        camera_button_e.setOnClickListener{
            dispatchTakePictureIntent()
        }

        pick_button_e.setOnClickListener{
            dispatchPickPictureIntent()
        }


    }

    private fun dispatchPickPictureIntent()
    {
        val frag : Fragment = this
        val intent : Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        frag.startActivityForResult(intent, REQUEST_GALLERY_PICK)

    }

    private fun dispatchTakePictureIntent() {

        val frag : Fragment = this

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            context!!,"com.example.android.fileprovider", it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    frag.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
        ).apply {
            mCurrentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val temp_file = File(mCurrentPhotoPath)
                val temp_file_uri = Uri.fromFile(temp_file)
                imageView_e.setImageURI(temp_file_uri)
                mNewPhotoUri  = temp_file_uri.toString()
            }
            else if (requestCode == REQUEST_GALLERY_PICK)
            {
                val photoURI : Uri? = data?.data
                val bmp : Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, photoURI)
                imageView_e.setImageBitmap(bmp)
                mNewPhotoUri  = photoURI.toString()

            }
        }
    }
}