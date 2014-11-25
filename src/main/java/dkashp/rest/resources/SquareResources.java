package dkashp.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dkashp.rest.dto.Shape;
import dkashp.rest.dto.ShapeIds;
import dkashp.rest.hibernate.utils.HibernateUtil;
import dkashp.rest.response.RestResponseForSquare;

@Path("shapes")
public class SquareResources {
	
	Logger logger = LoggerFactory.getLogger(SquareResources.class);
	
	@POST
	@Path("squares")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponseForSquare<Double> getSquares(ShapeIds ids){
		logger.info("Method getSquares starts");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponseForSquare<Double> response = new RestResponseForSquare<Double>();
		Shape shape;
		List<Double> squares = new ArrayList<Double>();
		try{
			session.beginTransaction();
			for (int id : ids.getIds()) {
				 shape = (Shape) session.get(Shape.class, id);
				 if(shape == null){
					 logger.error("There is no shape with id {}", id);
					 response.setStatus("400");
					 response.setMessage("There is no shape with id=" + id);
					 return response;
				 }
				 squares.add(shape.getSquare());
			}
			 logger.info("Return areas of particular shapes");
			 response.setStatus("200");
			 response.setMessage("Square calculated");
			 response.setSquares(squares);
		} finally{
			session.getTransaction().commit();
			session.close();
		}
		return response;
	}

}
