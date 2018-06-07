package pl.altkom.shop.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.stereotype.Repository;

import pl.altkom.shop.model.Product;

//@Repository
public class JDBCProductRepo implements ProductRepo {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public static class ProductSqlMappingQuery extends MappingSqlQuery<Product> {
		public ProductSqlMappingQuery(DataSource ds, String sql, SqlParameter... params) {
			setDataSource(ds);
			setSql(sql);
			setParameters(params);
			afterPropertiesSet();
		}

		@Override
		protected Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Product(rs.getLong("id"), rs.getString("name"), rs.getString("description"),
					rs.getInt("quantity"), rs.getBigDecimal("price"));
		}
	};

	private RowMapper<Product> productRowMapper = (rs, rowNum) -> new Product(rs.getLong("id"), rs.getString("name"),
			rs.getString("description"), rs.getInt("quantity"), rs.getBigDecimal("price"));
	private SimpleJdbcInsert insert;
	private ProductSqlMappingQuery byId;

	@PostConstruct
	private void init() {
		byId = new ProductSqlMappingQuery(jdbcTemplate.getDataSource(), "select * from product where id=?",
				new SqlParameter(java.sql.Types.BIGINT));
		insert = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("product")
				.usingGeneratedKeyColumns("id");
	}

	@Override
	public Long insert(Product product) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", product.getName());
		params.put("price", product.getPrice());
		params.put("quantity", product.getQuantity());
		return insert.executeAndReturnKey(params).longValue();
		// Long id = jdbcTemplate.queryForObject("select max(id) from product",
		// Long.class) + 1;
		// jdbcTemplate.update("insert into product (id,
		// name,description,quantity,price) VALUES (?,?,?,?,?)", id,
		// product.getName(), product.getDescription(), product.getQuantity(),
		// product.getPrice());
	}

	@Override
	public Long count() {
		return jdbcTemplate.queryForObject("select count(*) from product", Long.class);
	}

	@Override
	public void delete(Long id) {
		jdbcTemplate.update("delete from product where id=?", id);
	}

	@Override
	public Product find(Long id) {
		return this.byId.findObject(id);
		// return jdbcTemplate.queryForObject("select * from product where
		// id=?", new Object[] { id }, productRowMapper);
	}

	@Override
	public void update(Product product) {
		jdbcTemplate.update("update product set name=?,description=?,quantity=?,price=? where id = ?",
				product.getName(), product.getDescription(), product.getQuantity(), product.getPrice(),
				product.getId());

	}

	@Override
	public List<Product> getAll() {
		return jdbcTemplate.query("select * from product", productRowMapper);
	}

}
