package dkashp.rest.dto;

import javax.persistence.*;

@Entity
@Table(name="CIRCLES")
@PrimaryKeyJoinColumn(name="SHAPE_ID")
public class Circle extends Shape {
	
	@Column(name="CIRCLE_RADIUS")
	private int radius;
	
	public Circle(){
		
	}
	
	public Circle(double square, int radius) {
		super(square);
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
	
}
