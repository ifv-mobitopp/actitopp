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
public class HActivity
{
	
	// Enth�lt alle Attribute, die nicht direkt �ber Variablen ausgelesen werden k�nnen
	private Map<String, Double> attributes;
		
	private HDay day;	
	private HTour tour;
	
  private int index								= -99;
  private char type								= 'x';
  private int duration 						= -1;
  private int starttime 					= -1;
  
  
  private HTrip tripbeforeactivity;
  private HTrip tripafteractivity;
 
  private int jointStatus = -1;
  private List<ActitoppPerson> jointParticipants  = new ArrayList<ActitoppPerson>();

  /*
   * erwartete Wegezeit nach der Aktivit�t
   * nur relevant, falls Aktivit�t die letzte auf der Tour ist.
   */
//  private int estimatedTripTimeAfterActivity = -1;
  
  private byte mobiToppActType		= -1;
           

	/**
   * 
   * Konstruktor
   * 
   * @param parent
   * @param index
   */
	public HActivity(HTour parent, int index)
	{
		assert parent!=null : "Tour nicht initialisiert";
    this.tour = parent;
    
    this.day = parent.getDay();
    setIndex(index);
    
    this.attributes = new HashMap<String, Double>();
    setCreatorPersonIndex(day.getPerson().getPersIndex());
	}  
	
	/**
	 * 
	 * Konstruktor
	 * 
	 * @param parent
	 * @param index
	 * @param type
	 */
	public HActivity(HTour parent, int index, char type)
	{
	    this(parent, index);
	    setType(type);
	}  
	
	/**
	 * 
	 * Konstruktor f�r Aktivit�ten, wo nur Akt gemeinsam durchgef�hrt wird
	 *
	 * @param parent
	 * @param index
	 * @param type
	 * @param duration
	 * @param starttime
	 */
	public HActivity(HTour parent, int index, char type, int duration, int starttime, int jointStatus)
	{
	    this(parent, index, type);
	    setDuration(duration);
	    setStartTime(starttime);
	    setJointStatus(jointStatus);
	}  
	
	/**
	 * 
	 * Konstruktor f�r Aktivit�ten, wo Akt und Weg gemeinsam durchgef�hrt werden
	 *
	 * @param parent
	 * @param index
	 * @param type
	 * @param duration
	 * @param starttime
	 * @param jointStatus
	 * @param tripdurationbefore
	 */
	public HActivity(HTour parent, int index, char type, int duration, int starttime, int jointStatus, int tripdurationbefore)
	{
	    this(parent, index, type, duration, starttime, jointStatus);
	    tripbeforeactivity = new HTrip(this, tripdurationbefore);
	}  
	
	/**
	 * 
	 * Konstruktor f�r Aktivit�ten, wo nur Weg dahin gemeinsam durchgef�hrt wird
	 *
	 * @param parent
	 * @param index
	 * @param starttime
	 * @param jointStatus
	 * @param tripdurationbefore
	 */
	public HActivity(HTour parent, int index, int starttime, int jointStatus, int tripdurationbefore)
	{
	    this(parent, index);
	    setStartTime(starttime);
	    setJointStatus(jointStatus);
	    tripbeforeactivity = new HTrip(this, tripdurationbefore);
	}  
	
	

	/**
	 * 
	 * Konstruktor f�r Home-Aktivit�ten 
	 * 
	 * @param parent
	 * @param ActType
	 * @param duration
	 * @param starttime
	 */
	public HActivity(HDay parent, char ActType, int duration, int starttime)
	{
		assert parent!=null : "Tag nicht initialisiert";
		this.day = parent;
	  setType(ActType);
		setDuration(duration);
		setStartTime(starttime);
		setJointStatus(4);
	}



	public HTour getTour() 
	{
		return tour;
	}
	
	public HDay getDay() 
	{
		assert day != null : "Tag nicht initialisiert";
		return day;
	}
	
	public HWeekPattern getWeekPattern()
	{
		return day.getPattern();
	}
	
	public ActitoppPerson getPerson()
	{
		return day.getPerson();
	}

	public int getIndex()
	{
		assert index != -99 : "Index ist zum Zugriffszeitpunkt nicht initialisiert";
	    return index;
	}

	public void setIndex(int index)
	{
	    this.index = index;
	}

	public char getType() 
	{
		assert Configuration.ACTIVITY_TYPES.contains(type) || type=='H' : "ung�ltige Aktivit�t - nicht intialisiert oder nicht in ACTIVITYTYPES enthalten aktueller Wert:" + type;
		return type;
	}

	public void setType(char type) 
	{
		assert Configuration.ACTIVITY_TYPES.contains(type) || type=='H' : "ung�ltige Aktivit�t - nicht in ACTIVITYTYPES enthalten - Typ: " + type; 
		this.type = type;
	}

	public int getDuration() 
	{
		assert duration != -1 : "Duration ist zum Zugriffszeitpunkt nicht initialisiert";
		return duration;
	}

	public void setDuration(int duration) 
	{
		assert duration>0 : "zu setzende Dauer ist nicht gr��er 0 - Dauer:" + duration;
		this.duration = duration;
	}

	public int getStartTime() 
	{
		assert starttime != -1 : "Startzeit ist zum Zugriffszeitpunkt nicht initialisiert - Startzeit: " + starttime;
		return starttime;
	}

	public void setStartTime(int starttime) 
	{
		assert starttime>=0 : "zu setzende Startzeit ist negativ- Startzeit: " + starttime;
		this.starttime = starttime;
	}

	public int getJointStatus() {
		assert jointStatus!=-1 : "jointStatus not set";
		return jointStatus;
	}

	public void setJointStatus(int jointStatus) {
		assert jointStatus>=1 && jointStatus<=4 : "invalid value for jointStatus - actual value: " + jointStatus;
		this.jointStatus = jointStatus;
	}

	/**
	 * 
	 * @return the estimatedTripTimeBeforeActivity
	 */
	public int getEstimatedTripTimeBeforeActivity() 
	{
		assert tripBeforeActivityisScheduled() : "Trip vor der Aktvitit�t ist zum Zugriffszeitpunkt nicht initialisiert";
		int tmptriptimebefore = tripbeforeactivity.getTripduration();
		return tmptriptimebefore;
	}

	/**
	 * @return the estimatedTripTimeAfterActivity
	 */
	public int getEstimatedTripTimeAfterActivity() 
	{
		assert tripAfterActivityisScheduled() : "Trip nach der Aktvitit�t ist zum Zugriffszeitpunkt nicht initialisiert // nur bei letzter Aktivit�t in Tour gesetzt";
		int tmptriptimeafter = tripafteractivity.getTripduration();
		return tmptriptimeafter;
	}
	
	public byte getMobiToppActType() 
	{
		assert Configuration.ACTIVITY_TYPES_mobiTopp.contains(mobiToppActType) : "ung�ltige Aktivit�t - nicht in ACTIVITYTYPES_mobiTopp enthalten"; 
		return mobiToppActType;
	}

	public void setMobiToppActType(byte mobiToppActType) 
	{
		assert Configuration.ACTIVITY_TYPES_mobiTopp.contains(mobiToppActType) : "ung�ltige Aktivit�t - nicht in ACTIVITYTYPES_mobiTopp enthalten"; 
		this.mobiToppActType = mobiToppActType;
	}
	
	/**
   * Sortiert eine Liste mit Aktivit�ten chronologisch im Wochenverlauf
   * 
   * @param actList
   */
  public static void sortActivityListbyWeekStartTimes(List<HActivity> actList)
  {
  	assert actList != null : "Liste zum Sortieren ist leer";
  	
  Collections.sort(actList, new Comparator<HActivity>()
  {
	    @Override
	    public int compare(HActivity act1, HActivity act2)
	    {
	      if(act1.getStartTimeWeekContext()< act2.getStartTimeWeekContext())
	      {
	          return -1;
	      }
	      else if(act1.getStartTimeWeekContext() == act2.getStartTimeWeekContext())
	      {
	          return 0;
	      }
	      else
	      {
	          return 1;
	      }
	    }
	  });
  }
    
  /**
   * 
   * Methode zum Sortieren der Aktivit�tenliste nach Indizes
   * 
   * @param list
   */
  public static void sortActivityListbyIndices(List<HActivity> list)
  {
  	assert list != null : "Liste zum Sortieren ist leer";
  		
      Collections.sort(list, new Comparator<HActivity>()
      {
        @Override
        public int compare(HActivity o1, HActivity o2)
        {
        	int result = 99;
        	if			(o1.getDayIndex() <  o2.getDayIndex()) result = -1;
        	else if	(o1.getDayIndex() >  o2.getDayIndex()) result = +1;
        	else
        	{
          	if			(o1.getTour().getIndex() <   o2.getTour().getIndex()) result = -1;
          	else if	(o1.getTour().getIndex() >   o2.getTour().getIndex()) result = +1;
          	else
          	{
              if			(o1.getIndex() < o2.getIndex()) result = -1;
              else if	(o1.getIndex() > o2.getIndex()) result = +1;
              else 																		result = 0;
          	}
        	}
        	
        	assert result!=99 : "Could not compare these two activities! - Act1: " + o1 + " - Act2: " + o2;
        	return result;      		
        }
        
/*        
          @Override
          public int compare(HActivity o1, HActivity o2)
          {
              if(o1.getIndex() < o2.getIndex()) return -1;
              if(o1.getIndex() >o2.getIndex()) return 1;
              return 0;
          }
*/
      });
  }
  
  /**
   * 
   * Berechnet die Zeitdauern in Minuten zwischen dem Ende der ersten Aktivit�t (inkl. Weg) und dem Anfang der zweiten Aktivit�t (inkl. Weg vorher)
   * 
   * @param firstact
   * @param secondact
   * @return
   */
  public static int getTimebetweenTwoActivities(HActivity firstact, HActivity secondact)
  {
  	int result=-999999;
  	
  	int endezeitersteakt;
  	int anfangszeitzweiteakt;
  	
  	// Endezeit erste Aktivit�t
  	endezeitersteakt = firstact.getEndTimeWeekContext();
  	if (firstact.isActivityLastinTour())
  	{
  		endezeitersteakt += firstact.getEstimatedTripTimeAfterActivity();
  	}

  	// Anfangszeit zweite Aktivit�t
  	anfangszeitzweiteakt = secondact.getTripStartTimeBeforeActivityWeekContext();
  	
  	//R�ckgabe
  	result = anfangszeitzweiteakt - endezeitersteakt; 	
  	assert result!=-999999 : "Could not determine time between these two activities";
  	return result;
  }
  
  
  
  /**
	 * Pr�ft, ob sich zwei Aktivit�ten in ihren Zeitintervallen �berlagern
	 * false = Sie �berlagern sich nicht
	 * true = Sie �berlagern sich
	 * 
	 * @param tmpact
	 * @return
	 */
	public static boolean checkActivityOverlapping(HActivity act1, HActivity act2)
	{
		boolean result = false;
		
		// Zeitbelegung der aktuellen Aktivit�t ermitteln
		int starttime_first = act1.getStartTimeWeekContext() - (act1.tripBeforeActivityisScheduled() ? act1.getEstimatedTripTimeBeforeActivity() : 0);
		int endtime_first = (act1.durationisScheduled() ? act1.getEndTimeWeekContext() : act1.getStartTimeWeekContext()) + (act1.tripAfterActivityisScheduled() ? act1.getEstimatedTripTimeAfterActivity() : 0);
	
		// Zeitbelegung der anderen Aktivit�t ermitteln
		int starttime_second = act2.getStartTimeWeekContext() - (act2.tripBeforeActivityisScheduled() ? act2.getEstimatedTripTimeBeforeActivity() : 0);
		int endtime_second = (act2.durationisScheduled() ? act2.getEndTimeWeekContext() : act2.getStartTimeWeekContext()) + (act2.tripAfterActivityisScheduled() ? act2.getEstimatedTripTimeAfterActivity() : 0);
		
		if (
					// Start oder Ende der zweiten Aktivit�t liegen im Zeithorizont der ersten
					(starttime_second > starttime_first && starttime_second < endtime_first) ||
					(endtime_second 	> starttime_first && endtime_second 	< endtime_first)
					||
					// Start oder Ende der ersten Aktivit�t liegen im Zeithorizont der zweiten
					(starttime_first > starttime_second && starttime_first < endtime_second) ||
					(endtime_first 	 > starttime_second && endtime_first 	 < endtime_second)
			 )
		{
			result=true;
		}
		
		return result;
	}
	
	/**
	 * Legt wenn m�gliche Startzeiten f�r die �bergebenen Aktivit�ten fest
	 * 
	 * @param actliste
	 */
  public static void createPossibleStarttimes(List<HActivity> actliste)
  {
  	for (HActivity act : actliste)
    {
    	if (!act.startTimeisScheduled())
    	{
        /*
         * Falls die vorhergehende Aktivit�t in Tour bereits eine festgelegte Starteit und Dauer hat, 
         * dann lege die Startzeit ausgehend von der vorhergehenden Aktivit�t fest.
         */
        if (!act.isActivityFirstinTour() 
        				&& act.getPreviousActivityinTour().startTimeisScheduled() 
        				&& act.getPreviousActivityinTour().durationisScheduled())
    		{
        	act.setStartTime(act.getPreviousActivityinTour().getEndTime() + act.getEstimatedTripTimeBeforeActivity());
    		}	
        	
        /*
         * Falls die nachfolgede Aktivit�t in Tour bereits eine festgelegte Starteit hat und die Aktivit�t selbst bereits eine Dauer, 
         * dann lege die Startzeit ausgehend von der nachfolgenden Aktivit�t fest.
         */
        if (!act.isActivityLastinTour() 
        		&& act.durationisScheduled()
        		&& act.getNextActivityinTour().startTimeisScheduled())
        {
        	act.setStartTime(act.getNextActivityinTour().getTripStartTimeBeforeActivity() - act.getDuration());
        }
    	}
    }
  }

	/**
   * Vergleicht zwei Aktivit�ten in ihrer Reihenfolge
   * 
   * 0  - beide Aktivit�ten sind gleich
   * 1  - �bergebene Aktivit�t liegt NACH der anderen Aktivit�t (h�herer Tag, Tour oder Aktindex)
   * -1 - �bergebene Aktivit�t liegt VOR der anderen Aktivit�t
   * 
   * @param acttocompare
   * @return
   */
  public int compareTo(HActivity acttocompare) 
  {
    int result=99;
    if (acttocompare.getWeekDay() >  this.getWeekDay()) result = 1;
    if (acttocompare.getWeekDay() <  this.getWeekDay()) result = -1;
    if (acttocompare.getWeekDay() == this.getWeekDay())
    {
      if (acttocompare.getTour().getIndex() >  this.getTour().getIndex()) result = 1;
      if (acttocompare.getTour().getIndex() <  this.getTour().getIndex()) result = -1;    	
      if (acttocompare.getTour().getIndex() == this.getTour().getIndex())
      {
        if (acttocompare.getIndex() >  this.getIndex()) result = 1;
        if (acttocompare.getIndex() <  this.getIndex()) result = -1;
        if (acttocompare.getIndex() == this.getIndex()) result = 0;
      } 
    }
    assert result!=99 : "Could not compare these two activities! - Act1: " + this + " - Act2: " + acttocompare;
    return result;
  }
 

  @Override
	public String toString()
	{
  	String result="";
  	
  	if (isHomeActivity())
  	{
  		result= getDayIndex() + 	
  				" Start " + (startTimeisScheduled() ? getStartTimeWeekContext() : "n.a.") + 
  				" Ende " + (startTimeisScheduled() && durationisScheduled() ? getEndTimeWeekContext() : "n.a.") + 
  				" Dauer: " + (durationisScheduled() ? this.duration : "n.a.") + 
  				" Typ: " + (activitytypeisScheduled() ? this.type : "n.a.") + " (" + this.mobiToppActType + ")" + 
  				" jointStatus: " + this.jointStatus
  				;  		
  	}
  	else
  	{
  		result= getDayIndex() + "/" + getTour().getIndex() + "/" + getIndex() + 	
		  				" Start " + (startTimeisScheduled() ? getStartTimeWeekContext() : "n.a.") + 
		  				" Ende " + (startTimeisScheduled() && durationisScheduled() ? getEndTimeWeekContext() : "n.a.") + 
		  				" Dauer: " + (durationisScheduled() ? this.duration : "n.a.") + 
		  				" Typ: " + (activitytypeisScheduled() ? this.type : "n.a.") + " (" + this.mobiToppActType + ")" + 
		  				" jointStatus: " + this.jointStatus +
		  				" Weg hin: " + (tripBeforeActivityisScheduled() ? getEstimatedTripTimeBeforeActivity() : "n.a.") + 
		  				" Weg r�ck: " + (tripAfterActivityisScheduled() ? getEstimatedTripTimeAfterActivity() : "n.a.")
		  				;
  	}
		return result;
	}


	public boolean isActivityFirstinTour()
	{
		return getTour().getLowestActivityIndex()==getIndex();
	}
	
	public boolean isActivityLastinTour()
	{
		return getTour().getHighestActivityIndex()==getIndex();
	}
	
	
	/**
	 * 
	 * Bestimmt, ob vor der Aktivit�t ein Arbeitspendelweg von Zuhause stattfindet.
	 * Wird im Modellverlauf zur besseren Bestimmung von default-Wegezeiten genutzt
	 * Kann nur sicher belegt werden, ...
	 * 	... wenn es die erste Aktivit�t der Tour ist
	 *  ... der Zweck "W" ist
	 *  ... die Person eine Arbeitspendelentfernung hat
	 * 
	 * @return
	 */
	public boolean hasWorkCommutingTripbeforeActivity()
	{
		return ((isActivityFirstinTour() && getType()=='W' && (getPerson().getCommutingdistance_work() != 0.0)) ? true : false); 
	}
	
	/**
	 * 	 
	 * Bestimmt, ob nach der Aktivit�t ein Arbeitspendelweg nach Zuhause stattfindet.
	 * Wird im Modellverlauf zur besseren Bestimmung von default-Wegezeiten genutzt
	 * Kann nur sicher belegt werden, ...
	 * 	... wenn es die letzte Aktivit�t der Tour ist
	 *  ... der Zweck "W" ist
	 *  ... die Person eine Arbeitspendelentfernung hat
	 * 
	 * @return
	 */
	public boolean hasWorkCommutingTripafterActivity()
	{
		return ((isActivityLastinTour() && getType()=='W' && (getPerson().getCommutingdistance_work() != 0.0)) ? true : false); 
	}
	
	/**
	 * 
	 * Bestimmt, ob vor der Aktivit�t ein Bildungspendelweg von Zuhause stattfindet.
	 * Wird im Modellverlauf zur besseren Bestimmung von default-Wegezeiten genutzt
	 * Kann nur sicher belegt werden, ...
	 * 	... wenn es die erste Aktivit�t der Tour ist
	 *  ... der Zweck "E" ist
	 *  ... die Person eine Bildungspendelentfernung hat
	 *  
	 * @return
	 */
	public boolean hasEducationCommutingTripbeforeActivity()
	{
		return ((isActivityFirstinTour() && getType()=='E' && (getPerson().getCommutingdistance_education() != 0.0)) ? true : false); 
	}
	
	/**
	 * 	 
	 * Bestimmt, ob nach der Aktivit�t ein Bildungspendelweg nach Zuhause stattfindet.
	 * Wird im Modellverlauf zur besseren Bestimmung von default-Wegezeiten genutzt
	 * Kann nur sicher belegt werden, ...
	 * 	... wenn es die letzte Aktivit�t der Tour ist
	 *  ... der Zweck "E" ist
	 *  ... die Person eine Bildungspendelentfernung hat
	 * 
	 * @return
	 */
	public boolean hasEducationCommutingTripafterActivity()
	{
		return ((isActivityLastinTour() && getType()=='E' && (getPerson().getCommutingdistance_education() != 0.0)) ? true : false); 
	}
	
	/**
	 * 
	 * Gibt an, ob es sich bei der Aktivit�t um eine Home-Aktivit�t handelt
	 * 
	 * @return
	 */
	public boolean isHomeActivity()
	{
		boolean ishome = false;
		if (activitytypeisScheduled() && getType()=='H') ishome = true;
		return ishome;
	}

	/**
	 * 
	 * Gibt an, ob es sich um die Hauptaktivit�t der Tour handelt
	 * 
	 * @return
	 */
	public boolean isMainActivityoftheTour()
	{
		return this.getIndex()==0;
	}
	
	/**
	 * 
	 * Gibt an, ob es sich um die Hauptaktivit�t des Tages handelt
	 * 
	 * @return
	 */
	public boolean isMainActivityoftheDay()
	{
		return isMainActivityoftheTour() && getTour().isMainTouroftheDay();
	}
	
	/**
	 * 
	 * Gibt die vorherige Aktivit�t auf der Tour zur�ck
	 * 
	 * @return
	 */
	public HActivity getPreviousActivityinTour()
	{
		HActivity previousActivity;
		
		//Pr�fe, ob die Aktivt�t die erste in der Tour ist
    if (getIndex() != getTour().getLowestActivityIndex())
    {
    	previousActivity = getTour().getActivity(getIndex()-1);
    }
    // Falls die Aktivit�t die erste ist, gibt es keinen Vorg�nger in der Tour
    else
    {
    	previousActivity = null;
    }
    return previousActivity;
	}
	
	/**
	 * 
	 * Gibt die nachfolgende Aktivit�t auf der Tour zur�ck
	 * 
	 * @return
	 */
	public HActivity getNextActivityinTour()
	{
		HActivity nextActivity;
		
		//Pr�fe, ob die Aktivt�t die letzte in der Tour ist
    if (getIndex() != getTour().getHighestActivityIndex())
    {
    	nextActivity = getTour().getActivity(getIndex()+1);
    }
    // Falls die Aktivit�t die letzte ist, gibt es keinen Nachfolger in der Tour
    else
    {
    	nextActivity = null;
    }
    return nextActivity;
	}
	
	
	/**
	 * 
	 * Gibt die vorherige Au�er-Haus-Aktivit�t des Patterns zur�ck.
	 * 
	 * @return
	 */
	public HActivity getPreviousOutOfHomeActivityinPattern()
	{
		HActivity previousact=null;
		
		// Aktivit�t ist die erste in der Tour, das hei�t ermittel letzte Aktivit�t aus vorheriger Tour
		if (isActivityFirstinTour())
		{
			HTour vorherigeTour = getTour().getPreviousTourinPattern();
			if (vorherigeTour!=null) previousact = vorherigeTour.getLastActivityInTour();
		}
		else
		{
			previousact = getPreviousActivityinTour();
		}
		return previousact;
	}

	/**
	 * 
	 * Gibt die nachfolgende Au�er-Haus-Aktivit�t des Patterns zur�ck.
	 * 
	 * @return
	 */
	public HActivity getNextOutOfHomeActivityinPattern()
	{
		HActivity nextact=null;
		
		// Aktivit�t ist die letzte in der Tour, das hei�t ermittel erste Aktivit�t aus nachfolgender Tour
		if (isActivityLastinTour())
		{
			HTour naechsteTour = getTour().getNextTourinPattern();
			if (naechsteTour!=null) nextact = naechsteTour.getFirstActivityInTour();
		}
		else
		{
			nextact = getNextActivityinTour();
		}
		return nextact;
	}

	
	
	/**
	 * 
	 * Gibt den Endzeitpunkt der Aktivit�t zur�ck
	 * starttime + duration
	 * 
	 * @return
	 */
	public int getEndTime()
	{
		return getStartTime() + getDuration(); 
	}

	/**
	 * 
	 * @return
	 */
	public int getWeekDay()
	{
		int daynumber = -1;
		if (isHomeActivity())
		{
			daynumber = getDay().getWeekday();
		}
		else
		{
			daynumber = getTour().getDay().getWeekday();
		}
		return daynumber;
	}

	/**
	 * Gibt den Index des zugeh�rigen Tages zur�ck
	 * 
	 * @return
	 */
	public int getDayIndex()
	{
		return getDay().getIndex();
	}
	
	/**
	 * Gibt den Index der zugeh�rigen Tour zur�ck
	 * 
	 * @return
	 */
	public int getTourIndex()
	{
		return getTour().getIndex();
	}
	

	public int getStartTimeWeekContext()
	{
		return 1440*getDayIndex() + getStartTime();
	}

	public int getEndTimeWeekContext()
	{
		return getStartTimeWeekContext() + getDuration();
	}

	public int getTripStartTimeBeforeActivity()
	{
		// return getStartTime() - getEstimatedTripTime();
		return getStartTime() - tripbeforeactivity.getTripduration();
	}

	public int getTripStartTimeBeforeActivityWeekContext()
	{
		// return getStartTimeWeekContext() - getEstimatedTripTime();
		return getStartTimeWeekContext() - tripbeforeactivity.getTripduration();
	}
	
	public int getTripStartTimeAfterActivity()
	{
		return getEndTime();
	}
	
	public int getTripStartTimeAfterActivityWeekContext()
	{
		return getEndTimeWeekContext();
	}
	
	/**
	 * @return the tripbeforeactivity
	 */
	public HTrip getTripbeforeactivity() {
		return tripbeforeactivity;
	}

	/**
	 * @param tripbeforeactivity the tripbeforeactivity to set
	 */
	public void setTripbeforeactivity(HTrip tripbeforeactivity) {
		this.tripbeforeactivity = tripbeforeactivity;
	}

	/**
	 * @return the tripafteractivity
	 */
	public HTrip getTripafteractivity() {
		return tripafteractivity;
	}

	/**
	 * @param tripafteractivity the tripafteractivity to set
	 */
	public void setTripafteractivity(HTrip tripafteractivity) {
		this.tripafteractivity = tripafteractivity;
	}

	/**
	 * 
	 * Mean-Time-Berechnung
	 * 
	 * @return
	 */
	public int calculateMeanTime()
	{
		double timebudget = getPerson().getAttributefromMap(getType() + "budget_exact");
		double daysWithAct = getWeekPattern().countDaysWithSpecificActivity(getType());
		double specificActivitiesForCurrentDay = getDay().getTotalAmountOfActivitites(getType());
		
		// Mean-Time-Berechnung (zwischen 1 und 1440)
		double meantime;
		meantime = (int) Math.max((timebudget / daysWithAct) * (1 / specificActivitiesForCurrentDay),1.0);
		meantime = Math.min(meantime, 1440.0);
		
		return (int) meantime;
	}
	
	/**
	 * 
	 * Mean-Time-Kategorie-Berechnung
	 * 
	 * @return
	 */
	public int calculateMeanTimeCategory()
	{
		int meantime = calculateMeanTime();
		int meantimecategory = -99;
		for (int i=0; i<Configuration.NUMBER_OF_ACT_DURATION_CLASSES; i++)
		{
			if (meantime >= Configuration.ACT_TIME_TIMECLASSES_LB[i] && meantime <= Configuration.ACT_TIME_TIMECLASSES_UB[i]) meantimecategory = i;
		}
		assert meantimecategory!=-99 : "Konnte Kategorie nicht ermitteln!";
		return meantimecategory;
	}
	
	/**
	 * 
	 * Berechnet mittlere Wegzeiten je nach Aktivit�t und setzt TripTime Werte
	 * 
	 */
	public void calculateAndSetTripTimes()
	{

		/*
		 * Wegzeit vor der Aktivit�t
		 */
		
		//Default-Annahme
    int actualTripTime_beforeTrip = Configuration.FIXED_TRIP_TIME_ESTIMATOR;
    // Verbessere die Annahme der Wegezeit, falls ein Pendelweg vorliegt
    if (hasWorkCommutingTripbeforeActivity()) actualTripTime_beforeTrip = getPerson().getCommutingDuration_work();
    if (hasEducationCommutingTripbeforeActivity()) actualTripTime_beforeTrip = getPerson().getCommutingDuration_education();
		if (tripbeforeactivity==null)
		{   
			tripbeforeactivity = new HTrip(this, actualTripTime_beforeTrip);
		}
		else
		{
			tripbeforeactivity.setTripduration(actualTripTime_beforeTrip);
		}

		/*
		 * Wegzeit nach der Aktivit�t (nur bei letzter Akt in Tour)
		 */	
		
		if (isActivityLastinTour())
		{
			//Default-Annahme
	    int actualTripTime_afterTrip = Configuration.FIXED_TRIP_TIME_ESTIMATOR;
	    // Verbessere die Annahme der Wegzeit auf dem letzten Weg der Tour nach der Aktivit�t, falls Pendelweg vorliegt
	    if (hasWorkCommutingTripafterActivity()) actualTripTime_afterTrip = getPerson().getCommutingDuration_work();
	    if (hasEducationCommutingTripafterActivity()) actualTripTime_afterTrip = getPerson().getCommutingDuration_education();
	    if (tripafteractivity==null)
	    {
	    	tripafteractivity = new HTrip(this, actualTripTime_afterTrip);
	    }
	    else
	    {
	    	tripafteractivity.setTripduration(actualTripTime_afterTrip);
	    }
	    
		}
	}
	
	public boolean isScheduled()
	{
		return this.duration!=-1 && this.starttime!=-1 && this.type!='x' && this.jointStatus!=-1;
	}
	
	public boolean activitytypeisScheduled()
	{
		return type!='x';
	}
	
	public boolean tripBeforeActivityisScheduled()
	{
		return tripbeforeactivity!=null && tripbeforeactivity.getTripduration()!=-1;
	}
	
	public boolean tripAfterActivityisScheduled()
	{
		return tripafteractivity!=null && tripafteractivity.getTripduration()!=-1;
	}
	
	public boolean durationisScheduled()
	{
		return duration!=-1;
	}
	
	public boolean startTimeisScheduled()
	{
		return starttime!=-1;
	}
	
	/**
	 * @param attributes spezifischesAttribut f�r Map
	 */
	public void addAttributetoMap(String name, Double value) {
		assert !attributes.containsKey(name) : "Attribut ist bereits in Map enthalten!";
		this.attributes.put(name, value);
	}
	
	/**
	 * Liefert gew�nschtes Attribute aus der Map
	 * 
	 * @param name
	 * @return
	 */
	public Double getAttributefromMap(String name) {
		return this.attributes.get(name);
	}


	/**
	 * @return the attributes
	 */
	public Map<String, Double> getAttributesMap() {
		return attributes;
	}
	
	/**
	 * Gibt den Personenindex der Person an, durch welche diese Aktivit�t erstmalig erstellt wurde
	 * Bei gemeinsamen Aktivit�ten, die von anderen Personen �bernommen wurden entspricht dies dem Index 
	 * der Person, die diese Akt erstmals festgelegt hat
	 * 
	 * @return
	 */
	public int getCreatorPersonIndex()
	{
		int result = -1;
		if (attributes.containsKey("CreatorPersonIndex"))
		{
			result = getAttributefromMap("CreatorPersonIndex").intValue();
		}
		else
		{
			result = getPerson().getPersIndex();
		}
		assert result!=-1 : "CreatorPersonIndex konnte nicht bestimmt werden!";
		return result;
	}
	
	/**
	 * 
	 * Legt den Personenindex fest, der diese Aktivit�t erstmals erstellt hat
	 * 
	 * @param persindex
	 */
	public void setCreatorPersonIndex(int persindex)
	{
		if (attributes.containsKey("CreatorPersonIndex"))
		{
			attributes.remove("CreatorPersonIndex");
		}
		attributes.put("CreatorPersonIndex", (double) persindex);
	}

	/**
	 * @return the JointParticipants
	 */
	public List<ActitoppPerson> getJointParticipants() {
		return jointParticipants;
	}

	/**
	 * @param JointParticipants the JointParticipants to set
	 */
	public void setJointParticipants(List<ActitoppPerson> gemJointParticipants) {
		this.jointParticipants = gemJointParticipants;
	}
	
	/**
	 * F�gt eine Person hinzu, die gleichzeitig eine gemeinsame Weg/Akt hat
	 * 
	 * @param person
	 */
	public void addJointParticipant (ActitoppPerson person)
	{
		jointParticipants.add(person);
	}
	
	
	/**
	 * L�scht eine Person aus der Liste mit Personen, die gleichzeitig eine gemeinsame Weg/Akt hat
	 * 
	 * @param person
	 */
	public void removeJointParticipant (ActitoppPerson person)
	{
		jointParticipants.remove(person);
		
		// Wenn dadurch keine Teilnehmer mehr f�r die gemeinsame Aktivit�t �brig ist, entferne den Status als gemeinsame Aktivit�t
		if (jointParticipants.size()==0) setJointStatus(4);
	}
	
}
