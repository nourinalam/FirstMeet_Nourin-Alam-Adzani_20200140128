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
public class CustomerJpaController implements Serializable {

    public CustomerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customer customer) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Daftarmenu daftarmenu = customer.getDaftarmenu();
            if (daftarmenu != null) {
                daftarmenu = em.getReference(daftarmenu.getClass(), daftarmenu.getIdMenu());
                customer.setDaftarmenu(daftarmenu);
            }
            em.persist(customer);
            if (daftarmenu != null) {
                Customer oldIdCustomerOfDaftarmenu = daftarmenu.getIdCustomer();
                if (oldIdCustomerOfDaftarmenu != null) {
                    oldIdCustomerOfDaftarmenu.setDaftarmenu(null);
                    oldIdCustomerOfDaftarmenu = em.merge(oldIdCustomerOfDaftarmenu);
                }
                daftarmenu.setIdCustomer(customer);
                daftarmenu = em.merge(daftarmenu);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCustomer(customer.getIdCustomer()) != null) {
                throw new PreexistingEntityException("Customer " + customer + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customer customer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer persistentCustomer = em.find(Customer.class, customer.getIdCustomer());
            Daftarmenu daftarmenuOld = persistentCustomer.getDaftarmenu();
            Daftarmenu daftarmenuNew = customer.getDaftarmenu();
            List<String> illegalOrphanMessages = null;
            if (daftarmenuOld != null && !daftarmenuOld.equals(daftarmenuNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Daftarmenu " + daftarmenuOld + " since its idCustomer field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (daftarmenuNew != null) {
                daftarmenuNew = em.getReference(daftarmenuNew.getClass(), daftarmenuNew.getIdMenu());
                customer.setDaftarmenu(daftarmenuNew);
            }
            customer = em.merge(customer);
            if (daftarmenuNew != null && !daftarmenuNew.equals(daftarmenuOld)) {
                Customer oldIdCustomerOfDaftarmenu = daftarmenuNew.getIdCustomer();
                if (oldIdCustomerOfDaftarmenu != null) {
                    oldIdCustomerOfDaftarmenu.setDaftarmenu(null);
                    oldIdCustomerOfDaftarmenu = em.merge(oldIdCustomerOfDaftarmenu);
                }
                daftarmenuNew.setIdCustomer(customer);
                daftarmenuNew = em.merge(daftarmenuNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = customer.getIdCustomer();
                if (findCustomer(id) == null) {
                    throw new NonexistentEntityException("The customer with id " + id + " no longer exists.");
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
            Customer customer;
            try {
                customer = em.getReference(Customer.class, id);
                customer.getIdCustomer();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customer with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Daftarmenu daftarmenuOrphanCheck = customer.getDaftarmenu();
            if (daftarmenuOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Customer (" + customer + ") cannot be destroyed since the Daftarmenu " + daftarmenuOrphanCheck + " in its daftarmenu field has a non-nullable idCustomer field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(customer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customer> findCustomerEntities() {
        return findCustomerEntities(true, -1, -1);
    }

    public List<Customer> findCustomerEntities(int maxResults, int firstResult) {
        return findCustomerEntities(false, maxResults, firstResult);
    }

    private List<Customer> findCustomerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customer.class));
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

    public Customer findCustomer(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customer> rt = cq.from(Customer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
