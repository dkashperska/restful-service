package dkashp.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

import dkashp.rest.dto.Circle;
import dkashp.rest.hibernate.utils.HibernateUtil;

@Path("circle")
public class CircleResources{

	@GET
	@Path("get")
	@Produces(MediaType.APPLICATION_JSON)
	public Circle getCircle(@PathParam("id") int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Circle circle = (Circle) session.get(Circle.class, id);
		session.getTransaction().commit();
		session.close();
		return circle;
	}

	@POST
	@Path("create")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createCircle(Circle circle) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(circle);
		session.getTransaction().commit();
		session.close();
	}

}
