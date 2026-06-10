package com.example.mindtick

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object RecordStorage {

    private const val PREF_NAME =
        "mindtick_pref"

    private const val KEY_RECORDS =
        "records"

    val records =
        mutableListOf<RecordItem>()

    fun save(
        context: Context
    ) {

        val json =
            Gson().toJson(
                records
            )

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                KEY_RECORDS,
                json
            )
            .apply()
    }

    fun load(
        context: Context
    ) {

        val prefs =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        val json =
            prefs.getString(
                KEY_RECORDS,
                null
            )

        if (json != null) {

            val type =
                object :
                    TypeToken<
                            MutableList<RecordItem>
                            >() {}.type

            records.clear()

            records.addAll(

                Gson().fromJson(
                    json,
                    type
                )
            )
        }
    }
}