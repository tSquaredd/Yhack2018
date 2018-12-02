package com.tsquaredapplications.yhack2018.util

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseUtil {
    companion object {
        fun getOutletSwitchDbRef(deviceId: String): DatabaseReference {
            return FirebaseDatabase.getInstance().reference
                .child("devices").child(deviceId).child("status").child("isOn")
        }

        fun getOutletNamesDbRef(deviceId:String): DatabaseReference{
            return FirebaseDatabase.getInstance().reference.child("device-names").child(deviceId)
        }

        fun getOutletDataListDbRef(deviceId: String): DatabaseReference =
            FirebaseDatabase.getInstance().reference
                .child("devices").child(deviceId).child("data").child("list")


        // ENERGY
        fun getOutletLatestWattsDbRef(deviceId: String): DatabaseReference =
                FirebaseDatabase.getInstance().reference
                    .child("devices").child(deviceId).child("data").child("latest").child("watts")

        fun getOutletAvgCurrentDbRef(deviceId: String): DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("devices").child(deviceId).child("totals").child("average")

        fun getOutletTotalCurrentDbRef(deviceId: String): DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("devices").child(deviceId).child("totals").child("watts")


        // CARBON
        fun getOutletTotalCarbonDbRef(deviceId: String): DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("devices").child(deviceId).child("totals")
                    .child("energy").child("carbon").child("total")

        fun getOutletAvgCarbonDbRef(deviceId: String): DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("devices").child(deviceId).child("totals")
                    .child("energy").child("carbon").child("average")

        fun getOutletCurrentCarbonDbRef(deviceId: String): DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("devices").child(deviceId).child("totals")
            .child("energy").child("carbon").child("current")

        // COST
        fun getOutletTotalCostDbRef(deviceId: String):DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("devices").child(deviceId).child("totals")
                    .child("energy").child("price").child("total")

        fun getOutletAvgCostDbRef(deviceId: String):DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("devices").child(deviceId).child("totals")
                .child("energy").child("price").child("average")

        fun getOutletCurrentCostDbRef(deviceId: String): DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("devices").child(deviceId).child("totals")
                .child("energy").child("price").child("current")


        // HOME
        fun getHomeCurrentUsageDbRef(): DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("")
    }
}