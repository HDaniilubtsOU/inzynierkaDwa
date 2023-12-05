package com.example.inzynierka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.inzynierka.databinding.FragmentRegTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class RegTaskSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentRegTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_reg_task_sheet, container, false)
        binding = FragmentRegTaskSheetBinding.inflate(inflater, container, false)

        binding.saveButton.setOnClickListener{
            Toast.makeText(context, "click save", Toast.LENGTH_SHORT).show()
            saveAction()
        }
        return binding.root
    }

    private fun saveAction()
    {
        taskViewModel.name.value = binding.name.text.toString()
        taskViewModel.surname.value = binding.surname.text.toString()
        taskViewModel.mail.value = binding.mail.text.toString()
        taskViewModel.number.value = binding.number.text.toString()
        binding.name.setText("")
        binding.surname.setText("")
        binding.mail.setText("")
        binding.number.setText("")
        dismiss()
    }
}