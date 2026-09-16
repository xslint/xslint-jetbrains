/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Max Trunnikov
 * SPDX-License-Identifier: MIT
 */

package com.xslint.jetbrains

import com.intellij.util.lang.UrlClassLoader
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

class XslintServerTest {
    @get:Rule
    val folder = TemporaryFolder()

    @Test
    fun resolvesServerScriptUnderThePluginDirectory() {
        assertThat(
            "server script does not resolve under the plugin directory",
            XslintServer.script(Path.of("/plugins/xslint-jetbrains")),
            equalTo(
                Path.of(
                    "/plugins/xslint-jetbrains/xslint-lsp/node_modules/xslint-lsp/src/server.js",
                ),
            ),
        )
    }

    @Test
    fun findsThePluginDirectoryAboveItsOwnJar() {
        assertThat(
            "plugin directory does not resolve above the jar holding it",
            XslintServer.plugin(
                Path.of("/opt/ides/261/plugins/xslint-jetbrains/lib/xslint-jetbrains-0.0.7.jar"),
            ),
            equalTo(Path.of("/opt/ides/261/plugins/xslint-jetbrains")),
        )
    }

    @Test
    fun findsTheJarItsOwnClassIsLoadedFrom() {
        val jar = folder.newFolder("plugins", "xslint-jetbrains", "lib")
            .toPath()
            .resolve("xslint-jetbrains-0.0.7.jar")
        val entry = "com/xslint/jetbrains/XslintServer.class"
        JarOutputStream(Files.newOutputStream(jar)).use { out ->
            out.putNextEntry(JarEntry(entry))
            out.write(checkNotNull(XslintServer.javaClass.classLoader.getResourceAsStream(entry)).readBytes())
            out.closeEntry()
        }
        assertThat(
            "jar of a class loaded by the platform loader does not resolve",
            XslintServer.jar(
                UrlClassLoader.build()
                    .files(listOf(jar))
                    .parent(ClassLoader.getPlatformClassLoader())
                    .get()
                    .loadClass(XslintServer.javaClass.name),
            ),
            equalTo(jar),
        )
    }

    @Test
    fun refusesAClassTheRuntimeImageHolds() {
        assertThat(
            "a class standing behind no jar is refused without being named",
            assertThrows(IllegalStateException::class.java) {
                XslintServer.jar(String::class.java)
            }.message,
            equalTo("cannot locate the jar holding java.lang.String"),
        )
    }
}
