package dkashp.rest.dto;

import javax.persistence.*;

@Entity
@Table(name="RECTANGLES")
@DiscriminatorValue("Rectangle")
public class Rectangle extends Shape{
	
	@Column(name="RECTANGLE_WIDTH")
	private int width;
	
	@Column(name="RECTANGLE_LENGTH")
	private int length;

}
