package com.ruiz.emovie.data.local

import androidx.room.TypeConverter

class ListToStringDatabaseConverter {

    private val separator = ","

    @TypeConverter
    fun convertListToString(list: List<Int>): String {

        val strBuilder = StringBuilder()

        if (list.isNotEmpty()) {

            list.forEach { item ->
                strBuilder.append(item).append(separator)
            }

            strBuilder.setLength(strBuilder.length - separator.length)

        }

        return strBuilder.toString()
    }

    @TypeConverter
    fun convertStringToList(string: String): List<Int> {

        return if (string.isNotEmpty()) {

            val items = string.split(separator)
            val itemsInt: ArrayList<Int> = arrayListOf()
            items.forEach {
                itemsInt.add(it.toInt())
            }
            itemsInt.toList()

        } else {
            listOf()
        }
    }

}