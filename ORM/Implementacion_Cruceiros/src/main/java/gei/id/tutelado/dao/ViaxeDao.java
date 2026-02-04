package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cruceiro;
import gei.id.tutelado.model.Viaxe;

import java.util.List;

public interface ViaxeDao {

    void setup(Configuracion config);

    Viaxe recuperaPorCodigo(String codigo);

    Viaxe almacena(Viaxe viaxe);

    void elimina(Viaxe viaxe);

    Viaxe modifica(Viaxe viaxe);

    List<Viaxe> buscarViaxesPorCruceiro(Cruceiro cruceiro);

    Long contarPersoasEnViaxe(Viaxe viaxe);

    List<Viaxe> buscarViaxesSenCruceiro();
}