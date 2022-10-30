package model;

import java.time.LocalDate;

public class Order {
	private Long carBrandId, carModelId, carVersionId,
		carEngineId, carGearboxId;
	private double cost;
	private String carColor;
	private Type carType; 
	private LocalDate date;
	
	public Order(double cost, Long carBrand, Long carModel, String carColor,
			Long carVersion, Type carType, Long carEngine, Long carGearbox) {
		this.cost = cost;
		this.carBrandId = carBrand;
		this.carModelId = carModel;
		this.carColor = carColor;
		this.carVersionId = carVersion;
		this.carType = carType;
		this.carEngineId = carEngine;
		this.carGearboxId = carGearbox;
		date = LocalDate.now();
	}

	public Order(double cost, Long carBrand, Long carModel, String carColor,
			Long carVersion, Type carType, Long carEngine, Long carGearbox, LocalDate date) {
		this.cost = cost;
		this.carBrandId = carBrand;
		this.carModelId = carModel;
		this.carColor = carColor;
		this.carVersionId = carVersion;
		this.carType = carType;
		this.carEngineId = carEngine;
		this.carGearboxId = carGearbox;
		this.date = date;
	}

	public double getCost() {
		return cost;
	}

	public Long getCarBrand() {
		return carBrandId;
	}

	public Long getCarModel() {
		return carModelId;
	}

	public String getCarColor() {
		return carColor;
	}

	public Long getCarVersion() {
		return carVersionId;
	}

	public Type getCarType() {
		return carType;
	}

	public Long getCarEngine() {
		return carEngineId;
	}

	public Long getCarGearbox() {
		return carGearboxId;
	}
	
	public LocalDate getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "Order [carBrandId=" + carBrandId + ", carModelId=" + carModelId + ", carVersionId=" + carVersionId
				+ ", carEngineId=" + carEngineId + ", carGearboxId=" + carGearboxId + ", cost=" + cost + ", carColor="
				+ carColor + ", carType=" + carType + ", date=" + date + "]";
	}	
}
