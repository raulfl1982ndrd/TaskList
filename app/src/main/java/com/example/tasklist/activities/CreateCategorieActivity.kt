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
    companion object {
        const val CATEGORIE_ID = "CATEGORIE_ID"
    }
    private lateinit var binding: ActivityCreateCategorieBinding
    private var categorieId:Int = -1
    private lateinit var categorieDAO: CategorieDAO
    private lateinit var categorie:Categorie
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCategorieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categorieId = intent.getIntExtra(TaskActivity.CATEGORIE_ID,-1)

        categorieDAO = CategorieDAO(this)

            if (categorieId != -1){
                categorie = categorieDAO.find(categorieId)!!
                binding.nameEditText.setText(categorie.name)
            }

        binding.saveButton.setOnClickListener {
            val categorieName = binding.nameEditText.text.toString()

            if (categorieId != -1) {
                categorie = Categorie(categorieId, categorieName)
                categorieDAO.update(categorie)
            }else {
                categorie = Categorie(-1, categorieName)
                categorieDAO.insert(categorie)
            }
            Toast.makeText(this, "Tarea guardada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}