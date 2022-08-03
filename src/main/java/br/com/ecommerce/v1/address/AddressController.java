package br.com.ecommerce.v1.address;

import br.com.ecommerce.v1.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("clients/{idUser}/addresses")
public class AddressController {

    @Autowired
    private final AddressRepository repository;

    @Autowired
    private final ClientRepository clientRepository;

    public AddressController(AddressRepository repository, ClientRepository clientRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addressRegistration(@PathVariable Long idUser, @Valid @RequestBody AddressRequest request) {
        clientRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        Address address = request.toModel();

        repository.save(address);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AddressResponse(address));


    }
}
