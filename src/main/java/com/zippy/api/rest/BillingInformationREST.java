package com.zippy.api.rest;

import com.zippy.api.document.BillingInformation;
import com.zippy.api.document.Credential;
import com.zippy.api.document.User;
import com.zippy.api.dto.UpdateBillingInformationDTO;
import com.zippy.api.service.BillingInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billingInformation")
public class BillingInformationREST {
    private final BillingInformationService billingInformationService;

    public BillingInformationREST(BillingInformationService billingInformationService) {
        this.billingInformationService = billingInformationService;
    }

    @GetMapping("/get")
    @PreAuthorize("#credential.userId == #user.id")
    public ResponseEntity<?> getBillingInformation(@AuthenticationPrincipal Credential credential, @RequestBody User user) {
        return ResponseEntity.ok(billingInformationService.getBillingInformation(user.getBillingInformationId()));
    }

    @PostMapping("/add-card")
    @PreAuthorize("#credential.userId == #newCardDTO.userId")
    public ResponseEntity<?> addNewCard(@AuthenticationPrincipal Credential credential,
                                        @RequestBody UpdateBillingInformationDTO newCardDTO) {
        BillingInformation billingInformation = billingInformationService
                .getBillingInformation(newCardDTO.getBillingInformationId());
        billingInformation.getCards().add(newCardDTO.getNewCard());
        return ResponseEntity.ok(billingInformationService.saveBillingInformation(billingInformation));
    }

    @PostMapping("/recharge")
    @PreAuthorize("#credential.userId == #newBalanceDTO.userId")
    public ResponseEntity<?> RechargeMoney(@AuthenticationPrincipal Credential credential,
                                           @RequestBody UpdateBillingInformationDTO newBalanceDTO) {
        BillingInformation billingInformation = billingInformationService
                .getBillingInformation(newBalanceDTO.getBillingInformationId());
        billingInformation.getWallet().setBalance(billingInformation.getWallet().getBalance().add(newBalanceDTO.getMoney()));
        return ResponseEntity.ok(billingInformationService.saveBillingInformation(billingInformation));
    }

    @PostMapping("/add-transaction")
    @PreAuthorize("#credential.userId == #newTransactionDTO.userId")
    public ResponseEntity<?> addTransaction(@AuthenticationPrincipal Credential credential,
                                            @RequestBody UpdateBillingInformationDTO newTransactionDTO) {
        BillingInformation billingInformation = billingInformationService
                .getBillingInformation(newTransactionDTO.getBillingInformationId());
        billingInformation.getWallet().getTransactions().add(newTransactionDTO.getTransaction());
        return ResponseEntity.ok(billingInformationService.saveBillingInformation(billingInformation));
    }
}
