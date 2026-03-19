package com.example.app_totem

import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

object EpsonCommandGenerator {

    fun generateSenha(
        especialidade: Especialidade,
        senha: String
    ): ByteArray {

        val output = ByteArrayOutputStream()

        val dataHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
            .format(Date())

        output.write(byteArrayOf(0x1B, 0x40))

        output.write(byteArrayOf(0x1C, 0x26))
        output.write(byteArrayOf(0x1B, 0x74, 0x00))

        output.write(byteArrayOf(0x1B, 0x61, 0x01))

        output.write(byteArrayOf(0x1D, 0x21, 0x11))
        writeLine(output, "HOSPITAL")
        output.write(byteArrayOf(0x1D, 0x21, 0x00))

        writeLine(output, "------------------------------")

        writeLine(output, especialidade.nome.uppercase())

        writeLine(output, "")

        output.write(byteArrayOf(0x1D, 0x21, 0x33))
        writeLine(output, senha)
        output.write(byteArrayOf(0x1D, 0x21, 0x00))

        writeLine(output, "")

        writeLine(output, dataHora)

        writeLine(output, "------------------------------")

        addQrCode(output, "Senha:$senha - ${especialidade.nome}")

        writeLine(output, "")
        writeLine(output, "Aguarde ser chamado")
        writeLine(output, "")

        output.write(byteArrayOf(0x1D, 0x56, 0x41, 0x10))

        return output.toByteArray()
    }

    private fun addQrCode(output: ByteArrayOutputStream, data: String) {

        val bytes = data.toByteArray(Charsets.UTF_8)

        output.write(byteArrayOf(0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, 0x05))

        output.write(byteArrayOf(0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x30))

        val size = bytes.size + 3

        output.write(byteArrayOf(
            0x1D, 0x28, 0x6B,
            (size and 0xFF).toByte(),
            ((size shr 8) and 0xFF).toByte(),
            0x31, 0x50, 0x30
        ))

        output.write(bytes)

        output.write(byteArrayOf(0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30))
    }

    private fun writeLine(output: ByteArrayOutputStream, text: String) {
        output.write((text + "\n").toByteArray(Charsets.UTF_8))
    }
}













/*
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

object EpsonCommandGenerator {

    fun generate(product: Especialidade, logo: Bitmap?): ByteArray {

        val output = ByteArrayOutputStream()

        output.write(byteArrayOf(0x1B, 0x40))

        output.write(byteArrayOf(0x1C, 0x26))

        output.write(byteArrayOf(0x1B, 0x74, 0x00))

        output.write(byteArrayOf(0x1B, 0x61, 0x01))

        output.write(byteArrayOf(0x1D, 0x21, 0x11))
        writeLine(output, "PEDIDO")
        output.write(byteArrayOf(0x1D, 0x21, 0x00))

        writeLine(output, "------------------------------")

        output.write(byteArrayOf(0x1B, 0x61, 0x01))
        writeLine(output, product.name.uppercase())

        writeLine(output, "------------------------------")

        output.write(byteArrayOf(0x1B, 0x61, 0x00))

        writeLine(output, "Porcao: ${product.servingSize}")
        writeLine(output, "Calorias: ${product.calories}")
        writeLine(output, "Proteinas: ${product.protein}g")
        writeLine(output, "Carboidratos: ${product.carbohydrates}g")
        writeLine(output, "Gorduras: ${product.fats}g")

        writeLine(output, "------------------------------")

        output.write(byteArrayOf(0x1B, 0x61, 0x01))
        output.write(byteArrayOf(0x1D, 0x21, 0x22))
        writeLine(output, "R$ %.2f".format(product.price))

        output.write(byteArrayOf(0x1D, 0x21, 0x00))

        writeLine(output, "")
        writeLine(output, "Acesse nosso site:")

        output.write(byteArrayOf(0x1B, 0x61, 0x01))

        addQrCode(output, "https://bkpbr.com.br/")

        writeLine(output, "")
        writeLine(output, "Obrigado pela preferencia!")

        writeLine(output, "")
        writeLine(output, "")

        output.write(byteArrayOf(0x1D, 0x56, 0x41, 0x10))

        return output.toByteArray()
    }

    private fun addQrCode(output: ByteArrayOutputStream, data: String) {

        val bytes = data.toByteArray(Charsets.UTF_8)

        output.write(byteArrayOf(
            0x1D, 0x28, 0x6B,
            0x03, 0x00,
            0x31, 0x43, 0x05
        ))

        output.write(byteArrayOf(
            0x1D, 0x28, 0x6B,
            0x03, 0x00,
            0x31, 0x45, 0x30
        ))

        val size = bytes.size + 3

        output.write(byteArrayOf(
            0x1D, 0x28, 0x6B,
            (size and 0xFF).toByte(),
            ((size shr 8) and 0xFF).toByte(),
            0x31, 0x50, 0x30
        ))

        output.write(bytes)

        output.write(byteArrayOf(
            0x1D, 0x28, 0x6B,
            0x03, 0x00,
            0x31, 0x51, 0x30
        ))
    }

    private fun writeLine(output: ByteArrayOutputStream, text: String) {
        output.write((text + "\n").toByteArray(Charsets.UTF_8))
    }
}


 */