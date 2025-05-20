package com.example.tasklist.activities

import TaskAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.data.Categorie
import com.example.tasklist.data.CategorieDAO
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.data.Task
import com.example.tasklist.databinding.ActivityTaskBinding
import com.example.tasklist.databinding.AddTaskDialogBinding

class TaskActivity : AppCompatActivity() {

    companion object {
        const val CATEGORIE_ID = "CATEGORIE_ID"
    }
    private lateinit var categoryDAO: CategorieDAO
    private lateinit var binding: ActivityTaskBinding
    private var categorieId:Int = -1
    private lateinit var adapter: TaskAdapter
    private lateinit var taskList: List<Task>
    private lateinit var taskDAO: TaskDAO
    private lateinit var category: Categorie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categorieId = intent.getIntExtra(CATEGORIE_ID,-1)
        categoryDAO = CategorieDAO(this)
        category = categoryDAO.find(categorieId)!!
        taskDAO = TaskDAO(this)
        adapter = TaskAdapter(emptyList(), {
            Toast.makeText(this, "Click en tarea: ${taskList[it].name}", Toast.LENGTH_SHORT).show()
        }, onItemClickCheckBoxListener = { position ->
            // Aquí actualizas el estado done de la tarea
            val task = taskList[position]
            task.done = if (task.done == 1) 0 else 1
            // Actualiza en la base de datos si es necesario
            //adapter.notifyItemChanged(position)
        },
            onItemClickRemoveListener = { position ->
                taskDAO.delete(taskList[position])
                Toast.makeText(this, "Tarea borrada correctamente", Toast.LENGTH_SHORT).show()
                loadData(categorieId)
            }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            intent.putExtra(CreateTaskActivity.CATEGORIE_ID, categorieId)
            startActivity(intent)
        }
        binding.categorieName.text = category.name.lowercase().replaceFirstChar { it.uppercaseChar() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = category.name
        initView()
    }
    override fun onResume() {
        super.onResume()
        loadData(categorieId)
    }

    private fun initView() {
        binding.addTaskButton.setOnClickListener {
            addTask()
        }

        adapter = TaskAdapter(listOf(), {
            onItemClickListener(it)
        }, {
            onItemClickCheckBoxListener(it)
        }, {
            onItemClickRemoveListener(it)
        }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        configureGestures()
    }

    private fun loadData(categorieid:Int) {
        taskList = taskDAO.findByCategorie(categorieid)
        adapter.updateData(taskList)
    }
    private fun configureGestures() {
        val gestures = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        onItemClickRemoveListener(viewHolder.adapterPosition)
                    } else {
                        onItemClickCheckBoxListener(viewHolder.adapterPosition)
                    }
                }
            })
        gestures.attachToRecyclerView(binding.recyclerView)
    }
    private fun addTask() {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val binding: AddTaskDialogBinding = AddTaskDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        /*val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList.map { it.name })
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = spinnerAdapter*/

        dialogBuilder.setTitle(R.string.add_task_title)
        dialogBuilder.setIcon(R.drawable.ic_add_task)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.add_task_button, null)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()

        // Need to move listener after show dialog to prevent dismiss
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val taskName = binding.taskTextField.editText?.text.toString()
            if (taskName.isNotEmpty()) {
                val task = Task(-1, taskName, categorieId,0)
                taskDAO.insert(task)
                loadData(categorieId)
                Toast.makeText(this, R.string.add_task_success_message, Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                binding.taskTextField.error = getString(R.string.add_task_empty_error)
            }
        }
    }

    private fun editTask(position: Int) {
        val task: Task = taskList[position]

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val binding: AddTaskDialogBinding = AddTaskDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        /*val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList.map { it.name })
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = spinnerAdapter
        binding.categorySpinner.setSelection(categoryList.indexOf(task.category))*/

        binding.taskTextField.editText?.setText(task.name)

        dialogBuilder.setTitle(R.string.edit_task_title)
        dialogBuilder.setIcon(R.drawable.ic_add_task)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.edit_task_button, null)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()

        // Need to move listener after show dialog to prevent dismiss
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val taskName = binding.taskTextField.editText?.text.toString()
            if (taskName.isNotEmpty()) {
                task.name = taskName
                task.categorieId = category.id
                taskDAO.update(task)
                adapter.notifyItemChanged(position)
                Toast.makeText(this, R.string.edit_task_success_message, Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                binding.taskTextField.error = getString(R.string.add_task_empty_error)
            }
        }
    }

    private fun onItemClickListener(position:Int) {
        editTask(position)
    }

    private fun onItemClickCheckBoxListener(position:Int) {
        val task: Task = taskList[position]
        if (task.done == 0)
            task.done = 1
        else
            task.done = 0

        //   task.done = !task.done
        taskDAO.update(task)
        adapter.notifyItemChanged(position)
        loadData(categorieId)
        //adapter.notifyDataSetChanged()
    }

    private fun onItemClickRemoveListener(position:Int) {
        val task: Task = taskList[position]
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        dialogBuilder.setTitle(R.string.delete_task_title)
        dialogBuilder.setMessage(getString(R.string.delete_task_confirm_message, task.name))
        dialogBuilder.setIcon(R.drawable.ic_delete)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            adapter.notifyItemChanged(position)
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.delete_task_button) { dialog, _ ->
            taskDAO.delete(task)

            val newList = taskList.minus(task)
            adapter.updateItems(newList)

            taskList = newList.toMutableList()
            dialog.dismiss()
            Toast.makeText(this, R.string.delete_task_success_message, Toast.LENGTH_SHORT).show()
        }
        dialogBuilder.show()
    }

}