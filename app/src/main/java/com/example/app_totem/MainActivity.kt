package com.example.app_totem

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var printer: EpsonPrinterSDK
    private lateinit var listView: ListView
    private lateinit var printButton: Button
    private lateinit var txtTitle: TextView
    private lateinit var txtDetails: TextView
    private lateinit var btnConfigPrinter: Button

    private var selectedProduct: Product? = null

    private val products = listOf(
        Product("Hambúrguer", 25.90, "150g", 350, 20, 30, 15),
        Product("Pizza", 32.50, "120g", 280, 12, 35, 10),
        Product("Refrigerante", 8.00, "200ml", 150, 0, 40, 0),
        Product("Salada", 18.90, "100g", 90, 3, 10, 2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.list_products)
        printButton = findViewById(R.id.print_button)
        txtTitle = findViewById(R.id.txt_title)
        txtDetails = findViewById(R.id.txt_details)
        btnConfigPrinter = findViewById(R.id.btn_config_printer)

        listView.adapter = ProductAdapter(this, products)

        listView.setOnItemClickListener { _, _, position, _ ->
            val product = products[position]
            selectedProduct = product

            txtTitle.text = product.name

            txtDetails.text = """
                💰 R$ %.2f
                
                Porção: ${product.servingSize}
                Calorias: ${product.calories}
                Proteínas: ${product.protein}g
                Carboidratos: ${product.carbohydrates}g
                Gorduras: ${product.fats}g
            """.trimIndent().format(product.price)
        }

        printer = EpsonPrinterSDK(this)


        btnConfigPrinter.setOnClickListener {
            startActivity(Intent(this, PrinterConfigActivity::class.java))
        }

        printButton.setOnClickListener {

            val product = selectedProduct

            if (product == null) {
                Toast.makeText(this, "Selecione um produto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            printer.print(product)

            Toast.makeText(this, "Enviando impressão...", Toast.LENGTH_SHORT).show()
        }
    }
}