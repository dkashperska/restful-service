package dkashp.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dkashp.rest.dto.Shape;
import dkashp.rest.dto.ShapeIds;
import dkashp.rest.errorhandling.AppException;
import dkashp.rest.hibernate.utils.HibernateUtil;

@Path("shapes")
public class SquareResources {
	
	Logger logger = LoggerFactory.getLogger(SquareResources.class);
	
	@POST
	@Path("squares")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSquares(ShapeIds ids) throws AppException{
		logger.info("Method getSquares starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Shape shape;
		List<Double> squares = new ArrayList<Double>();
		try{
			session.beginTransaction();
			for (int id : ids.getIds()) {
				 shape = (Shape) session.get(Shape.class, id);
				if (shape == null) {
					logger.error("The shape with the id {} was not found in database", id);
					throw new AppException(
							Response.Status.NOT_FOUND.getStatusCode(),
							"The resource you are trying to get does not exist in the database",
							"Please verify existence of data in the database for the id - " + id);
				}
				 squares.add(shape.getSquare());
			}
			 logger.info("Return areas of particular shapes");
			 return Response.status(200).entity(squares).build();
		} finally{
			session.getTransaction().commit();
			session.close();
		}
	}

}
