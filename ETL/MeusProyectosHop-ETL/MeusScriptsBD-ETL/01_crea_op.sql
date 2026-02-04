-- ============================================================
-- CREACIÓN DE TABLAS SEGÚN EL DIAGRAMA E/R DE CRUCEROS (PostgreSQL)
-- ============================================================

-- Eliminamos tablas si ya existen
DROP TABLE IF EXISTS public.cruise_voyage;
DROP TABLE IF EXISTS public.passenger_booking;
DROP TABLE IF EXISTS public.booking;
DROP TABLE IF EXISTS public.passenger;
DROP TABLE IF EXISTS public.cruise;
DROP TABLE IF EXISTS public.voyage;

---------------------------------------------------------------
-- TABLA: passenger
---------------------------------------------------------------
CREATE TABLE public.passenger (
    dni character varying(10) PRIMARY KEY,
    fullname character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    country character varying(50),
    age numeric(3)
);

---------------------------------------------------------------
-- TABLA: cruise
---------------------------------------------------------------
CREATE TABLE public.cruise (
    id bigint PRIMARY KEY,
    name character varying(100) NOT NULL,
    type character varying(50),
    manufacturing_year numeric(4) NOT NULL,
    create_date date NOT NULL,
    mod_date date
);

---------------------------------------------------------------
-- TABLA: voyage
---------------------------------------------------------------
CREATE TABLE public.voyage (
    id bigint PRIMARY KEY,
    zone character varying(50) NOT NULL,
    init_date date NOT NULL,
    end_date date NOT NULL,
    price numeric (12,2),
    create_date date NOT NULL,
    mod_date date
);

---------------------------------------------------------------
-- TABLA: booking
---------------------------------------------------------------
CREATE TABLE public.booking (
    id bigint PRIMARY KEY,
    amount numeric(10,2) NOT NULL,
    date date NOT NULL,
    pay_passenger character varying(10) NOT NULL REFERENCES public.passenger(dni),
    cruise bigint NOT NULL REFERENCES public.cruise(id),
    voyage bigint NOT NULL REFERENCES public.voyage(id)
);

---------------------------------------------------------------
-- RELACIÓN: have (Passenger tiene Booking)
---------------------------------------------------------------
CREATE TABLE public.passenger_booking (
    passenger character varying(10) NOT NULL REFERENCES public.passenger(dni),
    booking bigint NOT NULL REFERENCES public.booking(id),
    PRIMARY KEY (passenger, booking)
);

---------------------------------------------------------------
-- RELACIÓN: use (Cruise usa Voyage)
---------------------------------------------------------------
CREATE TABLE public.cruise_voyage (
    cruise bigint NOT NULL REFERENCES public.cruise(id),
    voyage bigint NOT NULL REFERENCES public.voyage(id),
    PRIMARY KEY (cruise, voyage)
);

---------------------------------------------------------------
-- ASIGNACIÓN DE PROPIETARIO
---------------------------------------------------------------
ALTER TABLE public.passenger
    OWNER TO myuser;

ALTER TABLE public.cruise
    OWNER TO myuser;

ALTER TABLE public.voyage
    OWNER TO myuser;

ALTER TABLE public.booking
    OWNER TO myuser;

ALTER TABLE public.passenger_booking
    OWNER TO myuser;

ALTER TABLE public.cruise_voyage
    OWNER TO myuser;
