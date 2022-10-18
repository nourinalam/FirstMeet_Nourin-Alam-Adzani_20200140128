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
public class DaftarmenuJpaController implements Serializable {

    public DaftarmenuJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Daftarmenu daftarmenu) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Customer idCustomerOrphanCheck = daftarmenu.getIdCustomer();
        if (idCustomerOrphanCheck != null) {
            Daftarmenu oldDaftarmenuOfIdCustomer = idCustomerOrphanCheck.getDaftarmenu();
            if (oldDaftarmenuOfIdCustomer != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Customer " + idCustomerOrphanCheck + " already has an item of type Daftarmenu whose idCustomer column cannot be null. Please make another selection for the idCustomer field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer idCustomer = daftarmenu.getIdCustomer();
            if (idCustomer != null) {
                idCustomer = em.getReference(idCustomer.getClass(), idCustomer.getIdCustomer());
                daftarmenu.setIdCustomer(idCustomer);
            }
            Toko toko = daftarmenu.getToko();
            if (toko != null) {
                toko = em.getReference(toko.getClass(), toko.getIdToko());
                daftarmenu.setToko(toko);
            }
            em.persist(daftarmenu);
            if (idCustomer != null) {
                idCustomer.setDaftarmenu(daftarmenu);
                idCustomer = em.merge(idCustomer);
            }
            if (toko != null) {
                Daftarmenu oldIdMenuOfToko = toko.getIdMenu();
                if (oldIdMenuOfToko != null) {
                    oldIdMenuOfToko.setToko(null);
                    oldIdMenuOfToko = em.merge(oldIdMenuOfToko);
                }
                toko.setIdMenu(daftarmenu);
                toko = em.merge(toko);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDaftarmenu(daftarmenu.getIdMenu()) != null) {
                throw new PreexistingEntityException("Daftarmenu " + daftarmenu + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Daftarmenu daftarmenu) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Daftarmenu persistentDaftarmenu = em.find(Daftarmenu.class, daftarmenu.getIdMenu());
            Customer idCustomerOld = persistentDaftarmenu.getIdCustomer();
            Customer idCustomerNew = daftarmenu.getIdCustomer();
            Toko tokoOld = persistentDaftarmenu.getToko();
            Toko tokoNew = daftarmenu.getToko();
            List<String> illegalOrphanMessages = null;
            if (idCustomerNew != null && !idCustomerNew.equals(idCustomerOld)) {
                Daftarmenu oldDaftarmenuOfIdCustomer = idCustomerNew.getDaftarmenu();
                if (oldDaftarmenuOfIdCustomer != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Customer " + idCustomerNew + " already has an item of type Daftarmenu whose idCustomer column cannot be null. Please make another selection for the idCustomer field.");
                }
            }
            if (tokoOld != null && !tokoOld.equals(tokoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Toko " + tokoOld + " since its idMenu field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCustomerNew != null) {
                idCustomerNew = em.getReference(idCustomerNew.getClass(), idCustomerNew.getIdCustomer());
                daftarmenu.setIdCustomer(idCustomerNew);
            }
            if (tokoNew != null) {
                tokoNew = em.getReference(tokoNew.getClass(), tokoNew.getIdToko());
                daftarmenu.setToko(tokoNew);
            }
            daftarmenu = em.merge(daftarmenu);
            if (idCustomerOld != null && !idCustomerOld.equals(idCustomerNew)) {
                idCustomerOld.setDaftarmenu(null);
                idCustomerOld = em.merge(idCustomerOld);
            }
            if (idCustomerNew != null && !idCustomerNew.equals(idCustomerOld)) {
                idCustomerNew.setDaftarmenu(daftarmenu);
                idCustomerNew = em.merge(idCustomerNew);
            }
            if (tokoNew != null && !tokoNew.equals(tokoOld)) {
                Daftarmenu oldIdMenuOfToko = tokoNew.getIdMenu();
                if (oldIdMenuOfToko != null) {
                    oldIdMenuOfToko.setToko(null);
                    oldIdMenuOfToko = em.merge(oldIdMenuOfToko);
                }
                tokoNew.setIdMenu(daftarmenu);
                tokoNew = em.merge(tokoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = daftarmenu.getIdMenu();
                if (findDaftarmenu(id) == null) {
                    throw new NonexistentEntityException("The daftarmenu with id " + id + " no longer exists.");
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
            Daftarmenu daftarmenu;
            try {
                daftarmenu = em.getReference(Daftarmenu.class, id);
                daftarmenu.getIdMenu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The daftarmenu with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Toko tokoOrphanCheck = daftarmenu.getToko();
            if (tokoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Daftarmenu (" + daftarmenu + ") cannot be destroyed since the Toko " + tokoOrphanCheck + " in its toko field has a non-nullable idMenu field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Customer idCustomer = daftarmenu.getIdCustomer();
            if (idCustomer != null) {
                idCustomer.setDaftarmenu(null);
                idCustomer = em.merge(idCustomer);
            }
            em.remove(daftarmenu);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Daftarmenu> findDaftarmenuEntities() {
        return findDaftarmenuEntities(true, -1, -1);
    }

    public List<Daftarmenu> findDaftarmenuEntities(int maxResults, int firstResult) {
        return findDaftarmenuEntities(false, maxResults, firstResult);
    }

    private List<Daftarmenu> findDaftarmenuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Daftarmenu.class));
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

    public Daftarmenu findDaftarmenu(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Daftarmenu.class, id);
        } finally {
            em.close();
        }
    }

    public int getDaftarmenuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Daftarmenu> rt = cq.from(Daftarmenu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
