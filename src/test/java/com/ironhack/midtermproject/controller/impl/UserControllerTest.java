package com.ironhack.midtermproject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.repository.*;
import com.ironhack.midtermproject.service.impl.ThirdPartyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Address address1 = new Address("Passatge Flaugier 6","08041","Barcelona");
        Address address2 = new Address("Carrer de Rogent","08041","Barcelona");

        User user1 = new AccountHolder("Pilar Alvarez","pili","1234", LocalDate.parse("2007-01-15"),address1,null);
        User user2 = new AccountHolder("Macarena Garcia","maqui","1234", LocalDate.parse("1989-01-07"),address2,null);
        User user3 = new AccountHolder("Paula Lopez","pauli","1234", LocalDate.parse("2005-01-15"),address1,null);
        User user4 = new Admin("Jose Perez","jose","1234");


        userRepository.saveAll(List.of(user1, user2, user3, user4));

    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }



    @Test
    void createAccountHolder_Valid_Created() throws Exception {
        Address address1 = new Address("Passatge Flaugier 6","08041","Barcelona");
        AccountHolder ac = new AccountHolder("Pilar Perez","pililu","1234", LocalDate.parse("2007-01-15"),address1,null);
        String body = objectMapper.writeValueAsString(ac);
        MvcResult mvcResult = mockMvc.perform(post("/api/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    void createAccountHolder_NotValidUserName_NotAcceptable() throws Exception {
        Address address1 = new Address("Passatge Flaugier 6","08041","Barcelona");
        AccountHolder ac = new AccountHolder("Pilar Perez","pili","1234", LocalDate.parse("2007-01-15"),address1,null);
        String body = objectMapper.writeValueAsString(ac);
        MvcResult mvcResult = mockMvc.perform(post("/api/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable()).andReturn();
    }

    @Test
    void createAcdmin_Valid_Created() throws Exception {
        Admin a = new Admin("Jose Perez","josesito","1234");
        String body = objectMapper.writeValueAsString(a);
        MvcResult mvcResult = mockMvc.perform(post("/api/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    void createAcdmin_NotValidUsername_NotAcceptable() throws Exception {
        Admin a = new Admin("Jose Perez","jose","1234");
        String body = objectMapper.writeValueAsString(a);
        MvcResult mvcResult = mockMvc.perform(post("/api/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable()).andReturn();
    }


    @Test
    void deleteUser_Valid_Accepted() throws Exception {
        MvcResult mcvResult = mockMvc.perform(delete("/api/users")
                        .queryParam("userId", "1")
                )
                .andExpect(status().isAccepted()).andReturn();
    }

    @Test
    void deleteUser_NotValidId_NotFound() throws Exception {
        MvcResult mcvResult = mockMvc.perform(delete("/api/users")
                        .queryParam("userId", "10")
                )
                .andExpect(status().isNotFound()).andReturn();
    }

}