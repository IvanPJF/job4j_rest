package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenFindAllPersonsThenReturnJsonAllPersons() throws Exception {
        Person pFirst = Person.of(1);
        Person pSecond = Person.of(2);
        List<Person> list = List.of(pFirst, pSecond);
        String rslJson = new ObjectMapper().writeValueAsString(list);
        when(personRepository.findAll()).thenReturn(list);
        mockMvc.perform(get("/person").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(rslJson));
    }

    @Test
    public void whenFindPersonByIdThenStatusIsOkAndReturnOneJsonPerson() throws Exception {
        int id = 100;
        Person person = Person.of(id);
        String rslJson = new ObjectMapper().writeValueAsString(person);
        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        mockMvc.perform(get("/person/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(rslJson));
    }

    @Test
    public void whenFindPersonByIdThenStatusIsNotFoundAndReturnEmptyPerson() throws Exception {
        int id = 100;
        String rslJson = new ObjectMapper().writeValueAsString(new Person());
        when(personRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/person/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(rslJson));
    }

    @Test
    public void whenCreatePersonThenStatusIsOkAndReturnJsonPerson() throws Exception {
        Person newPerson = Person.of("root", "root");
        String reqJson = new ObjectMapper().writeValueAsString(newPerson);
        Person createPerson = Person.of(newPerson.getLogin(), newPerson.getPassword());
        createPerson.setId(1);
        String rslJson = new ObjectMapper().writeValueAsString(createPerson);
        when(personRepository.save(newPerson)).thenReturn(createPerson);
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(rslJson));
    }

    @Test
    public void whenUpdatePersonThenStatusIsOk() throws Exception {
        Person person = Person.of(1);
        String reqJson = new ObjectMapper().writeValueAsString(person);
        mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void whenDeletePersonThenStatusIsOk() throws Exception {
        mockMvc.perform(delete("/person/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}