package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cruceiro;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class CruceiroDaoJPA implements CruceiroDao {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void setup(Configuracion config) {
        this.emf = (EntityManagerFactory) config.get("EMF");
    }

    /* MO4.1 */
    @Override
    public Cruceiro recuperaPorCodigo(String codigo) {
        List<Cruceiro> cruceiros = new ArrayList<>();
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            cruceiros = em.createNamedQuery("Cruceiro.recuperaPorCodigo", Cruceiro.class)
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
        return (cruceiros.isEmpty() ? null : cruceiros.get(0));
    }

    /* MO4.2 */
    @Override
    public Cruceiro almacena(Cruceiro cruceiro) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            em.persist(cruceiro);

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
        return cruceiro;
    }

    /* MO4.3 */
    @Override
    public void elimina(Cruceiro cruceiro) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Cruceiro c = em.find(Cruceiro.class, cruceiro.getId());
            if (c != null) {
                em.remove(c);
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
    public Cruceiro modifica(Cruceiro cruceiro) {
        Cruceiro c = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            c = em.merge(cruceiro);

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
        return c;
    }

    /* MO4.6.c */
    @Override
    public List<Cruceiro> buscarCruceirosPorPorto(String porto) {
        List<Cruceiro> cruceiros = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            cruceiros = em.createQuery(
                            "SELECT c FROM Cruceiro c WHERE c.id IN " +
                                    "(SELECT v.cruceiro.id FROM Viaxe v WHERE :porto MEMBER OF v.itinerarioPortos )",
                            Cruceiro.class)
                    .setParameter("porto", porto)
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
        return cruceiros;
    }
}