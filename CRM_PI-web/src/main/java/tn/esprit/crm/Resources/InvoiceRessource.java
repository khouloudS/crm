package tn.esprit.crm.Resources;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.crm.entities.Invoice;
import tn.esprit.crm.entities.Payment;
import tn.esprit.crm.entities.Quote;
import tn.esprit.crm.inter.IInvoiceRemote;
import tn.esprit.crm.inter.IQuoteRemote;

@Path("invoice")
public class InvoiceRessource {
	@EJB
	IInvoiceRemote qm;
	// QuoteManagement qm = new QuoteManagement();

	@POST
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateInvoice(Invoice invoice, @PathParam(value = "id") String id) {
		if (qm.generateInvoice(invoice, id) != 0) {
			System.out.println("created");
			return Response.status(Status.CREATED).build();

		}

		return Response.status(Status.BAD_REQUEST).build();

	}

	@GET
	@Path("finIdQuoteByReference/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response finIdQuoteByReference(@PathParam(value = "id") String id) {

		return Response.status(Status.ACCEPTED).entity(qm.finIdQuoteByReference(id)).build();

	}

	@GET
	@Path("finIdQuoteReferenceById/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response finIdQuoteReferenceById(@PathParam(value = "id") Long id) {

		return Response.status(Status.ACCEPTED).entity(qm.finIdQuoteReferenceById(id)).build();

	}

	@GET
	@Path("getInvoiceDetails/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getInvoiceDetails(@PathParam(value = "id") String id) {
		qm.generateInvoiceToPdf(id);
		return Response.status(Status.ACCEPTED).entity(qm.getInvoiceDetails(id)).build();

	}

	@GET
	@Path("getInvoiceByReference/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getInvoiceByReference(@PathParam(value = "id") String id) {

		return Response.status(Status.ACCEPTED).entity(qm.getInvoiceByReference(id)).build();

	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response deleteInvoice(@PathParam("id") Long id) {
		return Response.status(Status.ACCEPTED).entity(qm.deleteInvoice(id)).build();
	}
	
	@PUT
	@Path("updateInvoiceStatus/{id}/{status}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response updateInvoiceStatus(@PathParam("id") Long id,@PathParam("status") String status) {
		return Response.status(Status.ACCEPTED).entity(qm.updateInvoiceStatus(status, id)).build();
	}
	
	@GET
	@Path("getAllInvoiceDetails")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getAllInvoiceDetails() {
		return Response.status(Status.ACCEPTED).entity(qm.getAllInvoiceDetails()).build();

	}
	
	
	@POST
	@Path("payeInvoice/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response payeInvoice(Payment payment, @PathParam(value = "id") String id) {
		if (qm.payeInvoice(payment, id) != 0) {
			System.out.println("created");
			return Response.status(Status.CREATED).entity(qm.payeInvoice(payment, id)).build();

		}

		return Response.status(Status.BAD_REQUEST).build();

	}
	
	@POST
	@Path("chargeNewCard/{amount}/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response chargeNewCard(@PathParam(value = "amount") double amount,@PathParam(value = "id") String id) throws Exception {
	
			return Response.status(Status.CREATED).entity(qm.chargeNewCard(amount,id)).build();

	}
	
	@GET
	@Path("totalAllInvoiceNotPaied")
	@Produces(MediaType.APPLICATION_JSON)

	public Response countNumberQuoteGeneratedToInvoice() {

		return Response.status(Status.ACCEPTED).entity(qm.totalAllInvoiceNotPaied()).build();

	}
}
