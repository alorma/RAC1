package com.alorma.rac.listening

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alorma.rac.core.BaseViewModel

class ListeningViewModel : BaseViewModel() {

    private val _status: MutableLiveData<ListeningStatus> = MutableLiveData()
    val status: LiveData<ListeningStatus>
        get() = _status

    fun onStreamActionClick() {
        _status.value?.let {
            val newStatus = when (it) {
                ListeningStatus.Nothing -> ListeningStatus.Playing.Now
                is ListeningStatus.Playing -> ListeningStatus.Nothing
            }
            _status.postValue(newStatus)
        } ?: _status.postValue(ListeningStatus.Playing.Now)
    }

}