package tn.esprit.crm.inter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import tn.esprit.crm.entities.Client;
import tn.esprit.crm.entities.Product;
import tn.esprit.crm.entities.ProductQuoteDetails;
import tn.esprit.crm.entities.Quote;

@Local
public interface IQuoteRemote {
	public Long generateQuote(Quote quote);

	public List<Product> findAllProductByQuote(String reference);

	public Long addProductQuoteDetails(Long productId, Long quoteId, int quantity);

	public boolean deleteProductFromDevis(Long quoteId, Long productId);

	public double calculateTotalQuoteUP(String reference);

	public double calculateTotalQuoteTva(String reference);

	public double calculateTotalQuoteTTC(String reference);

	public Map<String, Object> getQuoteWithProduct(String reference);

	public void generateQuotePDF(String reference);

	public boolean updateProductQuantity(Long quoteId, Long productId, int quantity);

	public List<Quote> notificationExpirationQuote();
	
	public boolean deleteQuoteExpired();
	
	public boolean updateQuoteDetails(String description,int duration);
	
	public List<Quote>getAllQuoteDetails();
	
	public List<Quote>getAllQuoteDetailsByClient(int id);
	
	public Long countNumberQuoteGeneratedToInvoice();
	
	

}
