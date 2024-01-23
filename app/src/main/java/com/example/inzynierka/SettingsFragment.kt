package com.example.inzynierka

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.inzynierka.databinding.FragmentSettingZalogBinding


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingZalogBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingZalogBinding.inflate(inflater, container, false)

        val activitySet = requireActivity()
        taskViewModel = ViewModelProvider(activitySet).get(TaskViewModel::class.java)    // не работает с передачей текста
        taskViewModel.name.observe(viewLifecycleOwner){
            binding.nameEditText.setText(it)
            Toast.makeText(context, "save text", Toast.LENGTH_SHORT).show()
        }
        binding.nameEditText.isEnabled = false
        taskViewModel.surname.observe(viewLifecycleOwner){
            binding.surnameEditText.setText(it)
        }
        binding.surnameEditText.isEnabled = false
        taskViewModel.mail.observe(viewLifecycleOwner){
            binding.emailEditText.setText(it)
        }
        binding.emailEditText.isEnabled = false
        taskViewModel.number.observe(viewLifecycleOwner){
            binding.numberEditText.setText(it)
        }
        binding.numberEditText.isEnabled = false


//      dodać warunek że jak nie zarejstrowany -> można zarejstrować się (bottom dodać dane)
        binding.usK.setOnClickListener{
            if (binding.usK.text == "usuń dane") {
                binding.usK.text = "załóż dane"
                binding.nameEditText.isEnabled = true
                binding.surnameEditText.isEnabled = true
                binding.emailEditText.isEnabled = true
                binding.numberEditText.isEnabled = true
                binding.nameEditText.text = null
                binding.emailEditText.text = null
                binding.surnameEditText.text = null
                binding.numberEditText.text = null
            } else if(
                binding.nameEditText.text!!.isEmpty() ||
                binding.surnameEditText.text!!.isEmpty() ||
                binding.emailEditText.text!!.isEmpty() ||
                binding.numberEditText.text!!.isEmpty()){
                Toast.makeText(context, "Do not empty!", Toast.LENGTH_SHORT).show()
            }else {
                binding.usK.text = "usuń dane"
                binding.nameEditText.isEnabled = false
                binding.surnameEditText.isEnabled = false
                binding.emailEditText.isEnabled = false
                binding.numberEditText.isEnabled = false
            }
            Toast.makeText(context, "usuń kąto/załóż kąto", Toast.LENGTH_SHORT).show()
        }
        binding.wyl.setOnClickListener{
            if (binding.wyl.text == "edytój dane") {
                binding.wyl.text = "zachowaj edycje"
                binding.nameEditText.isEnabled = true
                binding.surnameEditText.isEnabled = true
                binding.emailEditText.isEnabled = true
                binding.numberEditText.isEnabled = true

            } else {
                binding.wyl.text = "edytój dane"
                binding.nameEditText.isEnabled = false
                binding.surnameEditText.isEnabled = false
                binding.emailEditText.isEnabled = false
                binding.numberEditText.isEnabled = false
            }
            Toast.makeText(context, "edytój/save", Toast.LENGTH_SHORT).show()
        }
        binding.send.setOnClickListener{
            Toast.makeText(context, "send text", Toast.LENGTH_SHORT).show()
        }

        fun isValidEmail(email: String): Boolean {
            val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
            return emailRegex.matches(email)
        }
        binding.nameEditText.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.nameContainer.error = "Pole nie może być pustym"
            } else if (text!!.any { it.isDigit() }){
                binding.nameContainer.error = "Nie możesz używać liczb"
            } else {
                binding.nameContainer.helperText = "OK"
            }
        }
        binding.surnameEditText.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.surnameContainer.error = "Pole nie może być pustym"
            } else if (text!!.any { it.isDigit() }){
                binding.surnameContainer.error = "Pole nie może być pustym"
            } else {
                binding.surnameContainer.helperText = "OK"
            }
        }
        binding.emailEditText.doOnTextChanged { text, start, before, count ->
            val email = text.toString()
            if (email!!.isEmpty()){
                binding.emailContainer.error = "Pole nie może być pustym"
            } else if (!isValidEmail(email)){
                binding.emailContainer.error = "Nieprawidłowy format adresu e-mail"
            }else {
                binding.emailContainer.helperText = "OK"
            }
        }
        binding.numberEditText.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.numberContainer.error = "Pole nie może być pustym"
            }
            else if (text!!.length < 9){
                binding.numberContainer.error = "brak liczb"
            } else {
                binding.numberContainer.helperText = "OK"
            }
        }

        return binding.root
    }

//  funkcja dla dodawania tekstu (taskViewModel)
    private fun saveActionEdyt()
    {
        taskViewModel.name.value = binding.nameEditText.text.toString()
        taskViewModel.surname.value = binding.surnameEditText.text.toString()
        taskViewModel.mail.value = binding.emailEditText.text.toString()
        taskViewModel.number.value = binding.numberEditText.text.toString()
        binding.nameEditText.setText("")
        binding.surnameEditText.setText("")
        binding.emailEditText.setText("")
        binding.numberEditText.setText("")
        parentFragmentManager.popBackStack()
    }
}