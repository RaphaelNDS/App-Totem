package com.example.app_totem

import android.content.Context

object PrinterConfig {

    private const val PREF_NAME = "printer_config"
    private const val KEY_VENDOR_ID = "vendor_id"
    private const val KEY_PRODUCT_ID = "product_id"

    fun save(context: Context, vendorId: Int, productId: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_VENDOR_ID, vendorId)
            .putInt(KEY_PRODUCT_ID, productId)
            .apply()
    }

    fun getVendorId(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_VENDOR_ID, -1)
    }

    fun getProductId(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_PRODUCT_ID, -1)
    }

    fun isConfigured(context: Context): Boolean {
        return getVendorId(context) != -1
    }
}