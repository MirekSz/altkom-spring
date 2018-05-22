package pl.altkom.shop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import pl.altkom.shop.lib.Profiles;
import pl.altkom.shop.model.Product;
import pl.altkom.shop.repo.ProductRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { CoreConfig.class, TestConfig.class })
@Transactional
@Rollback(true)
@ActiveProfiles({ Profiles.TEST, Profiles.WEB })
public class ProductServiceWWWTest {

	@Inject
	ProductRepo repo;
	@Inject
	DataSource ds;
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void shouldGetProductList() throws Exception {
		// given
		Product product = new Product("SSD", "Szybki", 10, BigDecimal.TEN);
		repo.insert(product);
		assertThat(product.getOwner()).isNotEmpty();

		// when
		ResultActions actions = this.mockMvc
				.perform(get("/api/products").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith("application/json"));
		MockHttpServletResponse response = actions.andReturn().getResponse();

		// then
		actions.andExpect(jsonPath("$[0].name").value("SSD"));
		assertThat(response.getContentAsString()).contains("SSD");
	}

	@Test
	@WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
	public void shouldDeleteProduct() throws Exception {
		// given
		Product product = new Product("SSD", "Szybki", 10, BigDecimal.TEN);
		repo.insert(product);

		// when
		ResultActions perform = this.mockMvc.perform(delete("/api/products/{id}", product.getId()));

		// then
		perform.andExpect(status().isOk());
	}

}
