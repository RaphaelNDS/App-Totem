package com.example.app_totem

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PrinterConfigActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var txtSelected: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_printer_config)

        listView = findViewById(R.id.list_usb_devices)
        txtSelected = findViewById(R.id.txt_selected)

        loadDevices()
        showCurrentPrinter()
    }

    private fun loadDevices() {

        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val devices = usbManager.deviceList.values.toList()

        if (devices.isEmpty()) {
            Toast.makeText(this, "Nenhum dispositivo USB", Toast.LENGTH_SHORT).show()
            return
        }

        val items = devices.map {

            val name = buildString {
                append(it.manufacturerName ?: "Desconhecido")
                append(" ")
                append(it.productName ?: "")
            }

            """
        $name
        
        VID:${it.vendorId} PID:${it.productId}
        ${it.deviceName}
        """.trimIndent()
        }

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            items
        )

        listView.setOnItemClickListener { _, _, position, _ ->

            val device = devices[position]

            PrinterConfig.save(
                this,
                device.vendorId,
                device.productId
            )

            Toast.makeText(this, "Impressora salva!", Toast.LENGTH_SHORT).show()

            showCurrentPrinter()
        }
    }

    private fun showCurrentPrinter() {

        if (!PrinterConfig.isConfigured(this)) {
            txtSelected.text = "Nenhuma impressora configurada"
            return
        }

        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val devices = usbManager.deviceList.values

        val vendor = PrinterConfig.getVendorId(this)
        val product = PrinterConfig.getProductId(this)

        val device = devices.find {
            it.vendorId == vendor && it.productId == product
        }

        val name = device?.let {
            "${it.manufacturerName ?: ""} ${it.productName ?: ""}"
        } ?: "Desconhecido"

        txtSelected.text = """
        Impressora selecionada:
        $name
        
        VID:$vendor PID:$product
    """.trimIndent()
    }
}