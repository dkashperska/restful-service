package dkashp.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dkashp.rest.dto.Rectangle;
import dkashp.rest.errorhandling.AppException;
import dkashp.rest.hibernate.utils.HibernateUtil;

@Path("rectangles")
public class RectangleResources {
	
	Logger logger = LoggerFactory.getLogger(RectangleResources.class);
	@Context
	UriInfo uri;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRectangle(@PathParam("id") int id) throws AppException{
		logger.info("Method getRectangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Rectangle rectangle;
		try{
			session.beginTransaction();
			rectangle = (Rectangle) session.get(Rectangle.class, id);
			if(rectangle == null){
				logger.error("The shape with the id {} was not found in database", id);
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
						"The shape your requested with the id " + id +  " was not found in database", 
						"Verify the existence of the shape with the id " + id + " in database");
			} else {
				logger.info("The shape with the id {} exists", id);
				return Response.status(200).entity(rectangle).build();
			}
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRectangle(Rectangle rectangle) throws AppException{
		logger.info("Method createRectangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			double square = rectangle.getLength()*rectangle.getWidth();
			rectangle.setSquare(square);
			session.save(rectangle);
			logger.info("A new shape with the id {} has been created", rectangle.getId());
			return Response.status(Response.Status.CREATED).entity("A new shape has been created").
					header("Location", generateLocation(rectangle.getId())).build();
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFullyRectangle(@PathParam("id") int id, Rectangle rectangle) throws AppException{
		logger.info("Method updateFullyRectangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		rectangle.setId(id);
		if (isFullUpdate(rectangle)){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 
					"Please specify all properties for Full UPDATE",
					"required properties - length, width, square");
		}
		try{
			session.beginTransaction();
			session.update(rectangle);
			logger.info("The shape with the id {} has been updated fully" + id);
			return Response.status(Response.Status.OK).entity("The shape you specified has been fully updated")
					.header("Location", generateLocation(rectangle.getId())).build();
		} catch (HibernateException e){
			logger.error("The shape with the id {} was not found in database", id);
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + rectangle.getId());
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@POST
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response partialUpdateRectangle(@PathParam("id") int id, Rectangle rectangle) throws AppException{
		logger.info("Method partialUpdateRectangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Rectangle oldRectangle = null;
		double square;
		try {
			session.beginTransaction();
			oldRectangle = (Rectangle) session.get(Rectangle.class, id);
			if (oldRectangle == null) {
				logger.error("The shape with the id {} was not found in database", id);
				throw new AppException(
						Response.Status.NOT_FOUND.getStatusCode(),
						"The resource you are trying to update does not exist in the database",
						"Please verify existence of data in the database for the id - "+ id);
			}
			if (rectangle.getWidth() != 0) {
				oldRectangle.setWidth(rectangle.getWidth());
				square = rectangle.getWidth() * oldRectangle.getLength();
				oldRectangle.setSquare(square);
			}
			if (rectangle.getLength() != 0) {
				oldRectangle.setLength(rectangle.getLength());
				square = rectangle.getLength() * oldRectangle.getWidth();
				oldRectangle.setSquare(square);
			}
			if (rectangle.getWidth() != 0 && rectangle.getLength() != 0) {
				square = rectangle.getLength() * rectangle.getWidth();
				oldRectangle.setSquare(square);
			}
			if (rectangle.getSquare() != 0) {
				oldRectangle.setSquare(rectangle.getSquare());
			}
			logger.info("The shape with the id {} has been updated partially"+ id);
			return Response
					.status(Response.Status.OK)
					.entity("The shape you specified has been successfully updated")
					.build();
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeRectangle(@PathParam("id") int id) throws AppException{
		logger.info("Method removeRectangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Rectangle rectangle;
		try{
			session.beginTransaction();
			rectangle = (Rectangle) session.get(Rectangle.class, id);
			if(rectangle == null){
				logger.error("The shape with the id {} was not found in database", id);
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
						"The resource you are trying to delete does not exist in the database",
						"Please verify existence of data in the database for the id - " + id);
			}
			logger.info("Remove shape with id {}", id);
			session.delete(rectangle);
			return Response.status(Response.Status.NO_CONTENT).entity("Shape successfully removed from database").build();
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}

	private boolean isFullUpdate(Rectangle rectangle){
		return rectangle.getId() == 0
				|| rectangle.getLength() == 0
				|| rectangle.getWidth() == 0
				|| rectangle.getSquare() == 0;
	}
	
	private String generateLocation(int id){
		return uri.getAbsolutePath() + "/" + id;
	}
}
