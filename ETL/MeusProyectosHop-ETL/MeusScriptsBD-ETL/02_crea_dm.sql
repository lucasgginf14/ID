DROP TABLE IF EXISTS public.fact_bookings;
DROP TABLE IF EXISTS public.fact_bookings_scd2;
DROP TABLE IF EXISTS public.dim_cruise;
DROP TABLE IF EXISTS public.dim_voyage;
DROP TABLE IF EXISTS public.dim_voyage_scd2;
DROP TABLE IF EXISTS public.dim_date;
DROP SEQUENCE IF EXISTS seq_cruise;
DROP SEQUENCE IF EXISTS seq_voyage;
DROP SEQUENCE IF EXISTS seq_voyage_scd2;


-----------------------------------------------------------------------


--Taboa dimensional temporal (mes)

CREATE TABLE public.dim_date (
    id_date numeric(8,0) PRIMARY KEY,
    day numeric(2,0),
    month numeric(2,0),
    month_name character varying(15),
    season character varying(10),
    year numeric(4,0)
);

--Enchemos a táboa temporal a man

INSERT INTO public.dim_date (id_date, day, month, month_name, season, year) VALUES
('01012025', 1,  1, 'Xaneiro',  'Inverno',   2025),
('02012025', 2,  1, 'Xaneiro',  'Inverno',   2025),
('11052025', 11, 5, 'Maio',     'Primavera', 2025),
('10122025', 10, 12, 'Decembro','Inverno',   2025),
('18122025', 18, 12, 'Decembro','Inverno',   2025),
('20122025', 20, 12, 'Decembro','Inverno',   2025),
('28122025', 28, 12, 'Decembro','Inverno',   2025),
('10012026', 10, 1, 'Xaneiro',  'Inverno',   2026),
('20012026', 20, 1, 'Xaneiro',  'Inverno',   2026),
('05032026', 5,  3, 'Marzo',    'Primavera', 2026),
('12032026', 12, 3, 'Marzo',    'Primavera', 2026),
('15052026', 15, 5, 'Maio',     'Primavera', 2026),
('25052026', 25, 5, 'Maio',     'Primavera', 2026),
('01112026', 1,  11, 'Novembro','Outono',    2026),
('12112026', 12, 11, 'Novembro','Outono',    2026),
('12022026', 12, 2, 'Febreiro', 'Inverno',   2026),
('18022026', 18, 2, 'Febreiro', 'Inverno',   2026),
('20022026', 20, 2, 'Febreiro', 'Inverno',   2026),
('28022026', 28, 2, 'Febreiro', 'Inverno',   2026);


-----------------------------------------------------------------------


--Taboa dimensional cruceiros (version para SCD1)

CREATE TABLE public.dim_cruise (
    id_cruise bigint PRIMARY KEY,
    code_cruise bigint,
    name character varying(100),
    manufacturing_year numeric(4),
    type character varying(50),
    description_type character varying(255)
);


-----------------------------------------------------------------------


--Taboa dimensional voyage (version para SCD1)

CREATE TABLE public.dim_voyage (
    id_voyage bigint PRIMARY KEY,
    code_voyage bigint,
    init_date date,
    end_date date,
    duration integer,
    zone character varying(50),
    price numeric (12,2)
);


--Taboa dimensional cruceiros (version para SCD2)

CREATE TABLE public.dim_voyage_scd2 (
    id_voyage bigint PRIMARY KEY,
    code_voyage bigint,
    init_date date,
    end_date date,
    duration integer,
    zone character varying(50),
    price numeric (12,2),
    valid_from date,
    valid_to date,
    num_version numeric(6,0),
    current_version boolean
);


-----------------------------------------------------------------------


--Taboa de feitos de reservas (version para usar en caso scd1)

CREATE TABLE public.fact_bookings (
    id_cruise bigint NOT NULL,
    id_date numeric(8, 0) NOT NULL,
    id_voyage bigint NOT NULL,
    total_bookings numeric(6,0),
    total_passengers numeric(6,0),
    total_amount numeric(12,2),
    PRIMARY KEY (id_cruise, id_date, id_voyage)
);


-----------------------------------------------------------------------


--Taboa de feitos de reservas (version para usar en caso scd2)

CREATE TABLE public.fact_bookings_scd2 (
    id_cruise bigint NOT NULL,
    id_date numeric(8, 0) NOT NULL,
    id_voyage bigint NOT NULL,
    total_bookings numeric(6,0),
    total_passengers numeric(6,0),
    total_amount numeric(12,2),
    PRIMARY KEY (id_cruise, id_date, id_voyage)
);


-----------------------------------------------------------------------


-- Adicional: Secuencias para producion de claves substitutas

CREATE SEQUENCE seq_cruise
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_voyage
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_voyage_scd2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;