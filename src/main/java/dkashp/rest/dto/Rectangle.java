package dkashp.rest.dto;

import javax.persistence.*;

@Entity
@Table(name="RECTANGLES")
@PrimaryKeyJoinColumn(name="SHAPE_ID")
public class Rectangle extends Shape{
	
	@Column(name="RECTANGLE_WIDTH")
	private int width;
	
	@Column(name="RECTANGLE_LENGTH")
	private int length;
	
	public Rectangle(){
		
	}
	
	public Rectangle(double square,int width, int length) {
		super(square);
		this.width = width;
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
