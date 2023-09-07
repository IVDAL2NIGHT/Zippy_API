package com.zippy.api.constants;

public enum VehicleType {
    SCOOTER, BICYCLE;

    public static VehicleType fromString(String type) {
        switch (type) {
            case "SCOOTER":
                return SCOOTER;
            case "BICYCLE":
                return BICYCLE;
            default:
                return null;
        }
    }
}
