SET client_min_messages=ERROR;

\echo
\echo 'Creando bases de datos...'
-- Terminar conexiones activas

SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'ETLop' AND pid <> pg_backend_pid();

DROP DATABASE IF EXISTS "ETLop";
CREATE DATABASE "ETLop" OWNER myuser;

-- Terminar conexiones activas
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'ETLdm' AND pid <> pg_backend_pid();

DROP DATABASE IF EXISTS "ETLdm";
CREATE DATABASE "ETLdm" OWNER myuser;

\echo 'Bases de datos creadas'
\echo


