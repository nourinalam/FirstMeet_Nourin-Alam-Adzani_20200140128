/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cemilanpws.koneksidatabase;

import cemilanpws.koneksidatabase.exceptions.IllegalOrphanException;
import cemilanpws.koneksidatabase.exceptions.NonexistentEntityException;
import cemilanpws.koneksidatabase.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author user
 */
public class ManagerJpaController implements Serializable {

    public ManagerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Manager manager) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Toko idTokoOrphanCheck = manager.getIdToko();
        if (idTokoOrphanCheck != null) {
            Manager oldManagerOfIdToko = idTokoOrphanCheck.getManager();
            if (oldManagerOfIdToko != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Toko " + idTokoOrphanCheck + " already has an item of type Manager whose idToko column cannot be null. Please make another selection for the idToko field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Toko idToko = manager.getIdToko();
            if (idToko != null) {
                idToko = em.getReference(idToko.getClass(), idToko.getIdToko());
                manager.setIdToko(idToko);
            }
            em.persist(manager);
            if (idToko != null) {
                idToko.setManager(manager);
                idToko = em.merge(idToko);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findManager(manager.getIdManager()) != null) {
                throw new PreexistingEntityException("Manager " + manager + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Manager manager) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Manager persistentManager = em.find(Manager.class, manager.getIdManager());
            Toko idTokoOld = persistentManager.getIdToko();
            Toko idTokoNew = manager.getIdToko();
            List<String> illegalOrphanMessages = null;
            if (idTokoNew != null && !idTokoNew.equals(idTokoOld)) {
                Manager oldManagerOfIdToko = idTokoNew.getManager();
                if (oldManagerOfIdToko != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Toko " + idTokoNew + " already has an item of type Manager whose idToko column cannot be null. Please make another selection for the idToko field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTokoNew != null) {
                idTokoNew = em.getReference(idTokoNew.getClass(), idTokoNew.getIdToko());
                manager.setIdToko(idTokoNew);
            }
            manager = em.merge(manager);
            if (idTokoOld != null && !idTokoOld.equals(idTokoNew)) {
                idTokoOld.setManager(null);
                idTokoOld = em.merge(idTokoOld);
            }
            if (idTokoNew != null && !idTokoNew.equals(idTokoOld)) {
                idTokoNew.setManager(manager);
                idTokoNew = em.merge(idTokoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = manager.getIdManager();
                if (findManager(id) == null) {
                    throw new NonexistentEntityException("The manager with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Manager manager;
            try {
                manager = em.getReference(Manager.class, id);
                manager.getIdManager();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The manager with id " + id + " no longer exists.", enfe);
            }
            Toko idToko = manager.getIdToko();
            if (idToko != null) {
                idToko.setManager(null);
                idToko = em.merge(idToko);
            }
            em.remove(manager);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Manager> findManagerEntities() {
        return findManagerEntities(true, -1, -1);
    }

    public List<Manager> findManagerEntities(int maxResults, int firstResult) {
        return findManagerEntities(false, maxResults, firstResult);
    }

    private List<Manager> findManagerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Manager.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Manager findManager(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Manager.class, id);
        } finally {
            em.close();
        }
    }

    public int getManagerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Manager> rt = cq.from(Manager.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
