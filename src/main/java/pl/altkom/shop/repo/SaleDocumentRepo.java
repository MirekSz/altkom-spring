package pl.altkom.shop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.altkom.shop.model.SaleDocument;

@Repository
public interface SaleDocumentRepo extends JpaRepository<SaleDocument, Long> {

	SaleDocument findByNumber(String number);
}
