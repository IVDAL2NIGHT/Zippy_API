package com.zippy.api.service;

import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.document.Vehicle;
import com.zippy.api.exception.VehicleNotFoundException;
import com.zippy.api.repository.VehicleRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> all() {
        return vehicleRepository.findAll();
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle add(Vehicle vehicle) {
        return vehicleRepository.insert(vehicle);
    }

    public void delete(ObjectId id) {
        vehicleRepository.deleteById(id);
    }

    public Vehicle updateStatus(ObjectId id, VehicleStatus status) throws VehicleNotFoundException {
        Vehicle vehicle = getById(id);
        vehicle.setStatus(status);
        return save(vehicle);
    }

    public Vehicle getById(ObjectId id) throws VehicleNotFoundException {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("El id del vehículo no existe"));
    }

    public Vehicle getBySerial(String serial) throws VehicleNotFoundException {
        return vehicleRepository.findBySerial(serial)
                .orElseThrow(() -> new VehicleNotFoundException("El serial del vehículo no existe"));
    }


}