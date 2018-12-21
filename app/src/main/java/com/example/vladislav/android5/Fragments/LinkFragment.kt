package com.example.vladislav.android5.Fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.vladislav.android5.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.link_fragment.*

class LinkFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)


        val view = inflater.inflate(R.layout.link_fragment, container, false)

        val rssLinkEdit = view.findViewById<EditText>(R.id.apply_text_btn)

        return view


        //return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val rec = sharedPref?.getString("RSS_URL_STRING", "https://lenta.ru/rss")
        link_edit.setText(rec)

        apply_text_btn.setOnClickListener()
        {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val editor = sharedPref?.edit()
            editor?.putString("RSS_URL_STRING", link_edit.text.toString())
            editor?.apply()

            Toast.makeText(context, "Link successfully saved", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.rss_dest)

        }

    }

}