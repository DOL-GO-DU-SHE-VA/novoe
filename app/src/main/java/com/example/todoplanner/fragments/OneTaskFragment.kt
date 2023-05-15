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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoplanner.R
import com.example.todoplanner.adapters.CheckTask
import com.example.todoplanner.databinding.FragmentOneTaskBinding
import com.example.todoplanner.room.RoomViewModel
import com.example.todoplanner.viewmodel.OneTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class OneTaskFragment : Fragment() {

    private lateinit var id : String
    private lateinit var viewModel : OneTaskViewModel
    private lateinit var viewModelRoom : RoomViewModel
    private lateinit var sharedPreferences : SharedPreferences

    private var _binding : FragmentOneTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOneTaskBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("token", Context.MODE_PRIVATE)

        id = sharedPreferences.getLong("id", 0).toString()

        viewModel = ViewModelProvider(requireActivity())[OneTaskViewModel::class.java]
        viewModelRoom = ViewModelProvider(requireActivity())[RoomViewModel::class.java]

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Получаем задачу.
        viewModel.getTask(requireContext())
        viewModel.tasksMetadata.observe(viewLifecycleOwner){ task ->
            viewModel.statusMetadata.observe(viewLifecycleOwner){ status ->
                viewModel.priorityMetadata.observe(viewLifecycleOwner){ priority ->
                    viewModel.usernameMetadata.observe(viewLifecycleOwner){ username ->
                        if(task != null && status != null && priority != null && username != null){
                            // Выводим данные задачи.
                            try{
                                val context = requireContext()
                                val owner = "${username.first_name} ${username.middle_name} ${username.last_name}"
                                binding.owner.text = context.getString(R.string.ownerWE, owner)
                                binding.header.text = task.header
                                binding.header.setTextColor(CheckTask(context).getColor(task, status))
                                binding.status.text = context.getString(R.string.statusWE, status[task.status_id.toInt() - 1].name)
                                binding.priority.text = context.getString(R.string.priorityWE, priority[task.priority_id.toInt() - 1].name)
                                binding.deadline.text = context.getString(R.string.deadlineWE, task.deadline)
                                binding.description.text = task.description
                                binding.responsible.text = context.getString(R.string.responsibleWE, task.responsible)
                                binding.dateOfUpdater.text = context.getString(R.string.dateOfUpdaterWE, task.date_of_updater)
                                binding.dateOfCreation.text = context.getString(R.string.dateOfCreationWE, task.date_of_creation)
                            } catch (ex : Exception){
                                Log.e("OneTaskFragment.kt", "Ошибка при постановлении задач: " +
                                        ex.stackTraceToString())
                            }
                        }
                    }
                    // Получает метаданные пользователя.
                    viewModelRoom.userMetadata.observe(viewLifecycleOwner){ username ->
                        // Если пользователь является руководителем или создателем задачи.
                        if(username.role_id == 2L || task.owner_id == username.id){
                            binding.editTaskButton.visibility = View.VISIBLE
                            binding.deleteButton.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        binding.editStatusButton.setOnClickListener {
            setButton(false)
            val array = arrayOf("К выполнению","Выполняется","Выполнена","Отменена")
            var choiceStatus = ""
            AlertDialog.Builder(requireContext()).setTitle("Статус")
                .setItems(array) { _, p2 ->
                    when (p2) {
                        0 -> choiceStatus = "К выполнению"
                        1 -> choiceStatus = "Выполняется"
                        2 -> choiceStatus = "Выполнена"
                        3 -> choiceStatus = "Отменена"
                    }
                    val dateToday = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
                    // Меняем статус задачи.
                    viewModel.statusMetadata.observe(viewLifecycleOwner){
                        for(status in it){
                            if(status.name == choiceStatus){
                                viewModel.updateStatus(id.toLong(), status.id, dateToday)
                            }
                        }
                    }
                }.show()
            setButton(true)
        }

        binding.deleteButton.setOnClickListener {
            setButton(false)
            viewModel.deleteTask(id.toLong())
            findNavController().popBackStack()
        }
        // Слушатель нажатия на кнопку "Авторизоваться".
        binding.editTaskButton.setOnClickListener {
            setButton(false)
            findNavController().navigate(R.id.action_oneTaskFragment_to_editTaskFragment)
        }
    }

    private fun setButton(boolean: Boolean){
        binding.deleteButton.isEnabled = boolean
        binding.editTaskButton.isEnabled = boolean
        binding.editStatusButton.isEnabled = boolean
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}