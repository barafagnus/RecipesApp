package ru.vysokov.recipesapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vysokov.recipesapp.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
)

fun CategoryEntity.toModel() = Category(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)