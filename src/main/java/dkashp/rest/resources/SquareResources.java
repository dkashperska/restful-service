package dkashp.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

import dkashp.rest.dto.Shape;
import dkashp.rest.dto.ShapeIds;
import dkashp.rest.hibernate.utils.HibernateUtil;
import dkashp.rest.response.RestResponseForSquare;

@Path("shape")
public class SquareResources {
	
	@POST
	@Path("square")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponseForSquare<Double> getSquares(ShapeIds ids){
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestResponseForSquare<Double> response = new RestResponseForSquare<Double>();
		Shape shape;
		List<Double> squares = new ArrayList<Double>();
		try{
			session.beginTransaction();
			for (int id : ids.getIds()) {
				 shape = (Shape) session.get(Shape.class, id);
				 if(shape == null){
					 response.setStatus("400");
					 response.setMessage("There is no shape with id=" + id);
					 return response;
				 }
				 squares.add(shape.getSquare());
			}
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
