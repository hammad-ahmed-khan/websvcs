package oracle.retail.sim.common.productgroup;

import oracle.retail.sim.common.business.MessageText;

public enum ProductGroupMessageText implements MessageText {
  ADD_ITEMS_ERROR("Unable to add the following items to product group:"),
  ALL_ITEMS_MESSAGE("This product group contains all items for the store."),
  ATTRIBUTE_INVALID("Product group attribute is not modifiable."),
  CLASS_ALREADY_EXISTS("The class for this Item already exists on this product group."),
  CLASS_DEPT_EXISTS("The dept of this class already exists on this product group."),
  CLASS_DEPT_EXISTS_2("A class within this dept already exists on this product group."),
  DELETE_CONFIRM("Are you sure you want to delete the selected product group?"),
  DELETE_ERROR("A selected product group is attached to a schedule and cannot be deleted."),
  DELETE_DENIED("User is not authorized to delete this type of Product Group."),
  DELIVERY_DATE_ERROR("Requested Delivery Date must be greater than the Expiration Date."),
  DEPARTMENT_ALREADY_EXISTS("The dept for this Item already exists on this product group."),
  DUPLICATE_SCHEDULE("The Unit and Amount count cannot be scheduled since a similar one already exists for the given date and product group."),
  GROUP_NOT_SELECTED("You must select a product group."),
  HIERARCHY_ALREADY_EXISTS("The Hierarchy level already exists."),
  ITEM_ADD_ERROR("Single items may not be added to this product group."),
  ITEM_ALREADY_EXISTS("Item already exists."),
  ITEM_COUNT_TOO_LARGE("The selected element will exceed the maximum limit of {0}."),
  LOCKOUT_DAYS_ERROR("Start Date cannot be inside Lockout Days."),
  MISSING_HIERARCHY("You must select a hierarchy to add it to this group."),
  MISSING_ITEM("You must select an item to add it to this group."),
  MISSING_PROMOTION("You must enter a promotion ID to add items to this group."),
  MISSING_SUPPLIER("You must select a supplier to add items to this group."),
  MISSING_TYPE("You must select a type."),
  MISSING_DESCRIPTION("You must have a description."),
  MISSING_ITEM_STATUS("Please select at least one item status."),
  MISSING_ELEMENTS("You may not create an empty group. At least one element must be added."),
  MISSING_PROBLEM_LINE_CRITERIA("Please select at least one Problem Line criterion."),
  MISSING_SOH_STATUS("Please select at least one Stock On Hand type."),
  MISSING_VARIANCE("Must enter at least one of the Variance Units, Variance %, or Variance Value."),
  MISSING_WASTAGE_VARIANCE("You must enter at least either Shrinkage Units or Shrinkage %."),
  SUBCLASS_ALREADY_EXISTS("The subclass for this Item already exists on this product group"),
  SUBCLASS_CLASS_EXISTS("A subclass within this class already exists on this product group."),
  SUBCLASS_CLASS_EXISTS_2("The class of this subclass already exists on this product group."),
  UNSAVED_CHANGES_MESSAGE("Changes will not be saved. Do you want to continue?"),
  WASTAGE_NOTICE("Wastage is scheduled for today but it will not be extracted until the nightly batch run.");
  
  private final String message;
  
  ProductGroupMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\ProductGroupMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */