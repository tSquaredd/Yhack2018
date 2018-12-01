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


        fun getOutletLatestWattsDbRef(deviceId: String): DatabaseReference =
                FirebaseDatabase.getInstance().reference
                    .child("devices").child(deviceId).child("data").child("latest").child("watts")
    }
}