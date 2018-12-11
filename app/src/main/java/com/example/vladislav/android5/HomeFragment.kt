package com.example.vladislav.android5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.util.*


val REQUEST_IMAGE_CAPTURE = 1
val REQUEST_GALLERY_PICK = 9


class HomeFragment : Fragment() {

    var mCurrentPhotoPath : String = ""

    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        retainInstance = true
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        Snackbar.make(view, "HELLO HANDLER INSTALLED", Snackbar.LENGTH_LONG).show()
        camera_button.setOnClickListener{
            //StartCameraIntent()
            dispatchTakePictureIntent()
        }

        pick_button.setOnClickListener{
            dispatchPickPictureIntent()
        }

        xb.setOnClickListener{
            textView3.text = "SAAAAAAAAAAAAVEME"
        }

        textView3.text = savedInstanceState?.getString("twkey")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null)
        {
            //todo restore fragment here
            textView3.text = savedInstanceState?.getString("twkey")
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("twkey", textView3.text.toString())
        //todo save fragment here
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val temp_file = File(mCurrentPhotoPath)
                val temp_file_uri = Uri.fromFile(temp_file)
                imageView.setImageURI(temp_file_uri)
                //val photo = data?.getExtras()?.get("data") as Bitmap
                //imageView.setImageBitmap(photo)
            }
            else if (requestCode == REQUEST_GALLERY_PICK)
            {
                if(data != null)
                {
                    val photoURI : Uri = data.data!!
                    val bmp : Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, photoURI)
                    imageView.setImageBitmap(bmp)
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

    private fun dispatchPickPictureIntent()
    {
        val frag : Fragment = this
        val intent : Intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                            context!!,
                            "com.example.android.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    frag.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

    }

}