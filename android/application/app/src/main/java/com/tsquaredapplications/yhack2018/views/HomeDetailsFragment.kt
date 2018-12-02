package com.tsquaredapplications.yhack2018.views


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
