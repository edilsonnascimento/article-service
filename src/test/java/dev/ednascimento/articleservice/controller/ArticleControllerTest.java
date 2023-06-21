package dev.ednascimento.articleservice.controller;

import dev.ednascimento.articleservice.helper.IntegrationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ArticleControllerTest extends IntegrationHelper {

    private static final String URI_PATH = "/api/articles";
    private static final String URI_PATH_ID = "/api/articles/{id}";

    @Test
    void WHEN_to_GET_ALL_Articles_MUST_be_Listed_in_order_of_creation() throws Exception {
        // when
        mockMvc
                .perform(get(URI_PATH))
        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].id", containsInRelativeOrder(1)))
                .andExpect(jsonPath("$[*].title", containsInRelativeOrder("Hello word!")))
                .andExpect(jsonPath("$[*].body", containsInRelativeOrder("This is my fisrt blog post about SpringBoot.")));
    }

    @Test
    void WHEN_Get_Article_by_ID_MUST_return_an_Article() throws Exception {
        //given
        var id = 1;

        // when
        mockMvc
                .perform(get(URI_PATH, id))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].id", containsInRelativeOrder(1)))
                .andExpect(jsonPath("$[*].title", containsInRelativeOrder("Hello word!")))
                .andExpect(jsonPath("$[*].body", containsInRelativeOrder("This is my fisrt blog post about SpringBoot.")));
    }

    @Test
    void WHEN_Get_Article_by_INVALID_ID_MUST_be_returned_an_Error_Message() throws Exception {
        //given
        var id = Integer.valueOf("10");

        // when
        mockMvc
                .perform(get(URI_PATH_ID, id))
                // then
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.type", is("http://local:8081/problems")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Erro ao buscar endPoint")))
                .andExpect(jsonPath("$.categoria", is("User request")))
                .andExpect(jsonPath("$.timeStamp", notNullValue()));
    }
}