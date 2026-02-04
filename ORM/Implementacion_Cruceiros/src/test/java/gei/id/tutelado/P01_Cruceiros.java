package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.CruceiroDao;
import gei.id.tutelado.dao.CruceiroDaoJPA;
import gei.id.tutelado.dao.ViaxeDao;
import gei.id.tutelado.dao.ViaxeDaoJPA;
import gei.id.tutelado.model.Cruceiro;
import gei.id.tutelado.model.Viaxe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P01_Cruceiros {

    private final Logger log = LogManager.getLogger("gei.id.tutelado");

    private static final ProductorDatosProba productorDatos = new ProductorDatosProba();

    private static Configuracion cfg;
    private static CruceiroDao cruceiroDao;
    private static ViaxeDao viaxeDao;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            log.info("");
            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            log.info("Iniciando test: " + description.getMethodName());
            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }

        protected void finished(Description description) {
            log.info("");
            log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
            log.info("Finalizado test: " + description.getMethodName());
            log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
        }
    };

    @BeforeClass
    public static void init() throws Exception {
        cfg = new ConfiguracionJPA();
        cfg.start();

        cruceiroDao = new CruceiroDaoJPA();
        cruceiroDao.setup(cfg);

        viaxeDao = new ViaxeDaoJPA();
        viaxeDao.setup(cfg);

        productorDatos.setup(cfg);
    }

    @AfterClass
    public static void endclose() throws Exception {
        cfg.endUp();
    }

    @Before
    public void setUp() throws Exception {
        log.info("");
        log.info("Limpando BD --------------------------------------------------------------------------------------------");
        productorDatos.limpaBD();
    }

    @After
    public void tearDown() throws Exception {
        log.info("");
        log.info("Limpando BD --------------------------------------------------------------------------------------------");
        productorDatos.limpaBD();
    }


    @Test
    public void test01_Recuperacion() {
        Cruceiro c;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaCrucerosSoltos();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de recuperación por clave natural (codigo)\n");

        log.info("Probando recuperación por código EXISTENTE --------------------------------------------------");
        c = cruceiroDao.recuperaPorCodigo(productorDatos.c1.getCodigo());

        Assert.assertNotNull(c);
        Assert.assertEquals(productorDatos.c1.getCodigo(), c.getCodigo());
        Assert.assertEquals(productorDatos.c1.getNome(), c.getNome());
        Assert.assertEquals(productorDatos.c1.getCapacidade(), c.getCapacidade());


        log.info("");
        log.info("Probando recuperación por código INEXISTENTE -----------------------------------------------");
        c = cruceiroDao.recuperaPorCodigo("CODIGO_FALSO_123");
        Assert.assertNull(c);
    }


    @Test
    public void test02_Alta() {
        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaCrucerosSoltos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de gravación na BD (persist)\n");

        Assert.assertNull(productorDatos.c1.getId());

        cruceiroDao.almacena(productorDatos.c1);

        Assert.assertNotNull(productorDatos.c1.getId());
    }

    @Test
    public void test03_Eliminacion() {
        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaCrucerosSoltos();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de eliminación da BD (remove)\n");

        Assert.assertNotNull(cruceiroDao.recuperaPorCodigo(productorDatos.c1.getCodigo()));

        cruceiroDao.elimina(productorDatos.c1);

        Assert.assertNull(cruceiroDao.recuperaPorCodigo(productorDatos.c1.getCodigo()));
    }

    @Test
    public void test04_Modificacion() {
        Cruceiro c1_recuperado, c2_recuperado;
        String novoNome = "Nome Modificado";

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaCrucerosSoltos();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de modificación (merge)\n");

        c1_recuperado = cruceiroDao.recuperaPorCodigo(productorDatos.c1.getCodigo());

        Assert.assertNotEquals(novoNome, c1_recuperado.getNome());

        c1_recuperado.setNome(novoNome);

        cruceiroDao.modifica(c1_recuperado);

        c2_recuperado = cruceiroDao.recuperaPorCodigo(productorDatos.c1.getCodigo());

        Assert.assertEquals(novoNome, c2_recuperado.getNome());
    }

    @Test
    public void test07_CascadePersist() {
        productorDatos.creaCrucerosSoltos();
        Cruceiro c = productorDatos.c1;

        Viaxe v = new Viaxe();
        v.setCodigo("V-2025-B");
        v.setDataInicio(LocalDate.now().plusMonths(1));
        v.setDataFin(LocalDate.now().plusMonths(2));
        v.setItinerarioPortos(new ArrayList<>(List.of("Barcelona", "Marsella", "Napoles")));
        v.setCruceiro(c);
        c.getViaxes().add(v);

        Assert.assertNull(v.getId());

        cruceiroDao.almacena(c);

        Cruceiro recuperado = cruceiroDao.recuperaPorCodigo(c.getCodigo());
        Assert.assertNotNull(recuperado);
        Viaxe vRecuperado = viaxeDao.recuperaPorCodigo(v.getCodigo());
        Assert.assertNotNull(vRecuperado);

    }
}