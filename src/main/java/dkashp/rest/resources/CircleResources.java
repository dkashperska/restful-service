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

import dkashp.rest.dto.Circle;
import dkashp.rest.errorhandling.AppException;
import dkashp.rest.hibernate.utils.HibernateUtil;

@Path("circles")
public class CircleResources{

	Logger logger = LoggerFactory.getLogger(CircleResources.class);
	@Context
	UriInfo uri;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCircle(@PathParam("id") int id) throws AppException{
		logger.info("Method getCircle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Circle circle;
		try{
			session.beginTransaction();
			circle = (Circle) session.get(Circle.class, id);
			if(circle == null){
				logger.error("The shape with the id {} was not found in database", id);
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
						"The shape your requested with the id " + id +  " was not found in database", 
						"Verify the existence of the shape with the id " + id + " in database");
			} else {
				logger.info("The shape with the id {} exists", id);
				return Response.status(200).entity(circle).build();
			}
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCircle(Circle circle) throws AppException{
		logger.info("Method createCircle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			double square = Math.pow(circle.getRadius(), 2)*Math.PI;
			circle.setSquare(square);
			session.save(circle);
			logger.info("A new shape with the id {} has been created", circle.getId());
			return Response.status(Response.Status.CREATED).entity("A new shape has been created").
					header("Location", generateLocation(circle.getId())).build();
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFullyCircle(@PathParam("id") int id, Circle circle) throws AppException{
		logger.info("Method updateFullyCircle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		circle.setId(id);
		if (isFullUpdate(circle)){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 
					"Please specify all properties for Full UPDATE",
					"required properties - radius, square");
		}
		try{
			session.beginTransaction();
			session.update(circle);
			logger.info("The shape with the id {} has been updated fully" + id);
			return Response.status(Response.Status.OK).entity("The shape you specified has been fully updated")
					.header("Location", generateLocation(circle.getId())).build();
		} catch (HibernateException e){
			logger.error("The shape with the id {} was not found in database", id);
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + circle.getId());
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@POST
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response partialUpdateCircle(@PathParam("id") int id, Circle circle) throws AppException{
		logger.info("Method partialUpdateCircle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Circle oldCircle = null;
		try{
			session.beginTransaction();
			oldCircle = (Circle) session.get(Circle.class, id);
			if(oldCircle == null){
				logger.error("The shape with the id {} was not found in database", id);
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
						"The resource you are trying to update does not exist in the database",
						"Please verify existence of data in the database for the id - " + id);
			}
			if (circle.getRadius() != 0) {
				oldCircle.setRadius(circle.getRadius());
				double square = Math.pow(circle.getRadius(), 2) * Math.PI;
				oldCircle.setSquare(square);
			}
			if (circle.getSquare() != 0) {
				oldCircle.setSquare(circle.getSquare());
			}
			logger.info("The shape with the id {} has been updated partially" + id);
			return Response.status(Response.Status.OK).entity("The shape you specified has been successfully updated").build();
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCircle(@PathParam("id") int id) throws AppException{
		logger.info("Method removeCircle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Circle circle;
		try{
			session.beginTransaction();
			circle = (Circle) session.get(Circle.class, id);
			if(circle == null){
				logger.error("The shape with the id {} was not found in database", id);
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
						"The resource you are trying to delete does not exist in the database",
						"Please verify existence of data in the database for the id - " + id);
			}
			logger.info("Remove shape with id {}", id);
			session.delete(circle);
			return Response.status(Response.Status.NO_CONTENT).entity("Shape successfully removed from database").build();
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}
	
	private boolean isFullUpdate(Circle circle){
		return circle.getId() == 0
				|| circle.getRadius() == 0
				|| circle.getSquare() == 0;
	}
	
	private String generateLocation(int id){
		return uri.getAbsolutePath() + "/" + id;
	}
}
