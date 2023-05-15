package com.example.todoplanner.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoplanner.R
import com.example.todoplanner.adapters.CheckTask
import com.example.todoplanner.adapters.PriorityDataClass
import com.example.todoplanner.adapters.TasksDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.databinding.FragmentAddTaskBinding
import com.example.todoplanner.room.RoomViewModel
import com.example.todoplanner.viewmodel.AddTaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private lateinit var viewModel : AddTaskViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModelRoom : RoomViewModel

    private var _binding : FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val employers = ArrayList<UserNameDataClass>()
    private val priorities = ArrayList<PriorityDataClass>()

    private var security = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences("token", Context.MODE_PRIVATE)
        viewModel = ViewModelProvider(requireActivity())[AddTaskViewModel::class.java]
        viewModelRoom = ViewModelProvider(requireActivity())[RoomViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Получаем и выгружаем приоритеты в список.
        viewModel.metadataPriority.observe(viewLifecycleOwner){
            val array = ArrayList<String>()
            for(priority in it){
                priorities.add(priority)
                array.add(priority.name)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, array)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.priority.adapter = adapter
        }
        // Получаем сотрудников и выгружаем их ФИО в список.
        viewModel.metadataEmployers.observe(viewLifecycleOwner){ employersArray ->
            viewModelRoom.userMetadata.observe(viewLifecycleOwner){ username ->
                val array = ArrayList<String>()
                for(employer in employersArray){
                    val fio = "${employer.first_name} ${employer.middle_name} ${employer.last_name}"
                    if(username.role_id == 2L || fio == "${username.first_name} ${username.middle_name} ${username.last_name}"){
                        employers.add(employer)
                        array.add(fio)
                    }
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, array)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.responsible.adapter = adapter
            }
        }
        // Слушатель нажатия на кнопку "Добавить задачу".
        binding.addButton.setOnClickListener {
            // Если текст заголовка не пуст.
            if(binding.header.text.isNotEmpty()){
                val taskParser = CheckTask(requireContext())
                // Получаем сегодняшнюю дату.
                val dateToday = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(Date())
                // Получаем дату завершения задачи.
                val deadline = taskParser.fixDate(
                    binding.deadline.dayOfMonth,
                    binding.deadline.month,
                    binding.deadline.year
                )
                // Если дата валидна.
                if(taskParser.parseDate(dateToday, deadline)){
                    // Добавляем задачу.
                    viewModelRoom.userMetadata.observe(viewLifecycleOwner){ username ->
                        if(security){
                            viewModel.addTask(
                                TasksDataClass(
                                0,
                                username.role_id,
                                dateToday,
                                dateToday,
                                binding.description.text.toString(),
                                deadline,
                                getEmployerId(binding.responsible.selectedItem.toString()),
                                binding.header.text.toString(),
                                getPriorityId(binding.priority.selectedItem.toString()),
                                binding.responsible.selectedItem.toString(),
                                1,
                                username.id
                            )
                            )
                            security = false
                            // Возвращает на окно с задачами.
                            findNavController().popBackStack()
                        }
                    }
                } else {
                    Snackbar.make(it, "Неверно указана дата",
                        Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
    // Получение id приоритета из массива.
    private fun getPriorityId(priority : String) : Long {
        for(priorityDC in priorities){
            if(priorityDC.name == priority){
                return priorityDC.id
            }
        }
        return 0
    }
    // Получение id пользователя из массива.
    private fun getEmployerId(FIO : String) : Long {
        for(employer in employers){
            if("${employer.first_name} ${employer.middle_name} ${employer.last_name}" == FIO){
                return employer.id
            }
        }
        return 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}