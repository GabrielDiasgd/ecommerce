package br.com.ecommerce.v1.client;

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

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientRegisterControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ClientRepository repository;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void initEach () {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should return status 201 when all parameters are correct")
    public void shouldReturnStatus201WhenSuccess() throws Exception {

        ClientRequest clientRequest = new ClientRequest("teste", "44719190839", "teste@gmail.com", "teste");

        mvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientRequest)))
                .andExpect(status().isCreated());
        long count = repository.count();

        assertEquals(1, count);
    }

    @Test
    @DisplayName("Should return status 400 when parameters are in invalid format")
    public void shouldReturnStatus400WhenDataInvalid() throws Exception {
        ClientRequest clientRequest = new ClientRequest("", null, "", null);

        String jsonResponse = mvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientRequest)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorResponse errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        List<String> message = errorResponse.getFields().stream()
                .map(Field::getMessage)
                .collect(Collectors.toList());

        assertThat(message, containsInAnyOrder(
                "The field password must not be blank",
                "The field cpf must not be blank",
                "The field name must not be blank",
                "The field email must not be blank"
        ));
    }

    @Test
    @DisplayName("Should return status 400 when cpf format is invalid")
    public void shouldReturnStatus400WhenCPFInvalid() throws Exception {
        ClientRequest clientRequest = new ClientRequest("teste", "44719190830", "teste@gmail.com", "teste");

        String jsonResponse = mvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientRequest)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorResponse errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        List<String> message = errorResponse.getFields().stream()
                .map(Field::getMessage)
                .collect(Collectors.toList());

        assertThat(message, containsInAnyOrder(
                "The field cpf invalid Brazilian individual taxpayer registry number (CPF)"
        ));
    }

    @Test
    @DisplayName("Should return status 400 when email format is invalid")
    public void shouldReturnStatus400WhenEmailIsInvalid() throws Exception {
        ClientRequest clientRequest = new ClientRequest("teste", "44719190839", "testegmail.com", "teste");

        String jsonResponse = mvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientRequest)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorResponse errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        List<String> message = errorResponse.getFields().stream()
                .map(Field::getMessage)
                .collect(Collectors.toList());

        assertThat(message, containsInAnyOrder(
                "The field email must be a well-formed email address"
        ));
    }
}
