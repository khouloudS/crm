package tn.esprit.crm.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "productQuoteDetails")
public class ProductQuoteDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	// @EmbeddedId
	private ProductQuoteDetailsPk productQuoteDetailsPk;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	/*
	 * @Column(name="quantity",insertable=true,updatable=true) private int quantity;
	 */
	@ManyToOne(/* fetch = FetchType.EAGER, */ cascade = CascadeType.ALL)
	@JoinColumn(name = "productId", referencedColumnName = "id", insertable = false, updatable = false)
	private Product product;

	@ManyToOne(/* fetch = FetchType.EAGER, */ cascade = CascadeType.ALL)
	@JoinColumn(name = "quoteId", referencedColumnName = "id", insertable = false, updatable = false)
	private Quote quote;
//
//	public int getQuantity() {
//		return quantity;
//	}
//
//	public void setQuantity(int quantity) {
//		this.quantity = quantity;
//	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public ProductQuoteDetailsPk getProductQuoteDetailsPk() {
		return productQuoteDetailsPk;
	}

	public void setProductQuoteDetailsPk(ProductQuoteDetailsPk productQuoteDetailsPk) {
		this.productQuoteDetailsPk = productQuoteDetailsPk;
	}

}
