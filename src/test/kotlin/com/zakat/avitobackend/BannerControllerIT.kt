package com.zakat.avitobackend

import com.zakat.avitobackend.config.TokenBuilder
import jakarta.transaction.Transactional
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class BannerControllerIT {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var tokenBuilder: TokenBuilder

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
