package com.example.inzynierka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.inzynierka.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.button2.setOnClickListener{
            Toast.makeText(context, "click rejstracja", Toast.LENGTH_SHORT).show()
            RegTaskSheet().show(childFragmentManager, "newTaskTag")
        }

        return binding.root
    }
}