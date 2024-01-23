package com.example.inzynierka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
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
            if (binding.name.text!!.isEmpty() || binding.surname.text!!.isEmpty() ||
                binding.mail.text!!.isEmpty() || binding.number.text!!.isEmpty()){
                Toast.makeText(context, "Do not empty!", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(context, "click save", Toast.LENGTH_SHORT).show()
                saveAction()
            }

        }

        fun isValidEmail(email: String): Boolean {
            val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
            return emailRegex.matches(email)
        }
        binding.name.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.nameContainerSheet.error = "Pole nie może być pustym"
            } else if (text!!.any { it.isDigit() }){
                binding.nameContainerSheet.error = "Nie możesz używać liczb"
            } else {
                binding.nameContainerSheet.helperText = "OK"
            }
        }
        binding.surname.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.surnameContainerSheet.error = "Pole nie może być pustym"
            } else if (text!!.any { it.isDigit() }){
                binding.surnameContainerSheet.error = "Pole nie może być pustym"
            } else {
                binding.surnameContainerSheet.helperText = "OK"
            }
        }
        binding.mail.doOnTextChanged { text, start, before, count ->
            val email = text.toString()
            if (email!!.isEmpty()){
                binding.emailContainerSheet.error = "Pole nie może być pustym"
            } else if (!isValidEmail(email)){
                binding.emailContainerSheet.error = "Nieprawidłowy format adresu e-mail"
            }else {
                binding.emailContainerSheet.helperText = "OK"
            }
        }
        binding.number.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.numberContainerSheet.error = "Pole nie może być pustym"
            }
            else if (text!!.length < 9){
                binding.numberContainerSheet.error = "brak liczb"
            } else {
                binding.numberContainerSheet.helperText = "OK"
            }
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