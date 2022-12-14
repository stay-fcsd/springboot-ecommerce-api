package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.EmployeeRegistrationToken;
import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.event.EmployeeRegistrationTokenEvent;
import com.example.springbootecommerceapi.exception.UserException;
import com.example.springbootecommerceapi.model.EmailDTO;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.repository.EmployeeRegistrationRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminService {
    private final EmployeeRegistrationRepository employeeRegistrationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;
    private final HttpServletRequest request;

    @Autowired
    public AdminService(
            EmployeeRegistrationRepository employeeRegistrationRepository,
            UserRepository userRepository,
            ApplicationEventPublisher publisher,
            HttpServletRequest request
    ) {
        this.employeeRegistrationRepository = employeeRegistrationRepository;
        this.userRepository = userRepository;
        this.publisher = publisher;
        this.request = request;

    }

    public void createEmployeeRegistrationToken(String adminEmail, String employeeEmail) {
        Optional<UserEntity> admin = userRepository.findByEmail(adminEmail);

        if (admin.isEmpty()) {
            throw new UserException("User does not exist");
        }

        if (!admin.get().getRole().equals(Role.ADMIN)) {
            throw new UserException("You must be an admin to create employee registration token");
        }

        String token = UUID.randomUUID().toString();
        EmployeeRegistrationToken registrationToken =
                new EmployeeRegistrationToken(admin.get(), token, employeeEmail);

        employeeRegistrationRepository.save(registrationToken);
        publisher.publishEvent(new EmployeeRegistrationTokenEvent(admin.get(), employeeEmail, token));
    }

}
