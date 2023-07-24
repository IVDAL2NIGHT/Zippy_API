package com.zippy.api.constants;

public enum VehicleStatus {
    AVAILABLE,
    UNAVAILABLE,
    ON_ROUTE,
    MAINTENANCE,
    OUT_OF_SERVICE,
    RESERVED;

    public static VehicleStatus fromString(String status) {
        switch (status) {
            case "AVAILABLE":
                return AVAILABLE;
            case "UNAVAILABLE":
                return UNAVAILABLE;
            case "ON_ROUTE":
                return ON_ROUTE;
            case "MAINTENANCE":
                return MAINTENANCE;
            case "OUT_OF_SERVICE":
                return OUT_OF_SERVICE;
            case "RESERVED":
                return RESERVED;
            default:
                return null;
        }
    }
}
