package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.CruceiroDao;
import gei.id.tutelado.dao.CruceiroDaoJPA;
import gei.id.tutelado.dao.PersoaDao;
import gei.id.tutelado.dao.PersoaDaoJPA;
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
import java.util.Objects;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P04_Consultas {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProductorDatosProba productorDatos = new ProductorDatosProba();

    private static Configuracion cfg;
    private static PersoaDao persoaDao;
    private static ViaxeDao viaxeDao;
    private static CruceiroDao cruceiroDao;

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
    public void test08a_INNER_JOIN() {
        List<Viaxe> viaxes;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaGrafoCompleto();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da consulta JPQL con INNER JOIN (M04.6.a)\n");


        log.info("Probando cruceiro con viaxes (c1)...");
        viaxes = viaxeDao.buscarViaxesPorCruceiro(productorDatos.c1);
        Assert.assertEquals(2, viaxes.size());
        Assert.assertTrue(viaxes.contains(productorDatos.v1));
        Assert.assertTrue(viaxes.contains(productorDatos.v2));


        log.info("Probando cruceiro sen viaxes (c2)...");
        viaxes = viaxeDao.buscarViaxesPorCruceiro(productorDatos.c2);
        Assert.assertEquals(0, viaxes.size());
    }

    @Test
    public void test08b_OUTER_JOIN() {
        List<Viaxe> viaxes;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaGrafoCompleto();
        productorDatos.gravaDatos();

        Viaxe v3 = new Viaxe();
        v3.setCodigo("V-2025-ZZZ");
        v3.setDataInicio(LocalDate.now().plusMonths(1));
        v3.setDataFin(LocalDate.now().plusMonths(2));
        v3.setItinerarioPortos(new ArrayList<>(List.of("Barcelona", "Marsella", "Napoles")));
        viaxeDao.almacena(v3);

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da consulta JPQL con OUTER JOIN (M04.6.b)\n");

        log.info("Buscando viaxes sen cruceiro...");
        viaxes = viaxeDao.buscarViaxesSenCruceiro();
        Assert.assertEquals(1, viaxes.size());
        Assert.assertTrue(viaxes.contains(v3));
        Assert.assertNull(
                Objects.requireNonNull(viaxes
                                .stream()
                                .filter(v -> v.getCodigo().equals("V-2025-ZZZ"))
                                .findFirst().orElse(null))
                        .getCruceiro());
    }

    @Test
    public void test08c_SUBCONSULTA() {
        List<Cruceiro> cruceiros;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaGrafoCompleto();
        productorDatos.gravaDatos();


        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da consulta JPQL con SUBCONSULTA (M04.6.c)\n");


        log.info("Buscando cruceiros que pasan por 'Vigo'...");
        cruceiros = cruceiroDao.buscarCruceirosPorPorto("Vigo");
        Assert.assertEquals(1, cruceiros.size());
        Assert.assertEquals(productorDatos.c1, cruceiros.get(0));

        log.info("Buscando cruceiros que pasan por 'Marsella'...");
        cruceiros = cruceiroDao.buscarCruceirosPorPorto("Marsella");
        Assert.assertEquals(1, cruceiros.size());
        Assert.assertEquals(productorDatos.c1, cruceiros.get(0));

        log.info("Buscando cruceiros que pasan por 'Miami'...");
        cruceiros = cruceiroDao.buscarCruceirosPorPorto("Miami");
        Assert.assertEquals(0, cruceiros.size());
    }

    @Test
    public void test08d_AGREGACION() {
        Long numeroPersoas;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaGrafoCompleto();
        productorDatos.gravaDatos();

        log.info("");
        log.info("Inicio do test --------------------------------------------------------------------------------------------------");
        log.info("Obxectivo: Proba da consulta JPQL con AGREGACIÓN COUNT (M04.6.d)\n");

        log.info("Contando persoas na viaxe v1...");
        numeroPersoas = viaxeDao.contarPersoasEnViaxe(productorDatos.v1);
        Assert.assertEquals(Long.valueOf(2), numeroPersoas);

        log.info("Contando persoas na viaxe v2...");
        numeroPersoas = viaxeDao.contarPersoasEnViaxe(productorDatos.v2);
        Assert.assertEquals(Long.valueOf(1), numeroPersoas);
    }
}