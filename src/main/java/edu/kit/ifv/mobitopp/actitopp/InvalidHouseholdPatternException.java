package edu.kit.ifv.mobitopp.actitopp;


/**
 * @author Tim Hilgert
 * This Exception is thrown if the weekly pattern of a person is invalid and thus the whole household needs to be remodeled.
 * In that case, it is advised to redo the whole activity creation proces for that person, but with a different seed.
 * 
 * This expection may be caused by joint actions that are added to a persons schedule and thus the conflicting activities will not change 
 * when only modeling again the person again.
 * 
 */
public class InvalidHouseholdPatternException extends AbsInvalidPatternException
{

	private static final long serialVersionUID = 4952452597782995646L;
	private HWeekPattern faultyHWeekPattern;
  private String reason;
  private HActivity[] involvedActivities;
    

  public InvalidHouseholdPatternException(HWeekPattern faultyPattern, String reason)
  {
    this.faultyHWeekPattern = faultyPattern;
    this.reason = reason;
  }
    
  
	public String getReason()
  {
      return reason;
  }


  public void setReason(String reason)
  {
      this.reason = reason;
  }


  public HWeekPattern getFaultyPattern()
  {
      return faultyHWeekPattern;
  }


  public void setFaultyPattern(HWeekPattern faultyPattern)
  {
      this.faultyHWeekPattern = faultyPattern;
  }


	public HActivity[] getInvolvedActivities() {
		return involvedActivities;
	}
	

	public void setInvolvedActivities(HActivity[] involvedActivities) {
		this.involvedActivities = involvedActivities;
	}

}