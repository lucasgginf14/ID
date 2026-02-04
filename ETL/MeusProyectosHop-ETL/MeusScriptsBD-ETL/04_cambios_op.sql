--Simulamos estado da BD o dia 02/01/2025 despois de cambios 
-- acontecidos durante todo o día

--CARGA DE CRUCEIROS: Simulamos diferentes situacións porque esta táboa é utilizada para cargar dimension Cruceiro

-- CASO 1: Novo cruceiro dado de alta este mes, sen máis cambios durante o mes
INSERT INTO public.cruise (id, name, type, manufacturing_year, create_date, mod_date)
VALUES (6, 'Azure Horizon', 'themed', 2024, '2025-01-02', NULL);

-- CASO 2: Novo cruceiro dado de alta e modificado o mesmo día
INSERT INTO public.cruise (id, name, type, manufacturing_year, create_date, mod_date)
VALUES (7, 'Island Explorer', 'adventure', 2025, '2025-01-02', NULL);
-- Modificación posterior durante el mismo día (NON produce nova versión se SCD2)
UPDATE public.cruise
SET name = 'Island Explorer II', mod_date = '2025-01-02'
WHERE id = 7;

-- CASO 3: Cruceiro dado de alta ayer e modificado hoxe
-- (Ex.: id = 1 existía antes; agora actualizamos o nombre)
-- Esta actualización SI producía nova versión se aplicásemos SCD2
UPDATE public.cruise
SET name = 'Oceanic Adventure 1', mod_date = '2025-01-02'
WHERE id = 1;

-----------------------------------------

--CARGA DE PASAXEROS

--Rexistramos hoxe dous novos pasaxeros
INSERT INTO public.passenger (dni, fullname, email, country, age) VALUES
  ('P1000005', 'Ana Torres', 'ana.torres@example.com', 'Perú', 37),
  ('P1000006', 'Bruno Silva', 'bruno.silva@oldmail.com', 'Brasil', 29);

-----------------------------------------

--CARGA DE VIAXES: Simulamos diferentes situacións porque esta táboa é utilizada para cargar dimension Viaxe

-- CASO 1: Novo voyage dado de alta hoxe, sen máis cambios
INSERT INTO public.voyage (id, zone, init_date, end_date, price, create_date, mod_date)
VALUES (6, 'Baltic', '2026-02-20', '2026-02-28', 100, '2025-01-02', NULL);

-- CASO 2: Novo voyage dado de alta e modificado máis adiante no mes
INSERT INTO public.voyage (id, zone, init_date, end_date, price, create_date, mod_date)
VALUES (7, 'Aegean', '2026-02-12', '2026-02-18', 100, '2025-01-02', NULL);
-- Modificación posterior o mesmo día 
UPDATE public.voyage
SET zone = 'Indian', price = 99, mod_date = '2025-01-02'
WHERE id = 7;

-- CASO 3: Voyage creado ayer e modificado hoxe (producción de nova versión se fose SCD2sobreescribe a fila en SCD2)
UPDATE public.voyage
SET price = 120, mod_date = '2025-01-02'
WHERE id = 2;

-- CASO 4: Voyage creado ayer e modificado hoxe (sobreescribe a fila en SCD2)
UPDATE public.voyage
SET init_date = '2026-05-14', mod_date = '2025-01-02'
WHERE id = 3;

-----------------------------------------

--CARGA DE RESERVAS

--Rexistramos hoxe dúas novas reservas
INSERT INTO public.booking (id, amount, date, pay_passenger, cruise, voyage) VALUES
  (104, 1500.00, '2025-01-02', 'P1000005', 6, 6),
  (105, 980.50, '2025-01-02', 'P1000006', 7, 7);

-----------------------------------------

--CARGA DE RELACION CRUCEIRO_VIAXE

INSERT INTO public.cruise_voyage (cruise, voyage) VALUES
  (6, 6),
  (7, 7);

-----------------------------------------

--CARGA DE RELACION PASAXERO_RESERVA

INSERT INTO public.passenger_booking (passenger, booking) VALUES
  ('P1000001', 104),
  ('P1000002', 104),
  ('P1000005', 104),
  ('P1000006', 105),
  ('P1000003', 105);



