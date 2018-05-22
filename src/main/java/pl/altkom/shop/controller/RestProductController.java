package pl.altkom.shop.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.altkom.shop.model.Product;
import pl.altkom.shop.repo.ProductRepo;

@RestController
@RequestMapping("/api/products")
public class RestProductController {

	@Inject
	ProductRepo repo;

	@RequestMapping(method = RequestMethod.GET)
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
