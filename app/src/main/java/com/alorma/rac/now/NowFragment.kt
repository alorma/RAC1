package com.alorma.rac.now

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment

class NowFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(requireContext(), "Now", Toast.LENGTH_SHORT).show()
    }
}