package dkashp.rest.dto;

import javax.persistence.*;

@Entity
@Table(name="CIRCLES")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="SHAPE_TYPE", 
	discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("Circle")
public class Circle extends Shape {
	
	@Column(name="CIRCLE_RADIUS")
	private int radius;
	
	public Circle(){
		
	}
	
	public Circle(int id, double square, int radius) {
		super(id, square);
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
	
}
