package com.example.myapplication.models

data class Property(val id: Int, val title: String = "", val description: String = "", val image: String = "", val horizontal: Boolean = false, val data: List<Property>? = null)