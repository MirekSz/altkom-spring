package pl.altkom.shop;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.altkom.shop.lib.Profiles;
import pl.altkom.shop.model.Product;
import pl.altkom.shop.repo.ProductRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfig.class)
@Transactional
@Rollback(true)
@ActiveProfiles(Profiles.TEST)
public class SaleDocumentServiceTest {

	@Inject
	ProductRepo repo;

	@Test
	public void shouldAddProduct() throws Exception {
		// given
		Product product = new Product("SSD", "Szybki", 10, BigDecimal.TEN);
		Long count = repo.count();
		System.out.println(count);
		// when
		repo.insert(product);

		// then
		assertThat(repo.count()).isGreaterThan(count);
	}
}
