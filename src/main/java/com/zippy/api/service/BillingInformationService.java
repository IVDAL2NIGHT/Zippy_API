package com.zippy.api.service;

import com.zippy.api.document.BillingInformation;
import com.zippy.api.exception.BillingInformationNotFoundException;
import com.zippy.api.repository.BillingInformationRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BillingInformationService {
    private final BillingInformationRepository billingInformationRepository;

    public BillingInformationService(BillingInformationRepository billingInformationRepository) {
        this.billingInformationRepository = billingInformationRepository;
    }

    public BillingInformation add(BillingInformation billingInformation) {
        return billingInformationRepository.insert(billingInformation);
    }

    public BillingInformation save(BillingInformation billingInformation) {
        return billingInformationRepository.save(billingInformation);
    }

    public void delete(ObjectId id) {
        billingInformationRepository.deleteById(id);
    }

    public BillingInformation get(ObjectId id) throws BillingInformationNotFoundException {
        return billingInformationRepository.findById(id)
                .orElseThrow(() -> new BillingInformationNotFoundException("información de pago no encontrada"));
    }
}
