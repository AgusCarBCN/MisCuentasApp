package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import kotlin.math.roundToInt


import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

class ParserCalculator(private val input: String = "") {

    private var pos = 0
    private var token: String? = null

    // Avanza al siguiente token
    private fun nextToken() {
        while (pos < input.length && input[pos].isWhitespace()) pos++

        if (pos == input.length) {
            token = null
            return
        }

        // Número (con punto decimal)
        if (input[pos].isDigit() || input[pos] == '.') {
            val sb = StringBuilder()
            while (pos < input.length && (input[pos].isDigit() || input[pos] == '.')) {
                sb.append(input[pos])
                pos++
            }
            token = sb.toString()
            return
        }

        // Operadores o paréntesis
        if ("+-×÷()%".contains(input[pos])) {
            token = input[pos].toString()
            pos++
            return
        }

        throw IllegalArgumentException("Carácter inválido: ${input[pos]}")
    }

    fun evaluate(expression: String): BigDecimal {
        val tokenizer = ParserCalculator(expression)
        tokenizer.nextToken()
        val result = parseExpression(tokenizer)
        // Redondeo a 10 decimales para precisión
        return result.setScale(10, RoundingMode.HALF_UP).stripTrailingZeros()
    }

    private fun parseExpression(tokenizer: ParserCalculator): BigDecimal {
        var result = parseTerm(tokenizer)

        while (tokenizer.token in listOf("+", "-")) {
            val op = tokenizer.token!!
            tokenizer.nextToken()
            val term = parseTerm(tokenizer)
            result = when (op) {
                "+" -> result.add(term)
                "-" -> result.subtract(term)
                else -> throw IllegalStateException("Operador inválido: $op")
            }
        }
        return result
    }

    private fun parseTerm(tokenizer: ParserCalculator): BigDecimal {
        var result = parseFactor(tokenizer)
        while (tokenizer.token in listOf("×", "÷")) {
            val op = tokenizer.token!!
            tokenizer.nextToken()
            val factor = parseFactor(tokenizer)
            result = when (op) {
                "×" -> result.multiply(factor)
                "÷" -> result.divide(factor, 10, RoundingMode.HALF_UP)
                else -> throw IllegalStateException("Operador inválido: $op")
            }
        }
        return result
    }

    private fun parseFactor(tokenizer: ParserCalculator): BigDecimal {
        if (tokenizer.token == null) throw IllegalArgumentException("Falta el factor")

        // Número
        tokenizer.token!!.toBigDecimalOrNull()?.let {
            tokenizer.nextToken()
            var value = it

            // Manejar porcentaje
            if (tokenizer.token == "%") {
                value = value.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)
                tokenizer.nextToken()
            }

            return value
        }

        // Operador unario
        if (tokenizer.token in listOf("+", "-")) {
            val op = tokenizer.token!!
            tokenizer.nextToken()
            val factor = parseFactor(tokenizer)
            return when (op) {
                "+" -> factor
                "-" -> factor.negate()
                else -> throw IllegalStateException("Operador inválido: $op")
            }
        }

        // Paréntesis
        if (tokenizer.token == "(") {
            tokenizer.nextToken()
            val value = parseExpression(tokenizer)
            if (tokenizer.token == ")") {
                tokenizer.nextToken()

                // Manejar porcentaje después de paréntesis
                if (tokenizer.token == "%") {
                    val result = value.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)
                    tokenizer.nextToken()
                    return result
                }

                return value
            } else {
                throw IllegalArgumentException("Falta el paréntesis de cierre")
            }
        }

        throw IllegalArgumentException("Factor inválido: ${tokenizer.token}")
    }
}


/*class ParserCalculator(private val input:String="") {


        // Almacena la posición del carácter actual
        private var pos = 0

        // Almacena el token actual
        private var token: String? = null

        // Avanza al siguiente token en la entrada (los tokens son números, operadores, paréntesis, etc.)
        private fun nextToken() {

            // Ignora los espacios en blanco
            while (pos < input.length && input[pos].isWhitespace()) {
                pos++
            }

            // Si llega al final de la entrada, establece el token en null
            if (pos == input.length) {
                token = null
                return
            }

            // Si el carácter actual es un dígito o un punto decimal, analiza un número
            if (input[pos].isDigit() || input[pos] == '.') {

                val sb = StringBuilder()
                while (pos < input.length && (input[pos].isDigit() || input[pos] == '.')) {
                    sb.append(input[pos])
                    pos++
                }
                token = sb.toString()
                return
            }

            // Si el carácter actual es un operador o paréntesis, establece el token como dicho carácter
            if ("+-×÷()".contains(input[pos])) {
                token = input[pos].toString()
                pos++
                return
            }

            // Lanza una excepción si se encuentra un carácter inválido
            throw IllegalArgumentException("Carácter inválido: ${input[pos]}")
        }


        fun evaluate(expression: String): Double {

            val tokenizer = ParserCalculator(expression)

            tokenizer.nextToken()

            val result = parseExpression(tokenizer)

            // Redondeamos el resultado a dos decimales usando Math.round
            return (result * 100).roundToInt() / 100.0
            //return parseExpression(tokenizer)
        }

        // Función para analizar la expresión y realizar las operaciones de suma y resta
        private fun parseExpression(tokenizer: ParserCalculator): Double {

            var result = parseTerm(tokenizer)

            while (tokenizer.token in listOf("+", "-")) {

                val op = tokenizer.token!!

                tokenizer.nextToken()

                val term = parseTerm(tokenizer)

                result = when (op) {
                    "+" -> result + term
                    "-" -> result - term
                    else -> throw IllegalStateException("Operador inválido: $op")
                }
            }

            return result
        }

        // Función para analizar el término y realizar las operaciones de multiplicación y división
        private fun parseTerm(tokenizer: ParserCalculator): Double {

            var result = parseFactor(tokenizer)
            while (tokenizer.token in listOf("×", "÷")) {

                val op = tokenizer.token!!
                tokenizer.nextToken()
                val factor = parseFactor(tokenizer)

                result = when (op) {
                    "×" -> result * factor
                    "÷" -> result / factor
                    else -> throw IllegalStateException("Operador inválido: $op")
                }
            }
            return result
        }

        // Obtiene el valor del factor, realiza operaciones unarias y maneja paréntesis
        private fun parseFactor(tokenizer: ParserCalculator): Double {

            if (tokenizer.token == null) {
                throw IllegalArgumentException("Falta el factor")
            }

            // Verifica si el token es un número
            if (tokenizer.token!!.toDoubleOrNull() != null) {

                val value = tokenizer.token!!.toDouble()

                tokenizer.nextToken()

                return value
            }

            // Verifica si el token es un operador unario
            if (tokenizer.token in listOf("+", "-")) {

                val op = tokenizer.token!!

                tokenizer.nextToken()

                val factor = parseFactor(tokenizer)

                return when (op) {
                    "+" -> +factor
                    "-" -> -factor
                    else -> throw IllegalStateException("Operador inválido: $op")
                }
            }

            // Maneja los paréntesis
            if (tokenizer.token == "(" && tokenizer.input.indexOf(
                    ")",
                    tokenizer.pos
                ) != -1
            ) { // Condición añadida

                tokenizer.nextToken()

                // Analiza la expresión dentro del paréntesis, como (2-6+4) etc.
                val value = parseExpression(tokenizer)

                if (tokenizer.token == ")") {

                    tokenizer.nextToken()

                    return value
                } else {
                    throw IllegalArgumentException("Falta el paréntesis de cierre")
                }
            } else {
                throw IllegalArgumentException("Factor inválido: ${tokenizer.token}")
            }
        }
    }

*/



