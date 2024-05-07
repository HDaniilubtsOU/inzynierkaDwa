package com.example.inzynierka


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.inzynierka.data.UserDAO
import com.example.inzynierka.data.UserDatabase
import com.example.inzynierka.data.UserEntity
import com.example.inzynierka.databinding.FragmentSettingZalogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class SettingsFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentSettingZalogBinding
    private lateinit var taskViewModel: TaskViewModel

    private lateinit var userDao: UserDAO
    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingZalogBinding.inflate(inflater, container, false)

//  baza
        val db = UserDatabase.getInstance(requireActivity().applicationContext)   // получения экземпляра базы данных
        userDao = db.userDao()

        uid = generateUid("какие-то данные пользователя")
        loadUserData()
//        setupListeners()
//  baza

//        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
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


//        init_ui()

        return binding.root
    }


//  baza
    private fun loadUserData() {
//        val userD = UserEntity(uid = uid, name = "John", surname =  "Doe", email = "john@example.com", number = 999999999)
        lifecycleScope.launch {
//            userDao.insertUser(userD)
//            userDao.deleteAllUsers()
//            userDao.deleteUser(UserEntity(id = 16, uid = "", name = "John", surname =  "Doe", email = "john@example.com", number = 999999999))
            userDao.getUserByUid(uid)?.let { user ->
                taskViewModel.name.value = user.name
                taskViewModel.surname.value = user.surname
                taskViewModel.mail.value = user.email
                taskViewModel.number.value = user.number.toString()
//                binding.nameEditText.setText(user.name)
//                binding.surnameEditText.setText(user.surname)
//                binding.emailEditText.setText(user.email)
//                binding.numberEditText.setText(user.number.toString())
            }

            val users = withContext(Dispatchers.IO) {
                userDao.getAllUsers() // Получаем пользователя из базы данных
            }
            users?.let {
                println(it)
            }
        }
    }

    private fun setupListeners() {
//        binding.wyl.setOnClickListener {
////            val name = binding.nameEditText.text.toString()
////            val surname = binding.surnameEditText.text.toString()
////            val email = binding.emailEditText.text.toString()
////            val number = binding.numberEditText.text.toString().toIntOrNull() ?: 0
//            lifecycleScope.launch {
////                updateUser()
////                createUser(name, surname, email, number)
//            }
//        }

//      dodać warunek że jak nie zarejstrowany -> można zarejstrować się (bottom dodać dane)
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

                lifecycleScope.launch {
                updateUser()
                }
            }
            Toast.makeText(context, "edytój/save", Toast.LENGTH_SHORT).show()
        }

//        binding.usk.setOnClickListener {
//            lifecycleScope.launch {
//                deleteAllUsers()
//                clearFields()
//            }
//        }
        binding.usk.setOnClickListener{
            if (binding.usk.text == "usuń dane") {
                binding.usk.text = "załóż dane"
                binding.nameEditText.isEnabled = true
                binding.surnameEditText.isEnabled = true
                binding.emailEditText.isEnabled = true
                binding.numberEditText.isEnabled = true
                binding.nameEditText.text = null
                binding.emailEditText.text = null
                binding.surnameEditText.text = null
                binding.numberEditText.text = null

                lifecycleScope.launch {
                    deleteAllUsers()
                    clearFields()
                }
            } else if(
                binding.nameEditText.text!!.isEmpty() ||
                binding.surnameEditText.text!!.isEmpty() ||
                binding.emailEditText.text!!.isEmpty() ||
                binding.numberEditText.text!!.isEmpty()){
                Toast.makeText(context, "Do not empty!", Toast.LENGTH_SHORT).show()
            }else {
                binding.usk.text = "usuń dane"
                binding.nameEditText.isEnabled = false
                binding.surnameEditText.isEnabled = false
                binding.emailEditText.isEnabled = false
                binding.numberEditText.isEnabled = false

                lifecycleScope.launch {
                    addUserToDatabase()
                }
            }
            Toast.makeText(context, "usuń kąto/załóż kąto", Toast.LENGTH_SHORT).show()
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
        fun isValidEmail(email: String): Boolean {
            val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
            return emailRegex.matches(email)
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

        binding.send.setOnClickListener{
            Toast.makeText(context, "send text", Toast.LENGTH_SHORT).show()
        }
    }


    private fun generateUid(userData: String): String {
        return MessageDigest.getInstance("SHA-1").digest(userData.toByteArray()).toString()
    }
//    private suspend fun createUser(uid: String, name: String, surname: String, email: String, number: Int) {
//        val newUser = UserEntity(uid = uid, name = name, surname = surname, email = email, number = number)
//        userDao.insertUser(newUser)
//    }
    private suspend fun createUser(name: String, surname: String, email: String, number: Int) {
        val newUser = UserEntity(uid = uid, name = name, surname = surname, email = email, number = number)
        userDao.insertUser(newUser)
    }

    private fun addUserToDatabase() {
        val name = binding.nameEditText.text.toString()
        val surname = binding.surnameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val number = binding.numberEditText.text.toString().toIntOrNull() ?: 0
        lifecycleScope.launch {
            val newUserUid = generateUid("$name$surname$email$number")
//            createUser(newUserUid, name, surname, email, number)
            createUser(name, surname, email, number)
        }
    }


    private suspend fun updateUser() {
        val name = binding.nameEditText.text.toString()
        val surname = binding.surnameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val number = binding.numberEditText.text.toString().toIntOrNull() ?: 0

        userDao.getUserByUid(uid)?.let { user ->
            val updatedUser = user.copy(name = name, surname = surname, email = email, number = number)
            userDao.updateUser(updatedUser)
        }
    }

    private suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }

    private fun clearFields() {
        binding.nameEditText.text?.clear()
        binding.surnameEditText.text?.clear()
        binding.emailEditText.text?.clear()
        binding.numberEditText.text?.clear()
    }
//  baza



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

    private lateinit var name_: EditText
    private lateinit var text_: EditText
    private lateinit var mail_: EditText
    private lateinit var n_: String
    private lateinit var m_: String
    private lateinit var t_: String
    private lateinit var btn_send: Button

    private fun init_ui() {
        name_ = binding.nameEditText
        mail_ = binding.emailEditText
        text_ = binding.textEditText
        btn_send = binding.send
        btn_send.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.send -> {
                Toast.makeText(context, "send text", Toast.LENGTH_SHORT).show()
                n_ = name_.text.toString()
                m_ = mail_.text.toString()
                t_ = text_.text.toString()
                send_email(n_, m_, t_)
            }
            else -> {
                Toast.makeText(context, "что-то пошло не так", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun send_email(n_: String, m_: String, t_: String) {
        val properties: Properties = System.getProperties()
        properties["mail.smtp.host"] = Appdata.Gmail_Host
        properties["mail.smtp.port"] = "465"
        properties["mail.smtp.ssl.enable"] = "true"
        properties["mail.smtp.auth"] = "true"

        val session: Session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(Appdata.Sender_Email_Address, Appdata.Sender_Email_Password)
            }
        })

        val message: MimeMessage = MimeMessage(session)
        try {
            message.addRecipient(Message.RecipientType.TO, InternetAddress(Appdata.Reciver_Email_Address))
            message.subject = t_
            message.setText("From: $n_\nMail: $m_\nText: $t_")
            val thread = Thread {
                try {
                    Transport.send(message)
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            }
            thread.start()
        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }
}