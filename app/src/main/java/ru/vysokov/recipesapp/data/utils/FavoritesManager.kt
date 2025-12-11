package ru.vysokov.recipesapp.data.utils

import android.content.Context
import androidx.core.content.edit

object FavoritesManager {
    const val PREFS_NAME = "prefs_favorites"
    const val KEY_FAVORITES = "key_favorites"

    fun saveFavorites(context: Context, recipeIds: Set<String>) {
        val sharedPref = context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        sharedPref.edit {
            putStringSet(KEY_FAVORITES, recipeIds)
        }
    }

    fun getFavorites(context: Context): MutableSet<String> {
        val sharedPref = context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        return sharedPref.getStringSet(KEY_FAVORITES, emptySet())
            ?.toMutableSet() ?: mutableSetOf()
    }
}