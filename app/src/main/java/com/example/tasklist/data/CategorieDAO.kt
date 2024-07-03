package com.example.tasklist.data

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.example.tasklist.utils.DatabaseManager

class CategorieDAO(context: Context) {
        private val databaseManager: DatabaseManager = DatabaseManager(context)
        fun insert(categorie: Categorie) {
            val db = databaseManager.writableDatabase
            val values = ContentValues()
            values.put(Categorie.COLUMN_NAME_TITLE, categorie.name)

            val newRowId = db.insert(Categorie.TABLE_NAME, null, values)
            categorie.id = newRowId.toInt()

            db.close()
        }

        fun update(categorie: Categorie) {
            val db = databaseManager.writableDatabase
            val values = ContentValues()
            values.put(Categorie.COLUMN_NAME_TITLE, categorie.name)
            val updatedRows = db.update(
                Categorie.TABLE_NAME,
                values,
                "${BaseColumns._ID} = ${categorie.id}",
                null
            )

            db.close()
        }

        fun delete(categorie: Categorie) {
            val db = databaseManager.writableDatabase

            val deletedRows = db.delete(Categorie.TABLE_NAME, "${BaseColumns._ID} = ${categorie.id}", null)

            db.close()
        }

        fun find(id: Int) : Categorie? {
            val db = databaseManager.readableDatabase
            val projection = arrayOf(BaseColumns._ID, Categorie.COLUMN_NAME_TITLE)
            val cursor = db.query(
                Categorie.TABLE_NAME,                        // The table to query
                projection,                             // The array of columns to return (pass null to get all)
                "${BaseColumns._ID} = $id",      // The columns for the WHERE clause
                null,                         // The values for the WHERE clause
                null,                            // don't group the rows
                null,                             // don't filter by row groups
                null                             // The sort order
            )
            var categorie: Categorie? = null
            if (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Categorie.COLUMN_NAME_TITLE))
                categorie = Categorie(id, name)
            }
            cursor.close()
            db.close()
            return categorie
        }
        fun findAll() : List<Categorie> {
            val db = databaseManager.readableDatabase
            val projection = arrayOf(BaseColumns._ID, Categorie.COLUMN_NAME_TITLE)
            val cursor = db.query(
                Categorie.TABLE_NAME,                        // The table to query
                projection,                             // The array of columns to return (pass null to get all)
                null,                            // The columns for the WHERE clause
                null,                         // The values for the WHERE clause
                null,                            // don't group the rows
                null,                             // don't filter by row groups
                null                            // The sort order
            )

            var categories = mutableListOf<Categorie>()
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Categorie.COLUMN_NAME_TITLE))

                val categorie = Categorie(id, name)
                categories.add(categorie)
            }
            cursor.close()
            db.close()
            return categories
        }
    }