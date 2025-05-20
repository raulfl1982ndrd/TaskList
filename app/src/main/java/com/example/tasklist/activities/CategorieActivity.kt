package com.example.tasklist.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.R
import com.example.tasklist.adapters.CategorieAdapter
import com.example.tasklist.data.Categorie
import com.example.tasklist.data.CategorieDAO
import com.example.tasklist.databinding.ActivityCategorieBinding
import com.example.tasklist.databinding.AddCategoryDialogBinding
import java.util.Locale

class CategorieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategorieBinding
    private lateinit var adapter: CategorieAdapter
    private lateinit var categorieList: MutableList<Categorie>
    private lateinit var categorieDAO: CategorieDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategorieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categorieDAO = CategorieDAO(this)
        categorieList = categorieDAO.findAll().toMutableList()
        adapter = CategorieAdapter(this,emptyList(), {
            navigateToTask(categorieList[it]);
            Toast.makeText(this, "Click en tarea: ${categorieList[it].name}", Toast.LENGTH_SHORT).show()
        }, {
            navigateEditCategory(categorieList[it])
            Toast.makeText(this, "Editar tarea: ${categorieList[it].name}", Toast.LENGTH_SHORT).show()
        }, {
            //categorieDAO.delete(categorieList[it])
            deleteCategory(categorieList[it])
            Toast.makeText(this, "Tarea borrada correctamente", Toast.LENGTH_SHORT).show()
            loadData()

        },{
            onItemClickListener(it)
        }, {
            editCategory(it)
            return@CategorieAdapter true
        })

        binding.recyclerViewCategories.adapter = adapter
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(this)
        binding.addCategorieButton.setOnClickListener {
            val intent = Intent(this, CreateCategorieActivity::class.java)
            startActivity(intent)
        }
        initView()
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }
    private fun initView() {
        binding.addCategorieButton.setOnClickListener {
            addCategory()
        }

        adapter = CategorieAdapter(this, categorieList,

            {
                navigateToTask(categorieList[it]);
                Toast.makeText(this, "Click en tarea: ${categorieList[it].name}", Toast.LENGTH_SHORT).show()
            }, {
                navigateEditCategory(categorieList[it])
                Toast.makeText(this, "Editar tarea: ${categorieList[it].name}", Toast.LENGTH_SHORT).show()
            }, {
                //categorieDAO.delete(categorieList[it])
                deleteCategory(categorieList[it])
                Toast.makeText(this, "Tarea borrada correctamente", Toast.LENGTH_SHORT).show()
                loadData()
            },{
            onItemClickListener(it)
            }, {
                editCategory(it)
                return@CategorieAdapter true
            })

        binding.recyclerViewCategories.adapter = adapter
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(this)
    }

    private fun loadData() {
        categorieList = categorieDAO.findAll().toMutableList()
        adapter.updateData(categorieList)
    }
    private fun navigateToTask(categorie: Categorie) {
        //Toast.makeText(this, superhero.name, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TaskActivity.CATEGORIE_ID, categorie.id)
        startActivity(intent)
    }
    private fun navigateEditCategory(categorie: Categorie) {
        //Toast.makeText(this, superhero.name, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, CreateCategorieActivity::class.java)
        intent.putExtra(CreateCategorieActivity.CATEGORIE_ID, categorie.id)
        startActivity(intent)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_category -> {
                addCategory()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onItemClickListener(position:Int) {
        val category: Categorie = categorieList[position]
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TaskActivity.CATEGORIE_ID, category.id)
        startActivity(intent)
    }

    private fun addCategory() {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val binding: AddCategoryDialogBinding = AddCategoryDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        dialogBuilder.setTitle(R.string.add_category_title)
        dialogBuilder.setIcon(R.drawable.ic_add_task)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.add_category_button, null)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()

        // Need to move listener after show dialog to prevent dismiss
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            var categoryName = binding.taskTextField.editText?.text.toString()
            categoryName = categoryName.trimStart().trimEnd().lowercase().replaceFirstChar { it.uppercaseChar() }
            if (categoryName.isNotEmpty()) {
                val category = Categorie(-1, categoryName,"#000000")
                categorieDAO.insert(category)
                loadData()
                Toast.makeText(this, R.string.add_category_success_message, Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                binding.taskTextField.error = getString(R.string.add_category_empty_error)
            }
        }
    }

    private fun editCategory(position: Int) {
        val category: Categorie = categorieList[position]

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val binding: AddCategoryDialogBinding = AddCategoryDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        binding.taskTextField.editText?.setText(category.name)

        dialogBuilder.setTitle(R.string.edit_category_title)
        dialogBuilder.setIcon(R.drawable.ic_add_task)
        dialogBuilder.setNeutralButton(R.string.delete_category_button) { dialog, _ ->
            deleteCategory(category)
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.edit_category_button, null)

        val alertDialog: AlertDialog = dialogBuilder.create()

        alertDialog.setOnShowListener(DialogInterface.OnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                .setTextColor(getColor(R.color.negative_red))
        })

        alertDialog.show()

        // Need to move listener after show dialog to prevent dismiss
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            var categoryName = binding.taskTextField.editText?.text.toString()
            categoryName = categoryName.trimStart().trimEnd().lowercase().replaceFirstChar { it.uppercaseChar() }
            if (categoryName.isNotEmpty()) {
                category.name = categoryName
                category.color = "#000000"
                categorieDAO.update(category)
                adapter.notifyItemChanged(position)
                Toast.makeText(this, R.string.edit_category_success_message, Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                binding.taskTextField.error = getString(R.string.add_category_empty_error)
            }
        }
    }

    private fun deleteCategory(category: Categorie) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        dialogBuilder.setTitle(R.string.delete_category_title)
        dialogBuilder.setMessage(getString(R.string.delete_category_confirm_message, category.name))
        dialogBuilder.setIcon(R.drawable.ic_delete)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.delete_category_button) { dialog, _ ->
            categorieDAO.delete(category)
            loadData()
            //categoryList.remove(category)
            //adapter.notifyDataSetChanged()
            //adapter.notifyItemRemoved(position)
            dialog.dismiss()
            Toast.makeText(this, R.string.delete_category_success_message, Toast.LENGTH_SHORT).show()
        }

        val alertDialog: AlertDialog = dialogBuilder.create()

        alertDialog.setOnShowListener(DialogInterface.OnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getColor(R.color.negative_red))
        })

        alertDialog.show()
    }
}