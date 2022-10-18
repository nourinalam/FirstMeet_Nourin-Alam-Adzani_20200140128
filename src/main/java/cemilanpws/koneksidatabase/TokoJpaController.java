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
public class TokoJpaController implements Serializable {

    public TokoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Toko toko) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Daftarmenu idMenuOrphanCheck = toko.getIdMenu();
        if (idMenuOrphanCheck != null) {
            Toko oldTokoOfIdMenu = idMenuOrphanCheck.getToko();
            if (oldTokoOfIdMenu != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Daftarmenu " + idMenuOrphanCheck + " already has an item of type Toko whose idMenu column cannot be null. Please make another selection for the idMenu field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Manager manager = toko.getManager();
            if (manager != null) {
                manager = em.getReference(manager.getClass(), manager.getIdManager());
                toko.setManager(manager);
            }
            Daftarmenu idMenu = toko.getIdMenu();
            if (idMenu != null) {
                idMenu = em.getReference(idMenu.getClass(), idMenu.getIdMenu());
                toko.setIdMenu(idMenu);
            }
            em.persist(toko);
            if (manager != null) {
                Toko oldIdTokoOfManager = manager.getIdToko();
                if (oldIdTokoOfManager != null) {
                    oldIdTokoOfManager.setManager(null);
                    oldIdTokoOfManager = em.merge(oldIdTokoOfManager);
                }
                manager.setIdToko(toko);
                manager = em.merge(manager);
            }
            if (idMenu != null) {
                idMenu.setToko(toko);
                idMenu = em.merge(idMenu);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findToko(toko.getIdToko()) != null) {
                throw new PreexistingEntityException("Toko " + toko + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Toko toko) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Toko persistentToko = em.find(Toko.class, toko.getIdToko());
            Manager managerOld = persistentToko.getManager();
            Manager managerNew = toko.getManager();
            Daftarmenu idMenuOld = persistentToko.getIdMenu();
            Daftarmenu idMenuNew = toko.getIdMenu();
            List<String> illegalOrphanMessages = null;
            if (managerOld != null && !managerOld.equals(managerNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Manager " + managerOld + " since its idToko field is not nullable.");
            }
            if (idMenuNew != null && !idMenuNew.equals(idMenuOld)) {
                Toko oldTokoOfIdMenu = idMenuNew.getToko();
                if (oldTokoOfIdMenu != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Daftarmenu " + idMenuNew + " already has an item of type Toko whose idMenu column cannot be null. Please make another selection for the idMenu field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (managerNew != null) {
                managerNew = em.getReference(managerNew.getClass(), managerNew.getIdManager());
                toko.setManager(managerNew);
            }
            if (idMenuNew != null) {
                idMenuNew = em.getReference(idMenuNew.getClass(), idMenuNew.getIdMenu());
                toko.setIdMenu(idMenuNew);
            }
            toko = em.merge(toko);
            if (managerNew != null && !managerNew.equals(managerOld)) {
                Toko oldIdTokoOfManager = managerNew.getIdToko();
                if (oldIdTokoOfManager != null) {
                    oldIdTokoOfManager.setManager(null);
                    oldIdTokoOfManager = em.merge(oldIdTokoOfManager);
                }
                managerNew.setIdToko(toko);
                managerNew = em.merge(managerNew);
            }
            if (idMenuOld != null && !idMenuOld.equals(idMenuNew)) {
                idMenuOld.setToko(null);
                idMenuOld = em.merge(idMenuOld);
            }
            if (idMenuNew != null && !idMenuNew.equals(idMenuOld)) {
                idMenuNew.setToko(toko);
                idMenuNew = em.merge(idMenuNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = toko.getIdToko();
                if (findToko(id) == null) {
                    throw new NonexistentEntityException("The toko with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Toko toko;
            try {
                toko = em.getReference(Toko.class, id);
                toko.getIdToko();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The toko with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Manager managerOrphanCheck = toko.getManager();
            if (managerOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Toko (" + toko + ") cannot be destroyed since the Manager " + managerOrphanCheck + " in its manager field has a non-nullable idToko field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Daftarmenu idMenu = toko.getIdMenu();
            if (idMenu != null) {
                idMenu.setToko(null);
                idMenu = em.merge(idMenu);
            }
            em.remove(toko);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Toko> findTokoEntities() {
        return findTokoEntities(true, -1, -1);
    }

    public List<Toko> findTokoEntities(int maxResults, int firstResult) {
        return findTokoEntities(false, maxResults, firstResult);
    }

    private List<Toko> findTokoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Toko.class));
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

    public Toko findToko(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Toko.class, id);
        } finally {
            em.close();
        }
    }

    public int getTokoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Toko> rt = cq.from(Toko.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
