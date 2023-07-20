package com.zippy.api.rest;

import com.zippy.api.document.BillingInformation;
import com.zippy.api.document.Credential;
import com.zippy.api.document.User;
import com.zippy.api.dto.UpdateBillingInformationDTO;
import com.zippy.api.service.BillingInformationService;
import com.zippy.api.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;

@RestController
@RequestMapping("/api/billingInformation")
public class BillingInformationREST {
    private final BillingInformationService billingInformationService;
    private final UserService userService;

    public BillingInformationREST(BillingInformationService billingInformationService, UserService userService) {
        this.billingInformationService = billingInformationService;
        this.userService = userService;
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("#credential.userId == #id")
    public ResponseEntity<?> getBillingInformation(@AuthenticationPrincipal Credential credential, @PathVariable ObjectId id) {
        return ResponseEntity.ok(billingInformationService.get(userService
                .getById(id)
                .getBillingInformationId()
        ));
    }

    @PostMapping("/add-card/{id}")
    @PreAuthorize("#credential.userId == #id")
    public ResponseEntity<?> addNewCard(@AuthenticationPrincipal Credential credential,
                                        @RequestBody UpdateBillingInformationDTO newCardDTO,
                                        @PathVariable ObjectId id){

        return ResponseEntity.ok(billingInformationService.save(billingInformationService.get(userService
                        .getById(id)
                        .getBillingInformationId())
                .addCard(newCardDTO.newCard())
        ));
    }

    @PostMapping("/recharge/{id}")
    @PreAuthorize("#credential.userId == #id")
    public ResponseEntity<?> RechargeMoney(@AuthenticationPrincipal Credential credential,
                                           @RequestBody UpdateBillingInformationDTO newBalanceDTO,
                                           @PathVariable ObjectId id) {
        return ResponseEntity.ok(billingInformationService.save(billingInformationService.get(userService
                        .getById(id).getBillingInformationId())
                .addBalance(newBalanceDTO.money())
        ));
    }

    @PostMapping("/add-transaction/{id}")
    @PreAuthorize("#credential.userId == #id")
    public ResponseEntity<?> addTransaction(@AuthenticationPrincipal Credential credential,
                                            @RequestBody UpdateBillingInformationDTO newTransactionDTO,
                                            @PathVariable ObjectId id) {
        return ResponseEntity.ok(billingInformationService.save(billingInformationService.get(userService
                        .getById(id).getBillingInformationId())
                .addTransaction(newTransactionDTO.transaction())
        ));
    }
}
