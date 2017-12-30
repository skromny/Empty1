package com.recrutify.empty1.empty1

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.ref.PhantomReference

/**
 * Created by arturnowak on 28.12.2017.
 */
data class Project (
    val id: Int,
    val name: String,
    val companyId: Int,
    val companyName: String,
    val quantity: Int,
    val description: String,
    val location: String,
    val referenceId: String
) {
    class Deserializer : ResponseDeserializable<Project> {

        override fun deserialize(reader: Reader): Project? = Gson().fromJson(reader, Project::class.java)

        //override fun deserialize(reader: Reader) = Gson().fromJson(reader, LoginResponse::class.java)
    }

    class ListDeserializer : ResponseDeserializable<List<Project>> {
        override fun deserialize(reader: Reader): List<Project> {
            val type = object : TypeToken<List<Project>>() {}.type
            return Gson().fromJson(reader, type)
        }
    }
}