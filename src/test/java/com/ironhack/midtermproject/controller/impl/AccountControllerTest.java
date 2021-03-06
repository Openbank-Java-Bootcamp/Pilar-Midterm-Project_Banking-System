package com.ironhack.midtermproject.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.DTO.CheckingAccountDTO;
import com.ironhack.midtermproject.DTO.CreditAccountDTO;
import com.ironhack.midtermproject.DTO.OwnerTransferDTO;
import com.ironhack.midtermproject.DTO.SavingAccountDTO;
import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.repository.*;
import com.ironhack.midtermproject.service.impl.ThirdPartyService;
import com.ironhack.midtermproject.service.impl.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
class AccountControllerTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();


    private DateTimeFormatter df =
            new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd[ [HH][:mm][:ss][.SSS]]")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Address address1 = new Address("Passatge Flaugier 6","08041","Barcelona");
        Address address2 = new Address("Carrer de Rogent","08041","Barcelona");

        User user1 = userService.createAccountHolder(new AccountHolder("Pilar Alvarez","pili","1234", LocalDate.parse("2007-01-15"),address1,null));
        User user2 = userService.createAccountHolder(new AccountHolder("Macarena Garcia","maqui","1234", LocalDate.parse("1989-01-07"),address2,null));
        User user3 = userService.createAccountHolder(new AccountHolder("Paula Lopez","pauli","1234", LocalDate.parse("2005-01-15"),address1,null));
        User user4 = userService.createAdmin(new Admin("Jose Perez","jose","1234"));

        ThirdParty tp1 = thirdPartyService.saveThirdParty(new ThirdParty("Pedro Gomez", "1234"));

        Role role1 = new Role("ROLE_ACCOUNT_HOLDER");
        Role role2 = new Role("ROLE_ADMIN");

        Savings acc1 = new Savings(new Money(BigDecimal.valueOf(50000), Currency.getInstance("EUR")), (AccountHolder) user1, (AccountHolder) user2,"1234",new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")),BigDecimal.valueOf(0.4));
        CreditCard acc2 = new CreditCard(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user2,null,new Money(BigDecimal.valueOf(10000), Currency.getInstance("EUR")),BigDecimal.valueOf(0.15));
        Checking acc3 = new Checking(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user1,null,"1234");
        StudentChecking acc4 = new StudentChecking(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user3, (AccountHolder) user2,"1234");
        Savings acc5 = new Savings(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user2, (AccountHolder) user1,"1234",new Money(BigDecimal.valueOf(800), Currency.getInstance("EUR")),BigDecimal.valueOf(0.2));
        CreditCard acc6 = new CreditCard(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), (AccountHolder) user3,null,new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")),BigDecimal.valueOf(0.2));

        acc1.setCreationDate(LocalDate.parse("2022-02-01"));
        acc3.setMaintenanceCharged(LocalDate.parse("2022-03-02"));
        acc5.setAddedInterest(LocalDate.parse("2021-04-15"));
        acc6.setAddedInterest(LocalDate.parse("2022-03-02"));

        Transfer tf1 = new Transfer(1L,BigDecimal.valueOf(50), LocalDateTime.parse("2022-05-02",df));
        Transfer tf2 = new Transfer(1L,BigDecimal.valueOf(50), LocalDateTime.parse("2022-05-03",df));
        Transfer tf3 = new Transfer(1L,BigDecimal.valueOf(50), LocalDateTime.parse("2022-05-04",df));
        Transfer tf4 = new Transfer(1L,BigDecimal.valueOf(50), LocalDateTime.parse("2022-05-05",df));
        Transfer tf5 = new Transfer(1L,BigDecimal.valueOf(50), LocalDateTime.parse("2022-05-06",df));
        Transfer tf6 = new Transfer(1L,BigDecimal.valueOf(50), LocalDateTime.parse("2022-05-07",df));
        Transfer tf7 = new Transfer(1L,BigDecimal.valueOf(50), LocalDateTime.parse("2022-05-08",df));
        Transfer tf8 = new Transfer(1L,BigDecimal.valueOf(50), LocalDateTime.parse("2022-05-08",df));

        user1.getRoles().add(role1);
        user2.getRoles().add(role1);
        user3.getRoles().add(role1);
        user4.getRoles().add(role2);

        roleRepository.saveAll(List.of(role1,role2));
        userRepository.saveAll(List.of(user1, user2, user3, user4));
        thirdPartyRepository.save(tp1);
        accountRepository.saveAll(List.of(acc1, acc2, acc3, acc4, acc5, acc6));
        transferRepository.saveAll(List.of(tf1, tf2, tf3, tf4, tf5, tf6,tf7,tf8));
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
        MvcResult mvcResult = mockMvc.perform(post("/api/checking")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    void createCheckingAccount_NotValidOwnerId_NotFound() throws Exception {
        CheckingAccountDTO c = new CheckingAccountDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), 15L, null,"1234");
        String body = objectMapper.writeValueAsString(c); //transform into a string as a JSON object
        MvcResult mvcResult = mockMvc.perform(post("/api/checking")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void createSavingsAccount_Valid_Created() throws Exception {
        SavingAccountDTO s = new SavingAccountDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), 1L, null,"1234",new Money(BigDecimal.valueOf(800), Currency.getInstance("EUR")),BigDecimal.valueOf(0.2));
        String body = objectMapper.writeValueAsString(s);
        MvcResult mvcResult = mockMvc.perform(post("/api/saving")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    void createSavingsAccount_NotValidOwnerId_Created() throws Exception {
        SavingAccountDTO s = new SavingAccountDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), 20L, null,"1234",new Money(BigDecimal.valueOf(800), Currency.getInstance("EUR")),BigDecimal.valueOf(0.2));
        String body = objectMapper.writeValueAsString(s);
        MvcResult mvcResult = mockMvc.perform(post("/api/saving")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void createCreditAccount_Valid_Created() throws Exception {
        CreditAccountDTO c = new CreditAccountDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), 1L, null,new Money(BigDecimal.valueOf(2000), Currency.getInstance("EUR")),BigDecimal.valueOf(0.2));
        String body = objectMapper.writeValueAsString(c);
        MvcResult mvcResult = mockMvc.perform(post("/api/credit")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    void createCreditAccount_NotValidOwnerId_Created() throws Exception {
        CreditAccountDTO c = new CreditAccountDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), 15L, 1L,new Money(BigDecimal.valueOf(2000), Currency.getInstance("EUR")),BigDecimal.valueOf(0.2));
        String body = objectMapper.writeValueAsString(c);
        MvcResult mvcResult = mockMvc.perform(post("/api/credit")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();
    }


    @Test
    @WithMockUser(username = "pili", password = "1234", roles = {"ACCOUNT_HOLDER"})
    void transferMoney_Valid_NotContent() throws Exception {
        OwnerTransferDTO o = new OwnerTransferDTO(new Money(BigDecimal.valueOf(50), Currency.getInstance("EUR")), "Macarena Garcia", 2L,1L);
        String body = objectMapper.writeValueAsString(o);
        MvcResult mvcResult = mockMvc.perform(patch("/api/transfer")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @WithMockUser(username = "pili", password = "1234", roles = {"ACCOUNT_HOLDER"})
    void transferMoney_Fraud_BadRequest() throws Exception {
        OwnerTransferDTO o = new OwnerTransferDTO(new Money(BigDecimal.valueOf(1000), Currency.getInstance("EUR")), "Macarena Garcia", 2L,1L);
        String body = objectMapper.writeValueAsString(o);
        MvcResult mvcResult = mockMvc.perform(patch("/api/transfer")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
        Account acc = accountRepository.findById(o.getOwnAccountId()).get();
        assertEquals("FROZEN",((Savings) acc).getStatus().toString());
        //assertTrue(((Savings) acc).getStatus().toString().contains("FROZEN"));
    }


    @Test
    @WithMockUser(username = "pili", password = "1234", roles = {"ACCOUNT_HOLDER"})
    void transferMoney_AmountGraterThanBalance_BadRequest() throws Exception {
        OwnerTransferDTO o = new OwnerTransferDTO(new Money(BigDecimal.valueOf(60000), Currency.getInstance("EUR")), "Macarena Garcia", 2L,1L);
        String body = objectMapper.writeValueAsString(o);
        MvcResult mvcResult = mockMvc.perform(patch("/api/transfer")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithMockUser(username = "pauli", password = "1234", roles = {"ACCOUNT_HOLDER"})
    void transferMoney_AmountGraterThanCredit_BadRequest() throws Exception {
        OwnerTransferDTO o = new OwnerTransferDTO(new Money(BigDecimal.valueOf(2000), Currency.getInstance("EUR")), "Macarena Garcia", 2L,6L);
        String body = objectMapper.writeValueAsString(o);
        MvcResult mvcResult = mockMvc.perform(patch("/api/transfer")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithMockUser(username = "pili", password = "1234", roles = {"ACCOUNT_HOLDER"})
    void transferMoney_NotValidOwnAccountId_NotFound() throws Exception {
        OwnerTransferDTO o = new OwnerTransferDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), "Paula Lopez", 4L,15L);
        String body = objectMapper.writeValueAsString(o);
        MvcResult mvcResult = mockMvc.perform(patch("/api/transfer")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @WithMockUser(username = "pili", password = "1234", roles = {"ACCOUNT_HOLDER"})
    void transferMoney_NotAnAccountIdOwned_NotFound() throws Exception {
        OwnerTransferDTO o = new OwnerTransferDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), "Paula Lopez", 4L,6L);
        String body = objectMapper.writeValueAsString(o);
        MvcResult mvcResult = mockMvc.perform(patch("/api/transfer")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();
    }


    @Test
    @WithMockUser(username = "pili", password = "1234", roles = {"ACCOUNT_HOLDER"})
    void getBalance_Valid_MoneyObject() throws Exception {
        MvcResult mcvResult = mockMvc.perform(get("/api/balance")
                        .queryParam("accountId", "1")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mcvResult.getResponse().getContentAsString().contains("500"));
    }


    @Test
    @WithMockUser(username = "pauli", password = "1234", roles = {"ACCOUNT_HOLDER"})
    void getBalance_NoInterestAdded_InterestAdded() throws Exception {
        MvcResult mcvResult = mockMvc.perform(get("/api/balance")
                        .queryParam("accountId", "6")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mcvResult.getResponse().getContentAsString().contains("520"));
    }

    @Test
    void getBalanceAdmin_Valid_MoneyObjectOKStatus() throws Exception {
        MvcResult mcvResult = mockMvc.perform(get("/api/adminbalance")
                        .queryParam("accountId", "2")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mcvResult.getResponse().getContentAsString().contains("500"));
    }



    @Test
    void getBalanceAdmin_NotAddedInterest_MoneyObject() throws Exception {
        MvcResult mcvResult = mockMvc.perform(get("/api/adminbalance")
                        .queryParam("accountId", "5")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mcvResult.getResponse().getContentAsString().contains("600")); //no se me a??aden los intereses
    }


    @Test
    void changeBalance_Valid_StatusNoContent() throws Exception {
        MvcResult mcvResult = mockMvc.perform(patch("/api/setbalance")
                        .queryParam("accountId", "2")
                        .queryParam("newBalance", "1000")
                )
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    void changeBalance_NotValidId_StatusNotFound() throws Exception {
        MvcResult mcvResult = mockMvc.perform(patch("/api/setbalance")
                        .queryParam("accountId", "20")
                        .queryParam("newBalance", "1000")
                )
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void deleteAccount_Valid_NoContentStatus() throws Exception {
        MvcResult mcvResult = mockMvc.perform(delete("/api/account")
                        .queryParam("accountId", "2")
                )
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    void deleteAccount_NotValidId_NotFoundStatus() throws Exception {
        MvcResult mcvResult = mockMvc.perform(delete("/api/account")
                        .queryParam("accountId", "20")
                )
                .andExpect(status().isNotFound()).andReturn();
    }

}