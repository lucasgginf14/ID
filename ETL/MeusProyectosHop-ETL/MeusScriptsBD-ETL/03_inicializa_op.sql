--Simulamos estado da BD o dia 01/01/2025


--CARGA DE CRUCEIROS: Simulamos diferentes situacións porque esta táboa é utilizada para cargar dimension Cruceiro

--CASO 1: Fila nova, dada de alta hoxe, e que non se volve a modificar
INSERT INTO public.cruise (id, name, type, manufacturing_year, create_date, mod_date) VALUES
  (1, 'Oceanic Adventure', 'adventure', 2015, '2025-01-01', NULL),
  (2, 'Sunset Explorer', 'family', 2018, '2025-01-01', NULL),
  (3, 'Coral Voyager', 'luxury', 2020, '2025-01-01', NULL),
  (4, 'Polar Pioneer', 'adventure', 2012, '2025-01-01', NULL);

--CASO 2: Fila nova, dada de alta hoxe, e que no mesmo día é modificada
INSERT INTO public.cruise (id, name, type, manufacturing_year, create_date, mod_date) VALUES
  (5, 'Mediterranean date', 'single', 2016, '2025-01-01', NULL);
UPDATE public.cruise SET name = 'Mediterranean family', type = 'family', mod_date = '2025-01-01' where id = 5;

-----------------------------------------

--CARGA DE PASAXEROS

INSERT INTO public.passenger (dni, fullname, email, country, age) VALUES
  ('P1000001', 'Maria Lopez', 'maria.lopez@example.com', 'Espana', 34),
  ('P1000002', 'Carlos Garcia', 'carlos.garcia@example.com', 'Mexico', 41),
  ('P1000003', 'Lucia Fernandez', 'lucia.fernandez@example.com', 'Argentina', 29),
  ('P1000004', 'Miguel Rodriguez', 'miguel.rodriguez@example.com', 'Chile', 52);

-----------------------------------------

--CARGA DE VIAXES: Simulamos diferentes situacións porque esta táboa é utilizada para cargar dimensión Viaxe

--CASO 1: Filas novas, dadas de alta hoxe, e que non se volven a modificar
INSERT INTO public.voyage (id, zone, init_date, end_date, price, create_date, mod_date) VALUES
  (1, 'Caribbean', '2026-01-10', '2026-01-20', 100, '2025-01-01', NULL),
  (2, 'Mediterranean', '2026-03-05', '2026-03-12', 100, '2025-01-01', NULL),
  (3, 'South Pacific', '2026-05-15', '2026-05-25', 100, '2025-01-01', NULL),
  (4, 'Antarctica', '2026-11-01', '2026-11-12', 100, '2025-01-01', NULL);

--CASO 2: Fila nova, dada de alta hoxe, e que no mesmo día é modificada
INSERT INTO public.voyage (id, zone, init_date, end_date, price, create_date, mod_date) VALUES
  (5, 'Polar', '2025-12-10', '2025-12-20', 100, '2025-01-01', NULL);
UPDATE public.voyage SET init_date = '2025-12-18', end_date = '2025-12-28', price = 99, mod_date ='2025-01-01' where id = 5;

-----------------------------------------

--CARGA DE RESERVAS

--Casos contemplados: Grupo con fila única (1 viaxe, 1 cruceiro, 1 reserva), grupo con varias filas (1 viaxe, 1 cruceiro, 3 reserva)

INSERT INTO public.booking (id, amount, date, pay_passenger, cruise, voyage) VALUES
  (100, 1200.00, '2025-01-01', 'P1000001', 1, 1),
  (101, 2300.50, '2025-01-01', 'P1000002', 4, 5),
  (102, 4500.00, '2025-01-01', 'P1000003', 4, 5),
  (103, 5500.75, '2025-01-01', 'P1000004', 4, 5);

-----------------------------------------

--CARGA DE RELACION CRUCEIRO_VIAXE

INSERT INTO public.cruise_voyage (cruise, voyage) VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (4, 5),
  (5, 5);

-----------------------------------------

--CARGA DE RELACION PASAXERO_RESERVA

INSERT INTO public.passenger_booking (passenger, booking) VALUES
  ('P1000001', 100),
  ('P1000002', 101),
  ('P1000003', 102),
  ('P1000004', 103),
  ('P1000001', 103);


