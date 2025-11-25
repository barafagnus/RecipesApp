package ru.vysokov.recipesapp.data.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log

object AssetLoader {
    fun loadAssets(context: Context, imagePath: String?): Drawable? {

        if (imagePath.isNullOrEmpty()) {
            Log.e("!!!", "Image path is null or empty")
            return null
        }

        return try {
            Drawable.createFromStream(
                context.assets.open(imagePath),
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", e.stackTraceToString())
            null
        }

    }
}
