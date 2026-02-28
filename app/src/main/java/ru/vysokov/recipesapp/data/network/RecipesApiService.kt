package ru.vysokov.recipesapp.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vysokov.recipesapp.model.Category
import ru.vysokov.recipesapp.model.Recipe

interface RecipeApiService {
    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") recipeId: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesById(@Query("ids") recipesIds: String): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategory(@Path("id") categoryId: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategory(@Path("id") categoryId: Int?): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>
}