package com.unsa.etf.Identity.Service;

import com.unsa.etf.Identity.Service.Model.Permission;
import com.unsa.etf.Identity.Service.Model.Role;
import com.unsa.etf.Identity.Service.Model.RoleEnum;
import com.unsa.etf.Identity.Service.Model.User;
import com.unsa.etf.Identity.Service.Repository.PermissionRepository;
import com.unsa.etf.Identity.Service.Repository.RoleRepository;
import com.unsa.etf.Identity.Service.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@EnableDiscoveryClient
@SpringBootApplication
public class IdentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner (PermissionRepository permissionRepository,
										 RoleRepository roleRepository,
										 UserRepository userRepository) {

		return args -> {

			Role admin = new Role("Administrator");
			Role shopper = new Role("Shopper");

			roleRepository.saveAll(Arrays.asList(admin, shopper));

			Permission permission1 = new Permission("Edit products");
			Permission permission2 = new Permission("View products");
			Permission permission3 = new Permission("Delete products");
			Permission permission4 = new Permission("Add to cart");

			permissionRepository.saveAll(Arrays.asList(permission1, permission2, permission3, permission4));

			admin.setPermissions(Arrays.asList(permission1, permission2, permission3));
			shopper.setPermissions(Arrays.asList(permission2, permission4));

			roleRepository.saveAll(Arrays.asList(admin, shopper));

			User faruk = new User(
						"Faruk",
						"Smajlovic",
						"fsmajlovic",
						"do3218uejd",
						"fsmajlovic2@etf.unsa.ba",
						"061111222",
						"Envera Sehovica 24",
						RoleEnum.ADMIN);
			User kemal = new User(
						"Kemal",
						"Lazovic",
						"klazovic1",
						"jdoa9920",
						"klazovic1@etf.unsa.ba",
						"061333444",
						"Podbudakovici 4",
						RoleEnum.USER);

			User taida = new User(
						"Taida",
						"Kadric",
						"tkadric28",
						"da09dasp",
						"tkadric1@etf.unsa.ba",
						"061555666",
						"Sulejmana Filipovica 10",
						RoleEnum.USER);

			userRepository.saveAll(Arrays.asList(faruk, kemal, taida));
		};
	}
}
