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
        val name: String,
        val position: String,
        val description: String
) {
    class Deserializer : ResponseDeserializable<Candidate> {
//        [{
//            "id":81639,
//            "avatarLink":"https://rgcimagestore.azureedge.net/eaf3ebe1b9744722245a344231e521ea/714172c2-8b9c-47db-b122-0a4214782c80.jpg",
//            "avatar":null,
//            "name":"raz dwa trzy",
//            "sex":true,
//            "title":null,
//            "driver":null,
//            "birthDate":null,
//            "maritalStatus":null,
//            "countryOfOrigin":null,
//            "countryOfOriginName":"",
//            "car":null,
//            "creationTime":"2018-01-03T19:09:25.1670000Z",
//            "lastUpdate":"2018-01-04T09:20:16.8430000Z",
//            "accountId":1,"createdBy":7,
//            "isFavourite":false,
//            "labels":[],
//            "contractor":null,
//            "active":true,
//            "activeIn":null,
//            "hasLinkedIn":false,
//            "documentSearchResult":null,
//            "score":null,
//            "sourceId":1,
//            "responsibleUserId":null,
//            "responsibleUserName":"",
//            "connection":[
//                {"id":139313,"type":3,"value":"668 416 221"},
//                {"id":139314,"type":1,"value":"daniel.piotrowski49@gmail.com"},
//                {"id":139315,"type":3,"value":"05396207504"},
//                {"id":139316,"type":1,"value":"umutugrak@hotmail.com"}
//            ],
//            "address":[],
//            "position":null,
//            "description":null}
//
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