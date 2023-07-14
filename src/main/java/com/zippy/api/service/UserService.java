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
                new User()
                        .setId(new ObjectId())
                        .setFirstName(dto.firstName())
                        .setLastName(dto.lastName())
                        .setEmail(dto.email())
                        .setDocument(dto.document())
                        .setDocumentType(dto.documentType())
                        .setAddress(dto.address())
                        .setPhone(dto.phone())
                        .setOccupation(dto.occupation())
                        .setBirthday(dto.birthday())
                        .setBackupPerson(dto.backupPerson())
                        .setBillingInformationId(
                                billingInformationService.add(
                                        new BillingInformation()
                                                .setWallet(
                                                        new Wallet()
                                                                .setBalance(new BigDecimal(0))
                                                                .setTransactions(null)
                                                )
                                                .setCards(null)
                                                .setId(new ObjectId())
                                ).getId()
                        )
        );
    }
}
