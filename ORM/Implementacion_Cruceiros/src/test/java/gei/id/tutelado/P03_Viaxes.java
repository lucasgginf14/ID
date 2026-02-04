package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.ViaxeDao;
import gei.id.tutelado.dao.ViaxeDaoJPA;
import gei.id.tutelado.model.Viaxe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P03_Viaxes {

    private final Logger log = LogManager.getLogger("gei.id.tutelado");

    private static final ProductorDatosProba productorDatos = new ProductorDatosProba();

    private static Configuracion cfg;
    private static ViaxeDao viaxeDao;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            log.info("");
            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            log.info("Iniciando test: {}", description.getMethodName());
            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }

        protected void finished(Description description) {
            log.info("");
            log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
            log.info("Finalizado test: {}", description.getMethodName());
            log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
        }
    };

    @BeforeClass
    public static void init() throws Exception {
        cfg = new ConfiguracionJPA();
        cfg.start();

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
        Viaxe vRecuperado;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaViaxesSoltos();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de recuperación de Viaxe por código\n");

        vRecuperado = viaxeDao.recuperaPorCodigo(productorDatos.v1.getCodigo());
        Assert.assertNotNull(vRecuperado);
        Assert.assertEquals(productorDatos.v1.getCodigo(), vRecuperado.getCodigo());
        Assert.assertEquals(productorDatos.v1.getDataFin(), vRecuperado.getDataFin());

        Assert.assertNull(viaxeDao.recuperaPorCodigo("CODIGO_FALSO"));
    }

    @Test
    public void test02_Alta() {
        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaViaxesSoltos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de gravación de Viaxe\n");

        Assert.assertNull(productorDatos.v1.getId());

        viaxeDao.almacena(productorDatos.v1);

        Assert.assertNotNull(productorDatos.v1.getId());
    }

    @Test
    public void test03_Eliminacion() {
        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaViaxesSoltos();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de eliminación de Viaxe\n");

        Assert.assertNotNull(viaxeDao.recuperaPorCodigo(productorDatos.v1.getCodigo()));
        viaxeDao.elimina(productorDatos.v1);
        Assert.assertNull(viaxeDao.recuperaPorCodigo(productorDatos.v1.getCodigo()));
    }


    @Test
    public void test04_Modificacion() {
        Viaxe v1, v2;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaViaxesSoltos();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de modificación de atributos de Viaxe\n");

        v1 = viaxeDao.recuperaPorCodigo(productorDatos.v1.getCodigo());
        Assert.assertNotNull(v1);

        v1.setDataFin(v1.getDataFin().plusDays(10));
        viaxeDao.modifica(v1);

        v2 = viaxeDao.recuperaPorCodigo(productorDatos.v1.getCodigo());
        Assert.assertEquals(v1.getDataFin(), v2.getDataFin());
    }

    @Test
    public void test06_EAGER() {
        Viaxe v;
        boolean excepcionLanzada = false;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaGrafoCompleto();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de acceso EAGER (Viaxe -> Portos)\n");

        v = viaxeDao.recuperaPorCodigo(productorDatos.v1.getCodigo());

        log.info("Probando acceso EAGER fora de sesión...");
        try {
            Assert.assertNotNull(v.getItinerarioPortos());
            Assert.assertEquals(3, v.getItinerarioPortos().size());
        } catch (LazyInitializationException ex) {
            excepcionLanzada = true;
        }

        Assert.assertFalse(excepcionLanzada);
    }
}
