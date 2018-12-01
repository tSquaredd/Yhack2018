package com.tsquaredapplications.yhack2018.views


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.robinhood.ticker.TickerUtils
import com.tsquaredapplications.yhack2018.DataViewModel
import com.tsquaredapplications.yhack2018.R
import com.tsquaredapplications.yhack2018.SparkViewFloatAdapter
import kotlinx.android.synthetic.main.fragment_device_feed.*
import kotlinx.android.synthetic.main.outlet_feed_list_item.view.*


class DeviceFeedFragment : Fragment() {

    private lateinit var viewModel: DataViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outlet_one.usage_ticker.setCharacterLists(TickerUtils.provideAlphabeticalList())
        outlet_two.usage_ticker.setCharacterLists(TickerUtils.provideAlphabeticalList())



        setupObserver("outlet-one", outlet_one)
        setupObserver("outlet-two", outlet_two)

        setSwitchListeners()

        setupSwitchObservers()

    }

    private fun setupSwitchObservers() {
        viewModel.getSwitchObservable(0).observe(this, Observer {
            outlet_one.outlet_switch.isChecked = it
        })

        viewModel.getSwitchObservable(1).observe(this, Observer {
            outlet_two.outlet_switch.isChecked = it
        })
    }

    private fun setSwitchListeners() {
        outlet_one.outlet_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setSwitch("outlet-one", isChecked)
        }

        outlet_two.outlet_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setSwitch("outlet-two", isChecked)
        }




    }

    private fun setupObserver(deviceId: String, view: View) {
        val namesDbRef = FirebaseDatabase.getInstance().reference.child("device-names")

        namesDbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val nameDeviceOne = p0.child("outlet-one").getValue(String::class.java)
                val nameDeviceTwo = p0.child("outlet-two").getValue(String::class.java)

                outlet_one.outlet_name.text = nameDeviceOne
                outlet_two.outlet_name.text = nameDeviceTwo
            }

        })


        val dbRef = FirebaseDatabase.getInstance().reference
            .child("devices").child(deviceId)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {

                // Current Usage
                val latestDbRef = p0.child("data").child("latest")
                val currentUsageString = latestDbRef.child("watts").getValue(String::class.java)
                val currentUsage = currentUsageString?.toDouble()

                view.usage_ticker.text = "${currentUsage.toString()} kW/hr"

                // SparkView
                val childList = p0.child("data").child("list").children
                val wattsList = arrayListOf<Float>()
                for (child in childList) {
                    //val time = child.child("time").getValue(Int::class.java)
                    val watts = child.child("watts").getValue(String::class.java)
                    if (watts != null) {
                        wattsList.add(watts.toFloat())
                    }

                }
                view.spark_view.adapter = SparkViewFloatAdapter(wattsList)

            }


        })
    }


}
