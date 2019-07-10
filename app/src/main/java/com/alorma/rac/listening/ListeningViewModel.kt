package com.alorma.rac.listening

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alorma.rac.core.BaseViewModel

class ListeningViewModel : BaseViewModel() {

    private val _now: MutableLiveData<ListeningStatus> = MutableLiveData()
    val now: LiveData<ListeningStatus>
        get() = _now

    init {
        _now.postValue(ListeningStatus.Nothing)
    }

    override fun onStart() {
        super.onStart()
        _now.postValue(ListeningStatus.Playing.Now)
    }

}