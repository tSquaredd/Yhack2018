package com.tsquaredapplications.yhack2018

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    private val repo: DataRepository = DataRepository()

    fun getSwitchObservable(deviceId: String): MutableLiveData<Boolean> = repo.getSwitchObservable(deviceId)


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

    fun getAvgUsageObservable(deviceId: String) = repo.getAvgUsageObservable(deviceId)

    fun getTotalUsageObservable(deviceId: String) = repo.getTotalUsageObservable(deviceId)

    fun getTotalCarbonUsageObservable(deviceId: String) = repo.getTotalCarbonObservable(deviceId)

    fun getAvgCarbonUsageObservable(deviceId: String) = repo.getAvgCarbonObservable(deviceId)

    fun getCurrentCarbonObservable(deviceId: String) = repo.getCurrentCarbonObservable(deviceId)

    fun getCurrentCostObservable(deviceId: String) = repo.getCurrentCostObservable(deviceId)

    fun getTotalCostObservable(deviceId: String) = repo.getTotalCostObservable(deviceId)

    fun getAvgCostObservable(deviceId: String) = repo.getAvgCostObservable(deviceId)

    fun getGraphObservable(deviceId: String) = repo.getLineGraphObservable(deviceId)

    fun getHomeCurrentUsageObservable() = repo.getHomeCurrentUsageObservable()

    fun getHomeCurrentAvgObservable() = repo.getHomeCurrentUsageAvgObservable()

    fun getHomeCurrentTotalObservable()= repo.getHomeCurrentTotalObservable()
}