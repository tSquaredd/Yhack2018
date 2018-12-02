package com.tsquaredapplications.yhack2018.views


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.robinhood.ticker.TickerUtils
import com.tsquaredapplications.yhack2018.DataViewModel
import com.tsquaredapplications.yhack2018.R
import com.tsquaredapplications.yhack2018.SparkViewFloatAdapter
import com.tsquaredapplications.yhack2018.util.OutletNameUtil
import kotlinx.android.synthetic.main.fragment_device_feed.*
import kotlinx.android.synthetic.main.outlet_feed_list_item.view.*


class DeviceFeedFragment : Fragment() {

    private lateinit var viewModel: DataViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outlet_one.usage_ticker.setCharacterLists(TickerUtils.provideAlphabeticalList())
        outlet_two.usage_ticker.setCharacterLists(TickerUtils.provideAlphabeticalList())

        setSwitchListeners()
        setupSwitchObservers()
        setNameObservers()
        setCurrentUsageObservers()
        setUsageListObservers()
        setDetailClickListeners()

    }

    private fun setDetailClickListeners() {
        outlet_one.spark_view.setOnClickListener {
            navigateToDetailFragment(OutletNameUtil.OUTLET_ONE)
        }

        outlet_two.spark_view.setOnClickListener {
            navigateToDetailFragment(OutletNameUtil.OUTLET_TWO)
        }
    }

    private fun navigateToDetailFragment(deviceId: String){
        val directions =
            DeviceFeedFragmentDirections.actionNavFeedToNavDetails().setDeviceId(deviceId)

        findNavController().navigate(directions)
    }

    private fun setUsageListObservers() {
        viewModel.getUsageListObservable(OutletNameUtil.OUTLET_ONE)
            .observe(this, Observer {
                outlet_one.spark_view.adapter = SparkViewFloatAdapter(it)
            })

        viewModel.getUsageListObservable(OutletNameUtil.OUTLET_TWO)
            .observe(this, Observer {
                outlet_two.spark_view.adapter = SparkViewFloatAdapter(it)
            })
    }

    private fun setCurrentUsageObservers() {
        viewModel.getCurrentUsageObservable(OutletNameUtil.OUTLET_ONE)
            .observe(this, Observer {
                outlet_one.usage_ticker.text = "$it kW/hr"
            })

        viewModel.getCurrentUsageObservable(OutletNameUtil.OUTLET_TWO)
            .observe(this, Observer {
                outlet_two.usage_ticker.text = "$it kW/hr"
            })
    }

    private fun setupSwitchObservers() {
        viewModel.getSwitchObservable(OutletNameUtil.OUTLET_ONE).observe(this, Observer {
            outlet_one.outlet_switch.isChecked = it
        })

        viewModel.getSwitchObservable(OutletNameUtil.OUTLET_TWO).observe(this, Observer {
            outlet_two.outlet_switch.isChecked = it
        })
    }


    private fun setSwitchListeners() {
        master_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.masterToggle(isChecked)
        }

        outlet_one.outlet_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setSwitch(OutletNameUtil.OUTLET_ONE, isChecked)
        }

        outlet_two.outlet_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setSwitch(OutletNameUtil.OUTLET_TWO, isChecked)
        }
    }

    private fun setNameObservers() {
        viewModel.getNameObservable(OutletNameUtil.OUTLET_ONE).observe(this, Observer {
            outlet_one.outlet_name.text = it
        })

        viewModel.getNameObservable(OutletNameUtil.OUTLET_TWO).observe(this, Observer {
            outlet_two.outlet_name.text = it
        })
    }
}
