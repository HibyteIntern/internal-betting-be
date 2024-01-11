package ro.hibyte.betting.service

import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import org.json.JSONArray
import org.springframework.http.*


@Service
class WaspService {

    private val restTemplate = RestTemplate()

    fun sendPhotoToWasp(photo: MultipartFile): Long {

        val headers = HttpHeaders().apply {
            contentType = MediaType.MULTIPART_FORM_DATA
            set("X-API-KEY", "a4c1098d-9fda-4b27-b268-62a066f0cddd")
        }

        val fileResource: ByteArrayResource = object : ByteArrayResource(photo.bytes) {
            override fun getFilename(): String {
                return photo.originalFilename ?: "photo"
            }
        }
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap<String, Any>().apply {
            add("files", fileResource)
        }
        val requestEntity: HttpEntity<MultiValueMap<String, Any>> = HttpEntity(body, headers)
        val response = restTemplate.exchange(
            "https://wasp.live.hibyte.ro/api/files",
            HttpMethod.POST,
            requestEntity,
            String::class.java
        )
        val jsonObject = JSONArray(response.body)
        return jsonObject.getJSONObject(0).getLong("id")
    }


    fun getPhotoFromWasp(photoId: Long): ByteArray {

        val headers = HttpHeaders().apply {
            set("X-API-KEY", "a4c1098d-9fda-4b27-b268-62a066f0cddd")
            accept = listOf(MediaType.APPLICATION_OCTET_STREAM)
        }
        val requestEntity = HttpEntity<Any>(headers)
        val response = restTemplate.exchange(
            "https://wasp.live.hibyte.ro/api/files/$photoId",
            HttpMethod.GET,
            requestEntity,
            ByteArray::class.java
        )

        if (response.statusCode == HttpStatus.OK) {
            return response.body ?: throw RuntimeException("Body is null")
        } else {
            throw RuntimeException("Failed to get photo with status code: ${response.statusCode}")
        }
    }


}
