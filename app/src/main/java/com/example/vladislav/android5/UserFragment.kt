package com.example.vladislav.android5

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_fragment.*
import java.io.File
import java.io.IOException
import java.util.*


class UserFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY_PICK = 9

    var mCurrentPhotoPath : String = ""

    lateinit var currentUser : User

    var mNewPhotoUri : String? = null

    val mDatabase = FirebaseDatabase.getInstance().getReference()
    val mUsersRef = mDatabase.child("users")


    private lateinit var userView : View

    val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
            {

                currentUser.username = name_text_e?.text.toString()
                currentUser.last_name = last_name_text_e?.text.toString()
                currentUser.email = last_name_text_e?.text.toString()
                currentUser.phone = last_name_text_e?.text.toString()

                if(mNewPhotoUri != null)
                {
                    imageView?.setImageURI(Uri.parse(mNewPhotoUri))
                    currentUser.image_uri = mNewPhotoUri
                }
                //currentUser.image_uri = last_name_text_e?.text.toString()
//                val user = User(
//                        name_text_e.text.toString(),
//                        last_name_text_e.text.toString(),
//                        email_text_e.text.toString(),
//                        phone_text_e.text.toString(),
//                        currentUser.image_uri
//                        )
//
//                mUsersRef.child("Vladislav").setValue(user)
                mUsersRef.child("Vladislav").setValue(currentUser)
            }

            DialogInterface.BUTTON_NEGATIVE ->
            {
                name_text_e.setText(name_text.text)
                last_name_text_e.setText(last_name_text.text)
                email_text_e.setText(email_text.text)
                phone_text_e.setText(phone_text.text)
                imageView_e.setImageURI(Uri.parse(currentUser.image_uri))
            }
        }
        view_switcher_x.showNext()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.user_fragment, container, false)
    }

/*    override fun setUserFields(user: User)
    {
        name_text?.text = user.username
        name_text_e?.setText(user.username)

        last_name_text?.text = user.last_name
        last_name_text_e?.setText(user.last_name)

        phone_text?.text = user.phone
        phone_text_e?.setText(user.phone)

        email_text?.text = user.email
        email_text_e?.setText(user.email)
    }*/

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

/*        val dbMan = DbManager()

        dbMan.getUserFromDb(this)*/


        val userListener = object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)
                if(user != null) {
                    currentUser = user
                }
                user?.let {
                    name_text?.text = currentUser.username
                    name_text_e?.setText(currentUser.username)

                    last_name_text?.text = it.last_name
                    last_name_text_e?.setText(it.last_name)

                    phone_text?.text = it.phone
                    phone_text_e?.setText(it.phone)

                    email_text?.text = it.email
                    email_text_e?.setText(it.email)

                    val photoUri = Uri.parse(currentUser.image_uri)
                    imageView?.setImageURI(photoUri)
                    imageView_e?.setImageURI(photoUri)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        val currentUserRef = mUsersRef.child("Vladislav")


        currentUserRef.addValueEventListener(userListener)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        noedit_switch_btn.setOnClickListener{
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
            //StartCameraIntent()
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
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
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
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //val storageDir: File? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
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



                //currentUser.image_uri = temp_file_uri.toString()
                //val photo = data?.getExtras()?.get("data") as Bitmap
                //imageView.setImageBitmap(photo)
            }
            else if (requestCode == REQUEST_GALLERY_PICK)
            {
                if(data != null)
                {
                    val photoURI : Uri = data.data!!


                    val bmp : Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, photoURI)

                    imageView_e.setImageBitmap(bmp)

                    mNewPhotoUri  = photoURI.toString()


                    //currentUser.image_uri = photoURI.toString()
                }
            }

            //mUsersRef.child("Vladislav").setValue(currentUser)


        }
    }
}