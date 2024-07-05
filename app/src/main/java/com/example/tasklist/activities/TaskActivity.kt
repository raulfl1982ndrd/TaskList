package com.example.tasklist.activities

import TaskAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.data.Task
import com.example.tasklist.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    companion object {
        const val CATEGORIE_ID = "CATEGORIE_ID"
    }
    private lateinit var binding: ActivityTaskBinding
    private var categorieId:Int = -1
    private lateinit var adapter: TaskAdapter
    private lateinit var taskList: List<Task>
    private lateinit var taskDAO: TaskDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categorieId = intent.getIntExtra(CATEGORIE_ID,-1)

        taskDAO = TaskDAO(this)
        adapter = TaskAdapter(emptyList(), {
            Toast.makeText(this, "Click en tarea: ${taskList[it].name}", Toast.LENGTH_SHORT).show()
        }, {
            taskDAO.delete(taskList[it])
            Toast.makeText(this, "Tarea borrada correctamente", Toast.LENGTH_SHORT).show()
            loadData(categorieId)
        }, {
            val task = taskList[it]
            task.done = !task.done

            taskDAO.update(task)
            loadData(categorieId)
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            intent.putExtra(CreateTaskActivity.CATEGORIE_ID, categorieId)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        loadData(categorieId)
    }
    private fun loadData(categorieid:Int) {
        taskList = taskDAO.findByCategorie(categorieid)
        adapter.updateData(taskList)
    }
}