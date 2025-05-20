package com.example.tasklist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tasklist.data.Categorie
import com.example.tasklist.data.CategorieDAO
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
        categorieId = intent.getIntExtra(CreateTaskActivity.CATEGORIE_ID,-1)

        categorieDAO= CategorieDAO(this)

        if (categorieId != -1){
            categorie = categorieDAO.find(categorieId)!!
            binding.nameEditText.setText(categorie.name)
        }


        binding.saveButton.setOnClickListener {
            var categorieName = binding.nameEditText.text.toString()
            categorieName = categorieName.trimStart().trimEnd().lowercase().replaceFirstChar { it.uppercaseChar() }

            if (categorieName != "") {
                if (categorieId != -1) {
                    categorie = Categorie(categorieId, categorieName,"#000000")
                    categorieDAO.update(categorie)
                } else {
                    categorie = Categorie(-1, categorieName, "#000000")
                    categorieDAO.insert(categorie)
                }

                Toast.makeText(this, "Tarea guardada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "La categoria no puede estar vacia", Toast.LENGTH_SHORT).show()
                }
        }
    }
}