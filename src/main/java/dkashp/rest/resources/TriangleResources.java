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

import dkashp.rest.dto.Triangle;
import dkashp.rest.hibernate.utils.HibernateUtil;
import dkashp.rest.response.RestResponse;

@Path("triangles")
public class TriangleResources {

	Logger logger = LoggerFactory.getLogger(TriangleResources.class);
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<Triangle> getTriangle(@PathParam("id") int id) {
		logger.debug("Method getTriangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Triangle> response = new RestResponse<Triangle>();
		Triangle triangle;
		try{
			session.beginTransaction();
			triangle = (Triangle) session.get(Triangle.class, id);
			if(triangle == null){
				logger.error("There is no shape with id {}", id);
				response.setStatus("400");
				response.setMessage("There is no shape with id=" + id);
				return response;
			} else {
				logger.debug("Get shape with id {}", id);
				response.setStatus("200");
				response.setMessage("Shape exist");
				response.setObject(triangle);
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
	public RestResponse<Triangle> createTriangle(Triangle triangle) {
		logger.debug("Method createTriangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Triangle> response = new RestResponse<Triangle>();
		try{
			session.beginTransaction();
			double square = 0.5 * triangle.getBase() * triangle.getHeight();
			triangle.setSquare(square);
			session.save(triangle);
			logger.debug("Create shape with id {}", triangle.getId());
			response.setStatus("200");
			response.setMessage("Shape created");
			response.setObject(triangle);
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
	public RestResponse<Triangle> updateTriangle(@PathParam("id") int id, Triangle triangle){
		logger.debug("Method updateTriangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Triangle> response = new RestResponse<Triangle>();
		Triangle oldTriangle = null;
		double square;
		try{
			session.beginTransaction();
			if(id != 0){
				oldTriangle = (Triangle) session.get(Triangle.class, id);
				if(oldTriangle == null){
					logger.error("There is no shape with id {}", id);
					response.setStatus("400");
					response.setMessage("There is no shape with id=" + id);
					return response;
				} else {
					if(triangle.getBase() != 0){
						oldTriangle.setBase(triangle.getBase());
						square = 0.5 * triangle.getBase() * oldTriangle.getHeight();
						oldTriangle.setSquare(square);
					}
					if(triangle.getHeight() != 0){
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
					logger.debug("Update shape with id {}", id);
					response.setStatus("200");
					response.setMessage("Shape updated");
					response.setObject(oldTriangle);
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
	public RestResponse<Triangle> removeTriangle(@PathParam("id") int id){
		logger.debug("Method removeTriangle starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponse<Triangle> response = new RestResponse<Triangle>();
		Triangle triangle;
		try{
			session.beginTransaction();
			triangle = (Triangle) session.get(Triangle.class, id);
			if(triangle == null){
				logger.error("There is no shape with id {}", id);
				response.setStatus("400");
				response.setMessage("There is no shape with id=" + id);
				return response;
			} else {
				logger.debug("Remove shape with id {}", id);
				response.setStatus("200");
				response.setMessage("Shape deleted");
			}
			session.delete(triangle);
		} finally{
			session.getTransaction().commit();
			session.close();
		}
		return response;
	}
}
