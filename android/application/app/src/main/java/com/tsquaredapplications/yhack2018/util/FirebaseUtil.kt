package com.tsquaredapplications.yhack2018.util

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseUtil {
    companion object {
        fun getOutletSwitchDbRef(deviceId: String): DatabaseReference {
            return FirebaseDatabase.getInstance().reference
                .child("devices").child(deviceId).child("status").child("isOn")
        }
    }
}