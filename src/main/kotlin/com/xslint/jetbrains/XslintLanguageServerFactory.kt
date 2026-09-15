/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Max Trunnikov
 * SPDX-License-Identifier: MIT
 */

package com.xslint.jetbrains

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.LanguageServerFactory
import com.redhat.devtools.lsp4ij.server.OSProcessStreamConnectionProvider
import com.redhat.devtools.lsp4ij.server.StreamConnectionProvider
import java.nio.file.Path

/**
 * Where the xslint-lsp server sits, relative to the plugin directory the
 * Gradle installXslintLsp task bundles it into, and where that directory
 * sits relative to the jar this plugin's own classes are loaded from.
 */
object XslintServer {
    private const val RELATIVE = "xslint-lsp/node_modules/xslint-lsp/src/server.js"

    fun script(pluginPath: Path): Path = pluginPath.resolve(RELATIVE)

    fun plugin(jar: Path): Path = jar.parent.parent
}

/**
 * Exposes the xslint language server to LSP4IJ.
 */
class XslintLanguageServerFactory : LanguageServerFactory {
    override fun createConnectionProvider(project: Project): StreamConnectionProvider =
        XslintLanguageServer()
}

/**
 * Launches `node <bundled>/src/server.js --stdio`, where the server is the
 * xslint-lsp package bundled into this plugin's own directory at build time.
 */
class XslintLanguageServer : OSProcessStreamConnectionProvider() {
    init {
        val server = serverScript()
        super.setCommandLine(
            GeneralCommandLine("node", server.toString(), "--stdio")
                .withWorkDirectory(server.parent.toString())
                .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE),
        )
    }

    private fun serverScript(): Path =
        XslintServer.script(
            XslintServer.plugin(
                Path.of(javaClass.protectionDomain.codeSource.location.toURI()),
            ),
        )
}
