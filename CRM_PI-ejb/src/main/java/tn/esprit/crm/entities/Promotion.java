package tn.esprit.crm.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="Promotion")

public class Promotion implements Serializable{
	
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name="ID")
	 private int id;
	@Column(name="Start_date")
	private Date Start_date;
	@Column(name="end_date")
	private Date end_date;
	@Column(name="percentage")
	private float percentage;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getStart_date() {
		return Start_date;
	}
	public void setStart_date(Date start_date) {
		Start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public float getPercentage() {
		return percentage;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	public Promotion(int id, Date start_date, Date end_date, float percentage) {
		super();
		this.id = id;
		Start_date = start_date;
		this.end_date = end_date;
		this.percentage = percentage;
	}
	public Promotion() {
		super();
	}
	@Override
	public String toString() {
		return "Promotion [id=" + id + ", Start_date=" + Start_date + ", end_date=" + end_date + ", percentage="
				+ percentage + "]";
	}
	
	

}
