package dkashp.rest.dto;

import javax.persistence.*;

@Entity
@Table(name="TRIANGLES")
@DiscriminatorValue("Triangle")
public class Triangle extends Shape{

	@Column(name="TRIANGLE_BASE")
	private int base;
	
	@Column(name="TRIANGLE_HEIGHT")
	private int height;

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
