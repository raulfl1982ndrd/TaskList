package com.example.tasklist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tasklist.data.Categorie
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityCreateTaskBinding

class CreateTaskActivity : AppCompatActivity() {

    companion object {
        const val CATEGORIE_ID = "CATEGORIE_ID"
        const val TASK_ID = "TASK_ID"
    }

    private lateinit var binding: ActivityCreateTaskBinding
    private var categorieId:Int = -1
    private var taskId:Int = -1
    private lateinit var taskDAO: TaskDAO
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categorieId = intent.getIntExtra(CreateTaskActivity.CATEGORIE_ID,-1)
        taskId = intent.getIntExtra(CreateTaskActivity.TASK_ID,-1)
        Toast.makeText(this, "Editar tarea id: ${taskId}", Toast.LENGTH_SHORT).show()
        taskDAO = TaskDAO(this)

        if (taskId != -1){
            task = taskDAO.find(taskId)!!
            Toast.makeText(this, "Editar tarea name: ${task.name}", Toast.LENGTH_SHORT).show()
            binding.nameEditText.setText(task.name)
        }


        binding.saveButton.setOnClickListener {
            val taskName = binding.nameEditText.text.toString()

            if (taskName != ""){
                val task = Task(taskId, taskName,task.categorieId, task.done)
                taskDAO.update(task)
                Toast.makeText(this, "Tarea guardada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "La tarea no puede estar vacia", Toast.LENGTH_SHORT).show()
            }
        }
    }
}