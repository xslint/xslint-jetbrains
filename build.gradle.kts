// SPDX-FileCopyrightText: Copyright (c) 2026 Max Trunnikov
// SPDX-License-Identifier: MIT

import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("org.jetbrains.intellij.platform") version "2.18.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jetbrains.kotlinx.kover") version "0.9.9"
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity(providers.gradleProperty("platformVersion"))
        plugin(providers.gradleProperty("platformPlugins"))
        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }
    testImplementation("junit:junit:4.13.2")
}

intellijPlatform {
    pluginConfiguration {
        version = providers.gradleProperty("pluginVersion")
        description = "XSL/XSLT linting for JetBrains IDEs, powered by the " +
            "xslint language server."
        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = provider { null }
        }
    }
    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }
    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }
    pluginVerification {
        ides {
            recommended()
        }
    }
}

kotlin {
    jvmToolchain(21)
}

detekt {
    config.setFrom(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}

// Fetch the xslint-lsp Node server and bundle it into the plugin distribution,
// so the runtime finds it at <pluginPath>/xslint-lsp/node_modules/xslint-lsp.
val xslintLspDir = layout.buildDirectory.dir("xslint-lsp")
val npm = if (System.getProperty("os.name").startsWith("Windows", true)) "npm.cmd" else "npm"

val installXslintLsp = tasks.register<Exec>("installXslintLsp") {
    val out = xslintLspDir.get().asFile
    val server = providers.gradleProperty("xslintLspVersion").get()
    outputs.dir(out)
    doFirst { out.mkdirs() }
    commandLine(
        npm, "install", "--no-audit", "--no-fund",
        "--prefix", out.absolutePath, "xslint-lsp@$server",
    )
}

tasks {
    prepareSandbox {
        dependsOn(installXslintLsp)
        from(xslintLspDir) {
            into(pluginName.map { "$it/xslint-lsp" })
        }
    }
}
