package com.chat.sdk.activity.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chat.sdk.modal.ChatData
import com.chat.sdk.modal.Message
import com.chat.sdk.network.GetChatData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

internal class ChatViewModal : ViewModel() {
    private val errorMessage = MutableLiveData<String>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val chatData = MutableLiveData<ChatData>()
    val visitorMessage = MutableLiveData<ArrayList<Message>>()

    init {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            GetChatData.chatDataSharedFlow.collect { value ->
                chatData.postValue(value)
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

    fun addMessage(chat: ArrayList<Message>) {
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