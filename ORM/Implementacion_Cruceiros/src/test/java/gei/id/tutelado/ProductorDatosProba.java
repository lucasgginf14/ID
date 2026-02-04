package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cruceiro;
import gei.id.tutelado.model.Pasaxero;
import gei.id.tutelado.model.Persoa;
import gei.id.tutelado.model.Tripulante;
import gei.id.tutelado.model.Viaxe;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductorDatosProba {

    private EntityManagerFactory emf = null;

    public Cruceiro c1, c2;
    public Viaxe v1, v2;
    public Pasaxero pas1, pas2;
    public Tripulante tri1;

    public List<Cruceiro> listaCruceiros;
    public List<Viaxe> listaViaxes;
    public List<Persoa> listaPersoas;

    public void setup(Configuracion config) {
        this.emf = (EntityManagerFactory) config.get("EMF");
    }

    public void limpiarAnteriores() {
        this.listaCruceiros = null;
        this.listaViaxes = null;
        this.listaPersoas = null;
        this.c1 = null;
        this.c2 = null;
        this.v1 = null;
        this.v2 = null;
        this.pas1 = null;
        this.pas2 = null;
        this.tri1 = null;
    }

    public void creaCrucerosSoltos() {
        limpiarAnteriores();

        this.c1 = new Cruceiro();
        this.c1.setCodigo("C-001");
        this.c1.setNome("Costa Concordia");
        this.c1.setCapacidade(3000);

        this.c2 = new Cruceiro();
        this.c2.setCodigo("C-002");
        this.c2.setNome("Queen Mary 2");
        this.c2.setCapacidade(2500);

        this.listaCruceiros = new ArrayList<>();
        this.listaCruceiros.add(c1);
        this.listaCruceiros.add(c2);
    }

    public void creaViaxesSoltos() {
        limpiarAnteriores();

        this.v1 = new Viaxe();
        this.v1.setCodigo("V-2025-A");
        this.v1.setDataInicio(LocalDate.now().plusDays(10));
        this.v1.setDataFin(LocalDate.now().plusDays(20));
        this.v1.setItinerarioPortos(new ArrayList<>(List.of("Vigo", "Lisboa", "Casablanca")));

        this.v2 = new Viaxe();
        this.v2.setCodigo("V-2025-B");
        this.v2.setDataInicio(LocalDate.now().plusMonths(1));
        this.v2.setDataFin(LocalDate.now().plusMonths(2));
        this.v2.setItinerarioPortos(new ArrayList<>(List.of("Barcelona", "Marsella", "Napoles")));

        this.listaViaxes = new ArrayList<>();
        this.listaViaxes.add(v1);
        this.listaViaxes.add(v2);
    }

    public void creaPersonasSoltas() {
        limpiarAnteriores();

        this.pas1 = new Pasaxero();
        this.pas1.setDni("11111111A");
        this.pas1.setNome("Juan Pasaxero");
        this.pas1.setDataNacemento(LocalDate.of(1990, 5, 20));
        this.pas1.setNumPasaporte("P-111");
        this.pas1.setNacionalidade("Española");

        this.pas2 = new Pasaxero();
        this.pas2.setDni("22222222B");
        this.pas2.setNome("Maria Turista");
        this.pas2.setDataNacemento(LocalDate.of(1985, 3, 15));
        this.pas2.setNumPasaporte("P-222");
        this.pas2.setNacionalidade("Portuguesa");

        this.tri1 = new Tripulante();
        this.tri1.setDni("33333333C");
        this.tri1.setNome("Pedro Capitan");
        this.tri1.setDataNacemento(LocalDate.of(1975, 11, 2));
        this.tri1.setRango("Capitán");
        this.tri1.setDepartamento("Ponte de Mando");

        this.listaPersoas = new ArrayList<>();
        this.listaPersoas.add(pas1);
        this.listaPersoas.add(pas2);
        this.listaPersoas.add(tri1);
    }

    public void creaGrafoCompleto() {
        limpiarAnteriores();

        this.c1 = new Cruceiro();
        this.c1.setCodigo("C-001");
        this.c1.setNome("Costa Concordia");
        this.c1.setCapacidade(3000);
        this.c2 = new Cruceiro();
        this.c2.setCodigo("C-002");
        this.c2.setNome("Queen Mary 2");
        this.c2.setCapacidade(2500);
        this.listaCruceiros = new ArrayList<>();
        this.listaCruceiros.add(c1);
        this.listaCruceiros.add(c2);

        this.v1 = new Viaxe();
        this.v1.setCodigo("V-2025-A");
        this.v1.setDataInicio(LocalDate.now().plusDays(10));
        this.v1.setDataFin(LocalDate.now().plusDays(20));
        this.v1.setItinerarioPortos(new ArrayList<>(List.of("Vigo", "Lisboa", "Casablanca")));
        this.v2 = new Viaxe();
        this.v2.setCodigo("V-2025-B");
        this.v2.setDataInicio(LocalDate.now().plusMonths(1));
        this.v2.setDataFin(LocalDate.now().plusMonths(2));
        this.v2.setItinerarioPortos(new ArrayList<>(List.of("Barcelona", "Marsella", "Napoles")));
        this.listaViaxes = new ArrayList<>();
        this.listaViaxes.add(v1);
        this.listaViaxes.add(v2);

        this.pas1 = new Pasaxero();
        this.pas1.setDni("11111111A");
        this.pas1.setNome("Juan Pasaxero");
        this.pas1.setDataNacemento(LocalDate.of(1990, 5, 20));
        this.pas1.setNumPasaporte("P-111");
        this.pas1.setNacionalidade("Española");
        this.pas2 = new Pasaxero();
        this.pas2.setDni("22222222B");
        this.pas2.setNome("Maria Turista");
        this.pas2.setDataNacemento(LocalDate.of(1985, 3, 15));
        this.pas2.setNumPasaporte("P-222");
        this.pas2.setNacionalidade("Portuguesa");
        this.tri1 = new Tripulante();
        this.tri1.setDni("33333333C");
        this.tri1.setNome("Pedro Capitan");
        this.tri1.setDataNacemento(LocalDate.of(1975, 11, 2));
        this.tri1.setRango("Capitán");
        this.tri1.setDepartamento("Ponte de Mando");
        this.listaPersoas = new ArrayList<>();
        this.listaPersoas.add(pas1);
        this.listaPersoas.add(pas2);
        this.listaPersoas.add(tri1);

        this.v1.setCruceiro(this.c1);
        this.c1.getViaxes().add(this.v1);

        this.v1.getPersoas().add(this.pas1);
        this.pas1.getViaxes().add(this.v1);

        this.v1.getPersoas().add(this.tri1);
        this.tri1.getViaxes().add(this.v1);

        this.v2.getPersoas().add(this.pas2);
        this.pas2.getViaxes().add(this.v2);

        this.v2.setCruceiro(this.c1);
        this.c1.getViaxes().add(this.v2);
    }

    public void gravaDatos() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            if (listaPersoas != null) {
                for (Persoa p : listaPersoas) {
                    if (p.getId() == null) em.persist(p);
                    else em.merge(p);
                }
            }

            if (listaCruceiros != null) {
                for (Cruceiro c : listaCruceiros) {
                    if (c.getId() == null) em.persist(c);
                    else em.merge(c);
                }
            }

            if (listaViaxes != null) {
                for (Viaxe v : listaViaxes) {
                    if (v.getId() == null) em.persist(v);
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            }
            throw (e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public void limpaBD() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            for (Viaxe viaxe : em.createQuery("SELECT v FROM Viaxe v", Viaxe.class).getResultList())
                em.remove(viaxe);

            for (Persoa persoa : em.createQuery("SELECT p FROM Persoa p", Persoa.class).getResultList())
                em.remove(persoa);

            for (Cruceiro cruceiro : em.createQuery("SELECT c FROM Cruceiro c", Cruceiro.class).getResultList())
                em.remove(cruceiro);

            em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idCruceiro'").executeUpdate();
            em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idViaxe'").executeUpdate();
            em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idPersoa'").executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            }
            throw (e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
}