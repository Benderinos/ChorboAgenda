/*
 * Copyright 2021 dev.id
 */
package plugins

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

apply<DetektPlugin>()

configure<DetektExtension> {
    toolVersion = "1.17.1"
    input = project.files("src/main/kotlin")
    config = files("$rootDir/.detekt/config.yml")
    buildUponDefaultConfig = true
    reports {
        xml {
            enabled = true
            destination = project.file("build/reports/detekt/report.xml")
        }
        html {
            enabled = true
            destination = project.file("build/reports/detekt/report.html")
        }
    }
}
