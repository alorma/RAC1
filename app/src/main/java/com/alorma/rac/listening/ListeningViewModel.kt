package com.alorma.rac.listening

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alorma.rac.core.BaseViewModel

class ListeningViewModel : BaseViewModel() {

    private val _status: MutableLiveData<ListeningStatus> = MutableLiveData()
    val status: LiveData<ListeningStatus>
        get() = _status

    init {
        _status.postValue(ListeningStatus.Nothing)
    }

    override fun onStart() {
        super.onStart()
        _status.postValue(ListeningStatus.Playing.Now)
    }

}