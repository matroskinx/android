package com.example.vladislav.android5.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vladislav.android5.*
import com.example.vladislav.android5.Interfaces.IDb
import com.example.vladislav.android5.Interfaces.ISwitchActionBar
import com.example.vladislav.android5.Interfaces.ISwitchBottomNavigation
import com.example.vladislav.android5.Managers.DbManager
import com.example.vladislav.android5.Models.User
import com.example.vladislav.android5.OnlineUtil.Companion.isOnline
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_fragment.*
import java.io.File
import java.io.IOException
import java.util.*


class UserFragment : Fragment(), IDb {

    private val TAG : String = "UserFragment"

    var bnSwitchClickListener : ISwitchBottomNavigation? = null
    var abSwitchClickListener: ISwitchActionBar? = null


    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY_PICK = 9

    val dbMan = DbManager()
    var mNewPhotoUri : String? = null
    var mCurrentPhotoUri : String? = null


    fun saveToDb()
    {
        val user = User(
                username = name_text_e?.text.toString(),
                last_name = last_name_text_e?.text.toString(),
                email = email_text_e?.text.toString(),
                phone = phone_text_e?.text.toString(),
                image_uri = mCurrentPhotoUri
        )

        //user.image_uri = mCurrentPhotoUri


        Log.d(TAG, "Button+" + "Cur:" + mCurrentPhotoUri + "  " + "New:" + mNewPhotoUri)

        mNewPhotoUri?.let {
            imageView?.setImageURI(Uri.parse(mNewPhotoUri))
            user.image_uri = mNewPhotoUri.toString()
            mCurrentPhotoUri = mNewPhotoUri
            mNewPhotoUri = null
        }

        dbMan.saveUserToDb(user)
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(name_text_e.text.toString())) {
            name_text_e.error = "Required"
            isValid = false
        } else
        {
            name_text_e.error = null
        }

        if (TextUtils.isEmpty(last_name_text_e.text.toString())) {
            last_name_text_e.error = "Required"
            isValid = false
        } else {
            last_name_text_e.error = null
        }

        if (TextUtils.isEmpty(phone_text_e.text.toString())) {
            phone_text_e.error = "Required"
            isValid = false
        } else {
            phone_text_e.error = null
        }

        return isValid
    }


    val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which)
        {
            DialogInterface.BUTTON_POSITIVE ->
            {
                if(validateForm() != false)
                {
                    saveToDb()
                    view_switcher_x.showNext()
                }
            }

            DialogInterface.BUTTON_NEGATIVE ->
            {
                name_text_e.setText(name_text.text)
                last_name_text_e.setText(last_name_text.text)
                email_text_e.setText(email_text.text)
                phone_text_e.setText(phone_text.text)

                //imageView_e.setImageURI(Uri.parse(mCurrentPhotoUri))
                Picasso.with(context).load(Uri.parse(mCurrentPhotoUri)).into(imageView_e)
                Picasso.with(context).load(Uri.parse(mCurrentPhotoUri)).into(imageView)
                view_switcher_x.showNext()
            }
        }
        mNewPhotoUri = null
        bnSwitchClickListener?.showBottomNav()
        abSwitchClickListener?.showActionBarArrow()
    }


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

        mCurrentPhotoUri = user.image_uri

        //imageView.setImageURI(Uri.parse(mCurrentPhotoUri))
        // imageView_e.setImageURI(Uri.parse(mCurrentPhotoUri))

        Picasso.with(context).load(Uri.parse(mCurrentPhotoUri)).into(imageView)
        Picasso.with(context).load(Uri.parse(mCurrentPhotoUri)).into(imageView_e)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mNewPhotoUri = null

        if(isOnline(context as Context))
        {
            dbMan.getUserFromDb(this)
        }
        else
        {
            Toast.makeText(context, "No Internet connection", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.home_dest)
        }

        bnSwitchClickListener = context as ISwitchBottomNavigation
        abSwitchClickListener = context as ISwitchActionBar


        noedit_switch_btn.setOnClickListener{
            view_switcher_x.showNext()
            bnSwitchClickListener?.hideBottomNav()
            abSwitchClickListener?.hideActionBarArrow()
        }


        noedit_switch_btn_e.setOnClickListener{
            //switch back button

            val builder = AlertDialog.Builder(context)

            builder.setMessage("You are leaving user screen")
                    .setPositiveButton("Save and leave", dialogClickListener)
                    .setNegativeButton("Just leave", dialogClickListener)
                    .show()
        }

        camera_button_e.setOnClickListener{
            dispatchTakePictureIntent()
        }

        pick_button_e.setOnClickListener{
            dispatchPickPictureIntent()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val temp_file = File(mNewPhotoUri)
                val temp_file_uri = Uri.fromFile(temp_file)
                imageView_e.setImageURI(temp_file_uri)
            }
            else if (requestCode == REQUEST_GALLERY_PICK)
            {
                val photoURI : Uri? = data?.data
                val bmp : Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, photoURI)
                imageView_e.setImageBitmap(bmp)
                mNewPhotoUri  = photoURI.toString()
                Log.d(TAG, "Gallery_pick" + "Cur:" + mCurrentPhotoUri + "  " + "New:" + mNewPhotoUri)

            }
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
            takePictureIntent.resolveActivity(context?.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            context as Context,"com.example.android.fileprovider", it
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
            mNewPhotoUri = absolutePath
        }
    }
}