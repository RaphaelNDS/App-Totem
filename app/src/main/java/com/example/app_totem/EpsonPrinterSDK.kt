package com.example.app_totem

import android.content.Context
import android.util.Log
import com.epson.epos2.discovery.*
import com.epson.epos2.printer.*

class EpsonPrinterSDK(
    private val context: Context
) : ReceiveListener {

    private var printer: Printer? = null
    private var printerTarget: String? = null
    private var isDiscovering = false

    // 🔥 AGORA USA O GENERATOR (COM QR)
    fun print(product: Product) {
        val data = EpsonCommandGenerator.generate(product, null)
        printRaw(data)
    }

    // 🔥 ENVIO REAL (BYTE)
    fun printRaw(data: ByteArray) {

        if (printerTarget == null && !isDiscovering) {
            discover(data)
            return
        }

        if (printer == null || printerTarget == null) {
            Log.e("EPSON", "Impressora não pronta")
            return
        }

        if (!connect()) {
            Log.e("EPSON", "Falha conexão")
            return
        }

        try {

            printer?.clearCommandBuffer()

            // 🔥 ESSA LINHA É O SEGREDO DO QR
            printer?.addCommand(data)

            Log.d("EPSON", "Enviando RAW...")

            printer?.sendData(Printer.PARAM_DEFAULT)

        } catch (e: Exception) {
            Log.e("EPSON", "Erro imprimir RAW", e)
            printer?.clearCommandBuffer()
        }
    }

    private fun discover(data: ByteArray) {
        try {
            isDiscovering = true

            val filter = FilterOption().apply {
                deviceType = Discovery.TYPE_PRINTER
                epsonFilter = Discovery.FILTER_NAME
                usbDeviceName = Discovery.TRUE
            }

            Discovery.start(context, filter) { deviceInfo ->

                Log.d("EPSON", "Encontrou: ${deviceInfo.deviceName}")

                printerTarget = deviceInfo.target

                initPrinter(deviceInfo.deviceName)

                Discovery.stop()
                isDiscovering = false

                printRaw(data)
            }

        } catch (e: Exception) {
            Log.e("EPSON", "Erro discovery", e)
        }
    }

    private fun initPrinter(name: String) {
        try {
            val model = getPrinterModel(name)

            printer = Printer(model, Printer.LANG_EN, context)
            printer?.setReceiveEventListener(this)

        } catch (e: Exception) {
            Log.e("EPSON", "Erro init", e)
        }
    }

    private fun getPrinterModel(name: String): Int {
        return when {
            name.contains("L100") -> Printer.TM_L100
            name.contains("T20") -> Printer.TM_T20
            else -> Printer.TM_T20
        }
    }

    private fun connect(): Boolean {
        return try {
            printer?.connect(printerTarget, Printer.PARAM_DEFAULT)
            printer?.beginTransaction()
            true
        } catch (e: Exception) {
            Log.e("EPSON", "Erro conectar", e)
            false
        }
    }

    override fun onPtrReceive(
        printer: Printer?,
        code: Int,
        status: PrinterStatusInfo?,
        printJobId: String?
    ) {
        Log.d("EPSON", "Impressão finalizada")

        try {
            printer?.endTransaction()
            printer?.disconnect()
        } catch (e: Exception) {
            Log.e("EPSON", "Erro disconnect", e)
        }
    }
}


















/*
import android.content.Context
import android.util.Log
import com.epson.epos2.discovery.*
import com.epson.epos2.printer.*

class EpsonPrinterSDK(
    private val context: Context
) : ReceiveListener {

    private var printer: Printer? = null
    private var printerTarget: String? = null
    private var isDiscovering = false

    fun print(product: Product) {

        if (printerTarget == null && !isDiscovering) {
            discover(product)
            return
        }

        if (printer == null || printerTarget == null) {
            Log.e("EPSON", "Impressora não pronta")
            return
        }

        if (!connect()) {
            Log.e("EPSON", "Falha conexão")
            return
        }

        try {

            printer?.clearCommandBuffer()

            printer?.addTextAlign(Printer.ALIGN_CENTER)
            printer?.addFeedLine(1)

            printer?.addText("PEDIDO\n")
            printer?.addText("------------------------------\n")

            printer?.addTextSize(2, 2)
            printer?.addText("${product.name}\n")
            printer?.addTextSize(1, 1)

            printer?.addText("------------------------------\n")

            printer?.addTextAlign(Printer.ALIGN_LEFT)

            printer?.addText("Porção: ${product.servingSize}\n")
            printer?.addText("Calorias: ${product.calories}\n")
            printer?.addText("Proteínas: ${product.protein}g\n")
            printer?.addText("Carboidratos: ${product.carbohydrates}g\n")
            printer?.addText("Gorduras: ${product.fats}g\n")

            printer?.addText("------------------------------\n")

            printer?.addTextAlign(Printer.ALIGN_CENTER)
            printer?.addTextSize(2, 2)
            printer?.addText("R$ %.2f\n".format(product.price))

            printer?.addTextSize(1, 1)

            printer?.addFeedLine(2)
            printer?.addText("Obrigado!\n")
            printer?.addFeedLine(3)

            printer?.addCut(Printer.CUT_FEED)

            Log.d("EPSON", "Enviando dados...")

            printer?.sendData(Printer.PARAM_DEFAULT)

        } catch (e: Exception) {
            Log.e("EPSON", "Erro imprimir", e)
            printer?.clearCommandBuffer()
        }
    }

    private fun discover(product: Product) {
        try {
            isDiscovering = true

            val filter = FilterOption().apply {
                deviceType = Discovery.TYPE_PRINTER
                epsonFilter = Discovery.FILTER_NAME
                usbDeviceName = Discovery.TRUE
            }

            Discovery.start(context, filter) { deviceInfo ->

                Log.d("EPSON", "Encontrou: ${deviceInfo.deviceName}")

                printerTarget = deviceInfo.target

                initPrinter(deviceInfo.deviceName)

                Discovery.stop()

                isDiscovering = false

                print(product)
            }

        } catch (e: Exception) {
            Log.e("EPSON", "Erro discovery", e)
        }
    }

    private fun initPrinter(name: String) {
        try {
            val model = getPrinterModel(name)

            printer = Printer(model, Printer.LANG_EN, context)
            printer?.setReceiveEventListener(this)

        } catch (e: Exception) {
            Log.e("EPSON", "Erro init", e)
        }
    }

    private fun getPrinterModel(name: String): Int {
        return when {
            name.contains("L100") -> Printer.TM_L100
            name.contains("T20") -> Printer.TM_T20
            else -> Printer.TM_T20
        }
    }

    private fun connect(): Boolean {
        return try {
            printer?.connect(printerTarget, Printer.PARAM_DEFAULT)
            printer?.beginTransaction()
            true
        } catch (e: Exception) {
            Log.e("EPSON", "Erro conectar", e)
            false
        }
    }

    override fun onPtrReceive(
        printer: Printer?,
        code: Int,
        status: PrinterStatusInfo?,
        printJobId: String?
    ) {
        Log.d("EPSON", "Impressão finalizada")

        try {
            printer?.endTransaction()
            printer?.disconnect()
        } catch (e: Exception) {
            Log.e("EPSON", "Erro disconnect", e)
        }
    }
}

*/