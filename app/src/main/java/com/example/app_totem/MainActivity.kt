package com.example.app_totem

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.totem_hospital.R

class MainActivity : AppCompatActivity() {

    private lateinit var grid: GridView
    private lateinit var btnConfig: Button
    private lateinit var printer: EpsonPrinterSDK

    private val especialidades = listOf(
        Especialidade("Clínico Geral", "CG"),
        Especialidade("Ortopedia", "O"),
        Especialidade("Ortopedia Pediatra", "OP"),
        Especialidade("Pediatria", "P"),
        Especialidade("Cardiologia", "C"),
        Especialidade("Dermatologia", "D"),
        Especialidade("Ginecologia", "G"),
        Especialidade("Neurologia", "N"),
        Especialidade("Oftalmologia", "OF"),
        Especialidade("Urologia", "U")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grid = findViewById(R.id.grid_especialidades)
        btnConfig = findViewById(R.id.btn_config_printer)

        printer = EpsonPrinterSDK(this)

        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density

        val numColumns = when {
            screenWidthDp > 1000 -> 4
            screenWidthDp > 700 -> 3
            else -> 2
        }

        grid.numColumns = numColumns

        grid.adapter = EspecialidadeAdapter(this, especialidades)

        grid.setOnItemClickListener { _, _, position, _ ->

            val esp = especialidades[position]

            val senha = SenhaGenerator.gerarSenha(esp)

            val data = EpsonCommandGenerator.generateSenha(esp, senha)

            printer.printRaw(data)

            Toast.makeText(this, "Senha $senha gerada", Toast.LENGTH_SHORT).show()
        }

        btnConfig.setOnClickListener {
            startActivity(Intent(this, PrinterConfigActivity::class.java))
        }
    }
}