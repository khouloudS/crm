package tn.esprit.crm.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "reference")
	private String reference;
	@Column(name = "description")
	private String description;
	@Column(name = "status")
	private String status;
	@Temporal(TemporalType.DATE)
	@Column(name = "dateInvoice")
	private Date dateInvoice;

	@Column(name = "duration")
	private int duration;

	@Column(name = "total")
	private double total;

	@OneToOne(cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private Quote quote;

	
	@OneToOne(mappedBy = "invoice")
	private Payment payment;

	
	
	public Invoice() {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateInvoice() {
		return dateInvoice;
	}

	public void setDateInvoice(Date dateInvoice) {
		this.dateInvoice = dateInvoice;
	}

	public Invoice(String reference, String description, String status, Date dateInvoice) {
		super();
		this.reference = reference;
		this.description = description;
		this.status = status;
		this.dateInvoice = dateInvoice;
	}

	public Invoice(Long id, String reference, String description, String status, Date dateInvoice) {
		super();
		this.id = id;
		this.reference = reference;
		this.description = description;
		this.status = status;
		this.dateInvoice = dateInvoice;
	}

	public Invoice(String reference, String description, String status, Date dateInvoice, int duration, double total) {
		super();
		this.reference = reference;
		this.description = description;
		this.status = status;
		this.dateInvoice = dateInvoice;
		this.duration = duration;
		this.total = total;
	}

	public Invoice(Long id, String reference, String description, String status, Date dateInvoice, int duration,
			double total, Quote quote) {
		super();
		this.id = id;
		this.reference = reference;
		this.description = description;
		this.status = status;
		this.dateInvoice = dateInvoice;
		this.duration = duration;
		this.total = total;
		this.quote = quote;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

}
