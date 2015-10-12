package com.app.andy.bulkapp;

/**
 * Created by Andy on 10/11/2015.
 */
public class FoodItem {
	private String name;
	private double calorie;
	private String date;
	private int id;

	public FoodItem() {}

	public FoodItem(String name, double calorie, String date) {
		this.name = name;
		this.calorie = calorie;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCalorie() {
		return calorie;
	}

	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
