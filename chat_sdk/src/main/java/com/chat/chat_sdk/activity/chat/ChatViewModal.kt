package com.chat.chat_sdk.activity.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chat.chat_sdk.modal.Message
import com.chat.chat_sdk.network.ApiAdapter
import kotlinx.coroutines.*

class ChatViewModal : ViewModel() {
    private val errorMessage = MutableLiveData<String>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val messages = MutableLiveData<ArrayList<Message>>()
    val visitorMessage = MutableLiveData<ArrayList<Message>>()

    fun getChats(
        siteId: String,
        proprofs_language_id: String,
        proprofs_session: String,
        messageCounter: String,
        visitorName: String,
        visitorEmail: String
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ApiAdapter.apiClient.getChat(
                siteId, proprofs_language_id, proprofs_session, messageCounter,
                "330", "1", "chat_sdk", "0",
                visitorName,
                visitorEmail, ""
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    messages.postValue(response.body()?.messages)
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun addMessage(chat: ArrayList<Message>){
        visitorMessage.postValue(chat)
    }
}

class ChatViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModal::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModal() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}