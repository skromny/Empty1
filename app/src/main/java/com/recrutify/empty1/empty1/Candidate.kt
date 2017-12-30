package com.recrutify.empty1.empty1

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Reader

/**
 * Created by arturnowak on 29.12.2017.
 */
data class Candidate(
        val id: Int,
        val avatarLink: String,
        val name: String
) {
    class Deserializer : ResponseDeserializable<Candidate> {

        override fun deserialize(reader: Reader): Candidate? = Gson().fromJson(reader, Candidate::class.java)

        //override fun deserialize(reader: Reader) = Gson().fromJson(reader, LoginResponse::class.java)
    }

    class ListDeserializer : ResponseDeserializable<List<Candidate>> {
        override fun deserialize(reader: Reader): List<Candidate> {
            val type = object : TypeToken<List<Candidate>>() {}.type
            return Gson().fromJson(reader, type)
        }
    }
}