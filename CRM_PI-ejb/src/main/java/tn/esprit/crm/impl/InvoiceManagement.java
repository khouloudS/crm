package tn.esprit.crm.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.RandomStringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;

import tn.esprit.crm.entities.Invoice;
import tn.esprit.crm.entities.Payment;
import tn.esprit.crm.entities.Quote;
import tn.esprit.crm.inter.IInvoiceRemote;
import tn.esprit.crm.inter.IQuoteRemote;

@Stateful
@LocalBean
public class InvoiceManagement implements IInvoiceRemote {
	@PersistenceContext
	EntityManager entityManager;

	@EJB
	IQuoteRemote qm;

	@Override
	public Long generateInvoice(Invoice Invoice, String reference) {
		// TODO Auto-generated method stub

		boolean find = false;
		Date date = new Date();
		String referenceInvoice = RandomStringUtils.randomAlphanumeric(8);
		while (find == true) {
			String queryQ = (String) entityManager
					.createQuery("select e.reference from Invoice e " + "where e.reference=:reference")
					.setParameter("reference", referenceInvoice).getSingleResult();
			if (referenceInvoice.equals(queryQ)) {
				find = true;
			}

			referenceInvoice = RandomStringUtils.randomAlphanumeric(8);
		}

		Quote quoteManagedEntity = entityManager.find(Quote.class, finIdQuoteByReference(reference));

		quoteManagedEntity.setGeneratedToInvoice(1);
		String email = quoteManagedEntity.getClient().getEmail();

		Invoice.setReference("TN-CL-" + referenceInvoice);
		Invoice.setDateInvoice(date);
		Invoice.setDescription("Facture");
		Invoice.setDuration(10);
		Invoice.setStatus("non payé");

		Invoice.setQuote(quoteManagedEntity);

		Invoice.setTotal(qm.calculateTotalQuoteTTC(reference));

		entityManager.persist(Invoice);

		try {
			EmailClient.sendAsHtml(email, "Test email", "<h2>Java Mail Example</h2><p>hi there!</p>");

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Invoice.getId();
	}

	@Override
	public Long finIdQuoteByReference(String reference) {
		Long queryQ = (long) entityManager.createQuery("select e.id from Quote e " + "where e.reference=:reference")
				.setParameter("reference", reference).getSingleResult();
		return queryQ;

	}

	@Override
	public Map<String, Object> getInvoiceDetails(String reference) {
		// TODO Auto-generated method stub
		Long quoteId = (long) 0;

		TypedQuery<Invoice> queryC = entityManager.createQuery(
				"select e from Invoice e" + " join e.quote t " + "where e.reference=:reference", Invoice.class);

		queryC.setParameter("reference", reference);
		List<Invoice> c = new ArrayList<Invoice>();

		for (Invoice dep : queryC.getResultList()) {
			Invoice Invoice = new Invoice(dep.getId(), dep.getReference(), dep.getDescription(), dep.getStatus(),
					dep.getDateInvoice());
			Quote quote = new Quote(dep.getQuote().getReference(), dep.getQuote().getDescription(),
					dep.getQuote().getDuration(), dep.getQuote().getDateQuote());
			quoteId = dep.getQuote().getId();
			Invoice.setQuote(quote);
			c.add(Invoice);
		}

		String quoteReference = finIdQuoteReferenceById(quoteId);
		Map<String, Object> quoteList = new HashMap<String, Object>();

		Map<String, Object> quote = qm.getQuoteWithProduct(quoteReference);
		for (Map.Entry mapentry : quote.entrySet()) {

			for (Invoice dep : queryC.getResultList()) {

				System.out.println("Date before Addition: " + dep.getDateInvoice());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cld = Calendar.getInstance();

				cld.setTime(dep.getDateInvoice());

				// Incrementing the date by 1 day
				cld.add(Calendar.DAY_OF_MONTH, dep.getDuration());
				String expiredDay = sdf.format(cld.getTime());

				quoteList.put("reference", dep.getReference());
				quoteList.put("description", dep.getDescription());
				quoteList.put("status", dep.getStatus());
				quoteList.put("InvoiceDate", dep.getDateInvoice());
				quoteList.put("expiredDay", expiredDay);

			}

			if (mapentry.getKey().equals("unitPrice") || mapentry.getKey().equals("TTC")
					|| mapentry.getKey().equals("product") || mapentry.getKey().equals("TVA")) {
				quoteList.put((String) mapentry.getKey(), mapentry.getValue());
			}
		}

		return quoteList;
	}

	@Override
	public String finIdQuoteReferenceById(Long id) {
		// TODO Auto-generated method stub
		String queryQ = (String) entityManager.createQuery("select e.reference from Quote e " + "where e.id=:id")
				.setParameter("id", id).getSingleResult();
		return queryQ;
	}

	@Override
	public List<Invoice> getInvoiceByReference(String reference) {
		TypedQuery<Invoice> queryC = entityManager.createQuery(
				"select e from Invoice e" + " join e.quote t " + "where e.reference=:reference", Invoice.class);

		queryC.setParameter("reference", reference);
		List<Invoice> c = new ArrayList<Invoice>();

		for (Invoice dep : queryC.getResultList()) {
			Invoice Invoice = new Invoice(dep.getId(), dep.getReference(), dep.getDescription(), dep.getStatus(),
					dep.getDateInvoice());
			Quote quote = new Quote(dep.getQuote().getReference(), dep.getQuote().getDescription(),
					dep.getQuote().getDuration(), dep.getQuote().getDateQuote());
			Invoice.setQuote(quote);
			c.add(Invoice);
		}

		return c;
	}

	public String toString(String reference) {
		// TODO Auto-generated method stub
		Map<String, Object> map = getInvoiceDetails(reference);
		String data = "";
		System.out.println("Boucle for:");
		for (Map.Entry mapentry : map.entrySet()) {
			data = data + " " + mapentry.getKey() + "==>>" + mapentry.getValue() + "\n";
		}
		return data;
	}

	@Override
	public void generateInvoiceToPdf(String reference) {
		// TODO Auto-generated method stub

		Document document = new Document();
		try {
			PdfWriter.getInstance(document,
					new FileOutputStream("C:/Users/Khouloud/Desktop/4TWIN2019/JEE/pdf/"+reference+".pdf"));
			document.open();
			document.add(new Paragraph(toString(reference)));
			document.close();

		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}

		System.out.println("Pdf file is executed !!!");
	}

	@Override
	public boolean deleteInvoice(Long id) {

		entityManager.remove(entityManager.find(Invoice.class, id));
		return true;

	}

	@Override
	public boolean updateInvoiceStatus(String status, Long id) {
		Invoice queryQ = entityManager.find(Invoice.class, id);
		queryQ.setStatus(status);
		return true;
	}

	@Override
	public List<Invoice> getAllInvoiceDetails() {
		// TODO Auto-generated method stub
		Long quoteId = (long) 0;

		TypedQuery<Invoice> queryC = entityManager.createQuery("select e from Invoice e" + " join e.quote t ",
				Invoice.class);

//		List<Invoice> c = new ArrayList<Invoice>();
//
//		for (Invoice dep : queryC.getResultList()) {
//			Invoice Invoice = new Invoice(dep.getId(), dep.getReference(), dep.getDescription(), dep.getStatus(),
//					dep.getDateInvoice());
//			Quote quote = new Quote(dep.getQuote().getReference(), dep.getQuote().getDescription(),
//					dep.getQuote().getDuration(), dep.getQuote().getDateQuote());
//			quoteId = dep.getQuote().getId();
//			Invoice.setQuote(quote);
//			c.add(Invoice);
//		}
//
//		String quoteReference = finIdQuoteReferenceById(quoteId);
//		Map<String, Object> quoteList = new HashMap<String, Object>();

		// Map<String, Object> quote = qm.getQuoteWithProduct(quoteReference);

		List<Invoice> o = new ArrayList<Invoice>();

		for (Invoice dep : queryC.getResultList()) {

			System.out.println("Date before Addition: " + dep.getDateInvoice());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cld = Calendar.getInstance();

			cld.setTime(dep.getDateInvoice());

			// Incrementing the date by 1 day
			cld.add(Calendar.DAY_OF_MONTH, dep.getDuration());
			String expiredDay = sdf.format(cld.getTime());

			Date expiredDayDate = null;
			try {
				expiredDayDate = sdf.parse(expiredDay);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Date currentDate = new Date();

			long difference =  currentDate.getTime()-expiredDayDate.getTime();
			float daysBetween = (difference / (1000 * 60 * 60 * 24));

			System.out.println("day -----------> " + daysBetween);

			Quote quoteManagedEntity = entityManager.find(Quote.class, dep.getQuote().getId());

			quoteManagedEntity.setGeneratedToInvoice(1);
			String email = quoteManagedEntity.getClient().getEmail();

			if (daysBetween <= 3) {

				try {
					EmailClient.sendAsHtmlWithAttachement(email, "Bonjour " + quoteManagedEntity.getClient().getPrenom(),
							"<h2>Rappel</h2><p>Vous devez payer votre facture chez "
									+ quoteManagedEntity.getClient().getOpr() + " de reference " + dep.getReference()
									+ " elle exipra le " + expiredDay + "<br/> Bien à vous, <br/> Equipe "+ quoteManagedEntity.getClient().getOpr()+" </p>","C:/Users/Khouloud/Desktop/4TWIN2019/JEE/pdf/"+dep.getReference()+".pdf");

				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Invoice ccc = new Invoice(dep.getReference(), dep.getDescription(), dep.getStatus(), dep.getDateInvoice(),
					dep.getDuration(), dep.getTotal());

//				quoteList.put("reference", dep.getReference());
//				quoteList.put("description", dep.getDescription());
//				quoteList.put("status", dep.getStatus());
//				quoteList.put("InvoiceDate", dep.getDateInvoice());
			// o.put("expiredDay", expiredDay);

			o.add(ccc);

			System.out.println("-------------------->>> " + dep.getReference());
		}

		return o;
	}

	public Long finIdInvoiceByReference(String reference) {
		Long queryQ = (long) entityManager.createQuery("select e.id from Invoice e " + "where e.reference=:reference")
				.setParameter("reference", reference).getSingleResult();
		return queryQ;

	}

	@Override
	public Long payeInvoice(Payment payment, String reference) {
		Stripe.apiKey = "sk_test_TDmpiiOhUQmiXP1BP7hEuD4900atmZUolb";
		// String token = request.getParameter("stripeToken");
		// TODO Auto-generated method stub
		double amount = 0;
		Invoice queryC = entityManager.find(Invoice.class, finIdInvoiceByReference(reference));
		amount = queryC.getTotal();

		queryC.setStatus("paied");

		payment.setAmount((int) amount);
		payment.setCurrency("eur");
		payment.setOrderId(reference);

		payment.setInvoice(queryC);

		entityManager.persist(payment);

		try {
			chargeNewCard(amount,reference);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return payment.getId();
	}
	
	 public Charge chargeNewCard(double amount,String reference) throws Exception {
	      /*  Map<String, Object> chargeParams = new HashMap<String, Object>();
	        chargeParams.put("amount", (int)(amount * 100));
	        chargeParams.put("currency", "TND");
	        chargeParams.put("source", "tok_mastercard");
	        Map<String, String> initialMetadata = new HashMap<String, String>();
	        initialMetadata.put("order_id", reference);
	        chargeParams.put("metadata", initialMetadata);
	        Charge charge = Charge.create(chargeParams);
	        return charge;*/
		 Stripe.apiKey = "sk_test_TDmpiiOhUQmiXP1BP7hEuD4900atmZUolb";

		 Map<String, Object> chargeParams = new HashMap<String, Object>();
		 chargeParams.put("amount", (int) amount);
		 chargeParams.put("currency", "eur");
		 chargeParams.put("description", "facture ref: "+reference);
		 chargeParams.put("source", "tok_visa");
		 // ^ obtained with Stripe.js

		 RequestOptions options = RequestOptions
		   .builder()
		   .setIdempotencyKey("wukveaXjEMTskrgc"+reference)
		   .build();

		 Charge charge= Charge.create(chargeParams, options);
		 return charge;
	    }
	

	@Override
	public double totalAllInvoiceNotPaied() {
		List<Invoice> invoice = getAllInvoiceDetails();
		double total = 0;
		for(Invoice i : invoice)
		{
			if(i.getStatus().equals("non payé"))
			{
				total = total + i.getTotal();
			}
		}
			return total;
	}
}
