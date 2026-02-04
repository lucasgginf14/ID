package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Persoa;
import org.hibernate.LazyInitializationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class PersoaDaoJPA implements PersoaDao {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void setup(Configuracion config) {
        this.emf = (EntityManagerFactory) config.get("EMF");
    }

    /* MO4.1 */
    @Override
    public Persoa recuperaPorDni(String dni) {
        List<Persoa> persoas = new ArrayList<>();
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            persoas = em.createNamedQuery("Persoa.recuperaPorDni", Persoa.class)
                    .setParameter("dni", dni)
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
        return (persoas.isEmpty() ? null : persoas.get(0));
    }

    /* MO4.2 */
    @Override
    public Persoa almacena(Persoa persoa) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            em.persist(persoa);

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
        return persoa;
    }

    /* MO4.3 */
    @Override
    public void elimina(Persoa persoa) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Persoa p = em.find(Persoa.class, persoa.getId());
            if (p != null) {
                em.remove(p);
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
    public Persoa modifica(Persoa persoa) {
        Persoa p = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            p = em.merge(persoa);

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
        return p;
    }

    /* MO4.5 */
    @Override
    public Persoa restauraViaxes(Persoa persoa) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            try {
                persoa.getViaxes().size();
            } catch (Exception ex2) {
                if (ex2 instanceof LazyInitializationException) {
                    persoa = em.merge(persoa);
                    persoa.getViaxes().size();
                } else {
                    throw ex2;
                }
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
        return persoa;
    }
}