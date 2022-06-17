package br.com.ecommerce.v1.user;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserRegisterControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ClientRepository repository;

    @BeforeEach
    public void initEach () {
        repository.deleteAll();
    }
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should return status 201 when all parameters are correct")
    public void shouldReturnStatus201WhenSuccess() throws Exception {

        ClientRequest clientRequest = new ClientRequest("teste", "44719190839", "teste@gmail.com", "teste");

        mvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isCreated());
        long count = repository.count();

        assertEquals(1, count);
    }

    @Test
    @DisplayName("Should return status 400 when one of parameters are in invalid format")
    public void shouldReturnStatus400WhenDataInvalid() throws Exception {
        ClientRequest clientRequest = new ClientRequest("teste", "4471919083", "teste@gmail.com", "teste");

        mvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isBadRequest());
    }
}
