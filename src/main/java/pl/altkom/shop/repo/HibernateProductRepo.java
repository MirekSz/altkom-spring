package pl.altkom.shop.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.altkom.shop.aop.Monitoring;
import pl.altkom.shop.model.Product;

@Repository
@Transactional
public class HibernateProductRepo implements ProductRepo {
	@PersistenceContext
	EntityManager em;

	@Override
	public Long insert(Product product) {
		em.persist(product);
		return product.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public Long count() {
		return (Long) em.createQuery("SELECT count(*) FROM Product p").getSingleResult();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Override
	public void delete(Long id) {
		Product product = em.find(Product.class, id);
		em.remove(product);
	}

	@Override
	@Transactional(readOnly = true)
	public Product find(Long id) {
		Product product = em.find(Product.class, id);
		return product;
	}

	@Override
	public void update(Product product) {
		em.merge(product);
	}

	@Override
	@Monitoring
	@Transactional(readOnly = true)
	public List<Product> getAll() {
		return em.createQuery("FROM Product p").getResultList();
	}
}
