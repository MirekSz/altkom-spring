package pl.altkom.shop.repo;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.altkom.shop.model.Product;
import pl.altkom.shop.repo.data.ExtendedRepository;

@Repository
public interface SpringDataProductRepo extends ExtendedRepository<Product, Long>, SpringDataProductRepoCustom {

	@Query("FROM Product where id = :id")
	Optional<Product> find(@Param("id") Long id);

	@Query("select name FROM Product where name = :name")
	ProductInfo findByName(@Param("name") String name);

	@Modifying
	@Query("update Product p set p.price = :newPrice")
	void promotion(@Param("newPrice") BigDecimal price);

}
