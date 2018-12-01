package com.tsquaredapplications.yhack2018

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tsquaredapplications.yhack2018.util.FirebaseUtil

class DataRepository {

    private var outletOneLiveData = MutableLiveData<Boolean>()
    private var outletTwoLiveData = MutableLiveData<Boolean>()

    init {
        getOutletStream("outlet-one", outletOneLiveData)
        getOutletStream("outlet-two", outletTwoLiveData)
    }

        private fun getOutletStream(deviceId: String, liveData: MutableLiveData<Boolean>){

            val dbRef = FirebaseUtil.getOutletSwitchDbRef(deviceId)

            dbRef.addValueEventListener( object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    // Do nothing
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val isOn = p0.getValue(String::class.java)
                    isOn?.let {
                        liveData.value = isOn == "true"
                    }
                }
            })
    }


     fun getSwitchObservable(num: Int): MutableLiveData<Boolean>{
        return when(num){
            0-> outletOneLiveData
            else -> outletTwoLiveData
        }
    }

    fun setSwitch(deviceId: String, isOn:Boolean){
        val dbRef = FirebaseUtil.getOutletSwitchDbRef(deviceId)
        if (isOn) dbRef.setValue("true")
        else dbRef.setValue("false")
    }

}