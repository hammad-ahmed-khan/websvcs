package oracle.retail.sim.common.schedule;

import oracle.retail.sim.common.business.MessageText;

public enum ProductGroupScheduleMessageText implements MessageText {
  CONFIRM("The item request is scheduled for today. Would you like to generate it now?"),
  CONFIRM_NEW("The stock count is scheduled for today. Would you like to generate it now?"),
  CONFIRM_EDIT("The stock count is scheduled for today.  The existing stock count will be updated if it has not been started. Would you like to update?"),
  DELETE_CONFIRM("Are you sure you want to delete the selected schedule?"),
  DELETE_DENIED("User is not authorized to delete this type of Product Group Schedule."),
  INVALID_DATE("Start date must be greater than or equal to today."),
  INVALID_DATE_RANGE("Start Date may not be later than End Date."),
  INVALID_DAY_OF_MONTH("Invalid day of the month."),
  INVALID_END_DATE("End Date may not be earlier than today."),
  INVALID_LOCK_DATE("Unit & Amount Stock Counts must have a start date greater than or equal to today plus system lockout days."),
  INVALID_START_DATE("Start date must be greater than or equal to today."),
  MISSING_DESCRIPTION("You must have a description."),
  MISSING_END_DATE("You must choose an end date."),
  MISSING_GROUP("You must select a Count Group."),
  MISSING_STORE("You must choose at least one store for this Schedule."),
  MISSING_DAY_OF_WEEK("You must select at least one day of the week for this schedule."),
  MISSING_START_DATE("You must choose a start date."),
  START_DATE_EDIT_ERROR("Only editable start date is greater than or equal to today plus system lockout days."),
  STATUS_INVALID("Invalid stock count schedule status."),
  UNIT_DATE_ERROR("Unit & Amount Stock Counts must have a start date greater than or equal to today plus system lockout days."),
  WASTAGE_DATE_ERROR("Wastage Product Groups must have a start date greater or equal than today.");
  
  private final String message;
  
  ProductGroupScheduleMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\ProductGroupScheduleMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */