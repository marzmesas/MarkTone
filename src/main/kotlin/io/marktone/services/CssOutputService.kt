package io.marktone.services

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.components.Service
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service(Service.Level.APP)
class CssOutputService {

    fun outputPath(): Path {
        return Path.of(PathManager.getConfigPath(), "marktone", "generated", "marktone.css")
    }

    fun write(css: String): Path {
        val target = outputPath()
        Files.createDirectories(target.parent)

        val tempFile = Files.createTempFile(target.parent, "marktone-", ".tmp")
        Files.writeString(tempFile, css, StandardCharsets.UTF_8)
        Files.move(tempFile, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)

        return target
    }
}
