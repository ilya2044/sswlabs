package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @BeforeEach
    void setup() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Dog");
        pet.setStatus("available");
        petService.addPet(pet);
    }

    @Test
    void testGetPetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v3/pet/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dog"));
    }

    @Test
    void testAddPet() {
        Pet newPet = new Pet();
        newPet.setId(2L);
        newPet.setName("Cat");
        petService.addPet(newPet);
        Assertions.assertTrue(petService.getPetById(2L).isPresent());
    }
}
