package com.example.inzynierka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
//        return inflater.inflate(R.layout.fragment_settings, container, false)
        binding = FragmentSettingZalogBinding.inflate(inflater, container, false)

        val activitySet = requireActivity()
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)    // не работает с передачей текста
        taskViewModel.name.observe(viewLifecycleOwner){
            binding.nameEditText.text
        }
//        taskViewModel.surname.observe(this){
//            binding.taskSurname.text = String.format("Task Desc: %s", it)
//        }
//        taskViewModel.mail.observe(this){
//            binding.taskMail.text = String.format("Task Desc: %s", it)
//        }
//        taskViewModel.number.observe(this){
//            binding.taskNumber.text = String.format("Task Desc: %s", it)
//        }
        return binding.root
    }
}