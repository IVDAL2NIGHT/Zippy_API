package com.zippy.api.service;

import com.zippy.api.document.BillingInformation;
import com.zippy.api.document.User;
import com.zippy.api.dto.UserDTO;
import com.zippy.api.exception.UserNotFoundException;
import com.zippy.api.models.Wallet;
import com.zippy.api.repository.BillingInformationRepository;
import com.zippy.api.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BillingInformationRepository billingInformationRepository;

    public UserService(UserRepository userRepository, BillingInformationRepository billingInformationRepository) {
        this.userRepository = userRepository;
        this.billingInformationRepository = billingInformationRepository;
    }

    public User createNewUser(@Valid UserDTO userDTO) {
        BillingInformation billingInformation = new BillingInformation();
        billingInformation.setWallet(new Wallet(new BigDecimal(0), null));
        billingInformation.setId(new ObjectId());

        User user = new User(
                new ObjectId(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getBirthday(),
                userDTO.getOccupation(),
                userDTO.getEmail(),
                userDTO.getPhone(),
                userDTO.getDocument(),
                userDTO.getDocumentType(),
                billingInformation.getId(),
                userDTO.getBackupPerson(),
                userDTO.getAddress()
        );

        billingInformationRepository.save(billingInformation);
        return userRepository.save(user);
    }

    public Object saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User getUserById(ObjectId id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("The user with id " + id + " was not found"));
    }
}
