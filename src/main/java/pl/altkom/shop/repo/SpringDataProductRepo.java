package pl.altkom.shop.repo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.altkom.shop.model.Product;
import pl.altkom.shop.repo.data.ExtendedRepository;

@Repository
public interface SpringDataProductRepo extends ExtendedRepository<Product, Long>, SpringDataProductRepoCustom {

	@Query("FROM Product where id = :id")
	Optional<Product> find(@Param("id") Long id);

	@Cacheable("products")
	@Query("select name, price, quantity FROM Product where name = :name")
	List<ProductInfo> findByName(@Param("name") String name);

	@CacheEvict(value = "products", allEntries = true)
	@Transactional
	@Modifying
	@Query("update Product p set p.price = :newPrice")
	void promotion(@Param("newPrice") BigDecimal price);

}
