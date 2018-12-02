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

    private var outletOneAvgCurrentLiveData = MutableLiveData<Double>()
    private var outletTwoAvgCurrentLiveData = MutableLiveData<Double>()

    private var outletOneTotalCurrentLiveData = MutableLiveData<Double>()
    private var outletTwoTotalCurrentLiveData = MutableLiveData<Double>()

    private var outletOneTotalCarbonLiveData = MutableLiveData<Double>()
    private var outletTwoTotalCarbonLiveData = MutableLiveData<Double>()

    private var outletOneAvgCarbonLiveData = MutableLiveData<Double>()
    private var outletTwoAvgCarbonLiveData = MutableLiveData<Double>()

    private var outletOneCurrentCarbonLiveData = MutableLiveData<Double>()
    private var outletTwoCurrentCarbonLiveData = MutableLiveData<Double>()

    private var outletOneCostCurrentLiveData = MutableLiveData<Double>()
    private var outletTwoCostCurrentLiveData = MutableLiveData<Double>()

    private var outletOneCostAvgLiveData = MutableLiveData<Double>()
    private var outletTwoCostAvgLiveData = MutableLiveData<Double>()

    private var outletOneCostTotalLiveData = MutableLiveData<Double>()
    private var outletTwoCostTotalLiveData = MutableLiveData<Double>()

    init {
        getOutletSwitchStream(OutletNameUtil.OUTLET_ONE, outletOneLiveData)
        getOutletSwitchStream(OutletNameUtil.OUTLET_TWO, outletTwoLiveData)

        getOutletNameStream(OutletNameUtil.OUTLET_ONE, outletOneNameLiveData)
        getOutletNameStream(OutletNameUtil.OUTLET_TWO, outletTwoNameLiveData)

        getOutletCurrentStream(OutletNameUtil.OUTLET_ONE, outletOneCurrentLiveData)
        getOutletCurrentStream(OutletNameUtil.OUTLET_TWO, outletTwoCurrentLiveData)

        getOutletListStream(OutletNameUtil.OUTLET_ONE, outletOneListLiveData)
        getOutletListStream(OutletNameUtil.OUTLET_TWO, outletTwoListLiveData)

        getOutletAvgCurrentStream(OutletNameUtil.OUTLET_ONE, outletOneAvgCurrentLiveData)
        getOutletAvgCurrentStream(OutletNameUtil.OUTLET_TWO, outletTwoAvgCurrentLiveData)

        getOutletTotalCurrentStream(OutletNameUtil.OUTLET_ONE, outletOneTotalCurrentLiveData)
        getOutletTotalCurrentStream(OutletNameUtil.OUTLET_TWO, outletTwoTotalCurrentLiveData)

        getOutletAvgCarbonStream(OutletNameUtil.OUTLET_ONE, outletOneAvgCarbonLiveData)
        getOutletAvgCarbonStream(OutletNameUtil.OUTLET_TWO, outletTwoAvgCarbonLiveData)

        getOutletTotalCarbonStream(OutletNameUtil.OUTLET_ONE, outletOneTotalCarbonLiveData)
        getOutletTotalCarbonStream(OutletNameUtil.OUTLET_TWO, outletTwoTotalCarbonLiveData)

        getOutletCurrentCarbonStream(OutletNameUtil.OUTLET_ONE, outletOneCurrentCarbonLiveData)
        getOutletCurrentCarbonStream(OutletNameUtil.OUTLET_TWO, outletTwoCurrentCarbonLiveData)

        getOutletTotalCostStream(OutletNameUtil.OUTLET_ONE, outletOneCostTotalLiveData)
        getOutletTotalCostStream(OutletNameUtil.OUTLET_TWO, outletTwoCostTotalLiveData)

        getOutletAvgCostStream(OutletNameUtil.OUTLET_ONE, outletOneCostAvgLiveData)
        getOutletAvgCostStream(OutletNameUtil.OUTLET_TWO, outletTwoCostAvgLiveData)

        getOutletCurrentCostStream(OutletNameUtil.OUTLET_ONE, outletOneCostCurrentLiveData)
        getOutletCurrentCostStream(OutletNameUtil.OUTLET_TWO, outletTwoCostCurrentLiveData)

    }

    private fun getOutletTotalCostStream(deviceId: String, liveData: MutableLiveData<Double>){
        val dbRef = FirebaseUtil.getOutletTotalCostDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do Nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val totalCost = p0.getValue(Double::class.java)
                liveData.value = totalCost
            }

        })
    }

    private fun getOutletAvgCostStream(deviceId: String, liveData: MutableLiveData<Double>){
        val dbRef = FirebaseUtil.getOutletAvgCostDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do Nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val avgCost = p0.getValue(Double::class.java)
                liveData.value = avgCost
            }

        })
    }

    private fun getOutletCurrentCostStream(deviceId: String, liveData: MutableLiveData<Double>){
        val dbRef = FirebaseUtil.getOutletCurrentCostDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do Nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val currentCost = p0.getValue(Double::class.java)
                liveData.value = currentCost
            }

        })
    }

    private fun getOutletCurrentCarbonStream(deviceId: String, liveData: MutableLiveData<Double>){
        val dbRef = FirebaseUtil.getOutletCurrentCarbonDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do Nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val currentCarbon = p0.getValue(Double::class.java)
                liveData.value = currentCarbon
            }

        })
    }

    private fun getOutletAvgCarbonStream(deviceId: String, liveData: MutableLiveData<Double>) {
        val dbRef = FirebaseUtil.getOutletAvgCarbonDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val avgCarbon = p0.getValue(Double::class.java)
                liveData.value = avgCarbon
            }

        })
    }

    private fun getOutletTotalCarbonStream(deviceId: String, liveData: MutableLiveData<Double>) {
        val dbRef = FirebaseUtil.getOutletTotalCarbonDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val totalCarbon = p0.getValue(Double::class.java)
                liveData.value = totalCarbon
            }

        })
    }

    private fun getOutletTotalCurrentStream(deviceId: String, liveData: MutableLiveData<Double>) {
        val dbRef = FirebaseUtil.getOutletTotalCurrentDbRef(deviceId)
        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val totalUsage = p0.getValue(Double::class.java)
                liveData.value = totalUsage
            }

        })
    }

    private fun getOutletAvgCurrentStream(deviceId: String, liveData: MutableLiveData<Double>) {
        val dbRef = FirebaseUtil.getOutletAvgCurrentDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val avgUsage = p0.getValue(Double::class.java)
                liveData.value = avgUsage
            }

        })
    }

    private fun getOutletCurrentStream(deviceId: String, liveData: MutableLiveData<Double>) {
        val dbRef = FirebaseUtil.getOutletLatestWattsDbRef(deviceId)

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val currentUsage = p0.getValue(Double::class.java)
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
                    val watts = child.child("watts").getValue(Double::class.java)
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

    fun getSwitchObservable(deviceId:String): MutableLiveData<Boolean> {
        return when (deviceId) {
            OutletNameUtil.OUTLET_ONE -> outletOneLiveData
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

    fun getAvgUsageObservable(deviceId: String): MutableLiveData<Double> =
        if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneAvgCurrentLiveData else outletTwoAvgCurrentLiveData

    fun getTotalUsageObservable(deviceId: String): MutableLiveData<Double> =
            if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneTotalCurrentLiveData else outletTwoTotalCurrentLiveData

    fun getTotalCarbonObservable(deviceId: String): MutableLiveData<Double> =
            if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneTotalCarbonLiveData else outletTwoTotalCarbonLiveData

    fun getAvgCarbonObservable(deviceId: String): MutableLiveData<Double> =
        if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneAvgCarbonLiveData else outletTwoAvgCarbonLiveData

    fun getCurrentCarbonObservable(deviceId: String): MutableLiveData<Double> =
        if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneCurrentCarbonLiveData else outletTwoCurrentCarbonLiveData

    fun getTotalCostObservable(deviceId: String): MutableLiveData<Double> =
        if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneCostTotalLiveData else outletTwoCostTotalLiveData

    fun getAvgCostObservable(deviceId: String): MutableLiveData<Double> =
        if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneCostAvgLiveData else outletTwoCostAvgLiveData

    fun getCurrentCostObservable(deviceId: String): MutableLiveData<Double> =
        if (deviceId == OutletNameUtil.OUTLET_ONE) outletOneCostCurrentLiveData else outletTwoCostCurrentLiveData

}