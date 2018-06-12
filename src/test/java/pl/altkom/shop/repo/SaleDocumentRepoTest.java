package pl.altkom.shop.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.altkom.shop.CoreConfig;
import pl.altkom.shop.model.SaleDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CoreConfig.class })
@Transactional
@Rollback(true)
public class SaleDocumentRepoTest {
	@Autowired
	SaleDocumentRepo repo;

	@Test
	public void shouldAddProduct() throws Exception {
		// given
		String number = "FAS 12/2018";
		SaleDocument saleDocument = new SaleDocument();
		saleDocument.setNumber(number);
		saleDocument.setTotalPrice(BigDecimal.TEN);
		repo.save(saleDocument);

		// when
		SaleDocument finded = repo.findByNumber(number);

		// then
		assertThat(finded).isNotNull();
	}
}
