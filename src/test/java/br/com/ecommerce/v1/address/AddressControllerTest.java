package br.com.ecommerce.v1.address;

import br.com.ecommerce.common.exceptionHandler.ErrorResponse;
import br.com.ecommerce.v1.client.Client;
import br.com.ecommerce.v1.client.ClientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.nio.charset.StandardCharsets;
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
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AddressRepository addressRepository;
    private Client client;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        this.client = new Client("Teste", "64407832037", "teste@gmail.com", "dasdadasd");
        clientRepository.save(client);
    }

    @Test
    @DisplayName("Shouldn't register a address when parameters are invalid")
    void test() throws  Exception {
        //cenario
        AddressRequest addressRequest = new AddressRequest(
                "",
                "  ",
                null,
                "" ,
                "",
                "",
                null);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/clients/{idUser}/addresses", client.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addressRequest));

        //acao validacao

        String jsonResponse = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorResponse errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        List<String> response = errorResponse.getFields().stream()
                .map(field -> field.getMessage())
                .collect(Collectors.toList());

        assertThat(response)
                .hasSize(6)
                .contains("The field cep must not be blank",
                        "The field neighborhood must not be blank",
                        "The field city must not be blank",
                        "The field street must not be blank",
                        "The field state must not be blank",
                        "The field number must not be blank");
    }


    @Test
    @DisplayName("Shouldn't register a address when client doesn't exist")
    void test1() throws Exception {
        //cenario

        AddressRequest addressRequest = new AddressRequest(
                "Rua teste",
                "Bairro teeste",
                "140",
                "" ,
                "19815300",
                "tete",
                "Teste");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/clients/{idUser}/addresses", Integer.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addressRequest));

        //acao validacao

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Client not found", ((ResponseStatusException)resolvedException).getReason());
    }

    @Test
    @DisplayName("Should register a address when all parameters and client are valid")
    void test2() throws Exception {
        //cenario

        AddressRequest addressRequest = new AddressRequest(
                "Rua teste",
                "Bairro teeste",
                "140",
                "" ,
                "19815300",
                "tete",
                "Teste");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/clients/{idUser}/addresses", client.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addressRequest));

        //acao validacao

        String location = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://localhost/clients/*/addresses/*"))
                .andReturn().getResponse().getHeader("location");

        Long possibleIdAddress = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

        Optional<Address> address = addressRepository.findById(possibleIdAddress);

        assertTrue(address.isPresent(), "Should exist a address with this id");
        assertEquals(client.getId(), address.get().getClient().getId() );
    }

    @Test
    @DisplayName("Shouldn't get a list of addresses if the customer doesn't exist")
    void test3() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/clients/{idUser}/addresses", Integer.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Client not found", ((ResponseStatusException)resolvedException).getReason());

    }
    @Test
    @DisplayName("Should get a list of addresses from a customer")
    void test4() throws Exception {
        Address firstAddress = new Address(
                "Street test",
                "Neighborhood test",
                "140",
                "",
                "19815300",
                "test",
                "Test",
                client);

        Address secondAdress = new Address(
                "Street test",
                "Neighborhood test",
                "140",
                "",
                "19815300",
                "test",
                "Test",
                client);

        addressRepository.saveAll(List.of(firstAddress, secondAdress));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/clients/{idUser}/addresses", client.getId())
                .contentType(MediaType.APPLICATION_JSON);

        String jsonResponse = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        List<AddressResponse> addressResponses = mapper.readValue(jsonResponse, new TypeReference<List<AddressResponse>>() {});

        assertTrue(!addressResponses.isEmpty());
        assertEquals(2, addressResponses.size() );
    }

    @Test
    @DisplayName("Shouldn't delete a address when client doesn't exist")
    void test5() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/clients/{idUser}/addresses/{idAddress}", Integer.MAX_VALUE, Integer.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Client not found", ((ResponseStatusException)resolvedException).getReason());
    }

    @Test
    @DisplayName("Shouldn't delete a address when address doesn't exist")
    void test6() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/clients/{idUser}/addresses/{idAddress}", client.getId(), Integer.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Address not found", ((ResponseStatusException)resolvedException).getReason());
    }

    @Test
    @DisplayName("Shouldn't delete a address when address doesn't belong to the customer")
    void test7() throws Exception {
        Client clientWithoutAddress = new Client("Another Client", "64407832037", "client@gmail.com", "dasdadasda");
        clientRepository.save(clientWithoutAddress);

        Address address = new Address(
                "Street test",
                "Neighborhood test",
                "140",
                "",
                "19815300",
                "test",
                "Test",
                client);

        addressRepository.save(address);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/clients/{idUser}/addresses/{idAddress}", clientWithoutAddress.getId(), address.getId())
                .contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("This address doesn't belong to this client", ((ResponseStatusException)resolvedException).getReason());
    }

    @Test
    @DisplayName("Should delete a address")
    void test8() throws Exception {
              Address address = new Address(
                "Street test",
                "Neighborhood test",
                "140",
                "",
                "19815300",
                "test",
                "Test",
                client);

        addressRepository.save(address);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/clients/{idUser}/addresses/{idAddress}", client.getId(), address.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        List<Address> byClientId = addressRepository.findByClientId(client.getId());

        assertTrue(byClientId.isEmpty());
        assertTrue(!addressRepository.existsById(address.getId()));
    }
}