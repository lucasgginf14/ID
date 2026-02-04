psql -U postgres -d postgres -f 00_crear_BDs.sql

psql -U myuser   -d ETLop -f 01_crea_op.sql

psql -U myuser   -d ETLdm -f 02_crea_dm.sql

psql -U myuser   -d ETLop -f 03_inicializa_op.sql




