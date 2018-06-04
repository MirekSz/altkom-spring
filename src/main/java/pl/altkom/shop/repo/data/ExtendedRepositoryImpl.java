package pl.altkom.shop.repo.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

public class ExtendedRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID>
		implements ExtendedRepository<T, ID> {

	private EntityManager entityManager;

	private JpaEntityInformation<T, ?> entity;

	public ExtendedRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entity = entityInformation;
		this.entityManager = entityManager;
	}

	@Override
	public List<T> showDeleted(LocalDateTime localDateTime) {
		String entityName = entity.getEntityName();
		return entityManager.createQuery("FROM " + entityName).getResultList();
	}

}
