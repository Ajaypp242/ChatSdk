package com.chat.sdk.activity.pre_post_chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chat.sdk.modal.Operator
import com.chat.sdk.network.ApiAdapter
import kotlinx.coroutines.*

class PrePostViewModal : ViewModel() {
    private val errorMessage = MutableLiveData<String>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val operators = MutableLiveData<List<Operator>>()

    fun getChats(
        siteId: String,
        proprofs_language_id: String,
        proprofs_session: String,
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ApiAdapter.apiClient.getChat(
                siteId, proprofs_language_id, proprofs_session, "0",
                "330", "1", "chat_sdk", "0",
                "",
                "", ""
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    operators.postValue(response.body()?.operator_status)
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
}

class PrePostViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrePostViewModal::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrePostViewModal() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}