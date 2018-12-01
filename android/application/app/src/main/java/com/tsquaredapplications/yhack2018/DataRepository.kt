package com.tsquaredapplications.yhack2018

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tsquaredapplications.yhack2018.util.FirebaseUtil
import com.tsquaredapplications.yhack2018.util.OutletNameUtil

class DataRepository {

    private var outletOneLiveData = MutableLiveData<Boolean>()
    private var outletTwoLiveData = MutableLiveData<Boolean>()

    private var outletOneNameLiveData = MutableLiveData<String>()
    private var outletTwoNameLiveData = MutableLiveData<String>()

    private var outletOneListLiveData = MutableLiveData<List<Float>>()
    private var outletTwoListLiveData = MutableLiveData<List<Float>>()

    private var outletOneCurrentLiveData = MutableLiveData<Double>()
    private var outletTwoCurrentLiveData = MutableLiveData<Double>()

    init {
        getOutletSwitchStream(OutletNameUtil.OUTLET_ONE, outletOneLiveData)
        getOutletSwitchStream(OutletNameUtil.OUTLET_TWO, outletTwoLiveData)

        getOutletNameStream(OutletNameUtil.OUTLET_ONE, outletOneNameLiveData)
        getOutletNameStream(OutletNameUtil.OUTLET_TWO, outletTwoNameLiveData)

        getOutletCurrentStream(OutletNameUtil.OUTLET_ONE, outletOneCurrentLiveData)
        getOutletCurrentStream(OutletNameUtil.OUTLET_TWO, outletTwoCurrentLiveData)

        getOutletListStream(OutletNameUtil.OUTLET_ONE, outletOneListLiveData)
        getOutletListStream(OutletNameUtil.OUTLET_TWO, outletTwoListLiveData)
    }

    private fun getOutletCurrentStream(deviceId: String, liveData: MutableLiveData<Double>) {
        val dbRef = FirebaseUtil.getOutletLatestWattsDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val currentUsageString = p0.getValue(String::class.java)
                val currentUsage = currentUsageString?.toDouble()
                liveData.value = currentUsage
            }
        })
    }

    private fun getOutletListStream(deviceId: String, liveData: MutableLiveData<List<Float>>) {

        val dbRef = FirebaseUtil.getOutletDataListDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
               val children = p0.children
                val wattsList = arrayListOf<Float>()
                for (child in children){
                    val watts = child.child("watts").getValue(String::class.java)
                    watts?.let { nonNullWatts ->
                        wattsList.add(nonNullWatts.toFloat())
                    }
                }
                liveData.value = wattsList
            }
        })
    }

    private fun getOutletSwitchStream(deviceId: String, liveData: MutableLiveData<Boolean>) {

        val dbRef = FirebaseUtil.getOutletSwitchDbRef(deviceId)

        dbRef.addValueEventListener(object : ValueEventListener {
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

    private fun getOutletNameStream(deviceId: String, liveData: MutableLiveData<String>){
        val dbRef = FirebaseUtil.getOutletNamesDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val name = p0.getValue(String::class.java)
                liveData.value = name
            }

        })
    }

    fun getSwitchObservable(num: Int): MutableLiveData<Boolean> {
        return when (num) {
            0 -> outletOneLiveData
            else -> outletTwoLiveData
        }
    }

    fun setSwitch(deviceId: String, isOn: Boolean) {
        val dbRef = FirebaseUtil.getOutletSwitchDbRef(deviceId)
        if (isOn) dbRef.setValue("true")
        else dbRef.setValue("false")
    }

    fun getNameObservable(deviceId: String): MutableLiveData<String> {
        return when (deviceId) {
            OutletNameUtil.OUTLET_ONE -> outletOneNameLiveData
            else -> outletTwoNameLiveData
        }
    }

    fun getCurrentUsageObservable(deviceId: String): MutableLiveData<Double> =
        if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneCurrentLiveData else outletTwoCurrentLiveData

    fun getUsageListObservable(deviceId: String): MutableLiveData<List<Float>> =
            if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneListLiveData else outletTwoListLiveData

    fun masterToggle(toOn: Boolean) {
        val dbRefOne = FirebaseUtil.getOutletSwitchDbRef(OutletNameUtil.OUTLET_ONE)
        dbRefOne.setValue(toOn.toString())

        val dbRefTwo = FirebaseUtil.getOutletSwitchDbRef(OutletNameUtil.OUTLET_TWO)
        dbRefTwo.setValue(toOn.toString())
    }
}