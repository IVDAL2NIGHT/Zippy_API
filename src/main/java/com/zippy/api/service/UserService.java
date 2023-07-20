package com.zippy.api.service;

import com.zippy.api.document.BillingInformation;
import com.zippy.api.document.User;
import com.zippy.api.dto.UserDTO;
import com.zippy.api.exception.UserNotFoundException;
import com.zippy.api.models.Wallet;
import com.zippy.api.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BillingInformationService billingInformationService;

    public UserService(UserRepository userRepository, BillingInformationService billingInformationService) {
        this.userRepository = userRepository;
        this.billingInformationService = billingInformationService;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User getById(ObjectId id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("The user with id " + id + " was not found"));
    }

    public User add(User user) {
        return userRepository.insert(user);
    }

    public User createNewUser(@Valid UserDTO dto) {
        return add(
                User.builder()
                        .id(new ObjectId())
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .document(dto.document())
                        .documentType(dto.documentType())
                        .address(dto.address())
                        .phone(dto.phone())
                        .occupation(dto.occupation())
                        .birthDate(dto.birthday())
                        .backupPerson(dto.backupPerson())
                        .billingInformationId(
                                billingInformationService.add(
                                        BillingInformation.builder()
                                                .balance(new BigDecimal(0))
                                                .id(new ObjectId())
                                                .build()
                                ).getId()
                        ).build()
        );
    }
}
