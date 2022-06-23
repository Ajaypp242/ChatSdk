package com.chat.sdk.modal

data class Message(
    val id_ip: String,
    val message: String,
    val msg_status: String,
    val msgtm: String,
    val pushurl: String,
    val rand_no: String,
    val sno: String,
    val v_o: String
)


data class ChatData(val chat_status: String,
                    val messages: ArrayList<Message>,
                    val operator_status: List<Operator>
                    )
