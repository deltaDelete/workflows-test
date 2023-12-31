#!/usr/bin/env kotlin

@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.8.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV4
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Build artifact",
    on = listOf(Push()),
    sourceFile = __FILE__.toPath(),
) {
    job(id = "build_job", runsOn = UbuntuLatest) {
        uses(name = "Check out", action = CheckoutV4())
        run(name = "Print greeting", command = "echo 'Hello world!'")
        uses(
            name = "Setup Java", 
            action = SetupJavaV4(
                javaVersion = "17",
                distribution = SetupJavaV4.Distribution.Temurin
            )
        )
        run(name = "Sync Gradle", command = "./gradlew tasks")
        run(name = "Build jar", command = "./gradlew clean build")
    }
}.writeToFile()