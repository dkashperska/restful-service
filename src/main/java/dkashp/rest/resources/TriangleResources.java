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

import dkashp.rest.dto.Triangle;
import dkashp.rest.errorhandling.AppException;
import dkashp.rest.hibernate.utils.HibernateUtil;

@Path("triangles")
public class TriangleResources {

	Logger logger = LoggerFactory.getLogger(TriangleResources.class);
	
	@Context
	UriInfo uri;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTriangle(@PathParam("id") int id) throws AppException{
		logger.info("Method getTriangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Triangle triangle;
		try{
			session.beginTransaction();
			triangle = (Triangle) session.get(Triangle.class, id);
			if(triangle == null){
				logger.error("The shape with the id {} was not found in database", id);
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
						"The shape your requested with the id " + id +  " was not found in database", 
						"Verify the existence of the shape with the id " + id + " in database");
			} else {
				logger.info("The shape with the id {} exists", id);
				return Response.status(200).entity(triangle).build();
			}
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTriangle(Triangle triangle) throws AppException{
		logger.info("Method createTriangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			double square = 0.5 * triangle.getBase() * triangle.getHeight();
			triangle.setSquare(square);
			session.save(triangle);
			logger.info("A new shape with the id {} has been created", triangle.getId());
			return Response.status(Response.Status.CREATED).entity("A new shape has been created").
					header("Location", generateLocation(triangle.getId())).build();
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFullyTriangle(@PathParam("id") int id, Triangle triangle) throws AppException{
		logger.info("Method updateFullyTriangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		triangle.setId(id);
		if (isFullUpdate(triangle)){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 
					"Please specify all properties for Full UPDATE",
					"required properties - base, height, square");
		}
		try{
			session.beginTransaction();
			session.update(triangle);
			logger.info("The shape with the id {} has been updated fully" + id);
			return Response.status(Response.Status.OK).entity("The shape you specified has been fully updated")
					.header("Location", generateLocation(triangle.getId())).build();
		} catch (HibernateException e){
			logger.error("The shape with the id {} was not found in database", id);
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + triangle.getId());
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@POST
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response partialUpdateTriangle(@PathParam("id") int id, Triangle triangle) throws AppException{
		logger.info("Method partialUpdateTriangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Triangle oldTriangle = null;
		double square;
		try {
			session.beginTransaction();
			oldTriangle = (Triangle) session.get(Triangle.class, id);
			if (oldTriangle == null) {
				logger.error("The shape with the id {} was not found in database", id);
				throw new AppException(
						Response.Status.NOT_FOUND.getStatusCode(),
						"The resource you are trying to update does not exist in the database",
						"Please verify existence of data in the database for the id - " + id);
			}
			if (triangle.getBase() != 0) {
				oldTriangle.setBase(triangle.getBase());
				square = 0.5 * triangle.getBase() * oldTriangle.getHeight();
				oldTriangle.setSquare(square);
			}
			if (triangle.getHeight() != 0) {
				oldTriangle.setHeight(triangle.getHeight());
				square = 0.5 * triangle.getHeight() * oldTriangle.getBase();
				oldTriangle.setSquare(square);
			}
			if (triangle.getBase() != 0 && triangle.getHeight() != 0) {
				square = 0.5 * triangle.getBase() * triangle.getHeight();
				oldTriangle.setSquare(square);
			}
			if (triangle.getSquare() != 0) {
				oldTriangle.setSquare(triangle.getSquare());
			}
			logger.info("The shape with the id {} has been updated partially" + id);
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
	public Response removeTriangle(@PathParam("id") int id) throws AppException{
		logger.info("Method removeTriangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Triangle triangle;
		try{
			session.beginTransaction();
			triangle = (Triangle) session.get(Triangle.class, id);
			if(triangle == null){
				logger.error("The shape with the id {} was not found in database", id);
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
						"The resource you are trying to delete does not exist in the database",
						"Please verify existence of data in the database for the id - " + id);
			}
			logger.info("Remove shape with id {}", id);
			session.delete(triangle);
			return Response.status(Response.Status.NO_CONTENT).entity("Shape successfully removed from database").build();
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	private boolean isFullUpdate(Triangle triangle){
		return triangle.getId() == 0
				|| triangle.getBase() == 0
				|| triangle.getHeight() == 0
				|| triangle.getSquare() == 0;
	}
	
	private String generateLocation(int id){
		return uri.getAbsolutePath() + "/" + id;
	}
}
