package com.shashi.foodie.userservice.config;

import com.shashi.foodie.userservice.model.Role;
import com.shashi.foodie.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            Role customerRole = new Role();
            customerRole.setName(Role.RoleType.ROLE_CUSTOMER);
            roleRepository.save(customerRole);

            Role restaurantOwnerRole = new Role();
            restaurantOwnerRole.setName(Role.RoleType.ROLE_RESTAURANT_OWNER);
            roleRepository.save(restaurantOwnerRole);

            Role deliveryDriverRole = new Role();
            deliveryDriverRole.setName(Role.RoleType.ROLE_DELIVERY_DRIVER);
            roleRepository.save(deliveryDriverRole);

            Role adminRole = new Role();
            adminRole.setName(Role.RoleType.ROLE_ADMIN);
            roleRepository.save(adminRole);

            System.out.println("Default roles initialized successfully");
        }
    }
}
