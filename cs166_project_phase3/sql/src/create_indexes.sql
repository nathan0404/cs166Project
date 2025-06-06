-- Index to speed up searching for flights by departure and arrival city
CREATE INDEX idx_flight_departure_arrival ON Flight(DepartureCity, ArrivalCity);

-- Index to speed up searching for flight instances by flight number and date
CREATE INDEX idx_flightinstance_flightnumber_date ON FlightInstance(FlightNumber, FlightDate);

-- Index to speed up searching for repairs by plane and date
CREATE INDEX idx_repair_planeid_date ON Repair(PlaneID, RepairDate);

-- Index to speed up searching for maintenance requests by pilot
CREATE INDEX idx_maintreq_pilotid ON MaintenanceRequest(PilotID);

-- Added more indexes for query optimization tests
CREATE INDEX idx_schedule_flightnumber ON Schedule(FlightNumber);
CREATE INDEX idx_schedule_dayofweek ON Schedule(DayOfWeek);
CREATE INDEX idx_reservation_flightinstanceid ON Reservation(FlightInstanceID);
CREATE INDEX idx_repair_technicianid ON Repair(TechnicianID);
CREATE INDEX idx_maintreq_planeid ON MaintenanceRequest(PlaneID);
