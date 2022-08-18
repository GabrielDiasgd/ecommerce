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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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
        CategoryRequest categoryRequest = new CategoryRequest("Test Category");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://localhost/categories/*"));

        assertEquals(1, categoryRepository.findAll().size());
    }

    @Test
    @DisplayName("Shouldn't register a category when parameters are invalid")
    void test2() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest("");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryRequest));

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

    @Test
    @DisplayName("Shouldn't update a category when category doesn't exist")
    void test3() throws Exception {
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest("Test Update Category");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/categories/{id}", Integer.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryUpdateRequest));

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Category not found", ((ResponseStatusException) resolvedException).getReason());
    }


    @Test
    @DisplayName("Should update a category when all parameters are valid")
    void test4() throws Exception {
        Category category = new Category("Teste");
        categoryRepository.save(category);

        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest("Test Update Category");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/categories/{id}", category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryUpdateRequest));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        Optional<Category> updatedCategory = categoryRepository.findById(category.getId());

        assertTrue(updatedCategory.isPresent());
        assertEquals("Test Update Category", updatedCategory.get().getName());
    }

    @Test
    @DisplayName("Shouldn't update a category when parameters are invalid")
    void test5() throws Exception {
        Category category = new Category("Teste");
        categoryRepository.save(category);

        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(null);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/categories/{id}", category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryUpdateRequest));

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