package tn.esprit.crm.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ProductQuoteDetailsPk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long productId;

	private Long quoteId;
	private int quantity;

	public ProductQuoteDetailsPk() {
	}

	public ProductQuoteDetailsPk(Long productId, Long quoteId, int quantity) {
		super();
		this.productId = productId;
		this.quoteId = quoteId;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Long quoteId) {
		this.quoteId = quoteId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ProductQuoteDetailsPk(Long productId, Long quoteId) {
		super();
		this.productId = productId;
		this.quoteId = quoteId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((quoteId == null) ? 0 : quoteId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductQuoteDetailsPk other = (ProductQuoteDetailsPk) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (quantity != other.quantity)
			return false;
		if (quoteId == null) {
			if (other.quoteId != null)
				return false;
		} else if (!quoteId.equals(other.quoteId))
			return false;
		return true;
	}

}
