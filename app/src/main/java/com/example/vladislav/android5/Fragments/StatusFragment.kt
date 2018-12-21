package com.example.vladislav.android5.Fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.vladislav.android5.R

class StatusFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.status_fragment, container, false)
    }

}