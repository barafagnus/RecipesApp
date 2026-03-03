package ru.vysokov.recipesapp.di

interface Factory<T> {
    fun create(): T
}