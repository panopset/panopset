package com.panopset.beam

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BeamApplication

fun main(args: Array<String>) {
	runApplication<BeamApplication>(*args)
}
