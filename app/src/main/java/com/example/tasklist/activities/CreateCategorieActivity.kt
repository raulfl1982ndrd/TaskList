package com.example.tasklist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tasklist.R
import com.example.tasklist.data.Categorie
import com.example.tasklist.data.CategorieDAO
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityCreateCategorieBinding


class CreateCategorieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCategorieBinding

    private lateinit var categorieDAO: CategorieDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCategorieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categorieDAO = CategorieDAO(this)

        binding.saveButton.setOnClickListener {
            val categorieName = binding.nameEditText.text.toString()
            val categorie = Categorie(-1, categorieName)
            categorieDAO.insert(categorie)
            Toast.makeText(this, "Tarea guardada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}