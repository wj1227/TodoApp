package com.example.todoapp.fragments.list

interface Identifiable {
    val itentifier: Any

    override operator fun equals(other: Any?): Boolean

}