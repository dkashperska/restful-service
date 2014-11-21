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

import dkashp.rest.dto.Circle;
import dkashp.rest.hibernate.utils.HibernateUtil;
import dkashp.rest.response.RestResponse;

@Path("circles")
public class CircleResources{

	Logger logger = LoggerFactory.getLogger(CircleResources.class);
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<Circle> getCircle(@PathParam("id") int id) {
		logger.debug("Method getCircle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Circle> response = new RestResponse<Circle>();
		Circle circle;
		try{
			session.beginTransaction();
			circle = (Circle) session.get(Circle.class, id);
			if(circle == null){
				logger.error("There is no shape with id {}", id);
				response.setStatus("400");
				response.setMessage("There is no shape with id=" + id);
				return response;
			} else {
				logger.debug("Get shape with id {}", id);
				response.setStatus("200");
				response.setMessage("Shape exist");
				response.setObject(circle);
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
	public RestResponse<Circle> createCircle(Circle circle) {
		logger.debug("Method createCircle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Circle> response = new RestResponse<Circle>();
		try{
			session.beginTransaction();
			double square = Math.pow(circle.getRadius(), 2)*Math.PI;
			circle.setSquare(square);
			session.save(circle);
			logger.debug("Create shape with id {}", circle.getId());
			response.setStatus("200");
			response.setMessage("Shape created");
			response.setObject(circle);
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
	public RestResponse<Circle> updateCircle(@PathParam(value = "id") int id, Circle circle){
		logger.debug("Method updateCircle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Circle> response = new RestResponse<Circle>();
		Circle oldCircle = null;
		try{
			session.beginTransaction();
			if(id != 0){
				oldCircle = (Circle) session.get(Circle.class, id);
				if(oldCircle == null){
					logger.error("There is no shape with id {}", id);
					response.setStatus("400");
					response.setMessage("There is no shape with id=" + id);
					return response;
				} else {
				if (circle.getRadius() != 0) {
					oldCircle.setRadius(circle.getRadius());
					double square = Math.pow(circle.getRadius(), 2) * Math.PI;
					oldCircle.setSquare(square);
				}
				if (circle.getSquare() != 0) {
					oldCircle.setSquare(circle.getSquare());
				}
				logger.debug("Update shape with id {}", id);
				response.setStatus("200");
				response.setMessage("Shape updated");
				response.setObject(oldCircle);
			}
			} else{
				logger.error("Wrong request. There is no id");
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
	public RestResponse<Circle> removeCircle(@PathParam("id") int id){
		logger.debug("Method removeCircle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Circle> response = new RestResponse<Circle>();
		Circle circle;
		try{
			session.beginTransaction();
			circle = (Circle) session.get(Circle.class, id);
			if(circle == null){
				logger.error("There is no shape with id {}", id);
				response.setStatus("400");
				response.setMessage("There is no shape with id=" + id);
				return response;
			} else {
				logger.debug("Remove shape with id {}", id);
				response.setStatus("200");
				response.setMessage("Shape deleted");
			}
			session.delete(circle);
		} finally{
			session.getTransaction().commit();
			session.close();
		}
		return response;
	}
	
}
