package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Используем тестовый профиль для подключения к тестовой базе данных
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @BeforeEach
    void setup() {
        // Очистка базы данных перед каждым тестом
        petService.deleteAllPets();

        // Добавление тестовых данных
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Dog");
        pet.setStatus("available");
        petService.addPet(pet);
    }

    @Test
    void testGetPetById() throws Exception {
        // Проверка получения питомца по ID через REST API
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v3/pet/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dog"));
    }

    @Test
    void testAddPet() {
        // Проверка добавления нового питомца в базу данных
        Pet newPet = new Pet();
        newPet.setId(2L);
        newPet.setName("Cat");
        newPet.setStatus("available");
        petService.addPet(newPet);

        Optional<Pet> savedPet = petService.getPetById(2L);
        Assertions.assertTrue(savedPet.isPresent());
        Assertions.assertEquals("Cat", savedPet.get().getName());
    }

    @Test
    void testAddPetToDatabase() {
        // Интеграционный тест: проверка сохранения питомца в базу данных
        Pet newPet = new Pet();
        newPet.setId(3L);
        newPet.setName("Bird");
        newPet.setStatus("available");
        petService.addPet(newPet);

        Optional<Pet> savedPet = petService.getPetById(3L);
        Assertions.assertTrue(savedPet.isPresent());
        Assertions.assertEquals("Bird", savedPet.get().getName());
    }

    @Test
    void testDeletePet() {
        // Проверка удаления питомца из базы данных
        Pet newPet = new Pet();
        newPet.setId(4L);
        newPet.setName("Fish");
        newPet.setStatus("available");
        petService.addPet(newPet);

        // Убедимся, что питомец добавлен
        Optional<Pet> savedPet = petService.getPetById(4L);
        Assertions.assertTrue(savedPet.isPresent());

        // Удалим питомца
        petService.deletePetById(4L);

        // Проверим, что питомец удален
        Optional<Pet> deletedPet = petService.getPetById(4L);
        Assertions.assertFalse(deletedPet.isPresent());
    }
}