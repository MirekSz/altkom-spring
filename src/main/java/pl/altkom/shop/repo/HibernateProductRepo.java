package pl.altkom.shop.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.altkom.shop.model.Product;

@Repository
@Transactional
public class HibernateProductRepo implements ProductRepo {
	@PersistenceContext
	EntityManager em;

	@Override
	@PreAuthorize("#product.quantity != 0 OR hasRole('ROLE_ADMIN'))")
	public Long insert(Product product) {
		em.persist(product);
		return product.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public Long count() {
		return (Long) em.createQuery("SELECT count(*) FROM Product p").getSingleResult();
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void delete(Long id) {
		Product product = em.find(Product.class, id);
		em.remove(product);
	}

	@Override
	@Transactional(readOnly = true)
	@PostAuthorize("!(returnObject.price > 100 AND hasRole('ROLE_USER')) OR hasRole('ROLE_ADMIN')")
	public Product find(Long id) {
		Product product = em.find(Product.class, id);
		return product;
	}

	@Override
	public void update(Product product) {
		em.merge(product);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> getAll() {
		return em.createQuery("FROM Product p").getResultList();
	}
}
