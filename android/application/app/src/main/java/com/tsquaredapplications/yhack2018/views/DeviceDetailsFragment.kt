package com.tsquaredapplications.yhack2018.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.robinhood.ticker.TickerUtils
import com.tsquaredapplications.yhack2018.DataViewModel
import com.tsquaredapplications.yhack2018.R
import kotlinx.android.synthetic.main.fragment_device_details.*


class DeviceDetailsFragment : androidx.fragment.app.Fragment() {

    lateinit var deviceId: String
    private lateinit var viewModel: DataViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceId = DeviceDetailsFragmentArgs.fromBundle(arguments).deviceId

        setupTickers()
        setNameObserver()
        setSwitchObserver()
        setSwitchListener()
        setCurrentUsageObserver()
        setAvgUsageObserver()
        setTotalUsageObserver()

        setCarbonCurrentObserver()
        setCarbonAvgObserver()
        setCarbonTotalObserver()

        setCostCurrentObserver()
        setCostAvgObserver()
        setCostTotalObserver()

    }

    private fun setCostTotalObserver() {
        viewModel.getTotalCostObservable(deviceId).observe(this, Observer {
            cost_total_ticker.text = "$$it"
        })
    }

    private fun setCostAvgObserver() {
        viewModel.getAvgCostObservable(deviceId).observe(this, Observer {
            cost_avg_ticker.text = "$$it"
        })
    }

    private fun setCostCurrentObserver() {
        viewModel.getCurrentCostObservable(deviceId).observe(this, Observer{
            cost_current_ticker.text = "$$it"
        })
    }


    private fun setupTickers() {
        current_usage_ticker.setCharacterLists(TickerUtils.provideNumberList())
        avg_usage_ticker.setCharacterLists(TickerUtils.provideNumberList())
        total_usage_ticker.setCharacterLists(TickerUtils.provideNumberList())

        carbon_output_ticker.setCharacterLists(TickerUtils.provideNumberList())
        carbon_avg_ticker.setCharacterLists(TickerUtils.provideNumberList())
        carbon_total_ticker.setCharacterLists(TickerUtils.provideNumberList())

        cost_avg_ticker.setCharacterLists(TickerUtils.provideAlphabeticalList())
        cost_current_ticker.setCharacterLists(TickerUtils.provideAlphabeticalList())
        cost_total_ticker.setCharacterLists(TickerUtils.provideAlphabeticalList())

    }

    private fun setCarbonTotalObserver() {
        viewModel.getTotalCarbonUsageObservable(deviceId).observe(this, Observer {
            carbon_total_ticker.text = "$it"
        })
    }

    private fun setCarbonAvgObserver() {
        viewModel.getAvgCarbonUsageObservable(deviceId).observe(this, Observer{
            carbon_avg_ticker.text = "$it"
        })
    }

    private fun setCarbonCurrentObserver() {
        viewModel.getCurrentCarbonObservable(deviceId).observe(this, Observer {
            carbon_output_ticker.text = "$it"
        })
    }

    private fun setTotalUsageObserver() {
        viewModel.getTotalUsageObservable(deviceId).observe(this, Observer {
            total_usage_ticker.text = "$it"
        })
    }

    private fun setAvgUsageObserver() {
        viewModel.getAvgUsageObservable(deviceId).observe(this, Observer {
            avg_usage_ticker.text = "$it"
        })
    }

    private fun setCurrentUsageObserver() {
        viewModel.getCurrentUsageObservable(deviceId).observe(this, Observer{
            current_usage_ticker.text = "$it"
        })
    }

    private fun setSwitchListener() {
       device_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setSwitch(deviceId, isChecked)

       }
    }

    private fun setSwitchObserver() {
      viewModel.getSwitchObservable(deviceId).observe(this, Observer {
          device_switch.isChecked = it
      })
    }

    private fun setNameObserver() {
        viewModel.getNameObservable(deviceId).observe(this, Observer {
            device_name.text = it
        })
    }


}
