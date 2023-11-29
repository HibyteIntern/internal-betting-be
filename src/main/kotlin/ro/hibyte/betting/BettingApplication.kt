package ro.hibyte.betting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BettingApplication

fun main(args: Array<String>) {
	runApplication<BettingApplication>(*args)
}
