package com.tsquaredapplications.yhack2018.views


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.robinhood.ticker.TickerUtils
import com.tsquaredapplications.yhack2018.DataViewModel
import com.tsquaredapplications.yhack2018.R
import com.tsquaredapplications.yhack2018.util.OutletNameUtil
import kotlinx.android.synthetic.main.fragment_home_details.*

class HomeDetailsFragment : Fragment() {

    private lateinit var viewModel: DataViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMasterSwitchListener()

        setupTickers()

        setCurrentUsageObserver()
        setCurrentAvgObserver()
        setCurrentTotalObserver()
        setCurrentCarbonObserver()
        setAvgCarbonObserver()
        setTotalCarbonObserver()

        setCurrentCostObserver()
        setTotalCostObserver()
        setAvgCostObserver()

        setGraphObservers()

    }

    private fun setGraphObservers() {
        viewModel.getGraphObservable(OutletNameUtil.OUTLET_ONE).observe(this, Observer {
            it.color = Color.RED
            graph_view.addSeries(it)
            graph_view.viewport.isXAxisBoundsManual = true
            graph_view.viewport.setMaxX(it.highestValueX)
        })

        viewModel.getGraphObservable(OutletNameUtil.OUTLET_TWO).observe(this, Observer {
            it.color = Color.GREEN
            graph_view.addSeries(it)
            graph_view.viewport.isXAxisBoundsManual = true
            graph_view.viewport.setMaxX(it.highestValueX)
        })
    }

    private fun setAvgCostObserver() {
        viewModel.getHomeCostAvgObservable().observe(this, Observer {
            cost_avg_ticker.text = "$$it"
        })
    }

    private fun setTotalCostObserver() {
        viewModel.getHomeCostTotalObservable().observe(this, Observer {
            cost_total_ticker.text = "$$it"
        })
    }

    private fun setCurrentCostObserver() {
        viewModel.getHomeCostCurrentObserver().observe(this, Observer {
            cost_current_ticker.text = "$$it"
        })
    }

    private fun setTotalCarbonObserver() {
        viewModel.getHomeCarbonTotalObservable().observe(this, Observer {
            carbon_total_ticker.text = "$it"
        })
    }

    private fun setAvgCarbonObserver() {
        viewModel.getHomeCarbonAvgObservable().observe(this, Observer {
            carbon_avg_ticker.text = "$it"
        })
    }

    private fun setCurrentCarbonObserver() {
        viewModel.getHomeCarbonCurrentObservable().observe(this, Observer {
            carbon_output_ticker.text = "$it"
        })
    }

    private fun setCurrentUsageObserver() {
        viewModel.getHomeCurrentUsageObservable().observe(this, Observer {
            current_usage_ticker.text = "$it"
        })
    }

    private fun setCurrentTotalObserver(){
        viewModel.getHomeCurrentTotalObservable().observe(this, Observer {
            total_usage_ticker.text = "$it"
        })
    }

    private fun setCurrentAvgObserver(){
        viewModel.getHomeCurrentAvgObservable().observe(this, Observer {
            avg_usage_ticker.text = "$it"
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

    private fun setMasterSwitchListener() {
        master_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.masterToggle(isChecked)

        }
    }

}
