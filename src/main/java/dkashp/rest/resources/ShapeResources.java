package dkashp.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

import dkashp.rest.dto.Shape;
import dkashp.rest.hibernate.utils.HibernateUtil;

@Path("shape")
public class ShapeResources {

	@GET
	@Path("create")
	@Produces(MediaType.TEXT_PLAIN)
	public String createShape(){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
/*
		Shape shape = new Shape();
		shape.setSquare(10.12);*/
	//	session.save(shape);
		session.getTransaction().commit();

		return "OK";
	}
}
