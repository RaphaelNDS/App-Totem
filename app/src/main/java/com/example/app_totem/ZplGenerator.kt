package com.example.app_totem

object ZplGenerator {

    fun generate(product: Product): String {
        return """
            ^XA
            ^CI28
            
            ^FO50,50^A0N,50,50^FD${product.name}^FS
            
            ^FO50,150^A0N,30,30^FDPorção: ${product.servingSize}^FS
            ^FO50,200^FDCalorias: ${product.calories}^FS
            ^FO50,250^FDProteínas: ${product.protein}g^FS
            ^FO50,300^FDCarboidratos: ${product.carbohydrates}g^FS
            ^FO50,350^FDGorduras: ${product.fats}g^FS

            ^FO50,420^A0N,25,25^FDVisite nosso site:^FS

            ^FO50,460
            ^BQN,2,6
            ^FDLA,https://bkpbr.com.br/^FS

            ^XZ
        """.trimIndent()
    }
}