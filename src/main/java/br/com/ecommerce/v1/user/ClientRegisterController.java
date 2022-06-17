package br.com.ecommerce.v1.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/clients")
public class ClientRegisterController {

    @Autowired
    private final ClientRepository clientRepository;

    public ClientRegisterController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> register (@Valid @RequestBody ClientRequest clientRequest) {
        Client newClient = clientRequest.toModel();

        clientRepository.save(newClient);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
