package dkashp.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dkashp.rest.dto.Rectangle;
import dkashp.rest.hibernate.utils.HibernateUtil;
import dkashp.rest.response.RestResponse;

@Path("rectangles")
public class RectangleResources {
	
	Logger logger = LoggerFactory.getLogger(RectangleResources.class);
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<Rectangle> getRectangle(@PathParam("id") int id) {
		logger.debug("Method getRectangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Rectangle> response = new RestResponse<Rectangle>();
		Rectangle rectangle;
		try{
			session.beginTransaction();
			rectangle = (Rectangle) session.get(Rectangle.class, id);
			if(rectangle == null){
				logger.error("There is no shape with id {}", id);
				response.setStatus("400");
				response.setMessage("There is no shape with id=" + id);
				return response;
			} else {
				logger.debug("Get shape with id {}", id);
				response.setStatus("200");
				response.setMessage("Shape exist");
				response.setObject(rectangle);
			}
		} finally{
			session.getTransaction().commit();
			session.close();
		}
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<Rectangle> createRectangle(Rectangle rectangle) {
		logger.debug("Method createRectangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Rectangle> response = new RestResponse<Rectangle>();
		try{
			session.beginTransaction();
			double square = rectangle.getLength()*rectangle.getWidth();
			rectangle.setSquare(square);
			session.save(rectangle);
			logger.debug("Create shape with id {}", rectangle.getId());
			response.setStatus("200");
			response.setMessage("Shape created");
			response.setObject(rectangle);
		} finally{
			session.getTransaction().commit();
			session.close();
		}
		return response;
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<Rectangle> updateRectangle(@PathParam("id") int id, Rectangle rectangle){
		logger.debug("Method updateRectangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Rectangle> response = new RestResponse<Rectangle>();
		Rectangle oldRectangle = null;
		double square;
		try{
			session.beginTransaction();
			if(id != 0){
				oldRectangle = (Rectangle) session.get(Rectangle.class, id);
				if(oldRectangle == null){
					logger.error("There is no shape with id {}", id);
					response.setStatus("400");
					response.setMessage("There is no shape with id=" + id);
					return response;
				} else {
					if(rectangle.getWidth() != 0){
						oldRectangle.setWidth(rectangle.getWidth());
						square = rectangle.getWidth() * oldRectangle.getLength();
						oldRectangle.setSquare(square);
					}
					if(rectangle.getLength() != 0){
						oldRectangle.setLength(rectangle.getLength());
						square = rectangle.getLength() * oldRectangle.getWidth();
						oldRectangle.setSquare(square);
					}
					if (rectangle.getWidth() != 0 && rectangle.getLength() != 0) {
						square = rectangle.getLength()*rectangle.getWidth();
						oldRectangle.setSquare(square);
					}
					if (rectangle.getSquare() != 0) {
						oldRectangle.setSquare(rectangle.getSquare());
					}
					logger.debug("Update shape with id {}", id);
					response.setStatus("200");
					response.setMessage("Shape updated");
					response.setObject(oldRectangle);
			}
			} else{
				logger.error("Wrong request. There is no id ");
				response.setStatus("400");
				response.setMessage("Wrong request. There is no id");
			}
		} finally{
			session.getTransaction().commit();
			session.close();
		}
		return response;
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<Rectangle> removeRectangle(@PathParam("id") int id){
		logger.debug("Method removeRectangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Rectangle> response = new RestResponse<Rectangle>();
		Rectangle rectangle;
		try{
			session.beginTransaction();
			rectangle = (Rectangle) session.get(Rectangle.class, id);
			if(rectangle == null){
				logger.error("There is no shape with id {}", id);
				response.setStatus("400");
				response.setMessage("There is no shape with id=" + id);
				return response;
			} else {
				logger.debug("Remove shape with id {}", id);
				response.setStatus("200");
				response.setMessage("Shape deleted");
			}
			session.delete(rectangle);
		} finally{
			session.getTransaction().commit();
			session.close();
		}
		return response;
	}

}
