package com.example.tasklist.data

import android.provider.BaseColumns

data class Task(var id: Int, var name: String,var categorieId :Int, var done: Int = 0,) {

    companion object {
        const val TABLE_NAME = "Tasks"
        const val COLUMN_NAME_TITLE = "name"
        const val COLUMN_NAME_DONE = "done"
        const val COLUMN_ID_CATEGORIE = "colum_id_categorie"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME_TITLE TEXT," +
                    "$COLUMN_NAME_DONE INTEGER ," +
                    "$COLUMN_ID_CATEGORIE INTEGER ," +
                    "FOREIGN KEY($COLUMN_ID_CATEGORIE) REFERENCES CATEGORIES(_id) ON DELETE CASCADE" +
                    ")"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

}