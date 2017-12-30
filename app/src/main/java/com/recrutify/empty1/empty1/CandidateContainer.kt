package com.recrutify.empty1.empty1

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Reader

/**
 * Created by arturnowak on 28.12.2017.
 */
data class CandidateContainer (
    val candidate: Candidate,
    val projects: List<Project>
) {
    class Deserializer : ResponseDeserializable<CandidateContainer> {

        override fun deserialize(reader: Reader): CandidateContainer? = Gson().fromJson(reader, CandidateContainer::class.java)

        //override fun deserialize(reader: Reader) = Gson().fromJson(reader, LoginResponse::class.java)
    }

    class ListDeserializer : ResponseDeserializable<List<CandidateContainer>> {
        override fun deserialize(reader: Reader): List<CandidateContainer> {
            val type = object : TypeToken<List<CandidateContainer>>() {}.type
            return Gson().fromJson(reader, type)
        }
    }

}