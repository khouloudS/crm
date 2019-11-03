package tn.esprit.crm.Resources;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.crm.entities.ProductQuoteDetails;
import tn.esprit.crm.entities.Quote;
import tn.esprit.crm.impl.QuoteManagement;
import tn.esprit.crm.inter.IQuoteRemote;

@Path("quote")
public class QuoteRessource {
	@EJB
	IQuoteRemote qm;
	// QuoteManagement qm = new QuoteManagement();
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addInfoEmploye(Quote quote) {
		if (qm.generateQuote(quote) != 0) {
			System.out.println("created");
			return Response.status(Status.CREATED).build();

		}

		return Response.status(Status.BAD_REQUEST).build();

	}

	@POST
	@Path("addProductQuoteDetails/{quoteId}/{productId}/{quantity}")

	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProductQuoteDetails(@PathParam("quoteId") Long quoteId, @PathParam("productId") Long productId,
			@PathParam("quantity") int quantity) {

		return Response.status(Status.ACCEPTED).entity(qm.addProductQuoteDetails(quoteId, productId, quantity)).build();

	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response rechercheId(@PathParam(value = "id") String id) {

		return Response.status(Status.ACCEPTED).entity(qm.findAllProductByQuote(id)).build();

	}

	@DELETE
	@Path("{quoteId}/{productId}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response deleteProductFromQuote(@PathParam("quoteId") Long quoteId, @PathParam("productId") Long productId) {
		return Response.status(Status.ACCEPTED).entity(qm.deleteProductFromDevis(quoteId, productId)).build();
	}

	@PUT
	@Path("{quoteId}/{productId}/{quantity}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response updateProductQuantity(@PathParam("quoteId") Long quoteId, @PathParam("productId") Long productId,
			@PathParam("quantity") int quantity) {
		return Response.status(Status.ACCEPTED).entity(qm.updateProductQuantity(quoteId, productId, quantity)).build();
	}

	@GET
	@Path("calculateTotalQuoteUP/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response calculateTotalQuoteUP(@PathParam(value = "id") String id) {

		return Response.status(Status.ACCEPTED).entity(qm.calculateTotalQuoteUP(id)).build();

	}

	@GET
	@Path("calculateTotalQuoteTva/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response calculateTotalQuoteTva(@PathParam(value = "id") String id) {

		return Response.status(Status.ACCEPTED).entity(qm.calculateTotalQuoteTva(id)).build();

	}

	@GET
	@Path("calculateTotalQuoteTTC/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response calculateTotalQuoteTTC(@PathParam(value = "id") String id) {

		return Response.status(Status.ACCEPTED).entity(qm.calculateTotalQuoteTTC(id)).build();

	}

	@GET
	@Path("getQuoteWithProduct/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getQuoteWithProduct(@PathParam(value = "id") String id) {
		qm.generateQuotePDF(id);

		return Response.status(Status.ACCEPTED).entity(qm.getQuoteWithProduct(id)).build();

	}

	@GET
	@Path("notificationExpirationQuote")
	@Produces(MediaType.APPLICATION_JSON)
	public Response notificationExpirationQuote() {
		// return
		// Response.status(Status.ACCEPTED).entity(qm.notificationExpirationQuote(date)).build();
		return Response.status(Status.ACCEPTED).entity(qm.notificationExpirationQuote()).build();

	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)

	public Response deleteQuoteExpired() {
		return Response.status(Status.ACCEPTED).entity(qm.deleteQuoteExpired()).build();
	}

	@PUT
	@Path("updateQuoteDetails/{description}/{duration}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response updateQuoteDetails(@PathParam("description") String description,
			@PathParam("duration") int duration) {
		return Response.status(Status.ACCEPTED).entity(qm.updateQuoteDetails(description, duration)).build();
	}
	
	@GET
	@Path("getAllQuoteDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllQuoteDetails() {
		// return
		// Response.status(Status.ACCEPTED).entity(qm.notificationExpirationQuote(date)).build();
		return Response.status(Status.ACCEPTED).entity(qm.getAllQuoteDetails()).build();

	}
	
	@GET
	@Path("getAllQuoteDetailsByClient/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllQuoteDetailsByClient(@PathParam("id") int id) {
		// return
		// Response.status(Status.ACCEPTED).entity(qm.notificationExpirationQuote(date)).build();
		return Response.status(Status.ACCEPTED).entity(qm.getAllQuoteDetailsByClient(id)).build();

	}
	
	@GET
	@Path("countNumberQuoteGeneratedToInvoice")
	@Produces(MediaType.APPLICATION_JSON)

	public Response countNumberQuoteGeneratedToInvoice() {

		return Response.status(Status.ACCEPTED).entity(qm.countNumberQuoteGeneratedToInvoice()).build();

	}
}
