package dkashp.rest.dto;

import javax.persistence.*;

@Entity
@Table(name = "SHAPES")
@Inheritance(strategy = InheritanceType.JOINED)
public class Shape {

	@Id
	@GeneratedValue
	@Column(name="SHAPE_ID")
	private int id;
	
	@Column(name="SHAPE_SQUARE")
	private double square;
	
	public Shape() {
	}
	
	public Shape( double square) {
		this.square = square;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getSquare() {
		return square;
	}

	public void setSquare(double square) {
		this.square = square;
	}
}
