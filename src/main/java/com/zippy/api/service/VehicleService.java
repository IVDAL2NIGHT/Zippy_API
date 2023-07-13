package com.zippy.api.service;

import com.zippy.api.document.Vehicle;
import com.zippy.api.exception.VehicleNotFoundException;
import com.zippy.api.repository.VehicleRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> allVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicleById(ObjectId id) {
        vehicleRepository.deleteById(id);
    }

    public Vehicle getVehicleById(ObjectId id) throws VehicleNotFoundException {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("El id del vehículo no existe"));
    }

    public Vehicle getVehicleBySerial(String serial) throws VehicleNotFoundException {
        return vehicleRepository.findBySerial(serial)
                .orElseThrow(() -> new VehicleNotFoundException("El serial del vehículo no existe"));
    }


}