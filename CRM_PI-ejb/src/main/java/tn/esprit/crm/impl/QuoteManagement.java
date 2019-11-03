package tn.esprit.crm.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.RandomStringUtils;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import tn.esprit.crm.entities.Client;
import tn.esprit.crm.entities.Product;
import tn.esprit.crm.entities.ProductQuoteDetails;
import tn.esprit.crm.entities.ProductQuoteDetailsPk;
import tn.esprit.crm.entities.Quote;
import tn.esprit.crm.inter.IQuoteRemote;

@Stateful
@LocalBean
public class QuoteManagement implements IQuoteRemote {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Long generateQuote(Quote quote) {
		boolean find = false;
		Date date = new Date();
		String reference = RandomStringUtils.randomAlphanumeric(8);
		while (find == true) {
			String queryQ = (String) entityManager
					.createQuery("select e.reference from Quote e " + "where e.reference=:reference")
					.setParameter("reference", reference).getSingleResult();
			if (reference.equals(queryQ)) {
				find = true;
			}

			reference = RandomStringUtils.randomAlphanumeric(8);
		}

		quote.setReference("TN-" + reference);
		quote.setDateQuote(date);
		quote.setDescription("Devis");
		quote.setDuration(10);
		quote.setGeneratedToInvoice(0);

		entityManager.persist(quote);

		return quote.getId();
	}

	@Override
	public List<Product> findAllProductByQuote(String id) {

		TypedQuery<Product> query = entityManager.createQuery("select e from Product e "
				+ "join e.productQuoteDetails t " + "join t.quote m " + "where m.reference=:id", Product.class);

		query.setParameter("id", id);

		return query.getResultList();
	}

	@Override
	public Long addProductQuoteDetails(Long productId, Long quoteId, int quantity) {
		// TODO Auto-generated method stub
		ProductQuoteDetailsPk productQuoteDetailsPk = new ProductQuoteDetailsPk();
		productQuoteDetailsPk.setQuantity(quantity);
		productQuoteDetailsPk.setQuoteId(quoteId);
		productQuoteDetailsPk.setProductId(productId);

		ProductQuoteDetails productQuoteDetails = new ProductQuoteDetails();
		productQuoteDetails.setProductQuoteDetailsPk(productQuoteDetailsPk);

		entityManager.persist(productQuoteDetails);

		Long query = (long) entityManager.createQuery(
				"select count(c) from ProductQuoteDetails c where c.productQuoteDetailsPk.productId =:productId AND c.productQuoteDetailsPk.quoteId=:quoteId")
				.setParameter("quoteId", quoteId).setParameter("productId", productId).getFirstResult();
		return query;
	}

	@Override
	public boolean deleteProductFromDevis(Long quoteId, Long productId) {

		Query query = entityManager.createQuery(
				"DELETE FROM ProductQuoteDetails c WHERE c.productQuoteDetailsPk.productId =:productId AND c.productQuoteDetailsPk.quoteId=:quoteId");
		query.setParameter("productId", productId).setParameter("quoteId", quoteId);
		query.executeUpdate();
		System.out.println("helllooooo");
		return true;
	}

	@Override
	public double calculateTotalQuoteUP(String id) {

		float query = 0;
		List<Product> lst = findAllProductByQuote(id);
		for (Product p : lst) {
			Long productId = p.getId();
			Long quoteId = (long) entityManager.createQuery("select e.id from Quote e " + "where e.reference=:id")
					.setParameter("id", id).getSingleResult();
			
			int quantity = (int)entityManager.createQuery(
					"select c.productQuoteDetailsPk.quantity FROM ProductQuoteDetails c WHERE c.productQuoteDetailsPk.productId =:productId AND c.productQuoteDetailsPk.quoteId=:quoteId")
			         .setParameter("productId", productId).setParameter("quoteId", quoteId).getFirstResult();
			
			System.out.println("quantity "+quantity+" quoteId "+quoteId+ " prodId "+productId );
			
			if (p.getPromotion() == null) {
				query = query + (p.getPrix()*1);
			} else if (p.getPromotion() != null) {
				query = query + ((p.getPrix() - ((p.getPrix() * p.getPromotion().getPercentage()) / 100))*1);
			}
		}
//		Double query = (Double) entityManager.createQuery("select sum(e.prix) from Product e "
//				+ "join e.productQuoteDetails t " + "join t.quote m " + "where m.reference=:id").setParameter("id", id)
//				.getSingleResult();

		return query;

	}

	@Override
	public double calculateTotalQuoteTva(String id) {
		// TODO Auto-generated method stub
		Double query = (Double) entityManager.createQuery("select Avg(e.tva) from Product e "
				+ "join e.productQuoteDetails t " + "join t.quote m " + "where m.reference=:id").setParameter("id", id)
				.getSingleResult();
		return query;

	}

	@Override
	public double calculateTotalQuoteTTC(String reference) {
		// TODO Auto-generated method stub
		return calculateTotalQuoteUP(reference)
				+ ((calculateTotalQuoteUP(reference) * calculateTotalQuoteTva(reference) / 100));
	}

	
	public Long finIdQuoteByReference(String reference) {
		Long queryQ = (long) entityManager.createQuery("select e.id from Quote e " + "where e.reference=:reference")
				.setParameter("reference", reference).getSingleResult();
		return queryQ;

	}
	
	@Override
	public Map<String, Object> getQuoteWithProduct(String id) {

		TypedQuery<Quote> queryQ = entityManager.createQuery("select e from Quote e " + "where e.reference=:id",
				Quote.class);

		queryQ.setParameter("id", id);
		
		

		TypedQuery<Product> queryP = entityManager.createQuery("select e from Product e "
				+ "join e.productQuoteDetails t " + "join t.quote m " + "where m.reference=:id", Product.class);

		queryP.setParameter("id", id);
		
		

		Map<String, Object> quoteList = new HashMap<String, Object>();
		List<Product> productList = new ArrayList<>();

		// List<Object> quoteList = new ArrayList<>();
		// List<Object> productList = new ArrayList<>();
		for (Product pro : queryP.getResultList()) {
			Product p = new Product(pro.getName(), pro.getTva(), pro.getPrix(),pro.getPromotion());
			productList.add(p);
		}
		for (Quote dep : queryQ.getResultList()) {

			quoteList.put("firstname", dep.getClient().getPrenom());
			quoteList.put("name", dep.getClient().getNom());
			quoteList.put("address", dep.getClient().getAdresse());
			quoteList.put("reference", dep.getReference());
			quoteList.put("description", dep.getDescription());
			quoteList.put("date", dep.getDateQuote());
			quoteList.put("validity", dep.getDuration());
			quoteList.put("TTC", calculateTotalQuoteUP(id));
			quoteList.put("TVA", calculateTotalQuoteTva(id));
			quoteList.put("unitPrice", calculateTotalQuoteTTC(id));
			quoteList.put("product", productList);
		}

		return quoteList;
	}

	public String toString(String reference) {
		// TODO Auto-generated method stub
		Map<String, Object> map = getQuoteWithProduct(reference);
		String data = "";
		System.out.println("Boucle for:");
		for (Map.Entry mapentry : map.entrySet()) {
			data = data + "clé: " + mapentry.getKey() + " | valeur: " + mapentry.getValue() + "\n";
		}
		return data;
	}

	@Override
	public void generateQuotePDF(String reference) {
		// TODO Auto-generated method stub

		Document document = new Document();
		try {
			PdfWriter.getInstance(document,
					new FileOutputStream("C:/Users/Khouloud/Desktop/4TWIN2019/JEE/pdf/fichier5.pdf"));
			document.open();
			document.add(new Paragraph(toString(reference)));
			document.close();

		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Pdf file is executed !!!");
	}

	@Override
	public boolean updateProductQuantity(Long quoteId, Long productId, int quantity) {
		// TODO Auto-generated method stub
		Query query = entityManager.createQuery(
				"update ProductQuoteDetails c set productQuoteDetailsPk.quantity=:quantity where c.productQuoteDetailsPk.productId =:productId AND c.productQuoteDetailsPk.quoteId=:quoteId");
		query.setParameter("quantity", quantity);
		query.setParameter("quoteId", quoteId);
		query.setParameter("productId", productId);

		int modified = query.executeUpdate();
		if (modified != 1) {
			return false;
		}
		return true;
	}

	@Override
	public List<Quote> notificationExpirationQuote() {
		// TODO Auto-generated method stub
		int dif = 3;
		TypedQuery<Quote> query = entityManager.createQuery("select e from Quote e "

				+ "WHERE DATEDIFF(ADDDATE(e.dateQuote, e.duration),CURRENT_DATE )<=:dif and e.generatedToInvoice=0",
				Quote.class).setParameter("dif", dif);

		List<Quote> obj = new ArrayList<Quote>();

		for (Quote em : query.getResultList()) {
			System.out.println(" Reference --->> " + em.getReference());

			Quote q = new Quote(em.getId(), em.getReference(), em.getDescription(), em.getDuration(),
					em.getDateQuote());

			obj.add(q);
		}
		return obj;

	}

	@Override
	public boolean deleteQuoteExpired() {

		int dif = 0;
		Query query = entityManager.createQuery(
				"DELETE FROM Quote e WHERE DATEDIFF(ADDDATE(e.dateQuote, e.duration),CURRENT_DATE )<=:dif and e.generatedToInvoice=:dif")
				.setParameter("dif", dif);
		query.executeUpdate();
		System.out.println("deleted");
		return true;
	}

	@Override
	public boolean updateQuoteDetails(String description, int duration) {
		// TODO Auto-generated method stub
		TypedQuery<Quote> queryQ = entityManager.createQuery("select e from Quote e ", Quote.class);
		for (Quote q : queryQ.getResultList()) {
			q.setDescription(description);
			q.setDuration(duration);
		}
		return true;
	}

	@Override
	public List<Quote> getAllQuoteDetails() {
		// TODO Auto-generated method stub
		boolean deleteQuoteExpired = deleteQuoteExpired();
		List<Quote> getAll = new ArrayList<Quote>();
		if (deleteQuoteExpired == true) {
			List<Quote> queryQ = entityManager.createQuery("select e  from Quote e").getResultList();

			for (Quote q : queryQ) {

				Quote qq = new Quote(q.getId(), q.getReference(), q.getDescription(), q.getDuration(),
						q.getDateQuote());
				getAll.add(qq);
			}
		}
		return getAll;
	}

	@Override
	public List<Quote> getAllQuoteDetailsByClient(int id) {
		boolean deleteQuoteExpired = deleteQuoteExpired();
		List<Quote> getAll = new ArrayList<Quote>();
		if (deleteQuoteExpired == true) {
			List<Quote> queryQ = entityManager.createQuery("select e  from Quote e").getResultList();

			for (Quote q : queryQ) {
				if (q.getClient().getId() == id) {
					Quote qq = new Quote(q.getId(), q.getReference(), q.getDescription(), q.getDuration(),
							q.getDateQuote());
					getAll.add(qq);
				}
			}
		}
		return getAll;
	}

	@Override
	public Long countNumberQuoteGeneratedToInvoice() {
		// TODO Auto-generated method stub
		Long query = (Long) entityManager.createQuery("select count(e.id)  from Quote e where e.generatedToInvoice = 1")
				.getSingleResult();
		return query;
	}

}
