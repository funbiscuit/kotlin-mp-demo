import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.int

expect fun getPlatformName(): String


class Hello : CliktCommand() {
    private val count: Int by option().int().default(1).help("Number of greetings")
    private val name: String by option().prompt("Your first and last name").help("The person to greet")

    override fun run() {
        val result = with(name.split(' ').filter { it.isNotBlank() }) {
            runCatching {
                require(size == 2) { "Name must have two words" }
                "${this[0]} ${this[1]}"
            }
        }

        result
            .onSuccess { value ->
                repeat(count) {
                    echo("Hello $value!")
                }
            }
            .onFailure { println("Failed to parse input: ${it.message}") }
    }
}

fun main(args: Array<String>) {
    println("You're running on ${getPlatformName()}")
    Hello().main(args)
}
