package dkashp.rest.dto;

import javax.persistence.*;

@Entity
@Table(name="TRIANGLES")
@PrimaryKeyJoinColumn(name="SHAPE_ID")
public class Triangle extends Shape{

	@Column(name="TRIANGLE_BASE")
	private int base;
	
	@Column(name="TRIANGLE_HEIGHT")
	private int height;
	
	public Triangle(){
		
	}
	
	public Triangle(double square, int base, int height) {
		super(square);
		this.base = base;
		this.height = height;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
