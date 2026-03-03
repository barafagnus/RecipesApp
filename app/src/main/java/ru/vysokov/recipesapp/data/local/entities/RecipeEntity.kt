package ru.vysokov.recipesapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vysokov.recipesapp.model.Ingredient
import ru.vysokov.recipesapp.model.Recipe

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "category_id") val categoryId: Int?,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "ingredients") val ingredients: List<Ingredient>,
    @ColumnInfo(name = "method") val method: List<String>,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean?
)

fun RecipeEntity.toModel() = Recipe(
    id = id,
    title = title,
    ingredients = ingredients,
    method = method,
    imageUrl = imageUrl,
)

fun Recipe.toEntity(categoryId: Int?, isFavorite: Boolean?) = RecipeEntity(
    id = id,
    categoryId = categoryId,
    title = title,
    ingredients = ingredients,
    method = method,
    imageUrl = imageUrl,
    isFavorite = isFavorite
)