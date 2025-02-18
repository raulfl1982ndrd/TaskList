package com.example.tasklist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityCreateTaskBinding

class CreateTaskActivity : AppCompatActivity() {

    companion object {
        const val CATEGORIE_ID = "CATEGORIE_ID"
    }

    private lateinit var binding: ActivityCreateTaskBinding
    private var categorieId:Int = -1
    private lateinit var taskDAO: TaskDAO

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categorieId = intent.getIntExtra(CreateTaskActivity.CATEGORIE_ID,-1)
        taskDAO = TaskDAO(this)

        binding.saveButton.setOnClickListener {
            val taskName = binding.nameEditText.text.toString()

            if (taskName != ""){
                val task = Task(-1, taskName,categorieId)
                taskDAO.insert(task)
                Toast.makeText(this, "Tarea guardada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "La tarea no puede estar vacia", Toast.LENGTH_SHORT).show()
            }
        }
    }
}