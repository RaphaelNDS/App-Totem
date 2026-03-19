package com.example.app_totem

object SenhaGenerator {

    private val controle = mutableMapOf<String, Int>()

    fun gerarSenha(esp: Especialidade): String {

        val atual = controle.getOrDefault(esp.prefixo, 0) + 1
        controle[esp.prefixo] = atual

        return "${esp.prefixo}${atual.toString().padStart(3, '0')}"
    }
}