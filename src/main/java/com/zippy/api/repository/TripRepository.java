package com.zippy.api.repository;

import com.zippy.api.constants.TripStatus;
import com.zippy.api.document.Trip;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends MongoRepository<Trip, ObjectId> {

    Optional<Trip> findByUserIdAndStatus(ObjectId userId, TripStatus tripStatus);
    Optional<List<Trip>> findByUserId(ObjectId userId);
}
