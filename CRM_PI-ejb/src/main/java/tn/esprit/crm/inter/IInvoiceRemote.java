package tn.esprit.crm.inter;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.stripe.model.Charge;

import tn.esprit.crm.entities.Invoice;
import tn.esprit.crm.entities.Payment;
import tn.esprit.crm.entities.Quote;

@Local
public interface IInvoiceRemote {

	public Long generateInvoice(Invoice Invoice, String reference);

	public Long finIdQuoteByReference(String reference);

	public String finIdQuoteReferenceById(Long id);

	public Map<String, Object> getInvoiceDetails(String reference);

	public List<Invoice> getInvoiceByReference(String reference);

	public void generateInvoiceToPdf(String reference);

	public boolean deleteInvoice(Long id);

	public boolean updateInvoiceStatus(String status, Long id);

	public List<Invoice> getAllInvoiceDetails();
	
	public Long payeInvoice(Payment payment,String reference);
	
	public double totalAllInvoiceNotPaied();
	
	 public Charge chargeNewCard(double amount,String reference) throws Exception;
}
