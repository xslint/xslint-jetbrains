/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Max Trunnikov
 * SPDX-License-Identifier: MIT
 */

package com.xslint.jetbrains

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.LanguageServerFactory
import com.redhat.devtools.lsp4ij.server.OSProcessStreamConnectionProvider
import com.redhat.devtools.lsp4ij.server.StreamConnectionProvider
import java.nio.file.Path

private const val PLUGIN_ID = "com.xslint.jetbrains"

/**
 * Where the xslint-lsp server sits inside the plugin, relative to its own
 * installation directory. Bundled there by the Gradle installXslintLsp task.
 */
object XslintServer {
    private const val RELATIVE = "xslint-lsp/node_modules/xslint-lsp/src/server.js"

    fun script(pluginPath: Path): Path = pluginPath.resolve(RELATIVE)
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

    private fun serverScript(): Path {
        val plugin = PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID))
            ?: error("cannot locate the $PLUGIN_ID plugin installation directory")
        return XslintServer.script(plugin.pluginPath)
    }
}
