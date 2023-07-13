package com.zippy.api.repository;

import com.zippy.api.document.Vehicle;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, ObjectId> {
    Optional<Vehicle> findBySerial(String serial);
}
