package com.example.tasklist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.adapters.CategorieAdapter
import com.example.tasklist.data.Categorie
import com.example.tasklist.data.CategorieDAO
import com.example.tasklist.databinding.ActivityCategorieBinding

class CategorieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategorieBinding
    private lateinit var adapter: CategorieAdapter
    private lateinit var categorieList: List<Categorie>
    private lateinit var categorieDAO: CategorieDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategorieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categorieDAO = CategorieDAO(this)
        adapter = CategorieAdapter(emptyList(), {
            navigateToTask(categorieList[it]);
            Toast.makeText(this, "Click en tarea: ${categorieList[it].name}", Toast.LENGTH_SHORT).show()
        }, {
            navigateEditCategory(categorieList[it])
            Toast.makeText(this, "Editar tarea: ${categorieList[it].name}", Toast.LENGTH_SHORT).show()
        }, {
            categorieDAO.delete(categorieList[it])
            Toast.makeText(this, "Tarea borrada correctamente", Toast.LENGTH_SHORT).show()
            loadData()
        })

        binding.recyclerViewCategories.adapter = adapter
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(this)
        binding.addCategorieButton.setOnClickListener {
            val intent = Intent(this, CreateCategorieActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }
    private fun loadData() {
        categorieList = categorieDAO.findAll()
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
}