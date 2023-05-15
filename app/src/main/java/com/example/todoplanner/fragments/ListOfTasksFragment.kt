package com.example.todoplanner.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoplanner.R
import com.example.todoplanner.adapters.RecycleViewAdapter
import com.example.todoplanner.adapters.TasksDataClass
import com.example.todoplanner.databinding.FragmentListOfTasksBinding
import com.example.todoplanner.room.RoomViewModel
import com.example.todoplanner.viewmodel.ListOfTasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListOfTasksFragment : Fragment() {

    private lateinit var sort : String
    private lateinit var adapter : RecycleViewAdapter
    private lateinit var viewModel : ListOfTasksViewModel
    private lateinit var viewModelRoom : RoomViewModel
    private lateinit var sharedPreferences : SharedPreferences

    private var security = true

    private var _binding : FragmentListOfTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfTasksBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("token", Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(requireActivity())[ListOfTasksViewModel::class.java]
        viewModelRoom = ViewModelProvider(requireActivity())[RoomViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация меню в верхней панели.
        binding.toolBar.inflateMenu(R.menu.menu)
        binding.toolBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.setAdmin -> setAdminDialog()
                R.id.sortBy -> sortBy()
                R.id.logOut -> logOut()
            }
            true
        }
        // Получение задач.
        viewModel.tasksMetadata.observe(viewLifecycleOwner){ tasks ->
            val array = ArrayList<TasksDataClass>()
            viewModelRoom.userMetadata.observe(viewLifecycleOwner) { username ->
                array.clear()
                for (task in tasks) {
                    if ((username.role_id == 2L && task.created_by != 1L) || task.id_for_who == username.id) {
                        array.add(task)
                    }
                }
                sort = sharedPreferences.getString("sort", "Без сортировки").toString()
                when (sort) {
                    "По дате завершения" -> {
                        array.sortBy { sort ->
                            sort.deadline
                        }
                    }
                    "По ответственным" -> {
                        array.sortBy { sort ->
                            sort.responsible
                        }
                    }
                    else -> {
                        array.sortBy { sort ->
                            sort.id
                        }
                    }
                }
                viewModel.statusMetadata.observe(viewLifecycleOwner) { status ->
                    viewModel.priorityMetadata.observe(viewLifecycleOwner) { priority ->
                        adapter = RecycleViewAdapter(array, priority, status, requireActivity())
                        adapter.setOnRecycleViewClick(object :
                            RecycleViewAdapter.OnRecycleViewListener {
                            override fun onRecycleViewClick(position: Int) {
                                sharedPreferences.edit().putLong("id", array[position].id).apply()
                                findNavController().navigate(R.id.action_listOfTasksFragment_to_oneTaskFragment)

                            }
                        })
                        binding.recycleView.adapter = adapter
                    }
                }
            }
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_listOfTasksFragment_to_addTaskFragment)
        }
    }

    private fun sortBy(){
        security = true
        val sortArrayUser = arrayOf("По дате завершения", "Без сортировки")
        val sortArrayAdmin = arrayOf("По дате завершения", "По ответственным", "Без сортировки")
        viewModelRoom.userMetadata.observe(viewLifecycleOwner){
            if(it.role_id == 2L){
                if(security){
                    AlertDialog.Builder(requireContext()).setTitle("Сортировка по?")
                        .setItems(sortArrayAdmin) { _, p2 ->
                            when (p2) {
                                0 -> sharedPreferences.edit()
                                    .putString("sort", "По дате завершения").apply()
                                1 -> sharedPreferences.edit()
                                    .putString("sort", "По ответственным").apply()
                                2 -> sharedPreferences.edit()
                                    .putString("sort", "Без сортировки").apply()
                            }
                        }.show()
                    security = false
                }
            }
            else{
                if(security){
                    AlertDialog.Builder(requireContext()).setTitle("Сортировка по?")
                        .setItems(sortArrayUser) { _, p2 ->
                            when (p2) {
                                0 -> sharedPreferences.edit()
                                    .putString("sort", "По дате завершения").apply()
                                1 -> sharedPreferences.edit()
                                    .putString("sort", "Без сортировки").apply()
                            }
                        }
                        .show()
                    security = false
                }
            }
        }
    }

    private fun setAdminDialog(){
        viewModelRoom.userMetadata.observe(viewLifecycleOwner){
            if(it.role_id == 1L){
                val view = requireActivity().layoutInflater.inflate(
                    R.layout.set_admin_item,
                    null
                )
                if(security){
                    AlertDialog.Builder(requireContext())
                        .setView(view)
                        .setCancelable(false)
                        .setPositiveButton("Активировать") { _, _ ->
                            val adminPassword =
                                view?.findViewById<EditText>(R.id.passwordAdmin)?.text.toString()
                            if (adminPassword == "123123") {
                                viewModel.updateRole(it.id, 2)
                                viewModelRoom.updateRole(2L)
                            } else {
                                Toast.makeText(activity, "Пароль неверный", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }.show()
                    security = false
                }
            }
            else{
                Toast.makeText(activity,"Вы руководитель", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logOut(){
        viewModelRoom.userMetadata.observe(viewLifecycleOwner){
            viewModelRoom.delete(it)
            sharedPreferences.edit().putBoolean("isAuth", false).apply()
            activity?.viewModelStore?.clear()

            findNavController().navigate(R.id.action_listOfTasksFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}