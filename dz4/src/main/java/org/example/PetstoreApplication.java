package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
public class PetstoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetstoreApplication.class, args);
    }
}

class Pet {
    private Long id;
    private String name;
    private Category category;
    private List<Tag> tags;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

class Category {
    private Long id;
    private String name;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

class Tag {
    private Long id;
    private String name;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

@Repository
class PetRepository {
    private final Map<Long, Pet> petStore = new HashMap<>();

    public Optional<Pet> findById(Long id) { return Optional.ofNullable(petStore.get(id)); }
    public void save(Pet pet) { petStore.put(pet.getId(), pet); }
    public void deleteById(Long id) { petStore.remove(id); }
    public Collection<Pet> findAll() { return petStore.values(); }
    public void deleteAll() { petStore.clear(); }
}

@Service
class PetService {
    private final PetRepository repository;
    public PetService(PetRepository repository) { this.repository = repository; }

    public Pet addPet(Pet pet) { repository.save(pet); return pet; }
    public Pet updatePet(Pet pet) { repository.save(pet); return pet; }
    public Optional<Pet> getPetById(Long id) { return repository.findById(id); }
    public void deletePetById(Long id) { repository.deleteById(id); }
    public List<Pet> getAllPets() { return (List<Pet>) repository.findAll(); }
    public void deleteAllPets() { repository.deleteAll(); }
}

@RestController
@RequestMapping("/api/v3/pet")
class PetController {
    private final PetService service;
    public PetController(PetService service) { this.service = service; }

    @PostMapping
    public Pet addPet(@RequestBody Pet pet) { return service.addPet(pet); }

    @PutMapping
    public Pet updatePet(@RequestBody Pet pet) { return service.updatePet(pet); }

    @GetMapping("/{petId}")
    public Pet getPetById(@PathVariable Long petId) {
        return service.getPetById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
    }

    @DeleteMapping("/{petId}")
    public void deletePet(@PathVariable Long petId) { service.deletePetById(petId); }

    @GetMapping
    public List<Pet> getAllPets() { return service.getAllPets(); }
}