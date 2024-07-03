package com.example.tasklist.data

import android.provider.BaseColumns

data class Categorie(var id: Int, var name: String) {

    companion object {
        const val TABLE_NAME = "Categories"
        const val COLUMN_NAME_TITLE = "name"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME_TITLE TEXT" +
                    ")"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

}