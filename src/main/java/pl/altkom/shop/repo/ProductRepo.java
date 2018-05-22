package pl.altkom.shop.repo;

import java.util.List;

import pl.altkom.shop.model.Product;

public interface ProductRepo {

	public Long insert(Product product);

	Long count();

	public void delete(Long id);

	public Product find(Long id);

	public void update(Product product);

	public List<Product> getAll();
}
