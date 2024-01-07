package ro.hibyte.betting.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ro.hibyte.betting.service.PrizeDrawService

@RestController
@RequestMapping("api/v1/prize-draws")
@CrossOrigin(origins = ["http://localhost:4200"])
class PrizeDrawController(private val prizeDrawService: PrizeDrawService) {
}
