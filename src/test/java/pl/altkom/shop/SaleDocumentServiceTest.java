package pl.altkom.shop;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import pl.altkom.shop.model.Product;
import pl.altkom.shop.repo.ProductRepo;

@SpringJUnitConfig(classes = CoreConfig.class)
public class SaleDocumentServiceTest {

	@Inject
	ProductRepo repo;

	@Test
	public void shouldAddProduct() throws Exception {
		// given
		Product product = new Product();
		Long count = repo.count();

		// when
		repo.insert(product);

		// then
		assertThat(repo.count()).isGreaterThan(count);
	}
}
