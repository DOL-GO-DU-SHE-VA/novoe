package com.example.todoplanner.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoplanner.R
import com.example.todoplanner.adapters.AuthDataClass
import com.example.todoplanner.adapters.RoomUserNameDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.databinding.FragmentSignUpBinding
import com.example.todoplanner.room.RoomViewModel
import com.example.todoplanner.viewmodel.SignUpViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var viewModel : SignUpViewModel
    private lateinit var viewModelRoom : RoomViewModel
    private lateinit var sharedPreferences : SharedPreferences

    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("token", Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        viewModelRoom = ViewModelProvider(requireActivity())[RoomViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Слушатель нажатия на кнопку "Зарегистрироваться".
        binding.signUpButton.setOnClickListener{
            val login = binding.login.text.toString()
            val lastName = binding.lastName.text.toString()
            val password = binding.password.text.toString()
            val firstName = binding.name.text.toString()
            val middleName = binding.middleName.text.toString()
            // Если поля ввода не пустые.
            if(login.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty()
                && middleName.isNotEmpty() && lastName.isNotEmpty()){
                // Если ФИО и данные авторизации введены корректно.
                if(checkName(firstName) && checkName(middleName) && checkName(lastName) &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches()
                    && password.length >= 6){
                    // Регистрация данных авторизации пользователя.
                    viewModel.regAuth(AuthDataClass(0, login, password))
                    // Получение id данных авторизации пользователя.
                    viewModel.authMetadata.observe(viewLifecycleOwner){ metadataAuth ->
                        if(metadataAuth.login == login && metadataAuth.password == password){
                            val authId = metadataAuth.id
                            // Регистрация ФИО пользователя.
                            viewModel.regUsername(
                                UserNameDataClass(0, firstName,
                                middleName, lastName, authId,1)
                            )
                            viewModel.usernameMetadata.observe(viewLifecycleOwner){ metadataUsername ->
                                // Если данные пользователя верны.
                                if(
                                    metadataUsername.first_name == firstName &&
                                    metadataUsername.middle_name == middleName &&
                                    metadataUsername.last_name == lastName &&
                                    metadataUsername.auth_id == authId
                                ) {
                                    // Сохранение данных пользователя.
                                    sharedPreferences.edit()
                                        .putBoolean("isAuth", true)
                                        .apply()
                                    viewModelRoom.saveUser(
                                        RoomUserNameDataClass(
                                        metadataUsername.id,
                                        metadataUsername.first_name,
                                        metadataUsername.middle_name,
                                        metadataUsername.last_name,
                                        authId,
                                        metadataUsername.role_id
                                    )
                                    )
                                    // Переход на окно с задачами.
                                    findNavController()
                                        .navigate(R.id.action_signUpFragment_to_listOfTasksFragment)
                                }
                            }
                        }
                        else{
                            Snackbar.make(it, "Логин уже занят.", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
                else{
                    Snackbar.make(it, "Вы ввели неверное ФИО, логин или пароль.",
                        Snackbar.LENGTH_LONG).show()
                }
            }
            else{
                Snackbar.make(it, "Вы оставили какое-то поле пустым.",
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }
    // Проверка на валидность данных ФИО
    private fun checkName(name : String) : Boolean {
        try{
            val nameCharArray = name.toCharArray()
            for (char in nameCharArray){
                if(char.isDigit() || (!char.isLetter() && (!name.contains("Оглы") || !name.contains("оглы")))){
                    return false
                }
            }
            return true
        }
        catch(ex : Exception){
            return name.contains("Оглы") || name.contains("оглы")
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}