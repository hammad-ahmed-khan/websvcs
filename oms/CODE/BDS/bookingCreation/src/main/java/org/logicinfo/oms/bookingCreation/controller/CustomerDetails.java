package org.logicinfo.oms.bookingCreation.controller;


public class CustomerDetails {
	
	public CustomerDetails(){	
		
		System.out.println(" CustomerDetails  constructor ");
		
	}
	
	String firstName;
    String lastName;
    String customerId;
    String phoneNo;
    int language;
    String email;
    String gender;
    String nationality;
    
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getCustomerId(){
        return customerId;
    }
    public String getPhoneNo(){
        return phoneNo;
    }
    public int getLanguage(){
        return language;
    }
    public String getEmail(){
        return email;
    }
    public String getGender(){
        return gender;
    }
    public String getNationality(){
        return nationality;
    }
    
    
    public void setFirstName(String firstName){
        this.firstName=firstName;
    }
    
    public void setLastName(String lastName){
        this.lastName=lastName;
    }
    
    public void setCustomerId(String customerId){
        this.customerId=customerId;
    }
    
    public void setPhoneNo(String phoneNo){
        this.phoneNo=phoneNo;
    }
    
    public void setLanguage(int language){
        this.language=language;
    }
    
    public void setEmail(String email){
        this.email=email;
    }
    
    public void setGender(String gender){
        this.gender=gender;
    }
    
    public void setNationality(String nationality){
        this.nationality=nationality;
    }

}
