package tn.esprit.crm.entities;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@Table(name="Client")
public class Client implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name="ID")
	private int id;
	
	@Column(name="Nom")
	private  String Nom;
	
	@Column(name="Prenom")
	private String Prenom;
	
	@Column(name="Email")
	private String Email;
	
	@Column(name="Username")
	private String Username;
	
	@Column(name="Password")
	private String Password;

	@Column(name="CIN")
	private int CIN;
	
	@Column(name="DateBirth")
	@Temporal (TemporalType.DATE)
	private Date DateBirth;

	@Column(name="Adresse")
	private String Adresse;

	@Column(name="Operateur")
	@Enumerated(EnumType.STRING)
	private Operateur opr;
	
	@OneToMany(mappedBy="client")
	private List<Quote> quotes;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public String getPrenom() {
		return Prenom;
	}

	public void setPrenom(String prenom) {
		Prenom = prenom;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public int getCIN() {
		return CIN;
	}

	public void setCIN(int cIN) {
		CIN = cIN;
	}

	public Date getDateBirth() {
		return DateBirth;
	}

	public void setDateBirth(Date dateBirth) {
		DateBirth = dateBirth;
	}

	public String getAdresse() {
		return Adresse;
	}

	public void setAdresse(String adresse) {
		Adresse = adresse;
	}

	public Operateur getOpr() {
		return opr;
	}

	public void setOpr(Operateur opr) {
		this.opr = opr;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", Nom=" + Nom + ", Prenom=" + Prenom + ", Email=" + Email + ", Username="
				+ Username + ", Password=" + Password + ", CIN=" + CIN + ", DateBirth=" + DateBirth + ", Adresse="
				+ Adresse + ", opr=" + opr + "]";
	}

	public Client(int id, String nom, String prenom, String email, String username, String password, int cIN,
			Date dateBirth, String adresse, Operateur opr) {
		
		this.id = id;
		this.Nom = nom;
		this.Prenom = prenom;
		this.Email = email;
		this.Username = username;
		this.Password = password;
		this.CIN = cIN;
		this.DateBirth = dateBirth;
		this.Adresse = adresse;
		this.opr = opr;
	}

	public Client(String nom, String prenom, String email, String username, String password, int cIN, Date dateBirth,
			String adresse, Operateur opr) throws NoSuchAlgorithmException {
		cryptpasswords encryption = new cryptpasswords() ;
		this.Nom = nom;
		this.Prenom = prenom;
		this.Email = email;
		this.Username = username;
		this.Password = encryption.cryptme(password);
		this.CIN = cIN;
		this.DateBirth = dateBirth;
		this.Adresse = adresse;
		this.opr = opr;
	}

	public Client() {}

}
