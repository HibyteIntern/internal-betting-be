package ro.hibyte.betting


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["ro.hibyte.betting.repository"])
@EntityScan(basePackages = ["ro.hibyte.betting.entity"])
class BettingApplication

fun main(args: Array<String>) {
	runApplication<BettingApplication>(*args)
}

