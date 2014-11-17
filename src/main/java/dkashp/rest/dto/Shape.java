package dkashp.rest.dto;

import javax.persistence.*;

import org.hibernate.annotations.ForceDiscriminator;


@Entity
@Table(name = "SHAPES")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="SHAPE_TYPE", 
	discriminatorType=DiscriminatorType.STRING)
public abstract class Shape {

	@Id
	@Column(name="SHAPE_ID")
	private int id;
	
	@Column(name="SHAPE_SQUARE")
	private Double square;
	
	public Shape() {
	}
	
	public Shape(int id, Double square) {
		this.id = id;
		this.square = square;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getSquare() {
		return square;
	}

	public void setSquare(Double square) {
		this.square = square;
	}
}
