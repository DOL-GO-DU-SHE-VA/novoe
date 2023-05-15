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
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.databinding.FragmentEditTaskBinding
import com.example.todoplanner.room.RoomViewModel
import com.example.todoplanner.viewmodel.EditTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class EditTaskFragment : Fragment() {

    private lateinit var id : String
    private lateinit var viewModel: EditTaskViewModel
    private lateinit var viewModelRoom : RoomViewModel
    private lateinit var sharedPreferences : SharedPreferences

    private var _binding : FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    private val employers = ArrayList<UserNameDataClass>()
    private val priorities = ArrayList<PriorityDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("token", Context.MODE_PRIVATE)

        id = sharedPreferences.getLong("id", 0).toString()

        viewModel = ViewModelProvider(requireActivity())[EditTaskViewModel::class.java]
        viewModelRoom = ViewModelProvider(requireActivity())[RoomViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Получаем сотрудников и выгружаем их ФИО в список.
        viewModel.metadataEmployers.observe(viewLifecycleOwner){ employersArray ->
            viewModelRoom.userMetadata.observe(viewLifecycleOwner){ username ->
                val array = ArrayList<String>()
                for(employer in employersArray){
                    val fio = "${employer.first_name} ${employer.middle_name} ${employer.last_name}"
                    if(username.role_id == 2L || fio == "${username.first_name} ${username.middle_name} ${username.last_name}"){
                        array.add(fio)
                        employers.add(employer)
                    }
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, array)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.responsible.adapter = adapter
            }
        }

        viewModel.getTask(id.toLong())
        viewModel.metadataPriority.observe(viewLifecycleOwner){ priorities ->
            val array = ArrayList<String>()
            for(priority in priorities){
                array.add(priority.name)
                this.priorities.add(priority)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, array)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.priority.adapter = adapter
            viewModel.tasksMetadata.observe(viewLifecycleOwner){ task ->
                binding.header.setText(task.header)
                binding.description.setText(task.description)
                val dateArray = task.deadline.split('.')
                binding.deadline.updateDate(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[0]))
                binding.priority.setSelection(adapter.getPosition(priorities[task.priority_id.toInt() - 1].name))
            }
        }

        binding.editButton.setOnClickListener {
            val taskParser = CheckTask(requireContext())
            viewModel.getTask(id.toLong())
            // Если текст заголовка не пуст.
            if(binding.header.text.isNotEmpty()){
                // Получаем сегодняшнюю дату.
                val dateToday = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(Date())
                val deadline = taskParser.fixDate(
                    binding.deadline.dayOfMonth,
                    binding.deadline.month,
                    binding.deadline.year
                )
                // Если дата валидна.
                if(taskParser.parseDate(dateToday, deadline)){
                    viewModel.tasksMetadata.observe(viewLifecycleOwner){ task ->
                        viewModel.editTask(
                            task.created_by,
                            task.date_of_creation,
                            dateToday,
                            binding.description.text.toString(),
                            deadline,
                            getEmployerId(binding.responsible.selectedItem.toString()),
                            binding.header.text.toString(),
                            getPriorityId(binding.priority.selectedItem.toString()),
                            binding.responsible.selectedItem.toString(),
                            task.status_id,
                            task.owner_id,
                            id.toLong()
                        )
                        findNavController().popBackStack()
                    }
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