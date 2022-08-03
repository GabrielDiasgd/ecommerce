package br.com.ecommerce.common;

import br.com.ecommerce.v1.client.ClientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ApiExceptionHandlerTest {

    @Autowired
    private  MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Should return status 400 when one of parameters are in invalid format")
    public void shouldReturnBadRequestWhenCatchMethodArgumentNotValidException() throws Exception {

        ClientRequest clientRequest = new ClientRequest("", "44719190839", "teste@gmail.com", "teste");

        MvcResult result = mvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientRequest)))
                .andExpect(status().isBadRequest()).andReturn();



    }
}
