package com.ironhack.midtermproject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.DTO.CheckingAccountDTO;
import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class AccountControllerTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Address address1 = new Address("Passatge Flaugier 6","08041","Barcelona");
        Address address2 = new Address("Carrer de Rogent","08041","Barcelona");

        User user1 = new AccountHolder("Pilar Alvarez","pili","1234", LocalDate.parse("1990-01-15"),address1,null);
        User user2 = new AccountHolder("Macarena Garcia","maqui","1234", LocalDate.parse("1989-01-07"),address2,null);
        User user3 = new AccountHolder("Paula Lopez","pauli","1234", LocalDate.parse("2005-01-15"),address1,null);
        User user4 = new Admin("Jose Perez","jose","1234");

        ThirdParty tp1 = new ThirdParty("Pedro Gomez", "1234");

        Role role1 = new Role("ROLE_ACCOUNT_HOLDER");
        Role role2 = new Role("ROLE_ADMIN");

        Account acc1 = new Savings(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user1, (AccountHolder) user2,"1234",new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")),BigDecimal.valueOf(0.4));
        Account acc2 = new CreditCard(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user2,null,new Money(BigDecimal.valueOf(10000), Currency.getInstance("EUR")),BigDecimal.valueOf(0.15));
        Account acc3 = new Checking(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user1,null,"1234");
        Account acc4 = new StudentChecking(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user3, (AccountHolder) user2,"1234");
        Account acc5 = new Savings(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user2, (AccountHolder) user1,"1234",new Money(BigDecimal.valueOf(800), Currency.getInstance("EUR")),BigDecimal.valueOf(0.2));
        Account acc6 = new CreditCard(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user3,null,new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")),BigDecimal.valueOf(0.2));

        acc4.setCreationDate(LocalDate.parse("2022-03-02"));
        acc5.setCreationDate(LocalDate.parse("2021-04-15"));
        acc6.setCreationDate(LocalDate.parse("2022-03-02"));

        user1.getRoles().add(role1);
        user2.getRoles().add(role1);
        user3.getRoles().add(role1);
        user4.getRoles().add(role2);

        accountRepository.saveAll(List.of(acc1, acc2, acc3, acc4, acc5, acc6));
        userRepository.saveAll(List.of(user1, user2, user3, user4));
        thirdPartyRepository.save(tp1);
        roleRepository.saveAll(List.of(role1,role2));
    }

    @AfterEach
    void tearDown(){
        accountRepository.deleteAll();
        userRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void createCheckingAccount_Valid_Created() throws Exception {
        CheckingAccountDTO c = new CheckingAccountDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), 1L, null,"1234");
        String body = objectMapper.writeValueAsString(c); //transform into a string as a JSON object
        MvcResult mvcResult = mockMvc.perform(post("/checking")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
    }

}