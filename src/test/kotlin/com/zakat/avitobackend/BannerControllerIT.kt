package com.zakat.avitobackend

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.zakat.avitobackend.config.TokenBuilder
import com.zakat.avitobackend.dto.CreateBannerRequest
import com.zakat.avitobackend.dto.PatchBannerRequest
import jakarta.transaction.Transactional
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.*


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class BannerControllerIT {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var tokenBuilder: TokenBuilder

    companion object {
        private val createBannerRequest = CreateBannerRequest(
            content = jacksonObjectMapper().readTree(
                """
                {
                "text": "Hello, world!"
                }
            """.trimIndent()
            ),
            isActive = true,
            featureId = 1,
            tagIds = listOf(1, 2),
        )

        private val patchBannerRequest = PatchBannerRequest(
            isActive = false,
            featureId = 2,
            tagIds = listOf(4, 3),
            content = null,
        )
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Create banner")
    fun createBanner_ReturnsBannerId() {
        mockMvc.post("/banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(createBannerRequest)
        }
            .andDo { print() }
            .andExpectAll {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    content {
                        json(
                            """
                            {
                                "banner_id": 3
                            }
                        """.trimIndent()
                        )
                    }
                }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Create banner by user, returns forbidden")
    fun createBannerByUser_ReturnsForbidden() {
        mockMvc.post("/banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.userToken())
            }
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(createBannerRequest)
        }
            .andDo { print() }
            .andExpectAll {
                status { isForbidden() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Delete banner")
    fun deleteBanner_ReturnsNoContent() {
        mockMvc.delete("/banner/1") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
        }
            .andDo { print() }
            .andExpectAll {
                status { isNoContent() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Delete banner that doesn't exist")
    fun deleteBanner_ReturnsNotFound() {
        mockMvc.delete("/banner/1124") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
        }
            .andDo { print() }
            .andExpectAll {
                status { isNotFound() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Delete banner by user")
    fun deleteBannerByUser_ReturnsForbidden() {
        mockMvc.delete("/banner/1") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.userToken())
            }
        }
            .andDo { print() }
            .andExpectAll {
                status { isForbidden() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Patch banner")
    fun patchBanner_ReturnsOk() {
        mockMvc.patch("/banner/1") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(patchBannerRequest)
        }
            .andDo { print() }
            .andExpectAll {
                status { isOk() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Patch banner that doesn't exist")
    fun patchBanner_ReturnsNotFound() {
        mockMvc.patch("/banner/1124") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(patchBannerRequest)
        }
            .andDo { print() }
            .andExpectAll {
                status { isNotFound() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Patch banner by user")
    fun patchBannerByUser_ReturnsForbidden() {
        mockMvc.patch("/banner/1") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.userToken())
            }
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(patchBannerRequest)
        }
            .andDo { print() }
            .andExpectAll {
                status { isForbidden() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get user banner")
    fun getUserBanners_ReturnsBannerContent() {
        mockMvc.get("/user_banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.userToken())
            }
            queryParam("feature_id", "1")
            queryParam("tag_id", "2")
        }
            .andDo { print() }
            .andExpectAll {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(
                        """
                            {
                              "name": "John",
                              "age": 30,
                              "city": "New York",
                              "hobbies": [
                                "reading",
                                "traveling"
                              ]
                            }
                        """.trimIndent()
                    )
                }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get user banner that doesn't exist")
    fun getUserBanner_ReturnsNotFound() {
        mockMvc.get("/user_banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.userToken())
            }
            queryParam("feature_id", "1124124")
            queryParam("tag_id", "21241241")
        }
            .andDo { print() }
            .andExpectAll {
                status { isNotFound() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get user banner without query params")
    fun getUserBannerWithoutQueryParams_ReturnsBadRequest() {
        mockMvc.get("/user_banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.userToken())
            }
        }
            .andDo { print() }
            .andExpectAll {
                status { isBadRequest() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get user banner without token")
    fun getUserBannerWithoutToken_ReturnsUnauthorized() {
        mockMvc.get("/user_banner")
            .andDo { print() }
            .andExpectAll {
                status { isUnauthorized() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get banners by user, returns 403")
    fun getBannersByUser_ReturnsForbidden() {
        mockMvc.get("/banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.userToken())
            }
        }
            .andDo { print() }
            .andExpectAll {
                status { isForbidden() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get banners by authorized user, returns 401")
    fun getBannersUnAuthorized_ReturnsUnauthorized() {
        mockMvc.get("/banner")
            .andDo { print() }
            .andExpectAll {
                status { isUnauthorized() }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get banners without filters")
    fun getBannersWithoutFilter_ReturnsBannersList() {
        mockMvc.get("/banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
        }
            .andDo { print() }
            .andExpectAll {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(
                        """
                        [
                            {
                                "banner_id": 1,
                                "tag_ids": [1, 2],
                                "feature_id": 1,
                                "content": {
                                  "name": "John",
                                  "age": 30,
                                  "city": "New York",
                                  "hobbies": [
                                    "reading",
                                    "traveling"
                                  ]
                                },
                                "is_active": true,
                                "created_at": "2020-08-07T23:30:20.000+00:00",
                                "updated_at": "2020-08-07T23:30:20.000+00:00"
                              },
                              {
                                "banner_id": 2,
                                "tag_ids": [1, 3, 4],
                                "feature_id": 2,
                                    "content": {
                                    "product": "Laptop",
                                    "brand": "Apple",
                                    "specs": {
                                        "RAM": "8GB",
                                        "storage": "256GB SSD"
                                    }
                                },
                                "is_active": false,
                                "created_at": "2020-08-07T23:30:20.000+00:00",
                                "updated_at": "2020-08-07T23:30:20.000+00:00"
                              }
                        ]
                    """.trimIndent()
                    )
                }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get banners with tag id")
    fun getBannersWithTagId_ReturnsBannersListWithTag() {
        mockMvc.get("/banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
            queryParam("tag_id", "2")
        }
            .andDo { print() }
            .andExpectAll {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(
                        """
                        [
                              {
                                "banner_id": 1,
                                "tag_ids": [1, 2],
                                "feature_id": 1,
                                "content": {
                                  "name": "John",
                                  "age": 30,
                                  "city": "New York",
                                  "hobbies": [
                                    "reading",
                                    "traveling"
                                  ]
                                },
                                "is_active": true,
                                "created_at": "2020-08-07T23:30:20.000+00:00",
                                "updated_at": "2020-08-07T23:30:20.000+00:00"
                              }
                        ]
                    """.trimIndent()
                    )
                }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get banners with feature id")
    fun getBannersWithFeature_ReturnsBannersListWithFeature() {
        mockMvc.get("/banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
            queryParam("feature_id", "2")
        }
            .andDo { print() }
            .andExpectAll {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(
                        """
                        [
                            {
                                "banner_id": 2,
                                "tag_ids": [1, 3, 4],
                                "feature_id": 2,
                                    "content": {
                                    "product": "Laptop",
                                    "brand": "Apple",
                                    "specs": {
                                        "RAM": "8GB",
                                        "storage": "256GB SSD"
                                    }
                                },
                                "is_active": false,
                                "created_at": "2020-08-07T23:30:20.000+00:00",
                                "updated_at": "2020-08-07T23:30:20.000+00:00"
                              }
                        ]
                    """.trimIndent()
                    )
                }
            }
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("Get banners with invalid limit and offset")
    fun getBannersWithInvalidLimitAndOffset_ReturnErrors() {
        mockMvc.get("/banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
            queryParam("limit", "-12")
        }
            .andDo { print() }
            .andExpectAll {
                status {
                    isBadRequest()
                    reason("Limit must be greater then 1")
                }
            }

        mockMvc.get("/banner") {
            headers {
                add("Authorization", "Bearer " + tokenBuilder.adminToken())
            }
            queryParam("offset", "-1")
        }
            .andDo { print() }
            .andExpectAll {
                status {
                    isBadRequest()
                    reason("Offset must be greater then 0")
                }
            }
    }

}
