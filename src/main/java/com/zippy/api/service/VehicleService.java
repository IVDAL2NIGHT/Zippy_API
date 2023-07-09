package com.zippy.api.service;

import com.zippy.api.document.Vehicle;
import com.zippy.api.repository.VehicleRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import com.zippy.api.exception.VehicleNotFoundException;

import java.util.List;

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
        return vehicleRepository.findById(id).orElseThrow(() -> new VehicleNotFoundException("El id del vehículo no existe"));
    }

    public Vehicle getVehicleBySerial(String serial) {
        return vehicleRepository.findBySerial(serial);
    }


}