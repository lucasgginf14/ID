package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.CruceiroDao;
import gei.id.tutelado.dao.CruceiroDaoJPA;
import gei.id.tutelado.dao.PersoaDao;
import gei.id.tutelado.dao.PersoaDaoJPA;
import gei.id.tutelado.dao.ViaxeDao;
import gei.id.tutelado.dao.ViaxeDaoJPA;
import gei.id.tutelado.model.Pasaxero;
import gei.id.tutelado.model.Persoa;
import gei.id.tutelado.model.Tripulante;
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
public class P02_Persoas {

    private final Logger log = LogManager.getLogger("gei.id.tutelado");

    private static final ProductorDatosProba productorDatos = new ProductorDatosProba();

    private static Configuracion cfg;
    private static PersoaDao persoaDao;
    private static ViaxeDao viaxeDao;
    private static CruceiroDao cruceiroDao;

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

        persoaDao = new PersoaDaoJPA();
        persoaDao.setup(cfg);

        viaxeDao = new ViaxeDaoJPA();
        viaxeDao.setup(cfg);

        cruceiroDao = new CruceiroDaoJPA();
        cruceiroDao.setup(cfg);

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
        Pasaxero pRecuperado;
        Tripulante tRecuperado;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaPersonasSoltas();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de recuperación de subclases (Pasaxero, Tripulante) por DNI\n");

        pRecuperado = (Pasaxero) persoaDao.recuperaPorDni(productorDatos.pas1.getDni());
        Assert.assertNotNull(pRecuperado);
        Assert.assertEquals(productorDatos.pas1.getNome(), pRecuperado.getNome());
        Assert.assertEquals(productorDatos.pas1.getNumPasaporte(), pRecuperado.getNumPasaporte());
        Assert.assertEquals(productorDatos.pas1.getNacionalidade(), pRecuperado.getNacionalidade());

        tRecuperado = (Tripulante) persoaDao.recuperaPorDni(productorDatos.tri1.getDni());
        Assert.assertNotNull(tRecuperado);
        Assert.assertEquals(productorDatos.tri1.getNome(), tRecuperado.getNome());
        Assert.assertEquals(productorDatos.tri1.getRango(), tRecuperado.getRango());
        Assert.assertEquals(productorDatos.tri1.getDepartamento(), tRecuperado.getDepartamento());

        Assert.assertNull(persoaDao.recuperaPorDni("DNI_FALSO"));
    }

    @Test
    public void test02_Alta() {
        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaPersonasSoltas();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de gravación de subtipos (Pasaxero, Tripulante)\n");

        Assert.assertNull(productorDatos.pas1.getId());
        Assert.assertNull(productorDatos.tri1.getId());

        persoaDao.almacena(productorDatos.pas1);
        persoaDao.almacena(productorDatos.tri1);

        Assert.assertNotNull(productorDatos.pas1.getId());
        Assert.assertNotNull(productorDatos.tri1.getId());
    }

    @Test
    public void test03_Eliminacion() {
        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaPersonasSoltas();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de eliminación de subtipos\n");

        Assert.assertNotNull(persoaDao.recuperaPorDni(productorDatos.pas1.getDni()));
        persoaDao.elimina(productorDatos.pas1);
        Assert.assertNull(persoaDao.recuperaPorDni(productorDatos.pas1.getDni()));
    }

    @Test
    public void test04_Modificacion() {
        Tripulante t1, t2;
        String nuevoRango = "Comandante Supremo";

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaPersonasSoltas();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de modificación de atributos propios de subclase\n");

        t1 = (Tripulante) persoaDao.recuperaPorDni(productorDatos.tri1.getDni());

        t1.setRango(nuevoRango);
        persoaDao.modifica(t1);

        t2 = (Tripulante) persoaDao.recuperaPorDni(productorDatos.tri1.getDni());
        Assert.assertEquals(nuevoRango, t2.getRango());
    }

    @Test
    public void test05_LAZY() {
        Persoa p;
        boolean excepcionLanzada = false;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaGrafoCompleto();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba de acceso LAZY e restauración (inicialización forzada)\n");

        p = persoaDao.recuperaPorDni(productorDatos.pas1.getDni());

        log.info("Probando acceso LAZY fallido (esperase excepción)...");
        try {
            p.getViaxes().size();
        } catch (LazyInitializationException ex) {
            excepcionLanzada = true;
            log.info("Excepción capturada correctamente: {}", ex.getClass().getName());
        }
        Assert.assertTrue(excepcionLanzada);

        log.info("Probando restauración da colección...");
        p = persoaDao.restauraViaxes(p);

        Assert.assertEquals(1, p.getViaxes().size());
        log.info("Acceso correcto tras restauración. Viaxes: {}", p.getViaxes().size());
    }
}