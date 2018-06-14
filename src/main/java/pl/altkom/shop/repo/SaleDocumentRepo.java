package pl.altkom.shop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.altkom.shop.model.SaleDocument;

@Repository
public interface SaleDocumentRepo extends JpaRepository<SaleDocument, Long> {

	SaleDocument findByNumber(String number);

	List<SaleDocument> queryByOrderByTotalPrice();

	List<SaleDocument> findTop10ByOrderById();

	@Query("FROM SaleDocument where size(items) > 3")
	List<SaleDocument> findWith3Items();
}
