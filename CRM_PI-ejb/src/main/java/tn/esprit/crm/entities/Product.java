package tn.esprit.crm.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product implements Serializable {
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue (strategy = GenerationType.IDENTITY)
@Column(name="id")
private Long id; 
@Column(name="name")
private String name; 
@Column(name="tva")
private float tva;
@Column(name="prix")
private float prix;


@OneToMany(mappedBy="product")
private Set<ProductQuoteDetails> productQuoteDetails;
public Product() {
	// TODO Auto-generated constructor stub
}


@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
private Promotion Promotion;

public Promotion getPromotion()
{
	return Promotion;
}
public Promotion setPromotion(Promotion Promotion)
{
	return this.Promotion=Promotion;
}


public Product(String name, float tva, float prix) {
	super();
	this.name = name;
	this.tva = tva;
	this.prix = prix;
}



public Product(String name, float tva, float prix,
		Promotion promotion) {
	super();
	this.name = name;
	this.tva = tva;
	this.prix = prix;
	Promotion = promotion;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public float getTva() {
	return tva;
}
public void setTva(float tva) {
	this.tva = tva;
}
public float getPrix() {
	return prix;
}
public void setPrix(float prix) {
	this.prix = prix;
}
@Override
public String toString() {
	return "Product [id=" + id + ", name=" + name + ", tva=" + tva + ", prix=" + prix + "]";
}

}