package br.com.ecommerce.v1.address;

import br.com.ecommerce.v1.client.Client;
import br.com.ecommerce.v1.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> addressRegistration(@PathVariable Long idUser, @Valid @RequestBody AddressRequest request, UriComponentsBuilder uri) {
        Client client = clientRepository.findById(idUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        Address address = request.toModel(client);
        client.getAddresses().add(address);

        clientRepository.save(client);

        URI location = uri.path("clients/{idUser}/addresses/{idAddress}").buildAndExpand(client.getId(), address.getId()).toUri();

        return ResponseEntity.created(location).body(new AddressResponse(address));
    }

    @GetMapping
    public ResponseEntity<?> addressList(@PathVariable Long idUser) {
        Client client = clientRepository.findById(idUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        List<AddressResponse> addressResponseList = client.getAddresses().stream()
                .map(address -> new AddressResponse(address))
                .collect(Collectors.toList());

        return ResponseEntity.ok(addressResponseList);
    }

    @DeleteMapping("/{idAddress}")
    public void removeAddress(@PathVariable Long idUser, @PathVariable Long idAddress){
        Client client = clientRepository.findById(idUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        Address address = repository.findById(idAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        if (!address.belongs(client)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "This address doesn't belong to this client");
        }

        repository.deleteById(address.getId());
    }
}
