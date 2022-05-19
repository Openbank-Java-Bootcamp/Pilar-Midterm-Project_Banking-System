package com.ironhack.midtermproject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.DTO.CheckingAccountDTO;
import com.ironhack.midtermproject.DTO.RoleToUserDTO;
import com.ironhack.midtermproject.model.Admin;
import com.ironhack.midtermproject.model.Money;
import com.ironhack.midtermproject.model.Role;
import com.ironhack.midtermproject.model.User;
import com.ironhack.midtermproject.repository.RoleRepository;
import com.ironhack.midtermproject.repository.UserRepository;
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
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class RoleControllerTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        User user = new Admin("Jose Perez","jose","1234");
        Role role = new Role("ROLE_ADMIN");
        userRepository.save(user);
        roleRepository.save(role);
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteById(1L);
        roleRepository.deleteById(1L);
    }

    @Test
    void saveRole_Valid_StatusCreated() throws Exception {
        Role role = new Role("ROLE_USER");
        String body = objectMapper.writeValueAsString(role);
        MvcResult mvcResult = mockMvc.perform(post("/api/roles")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    void addRoleToUser_Valid_NoContent() throws Exception {
        RoleToUserDTO roleToUser = new RoleToUserDTO("jose","ROLE_ADMIN");
        String body = objectMapper.writeValueAsString(roleToUser);
        MvcResult mvcResult = mockMvc.perform(post("/api/roletouser")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();
    }

    @Test
    void addRoleToUser_NotValidUsername_NotFound() throws Exception {
        RoleToUserDTO roleToUser = new RoleToUserDTO("paracucho","ROLE_ADMIN");
        String body = objectMapper.writeValueAsString(roleToUser);
        MvcResult mvcResult = mockMvc.perform(post("/api/roletouser")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();
    }

}