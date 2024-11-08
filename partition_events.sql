-- Drop the main table and its partitions if they exist
DROP TABLE IF EXISTS events CASCADE;

-- Create the main events table partitioned by event_date_time
CREATE TABLE events (
                        id BIGINT,
                        event_name VARCHAR(255),
                        event_date_time TIMESTAMP,
                        is_paid BOOLEAN,
                        price DECIMAL,
                        available_seats INT,
                        event_type_id BIGINT,
                        organizer_id BIGINT,
                        PRIMARY KEY (id, event_date_time) -- Include event_date_time in the primary key for partitioning
) PARTITION BY RANGE (event_date_time);

-- Create partitions for specific date ranges
CREATE TABLE events_2024 PARTITION OF events
    FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');

CREATE TABLE events_2025 PARTITION OF events
    FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');

-- Sample Data Inserts to Test Partitioning
-- Insert record into events_2024 partition
INSERT INTO events (id, event_name, event_date_time, is_paid, price, available_seats, event_type_id, organizer_id)
VALUES (1, 'Concert A', '2024-06-15 19:30:00', true, 50.0, 100, 4, 1);

-- Insert record into events_2025 partition
INSERT INTO events (id, event_name, event_date_time, is_paid, price, available_seats, event_type_id, organizer_id)
VALUES (2, 'Concert B', '2025-03-10 20:00:00', false, 30.0, 150, 4, 2);

-- Queries to Verify Partitioning
-- Query to check data in events_2024 partition
SELECT * FROM events_2024;

-- Query to check data in events_2025 partition
SELECT * FROM events_2025;

-- Query to check data in the main events table (should return results from both partitions)
SELECT * FROM events;