/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Max Trunnikov
 * SPDX-License-Identifier: MIT
 */

package com.xslint.jetbrains

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.nio.file.Path

class XslintServerTest {
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
}
