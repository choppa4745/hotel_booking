CREATE OR REPLACE FUNCTION calculate_booking_price(
    p_room_id INTEGER,
    p_check_in_date DATE,
    p_check_out_date DATE
) RETURNS DECIMAL(10,2) AS $$
DECLARE
v_nights INTEGER;
    v_base_price DECIMAL(10,2);
    v_total_price DECIMAL(10,2);
    v_season_multiplier DECIMAL(3,2) := 1.0;
BEGIN
    v_nights := p_check_out_date - p_check_in_date;

    IF v_nights <= 0 THEN
        RAISE EXCEPTION 'Invalid date range';
END IF;

SELECT base_price INTO v_base_price
FROM rooms r
         JOIN room_types rt ON r.type_id = rt.type_id
WHERE r.room_id = p_room_id;

IF EXTRACT(MONTH FROM p_check_in_date) IN (6,7,8,12) THEN
        v_season_multiplier := 1.2;
    ELSIF EXTRACT(MONTH FROM p_check_in_date) IN (1,2,11) THEN
        v_season_multiplier := 0.8;
END IF;

    IF v_nights >= 7 THEN
        v_season_multiplier := v_season_multiplier * 0.9;
    ELSIF v_nights >= 3 THEN
        v_season_multiplier := v_season_multiplier * 0.95;
END IF;

    v_total_price := v_base_price * v_nights * v_season_multiplier;
    RETURN v_total_price;
END; $$ LANGUAGE plpgsql;