package br.com.ecommerce.v1.category;

import br.com.ecommerce.common.exceptionHandler.ErrorResponse;
import br.com.ecommerce.common.exceptionHandler.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Should register a category when all parameters are valid")
    void test1() throws Exception {
        //cenario
        CategoryRequest categoryRequest = new CategoryRequest("Test Category");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryRequest));

        //acao validacao

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://localhost/categories/*"));

        assertEquals(1, categoryRepository.findAll().size());
    }

    @Test
    @DisplayName("Shouldn't register a category when parameters are invalid")
    void test2() throws Exception {
        //cenario
        CategoryRequest categoryRequest = new CategoryRequest("");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryRequest));

        //acao validacao

        String jsonResponse = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorResponse errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        List<String> messages = errorResponse.getFields().stream()
                .map(Field::getMessage)
                .collect(Collectors.toList());

        assertThat(messages)
                .hasSize(1)
                .contains("The field name must not be blank");
    }

}