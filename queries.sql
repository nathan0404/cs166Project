-- Feature 1: Get schedule for a flight number
SELECT ScheduleID, FlightNumber, DayOfWeek, DepartureTime, ArrivalTime FROM Schedule WHERE FlightNumber = 'F100';

-- Feature 2: Get seats available and sold for a flight and date
SELECT SeatsTotal, SeatsSold FROM FlightInstance WHERE FlightNumber = 'F100' AND FlightDate = '5/5/25';

-- Feature 3: Check if flight departed and arrived on time for a flight and date
SELECT DepartedOnTime, ArrivedOnTime FROM FlightInstance WHERE FlightNumber = 'F100' AND FlightDate = '5/5/25';

-- Feature 4: Get all flights scheduled on a day of the week
SELECT * FROM Schedule WHERE DayOfWeek = 'Monday';

-- Feature 5: List passengers by reservation status for a flight and date
SELECT ReservationID, CustomerID, Status FROM Reservation WHERE FlightInstanceID = (SELECT FlightInstance.FlightInstanceID FROM FlightInstance WHERE FlightInstance.FlightNumber = 'F100' AND FlightInstance.FlightDate = '5/5/25');

-- Feature 6: Get traveler info by reservation number
SELECT * FROM Customer WHERE CustomerID = (SELECT CustomerID FROM Reservation WHERE ReservationID = 'R0001');

-- Feature 7: Get plane info by plane number
SELECT Make, Model, Year, LastRepairDate FROM Plane WHERE PlaneID = 'PL001';

-- Feature 8: List all repairs by technician ID
SELECT RepairID, PlaneID, RepairCode, RepairDate FROM Repair WHERE TechnicianID = 'T001';

-- Feature 9: List repairs for a plane in a date range
SELECT RepairID, RepairCode, RepairDate FROM Repair WHERE PlaneID = 'PL001' AND RepairDate BETWEEN '2025-04-03' AND '2025-04-09';

-- Feature 10: Flight statistics for a flight and date range
SELECT COUNT(CASE WHEN DepartedOnTime IS TRUE THEN 1 END) AS departed_on_time, COUNT(CASE WHEN ArrivedOnTime IS TRUE THEN 1 END) AS arrived_on_time, SUM(SeatsSold) AS Total_Sold_Seats, SUM(SeatsTotal - SeatsSold) AS Total_Unsold_Seats FROM FlightInstance WHERE FlightNumber = 'F100' AND FlightDate BETWEEN '5/5/25' AND '6/1/25';

-- Feature 11: Find all flights for a route and date
SELECT F.FlightNumber, S.DepartureTime, S.ArrivalTime, FI.NumOfStops, ROUND(100.0 * SUM(CASE WHEN FI.DepartedOnTime AND FI.ArrivedOnTime THEN 1 ELSE 0 END) / COUNT(*), 2) AS OnTimePercentage FROM Flight F JOIN Schedule S ON F.FlightNumber = S.FlightNumber JOIN FlightInstance FI ON F.FlightNumber = FI.FlightNumber WHERE F.DepartureCity = 'New York' AND F.ArrivalCity = 'Miami' AND FI.FlightDate = '5/5/25' GROUP BY F.FlightNumber, S.DepartureTime, S.ArrivalTime, FI.NumOfStops;

-- Feature 12: Find all ticket costs for a flight number
SELECT FlightDate, TicketCost FROM FlightInstance WHERE FlightNumber = 'F100';

-- Feature 13: Find airplane type for a flight number
SELECT P.Make, P.Model FROM Flight F JOIN Plane P ON F.PlaneID = P.PlaneID WHERE F.FlightNumber = 'F100';

-- Feature 14: Make a reservation (see Java logic for full process)
-- SELECT FlightInstanceID, SeatsTotal, SeatsSold FROM FlightInstance WHERE FlightNumber = 'F100' AND FlightDate = '5/5/25';
-- INSERT INTO Reservation (ReservationID, CustomerID, FlightInstanceID, Status) VALUES ('R0001', '1', '<flightInstanceID>', '<status>');

-- Feature 15: List repairs for a plane in a date range (maintenance staff)
SELECT RepairDate, RepairCode FROM Repair WHERE PlaneID = 'PL001' AND RepairDate BETWEEN '5/5/25' AND '6/1/25';

-- Feature 16: List maintenance requests by pilot ID
SELECT PlaneID, RepairCode, RequestDate FROM MaintenanceRequest WHERE PilotID = 'P001';

-- Feature 17: Log a repair (see Java logic for full process)
-- SELECT COALESCE(MAX(RepairID), 0) + 1 FROM Repair;
-- INSERT INTO Repair (RepairID, PlaneID, RepairCode, RepairDate, TechnicianID) VALUES ('<repairID>', 'PL001', 'RC001', '5/5/25', 'T001');

-- Feature 18: Make maintenance request (see Java logic for full process)
-- SELECT COALESCE(MAX(RequestID), 0) + 1 FROM MaintenanceRequest;
-- INSERT INTO MaintenanceRequest (RequestID, PlaneID, RepairCode, RequestDate, PilotID) VALUES ('<requestID>', 'PL001', 'RC001', '5/5/25', 'P001'); 