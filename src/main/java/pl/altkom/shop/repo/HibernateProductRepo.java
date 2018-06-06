package pl.altkom.shop.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

import org.springframework.stereotype.Repository;

import pl.altkom.shop.model.Product;

@Repository
public class HibernateProductRepo implements ProductRepo {
	@PersistenceUnit
	EntityManagerFactory emf;

	private EntityManager startTx() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		return em;
	}

	private void commit(EntityManager em) {
		EntityTransaction transaction = em.getTransaction();
		transaction.commit();
	}

	@Override
	public Long insert(Product product) {
		EntityManager em = startTx();
		em.persist(product);
		commit(em);
		return product.getId();
	}

	@Override
	public Long count() {
		EntityManager em = startTx();
		try {
			return (Long) em.createQuery("SELECT count(*) FROM Product p").getSingleResult();
		} finally {
			commit(em);
		}
	}

	@Override
	public void delete(Long id) {
		EntityManager em = startTx();
		Product product = em.find(Product.class, id);
		em.remove(product);
		commit(em);
	}

	@Override
	public Product find(Long id) {
		EntityManager em = startTx();
		try {
			Product product = em.find(Product.class, id);
			return product;
		} finally {
			commit(em);
		}
	}

	@Override
	public void update(Product product) {
		EntityManager em = startTx();
		em.merge(product);
		commit(em);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAll() {
		EntityManager em = startTx();
		try {
			return em.createQuery("FROM Product p").getResultList();
		} finally {
			commit(em);
		}
	}
}
