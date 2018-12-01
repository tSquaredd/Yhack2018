package com.tsquaredapplications.yhack2018

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    private val repo: DataRepository = DataRepository()
    private var outletOneLiveData = MutableLiveData<Boolean>()
    private var outletTwoLiveData = MutableLiveData<Boolean>()


    init {

        outletOneLiveData = repo.getSwitchObservable(0)
        outletTwoLiveData = repo.getSwitchObservable(1)
    }

    fun getSwitchObservable(num: Int): MutableLiveData<Boolean> {
        return when (num) {
            0 -> return outletOneLiveData
            else -> outletTwoLiveData
        }
    }

    fun setSwitch(deviceId: String, isOn: Boolean) {
        repo.setSwitch(deviceId, isOn)
    }

    fun getNameObservable(deviceId: String): MutableLiveData<String>{
       return  repo.getNameObservable(deviceId)
    }

    fun getCurrentUsageObservable(deviceId: String) = repo.getCurrentUsageObservable(deviceId)

    fun getUsageListObservable(deviceId: String) = repo.getUsageListObservable(deviceId)

    fun masterToggle(toOn: Boolean){
        repo.masterToggle(toOn)
    }
}