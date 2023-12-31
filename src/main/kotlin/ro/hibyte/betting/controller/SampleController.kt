package ro.hibyte.betting.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/sample")
class SampleController {

    @GetMapping(produces = ["text/plain"])
    fun getSample(): String = "Hello World!"
}