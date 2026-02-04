package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cruceiro;
import java.util.List;

public interface CruceiroDao {

    void setup(Configuracion config);

    Cruceiro recuperaPorCodigo(String codigo);

    Cruceiro almacena(Cruceiro cruceiro);

    void elimina(Cruceiro cruceiro);

    Cruceiro modifica(Cruceiro cruceiro);

    List<Cruceiro> buscarCruceirosPorPorto(String puerto);

}