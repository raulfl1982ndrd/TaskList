package com.example.tasklist.data
import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.example.tasklist.utils.DatabaseManager
import com.example.tasklist.data.Categorie
class TaskDAO(context: Context) {
    private val databaseManager: DatabaseManager = DatabaseManager(context)
    fun insert(task: Task) {
        val db = databaseManager.writableDatabase
        val values = ContentValues()
        values.put(Task.COLUMN_NAME_TITLE, task.name)
        values.put(Task.COLUMN_NAME_DONE, task.done)
        values.put(Task.COLUMN_ID_CATEGORIE, task.categorieId)
        val newRowId = db.insert(Task.TABLE_NAME, null, values)
        task.id = newRowId.toInt()

        db.close()
    }

    fun update(task: Task) {
        val db = databaseManager.writableDatabase
        val values = ContentValues()
        values.put(Task.COLUMN_NAME_TITLE, task.name)
        values.put(Task.COLUMN_NAME_DONE, task.done)
        val updatedRows = db.update(
            Task.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ${task.id}",
            null
        )

        db.close()
    }

    fun delete(task: Task) {
        val db = databaseManager.writableDatabase

        val deletedRows = db.delete(Task.TABLE_NAME, "${BaseColumns._ID} = ${task.id}", null)

        db.close()
    }

    fun find(id: Int) : Task? {
        val db = databaseManager.readableDatabase
        val projection = arrayOf(BaseColumns._ID, Task.COLUMN_NAME_TITLE, Task.COLUMN_NAME_DONE)
        val cursor = db.query(
            Task.TABLE_NAME,                        // The table to query
            projection,                             // The array of columns to return (pass null to get all)
            "${BaseColumns._ID} = $id",      // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            null                             // The sort order
        )
        var task: Task? = null
        if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
            val catid = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_ID_CATEGORIE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) == 1
            task = Task(id, name, catid, done)
        }
        cursor.close()
        db.close()
        return task
    }
    fun findAll() : List<Task> {
        val db = databaseManager.readableDatabase
        val projection = arrayOf(BaseColumns._ID, Task.COLUMN_NAME_TITLE, Task.COLUMN_NAME_DONE)
        val cursor = db.query(
            Task.TABLE_NAME,                        // The table to query
            projection,                             // The array of columns to return (pass null to get all)
            null,                            // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            "${Task.COLUMN_NAME_DONE} ASC"                             // The sort order
        )

        var tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
            val catid = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_ID_CATEGORIE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) == 1
            val task = Task(id, name,catid, done)
            tasks.add(task)
        }
        cursor.close()
        db.close()
        return tasks
    }
    fun findByCategorie(categorie_id: Int) : List<Task> {
        val db = databaseManager.readableDatabase
        val projection = arrayOf(BaseColumns._ID, Task.COLUMN_NAME_TITLE, Task.COLUMN_NAME_DONE,Task.COLUMN_ID_CATEGORIE)
        val cursor = db.query(
            Task.TABLE_NAME,                        // The table to query
            projection,                             // The array of columns to return (pass null to get all)
            "${Task.COLUMN_ID_CATEGORIE} = $categorie_id",      // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            "${Task.COLUMN_NAME_DONE} ASC"                             // The sort order
        )

        var tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) == 1
            val catid = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_ID_CATEGORIE))
            val task = Task(id, name,catid, done)
            tasks.add(task)
        }
        cursor.close()
        db.close()
        return tasks
    }
    fun countByCategoryAndDone(categorie: Categorie): Int {
        val db = databaseManager.writableDatabase

        val cursor = db.query(
            Task.TABLE_NAME,                 // The table to query
            arrayOf("COUNT(*)"),     // The array of columns to return (pass null to get all)
            "${Task.COLUMN_ID_CATEGORIE} = ${categorie.id} AND ${Task.COLUMN_NAME_DONE} = 0",                // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        var count = -1
        if (cursor.moveToNext()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        db.close()

        return count
    }
}