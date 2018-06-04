package pl.altkom.shop.repo;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;

public interface ProductInfo {

	public String getName();

	public BigDecimal getPrice();

	public Integer getQuantity();

	@Value("#{target.price + ' ' + target.quantity}")
	String getDetails();
}
