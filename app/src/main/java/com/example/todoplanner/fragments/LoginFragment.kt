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
import com.example.todoplanner.adapters.RoomUserNameDataClass
import com.example.todoplanner.databinding.FragmentLoginBinding
import com.example.todoplanner.room.RoomViewModel
import com.example.todoplanner.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var viewModel : LoginViewModel
    private lateinit var viewModelRoom : RoomViewModel
    private lateinit var sharedPreferences : SharedPreferences

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("token", Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        viewModelRoom = ViewModelProvider(requireActivity())[RoomViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Если пользователь авторизован, то переходит на окно с задачами.
        if(sharedPreferences.getBoolean("isAuth", false)){
            findNavController().navigate(R.id.action_loginFragment_to_listOfTasksFragment)
        }

        // Слушатель нажатия на кнопку "Авторизоваться".
        binding.loginButton.setOnClickListener{
            val login = binding.login.text.toString()
            val password = binding.password.text.toString()
            // Если поля ввода не пусты.
            if(login.isNotEmpty() && password.isNotEmpty()){
                // Если логин и пароль валидны.
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches()
                    && password.length >= 6){
                    // Авторизация пользователя.
                    viewModel.auth(login, password)
                    viewModel.authMetadata.observe(viewLifecycleOwner){ metadata ->
                        // Если в ответе правильный логин и пароль.
                        if(metadata.login == login && metadata.password == password){
                            // Получение данных пользователя.
                            viewModel.getUsername(metadata.id)
                            viewModel.usernameMetadata.observe(viewLifecycleOwner){ usernameMetadata ->
                                // Сохранение сессии пользователя.
                                sharedPreferences.edit().putBoolean("isAuth", true).apply()
                                viewModelRoom.saveUser(
                                    RoomUserNameDataClass(
                                    usernameMetadata.id,
                                    usernameMetadata.first_name,
                                    usernameMetadata.middle_name,
                                    usernameMetadata.last_name,
                                    usernameMetadata.auth_id,
                                    usernameMetadata.role_id
                                )
                                )
                                // Переход на окно с задачами.
                                findNavController()
                                    .navigate(R.id.action_loginFragment_to_listOfTasksFragment)
                            }
                        }
                        else{
                            Snackbar.make(it, "Неверный логин или пароль.",
                                Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
                else{
                    Snackbar.make(it, "Неверный логин или пароль.",
                        Snackbar.LENGTH_LONG).show()
                }
            }
            else{
                Snackbar.make(it, "Вы не ввели логин или пароль.",
                    Snackbar.LENGTH_LONG).show()
            }
        }
        // Слушатель нажатия на надпись "У меня ещё нет аккаунта".
        binding.textToReg.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}