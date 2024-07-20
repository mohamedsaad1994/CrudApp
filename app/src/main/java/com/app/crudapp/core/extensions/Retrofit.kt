package com.app.crudapp.core.extensions

import retrofit2.Retrofit

inline fun <reified T> Retrofit.create(): T = create(T::class.java)
