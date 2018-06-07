package pl.altkom.shop.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import pl.altkom.shop.model.Product;
import pl.altkom.shop.model.QProduct;
import pl.altkom.shop.repo.ProductRepo;
import pl.altkom.shop.repo.SpringDataProductRepo;

@RestController
@RequestMapping("/api/products")
public class RestProductController {

	@Inject
	ProductRepo repo;
	@Inject
	SpringDataProductRepo springDataProductRepo;

	@RequestMapping(value = "/ds", method = RequestMethod.GET)
	public DataTablesOutput<Product> datatables(DataTablesInput req) {
		DataTablesOutput<Product> dataTablesOutput = new DataTablesOutput<>();
		// dataTablesOutput.setData(repo.getAll());
		dataTablesOutput.setData(Lists.newArrayList(springDataProductRepo.findAll(QProduct.product.name.desc())));
		return dataTablesOutput;
	}

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public List<Product> list() {
		return repo.getAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		repo.delete(id);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody Product product) {
		repo.update(product);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Long> save(@RequestBody Product product) {
		Long id = repo.insert(product);
		return new ResponseEntity<Long>(id, HttpStatus.CREATED);
	}

}
