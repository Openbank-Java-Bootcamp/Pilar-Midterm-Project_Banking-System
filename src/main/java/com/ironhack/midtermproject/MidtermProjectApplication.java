package com.ironhack.midtermproject;

import com.ironhack.midtermproject.DTO.CheckingAccountDTO;
import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.repository.AccountRepository;
import com.ironhack.midtermproject.repository.UserRepository;
import com.ironhack.midtermproject.service.impl.AccountService;
import com.ironhack.midtermproject.service.impl.RoleService;
import com.ironhack.midtermproject.service.impl.ThirdPartyService;
import com.ironhack.midtermproject.service.impl.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;

@SpringBootApplication
public class MidtermProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MidtermProjectApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService, ThirdPartyService thirdPartyService, AccountService accountService, UserRepository userRepository) {
		return args -> {
			roleService.saveRole(new Role("ROLE_ACCOUNT_HOLDER"));
			roleService.saveRole(new Role("ROLE_ADMIN"));

			userService.saveUser(new AccountHolder("John Doe", "john", "1234", LocalDate.parse("1995-02-15"), new Address(),null));
			userService.saveUser(new AccountHolder("James Smith", "james", "1234", LocalDate.parse("2003-02-15"), new Address(),null));
			userService.saveUser(new AccountHolder("Jane Carry", "jane", "1234", LocalDate.parse("2018-02-15"), new Address(),null));
			userService.saveUser(new Admin("Chris Anderson", "chris", "1234"));

			thirdPartyService.saveThirdParty(new ThirdParty("Pedrito","4321"));

			roleService.addRoleToUser("john", "ROLE_ACCOUNT_HOLDER");
			roleService.addRoleToUser("james", "ROLE_ACCOUNT_HOLDER");
			roleService.addRoleToUser("jane", "ROLE_ACCOUNT_HOLDER");
			roleService.addRoleToUser("chris", "ROLE_ADMIN");

			accountService.createCheckingAccount(new CheckingAccountDTO(new Money(new BigDecimal("200"), Currency.getInstance("EUR")), 1L,null,"1357"));
			accountService.createCheckingAccount(new CheckingAccountDTO(new Money(new BigDecimal("500"), Currency.getInstance("EUR")), 2L,null,"1387"));
		};
	}

}
