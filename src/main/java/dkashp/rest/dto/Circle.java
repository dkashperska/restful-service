package dkashp.rest.dto;

import javax.persistence.*;

@Entity
@Table(name="CIRCLES")
@DiscriminatorValue("Circle")
public class Circle extends Shape {
	
	@Column(name="CIRCLE_RADIUS")
	private int radius;
	
	public Circle(){
		
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
	
}
