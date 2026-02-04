package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Persoa;

public interface PersoaDao {

    void setup(Configuracion config);

    Persoa recuperaPorDni(String dni);

    Persoa almacena(Persoa persoa);

    void elimina(Persoa persoa);

    Persoa modifica(Persoa persoa);

    Persoa restauraViaxes(Persoa persoa);
}