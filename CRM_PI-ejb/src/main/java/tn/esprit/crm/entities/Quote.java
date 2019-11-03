package tn.esprit.crm.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "quote")
public class Quote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "reference")
	private String reference;
	@Column(name = "description")
	private String description;
	@Column(name = "duration")
	private int duration;
	@Temporal(TemporalType.DATE)
	@Column(name = "dateQuote")
	private Date dateQuote;

	@Column(name = "generatedToInvoice")
	private int generatedToInvoice;

	@OneToOne(mappedBy = "quote")
	private Invoice Invoice;

	@OneToMany(mappedBy = "quote")
	private Set<ProductQuoteDetails> productQuoteDetails;

	
	@ManyToOne
	private Client client;
	
	
	public Quote() {
	}

	public Quote(String reference, String description, int duration, Date dateQuote) {
		super();
		this.reference = reference;
		this.description = description;
		this.duration = duration;
		this.dateQuote = dateQuote;
	}

	public Quote(Long id, String reference, String description, int duration, Date dateQuote, Invoice Invoice,
			Set<ProductQuoteDetails> productQuoteDetails) {
		super();
		this.id = id;
		this.reference = reference;
		this.description = description;
		this.duration = duration;
		this.dateQuote = dateQuote;
		this.Invoice = Invoice;
		this.productQuoteDetails = productQuoteDetails;
	}

	public Quote(Long id, String reference, String description, int duration, Date dateQuote) {
		super();
		this.id = id;
		this.reference = reference;
		this.description = description;
		this.duration = duration;
		this.dateQuote = dateQuote;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getDateQuote() {
		return dateQuote;
	}

	public void setDateQuote(Date dateQuote) {
		this.dateQuote = dateQuote;
	}

	public Set<ProductQuoteDetails> getProductQuoteDetails() {
		return productQuoteDetails;
	}

	public void setProductQuoteDetails(Set<ProductQuoteDetails> productQuoteDetails) {
		this.productQuoteDetails = productQuoteDetails;
	}

	public int getGeneratedToInvoice() {
		return generatedToInvoice;
	}

	public void setGeneratedToInvoice(int generatedToInvoice) {
		this.generatedToInvoice = generatedToInvoice;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	// Constructeurs, getters, setters

	
}
