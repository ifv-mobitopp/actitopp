package edu.kit.ifv.mobitopp.actitopp;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Tim Hilgert
 *
 */
public class HTour
{
	// Enth�lt alle Attribute, die nicht direkt �ber Variablen ausgelesen werden k�nnen
	private Map<String, Double> attributes;
	
  private HDay day;
  private List<HActivity> activities;
  private int index		= -99;
  private int starttime 	= -1;

   
  /**
   * 
   * Konstruktor
   *   
   * @param parent
   * @param index
   */
  public HTour(HDay parent, int index)
  {
  	assert parent!=null : "Tag nicht initialisiert";
    this.day = parent;
    setIndex(index);  
    this.activities = new ArrayList<HActivity>();
    
    this.attributes = new HashMap<String, Double>();
  }
    

    
  public HDay getDay()
	{
		assert day != null : "Tag nicht initialisiert";
	    return day;
	}
  
  public HWeekPattern getWeekPattern()
  {
  	return getDay().getPattern();
  }
  
  public ActitoppPerson getPerson()
  {
  	return getDay().getPerson();
  }


  public List<HActivity> getActivities()
	{
		assert activities != null : "Aktivit�tenliste nicht initialisiert";
	  return activities;
	}

  public void addActivity(HActivity act)
  {
  	assert act.getIndex()!=-99 : "Index der Aktivit�t nicht initialisiert";
  	boolean actindexexisitiert = false;
  	for (HActivity tmpact : activities)
  	{
  		if (tmpact.getIndex() == act.getIndex()) actindexexisitiert = true;
  	}
  	assert !actindexexisitiert : "Es gibt bereits eine Aktivit�t mit diesem Index";
  	activities.add(act);
  }

	public int getIndex()
	{
		assert index != -99 : "Index nicht initilialisiert";
	  return index;
	}


	public void setIndex(int index)
	{
	    this.index = index;
	}


	public int getStartTime()
	{
		assert starttime >= 0 : "Startzeit negativ - Startzeit: " + starttime;
	    return starttime;
	}


	public void setStartTime(int chosenStartTime)
	{
		assert chosenStartTime >= 0 : "Startzeit negativ - Startzeit: " + chosenStartTime;
	    this.starttime = chosenStartTime;
	}


	/**
	 * 
	 * Sortierung einer Tourliste nach Index
	 * 
	 * @param list
	 */
	public static void sortTourList(List<HTour> list)
	{
		assert list != null : "Liste zum Sortieren ist leer";
		
	    Collections.sort(list, new Comparator<HTour>()
	    {
	        @Override
	        public int compare(HTour o1, HTour o2)
	        {
	            if(o1.getIndex() < o2.getIndex()) return -1;
	            if(o1.getIndex() > o2.getIndex()) return 1;
	            return 0;
	        }
	    });
	}

	/**
	 * 
	 * Pr�ft, ob die Tour keine Zeitl�cken aufweist, das hei�t ob Akt und Wege direkt aneinander gereiht sind
	 * 
	 * @param tour
	 * @return
	 */
	public boolean tourisFreeofGaps()
	{
		boolean gapfree = true;
		HActivity.sortActivityListbyIndices(activities);
		
		for (int i=0 ; i<activities.size()-1; i++)
		{
			HActivity aktuelleakt = activities.get(i);
			HActivity naechsteakt = activities.get(i+1);
			
			if (aktuelleakt.getEndTimeWeekContext() != naechsteakt.getTripStartTimeBeforeActivityWeekContext()) 
			{
				gapfree=false;
			}
		}	
		
		return gapfree;
	}

	@Override
  public String toString()
  {
		String tostring = "";
		if (this.isScheduled())
		{
			tostring = 	getDay().getIndex() + "/" + getIndex() + 
									" Start " + getStartTimeWeekContext() + 
    							" Ende " + getEndTimeWeekContext() + 
    							" Dauer: " + getTourDuration();  
		}
		else
		{
			tostring = 	getDay().getIndex() + "/" + getIndex() + 
									" Start --- " + 
									" Ende --- " +  
									" Dauer: " + getTourDuration(); 
		}
  	return 	tostring;
  }   
	
	/**
   * 
   * Erstellt Startzeiten f�r jede Aktivit�t einer bestimmten Tour
   * 
   */
  public void createStartTimesforActivities()
  {
    // ! sorts the list permanently
    HActivity.sortActivityListbyIndices(getActivities());
    
    for (HActivity act : getActivities())
    {
    	// Bei erster Aktivit�t in Tour wird die Startzeit durch den Beginn der Tour bestimmt
    	if (!act.startTimeisScheduled())
    	{
    		if (act.isActivityFirstinTour())
      	{
      		act.setStartTime(getStartTime() + act.getEstimatedTripTimeBeforeActivity());
      	}
      	// Ansonsten durch das Ende der vorherigen Aktivit�t
      	else
      	{
      		act.setStartTime(act.getPreviousActivityinTour().getEndTime() + act.getEstimatedTripTimeBeforeActivity());
      	}
    	}
    }
    
    // Pr�fe, ob die Tour einen l�ckenlosen Ablauf hat, das hei�t keine Leerzeit zwischen Akt und Wegen
    if (!tourisFreeofGaps()) System.err.println("Tour hat L�cken! " + this); 
  }
  
  
	
	/**
	 * 
	 * Pr�ft, ob die Startzeit der Tour bereits festgelegt wurde
	 * 
	 * @return
	 */
	public boolean isScheduled()
	{
		return starttime!=-1;
	}
	
	/**
	 * 
	 * Gibt an, ob es sich um die Haupttour des Tages handelt
	 * 
	 * @return
	 */
	public boolean isMainTouroftheDay()
	{
		return this.getIndex()==0;
	}
	


  
  /**
   * 
   * Gibt die Tourduration inkl. default Trip times zur�ck
   * 
   * @return
   */
  public int getTourDuration()
  {
  	return getActDuration() + getTripDuration();
  }
  
  /**
   * 
   * Gibt die reine Aktivit�tenzeit auf der Tour zur�ck
   * 
   * @return
   */
  public int getActDuration()
  {
    int sum = 0;
    for(HActivity act : activities)
    {       	
      sum += (act.durationisScheduled() ? act.getDuration() : 0);
    }
    
    return sum;
  }
  
  /**
   * 
   * Gibt die reine Wegzeit auf der Tour zur�ck
   *     
   * @return
   */
  public int getTripDuration()
  {
    int sum = 0;
    for(HActivity act : activities)
    {
    	sum += (act.tripBeforeActivityisScheduled() ? act.getEstimatedTripTimeBeforeActivity() : 0);
    	sum += (act.tripAfterActivityisScheduled() ? act.getEstimatedTripTimeAfterActivity() : 0);    	
    }
    
    return sum;
  }
  
  /**
   * 
   * Gibt die Endzeit der Tour zur�ck
   * 
   * @return
   */
  public int getEndTime()
  {
  	return getLastActivityInTour().getEndTime() + getLastActivityInTour().getEstimatedTripTimeAfterActivity();
  }

  /**
   * 
   * Gibt explizit die Aktivit�t mit dem gesuchten Index zur�ck
   * 
   * @param index
   * @return
   */
  public HActivity getActivity(int index)
  {
  	HActivity indexact = null;
  	for (HActivity activity : getActivities())
  	{
  		if (activity.getIndex()==index)
  		{
  			indexact = activity;
  		}
  	}
  	assert indexact != null : "Aktivit�t konnte nicht gefunden werden";
  	return indexact;
  	
  }
  
  
  /**
   *
   * Gibt den Index der erste Aktivit�t der Tour zur�ck
   * 
   * @return
   */
  public int getLowestActivityIndex()
  {
      int min = +99;
      for(HActivity act : this.activities)
      {
          if(act.getIndex() < min) min = act.getIndex();
      }
      assert min<=0 : "minimaler AktIndex der Tour ist gr��er 0 - index: " + min;
      return min;
  }
     
  
  /**
  *
  * Gibt den Index der letzten Aktivit�t der Tour zur�ck
  * 
  * @return
  */
  public int getHighestActivityIndex()
  {
      int max = -99;
      for(HActivity act : this.activities)
      {
          if(act.getIndex() > max) 
          {
          	max = act.getIndex();
          }
      }
      assert max>=0 : "maximaler AktIndex der Tour ist kleiner 0 - index: " + max;
      return max;
  }

  /**
   * 
   * Gibt die erste Aktivit�t der Tour zur�ck
   * 
   * @return
   */
  public HActivity getFirstActivityInTour()
  {
  	return getActivity(getLowestActivityIndex());    	
  }
  
  /**
   * 
   * Gibt die letzte Aktivit�t der Tour zur�ck
   * 
   * @return
   */
  public HActivity getLastActivityInTour()
  {
  	return getActivity(getHighestActivityIndex());    	
  }
  
  public int getAmountOfActivities()
  {
  	return getActivities().size();
  }

  
  public int getStartTimeWeekContext()
  {
  	return (1440*getDay().getIndex()) + getStartTime();
  }
  
  public int getEndTimeWeekContext()
  {
  	return (1440*getDay().getIndex()) + getEndTime();
  }


	/**
	 * 
	 * Gibt die vorherige Tour im Pattern zur�ck
	 * (letzte Tour des Vortags oder vorherige Tour des aktuellen Tags)
	 * 
	 * @return
	 */
	public HTour getPreviousTourinPattern()
	{
		HTour previousTour;
		//Pr�fe, ob die Tour die erste Tour des Tages ist
    if (index == day.getLowestTourIndex())
    {
    	// Suche so lange r�ckw�rts, bis ein Tag mit Touren gefunden wird oder es keinen Vortag mehr gibt
    	HDay previousDaywithTour = day;
    	do 
  		{
    		previousDaywithTour = previousDaywithTour.getPreviousDay();	
  			if (previousDaywithTour!=null && !previousDaywithTour.isHomeDay()) break;
  		}
  		while(previousDaywithTour!=null);
    	
    	// Falls kein Vortag mit Tour existiert wird null zur�ckgegeben
    	if (previousDaywithTour==null)
    	{
    		previousTour = null;
    	}
    	// Andernfalls die letzte Tour des Vortags
    	else
    	{
    		previousTour = previousDaywithTour.getLastTourOfDay();
    	}  	
    }
    // Falls nicht wird die vorherige Tour dieses Tages zur�ckgegeben
    else
    {
    	previousTour = day.getTour(index-1);
    }
    return previousTour;
	}
	
	/**
	 * 
	 * Gibt die n�chste Tour im Pattern zur�ck
	 * (erste Tour des Folgetags oder n�chste Tour des aktuellen Tags)
	 * 
	 * @return
	 */
	public HTour getNextTourinPattern()
	{
		HTour nextTour;
		//Pr�fe, ob die Tour die letzte Tour des Tages ist
    if (index == day.getHighestTourIndex())
    {
    	// Suche so lange vorw�rts, bis ein Tag mit Touren gefunden wird oder es keinen Folgetag mehr gibt
    	HDay nextDaywithTour = day;
    	do 
  		{
    		nextDaywithTour = nextDaywithTour.getNextDay();	
  			if (nextDaywithTour!=null && !nextDaywithTour.isHomeDay()) break;
  		}
  		while(nextDaywithTour!=null);
    	
    	// Falls kein Folgetag mit Tour existiert wird null zur�ckgegeben
    	if (nextDaywithTour==null)
    	{
    		nextTour = null;
    	}
    	// Andernfalls die letzte Tour des Vortags
    	else
    	{
    		nextTour = nextDaywithTour.getFirstTourOfDay();
    	}  	
    }
    // Falls nicht wird die n�chste Tour dieses Tages zur�ckgegeben
    else
    {
    	nextTour = day.getTour(index+1);
    }
    return nextTour;
	}

	/**
	 * @param attributes spezifischesAttribut f�r Map
	 */
	public void addAttributetoMap(String name, Double value) {
		this.attributes.put(name, value);
	}


	/**
	 * @return the attributes
	 */
	public Map<String, Double> getAttributesMap() {
		return attributes;
	}
	
}
