package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cruceiro;
import gei.id.tutelado.model.Viaxe;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class ViaxeDaoJPA implements ViaxeDao {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void setup(Configuracion config) {
        this.emf = (EntityManagerFactory) config.get("EMF");
    }


    /* MO4.1 */
    @Override
    public Viaxe recuperaPorCodigo(String codigo) {
        List<Viaxe> viaxes = new ArrayList<>();
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            viaxes = em.createNamedQuery("Viaxe.recuperaPorCodigo", Viaxe.class)
                    .setParameter("codigo", codigo)
                    .getResultList();

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                throw (ex);
            }
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return (viaxes.isEmpty() ? null : viaxes.get(0));
    }

    /* MO4.2 */
    @Override
    public Viaxe almacena(Viaxe viaxe) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            em.persist(viaxe);

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                throw (ex);
            }
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return viaxe;
    }

    /* MO4.3 */
    @Override
    public void elimina(Viaxe viaxe) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Viaxe v = em.find(Viaxe.class, viaxe.getId());
            if (v != null) {
                em.remove(v);
            }

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                throw (ex);
            }
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /* MO4.4 */
    @Override
    public Viaxe modifica(Viaxe viaxe) {
        Viaxe v = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            v = em.merge(viaxe);

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                throw (ex);
            }
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return v;
    }

    /* MO4.6.a */
    @Override
    public List<Viaxe> buscarViaxesPorCruceiro(Cruceiro cruceiro) {
        List<Viaxe> viaxes = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            viaxes = em.createQuery("SELECT v FROM Cruceiro c INNER JOIN c.viaxes v WHERE c = :cruceiro", Viaxe.class)
                    .setParameter("cruceiro", cruceiro)
                    .getResultList();

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                throw (ex);
            }
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return viaxes;
    }

    /* MO4.6.b */
    @Override
    public List<Viaxe> buscarViaxesSenCruceiro() {
        List<Viaxe> viaxes = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            viaxes = em.createQuery("SELECT v FROM Viaxe v " +
                                    "LEFT OUTER JOIN v.cruceiro c " +
                                    "WHERE c IS NULL",
                            Viaxe.class)
                    .getResultList();

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                throw (ex);
            }
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return viaxes;
    }

    /* MO4.6.d */
    @Override
    public Long contarPersoasEnViaxe(Viaxe viaxe) {
        Long contador = 0L;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            contador = em.createQuery("SELECT COUNT(p) FROM Viaxe v JOIN v.persoas p WHERE v = :viaxe", Long.class)
                    .setParameter("viaxe", viaxe)
                    .getSingleResult();

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                throw (ex);
            }
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return contador;
    }
}