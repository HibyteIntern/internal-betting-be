package ro.hibyte.betting.demodata

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import ro.hibyte.betting.dto.UserProfileDTO


fun main() {

    val yamlReader = YamlReader()
    yamlReader.loadYamlAsList<UserProfileDTO>("users.yml").forEach { println(it) }
}

class YamlReader {
    inline fun <reified T> loadYamlAsList(filePath: String): List<T> {
        YamlReader::class.java.classLoader.getResourceAsStream("data/$filePath").use { inputStream ->
            val mapper = ObjectMapper(YAMLFactory())
            return mapper.readValue(inputStream, Array<T>::class.java).toList()
        }
    }
}
