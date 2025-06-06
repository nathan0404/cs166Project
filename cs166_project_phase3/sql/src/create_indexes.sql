-- Index to speed up searching for flights by departure and arrival city
CREATE INDEX idx_flight_departure_arrival ON Flight(DepartureCity, ArrivalCity);

-- Index to speed up searching for flight instances by flight number and date
CREATE INDEX idx_flightinstance_flightnumber_date ON FlightInstance(FlightNumber, FlightDate);

-- Index to speed up searching for repairs by plane and date
CREATE INDEX idx_repair_planeid_date ON Repair(PlaneID, RepairDate);

-- Index to speed up searching for maintenance requests by pilot
CREATE INDEX idx_maintreq_pilotid ON MaintenanceRequest(PilotID);
