package org.logicinfo.oms.bookingCreation.controller;

public class DeliveryAddress {
	public DeliveryAddress() {
        super();
    }
    
	private String firstName;
	private String lastName;
	private String phoneNo; 
	private String lat;
	private String lng;
	private String country; 
	private String state;
	private String province;
	private String city;
	private String area;
	private String streetName;
	private String postal;
	private String address1;
	private String address2;
	private String address3;
    
    public String getFirstName(){
    	return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getPhoneNo(){
        return phoneNo;
    }
    public String getLat() {
		return lat;
	}
	public String getLng() {
		return lng;
	}
    public String getCountry(){
        return country;
    }
    public String getState(){
        return state;
    }
    public String getProvince(){
        return province;
    }
    public String getCity(){
        return city;
    }
    public String getArea(){
        return area;
    }
    public String getStreetName(){
        return streetName;
    }
    public String getPostal(){
        return postal;
    }
    public String getAddress1(){
        return address1;
    }
    public String getAddress2(){
        return address2;
    }
    public String getAddress3(){
        return address3;
    }
    
    public void setFirstName(String firstName){
    this.firstName=firstName;
    }
    
    public void setLastName(String lastName){
    this.lastName=lastName;
    }
    
    public void setPhoneNo(String phoneNo){
    this.phoneNo=phoneNo;
    }
    
    
    public void setCountry(String country){
    this.country=country;
    }
    
    public void setState(String state){
    this.state=state;
    }
    
    public void setProvince(String province){
    this.province=province;
    }
    
    public void setCity(String city){
    this.city=city;
    }
    
    public void setArea(String area){
    this.area=area;
    }
    
    public void setStreetName(String streetName){
    this.streetName=streetName;
    }
    
    public void setPostal(String postal){
    this.postal=postal;
    }
    
    public void setAddress1(String address1){
    this.address1=address1;
    }
    
    public void setAddress2(String address2){
    this.address2=address2;
    }
    
    public void setAddress3(String address3){
    this.address3=address3;
    }
    
    public void setLat(String lat) {
		this.lat = lat;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}

}
