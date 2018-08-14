package edu.kit.ifv.mobitopp.actitopp.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import edu.kit.ifv.mobitopp.actitopp.*;


public class CSVExportWriter 
{
	
	FileWriter writer;
	
	/**
	 * 
	 * Konstruktor
	 *
	 * @param filename
	 * @throws IOException
	 */
	public CSVExportWriter(String filename) throws IOException
	{
		writer = new FileWriter(filename);
	}
	

	/**
	 * 
	 * Export der Wegedaten der Personen
	 * 
	 * @param personmap
	 * @throws IOException
	 */
	public void exportTripData(HashMap<Number,ActitoppPerson> personmap) throws IOException
	{
						  	
	  	// Header
	  	writer.append("HHIndex;PersNr;PersIndex;WOTAG;anzeit;anzeit_woche;abzeit;abzeit_woche;Dauer;zweck_text;jointStatus");
	  	writer.append('\n');
	  	writer.flush();

	  	// Durchlaufe alle Personen
	  	for (Number key : personmap.keySet())
			{
				ActitoppPerson actperson = personmap.get(key);
	  		
	  		// Weginformation aus Aktivit�tsdaten herauslesen
	  		for (HActivity act : actperson.getWeekPattern().getAllActivities())
	  		{
	  			if (act.tripBeforeActivityisScheduled() && act.getEstimatedTripTimeBeforeActivity()>0)
	  			{
	    			writer.append(writeTripBeforeActivity(act));
	    			writer.flush();
	  			}
	  			if (act.tripAfterActivityisScheduled() && act.getEstimatedTripTimeAfterActivity()>0)
	  			{
	    			writer.append(writeTripAfterActivity(act));
	    			writer.flush();
	  			}
	  		}
	  	}
	  	writer.close();
	}
	
	
	/**
	 * 
	 * Export der Aktivit�tsdaten der Personen
	 * 
	 * @param personmap
	 * @throws IOException
	 */
	public void exportActivityData(HashMap<Number,ActitoppPerson> personmap) throws IOException
	{
						  	
	  	// Header
	  	writer.append("HHIndex;PersNr;PersIndex;WOTAG;TourIndex;AktIndex;startzeit;startzeit_woche;endzeit;endzeit_woche;Dauer;zweck;jointStatus");
	  	writer.append('\n');
	  	writer.flush();

	  	// Durchlaufe alle Personen
	  	for (Number key : personmap.keySet())
			{
				ActitoppPerson actperson = personmap.get(key);
	  		
	  		// F�ge alle Aktivit�ten hinzu
	  		for (HActivity act : actperson.getWeekPattern().getAllActivities())
	  		{
	  			if (act.isScheduled())
	  			{
	    			writer.append(writeActivity(act));
	    			writer.flush();
	  			}
	  		}
	  	}
	  	writer.close();
	}
	
	
	/**
	 * 
	 * Schreibe Zeile mit Aktivit�teninfos
	 * 
	 * @param act
	 * @return
	 */
	public String writeActivity(HActivity act)
	{
		
		assert act.isScheduled():"Activity is not fully scheduled";
		
		String rueckgabe="";
		
		/*
		 * TourINdex und Aktindex f�r Heimaktivit�ten bestimmen
		 * Falls Tag ein kompletter Heimtag ist, dann wird 0/0 zur�ckgegeben, ansonsten -99/-99
		 */
		int tmptourindex=0;
		int tmpaktindex=0;
		if (act.getDay().getAmountOfTours()>0)
		{
			tmptourindex=-99;
			tmpaktindex=-99;
		}
			
		// HHIndex
		rueckgabe += act.getPerson().getHousehold().getHouseholdIndex() + ";";		
		// PersNr
		rueckgabe += act.getPerson().getPersNrinHousehold() + ";";
		// PersIndex
		rueckgabe += act.getPerson().getPersIndex() + ";";
		// WOTAG
		rueckgabe += act.getWeekDay() + ";";
		// TourIndex
		rueckgabe += (act.isHomeActivity() ? tmptourindex : act.getTourIndex()) + ";";
		// AktIndex
		rueckgabe += (act.isHomeActivity() ? tmpaktindex : act.getIndex()) + ";";
		// Startzeit
		rueckgabe += act.getStartTime() + ";";
		// Startzeit_woche
		rueckgabe += act.getStartTimeWeekContext() + ";"; 
		// Endzeit
		rueckgabe += act.getEndTime() + ";";		
		// Endzeit_woche
		rueckgabe += act.getEndTimeWeekContext() + ";";  
		// Dauer
		rueckgabe += act.getDuration() + ";";
		// Zweck
		rueckgabe += act.getType() + ";";
		// joint Status
		rueckgabe += (Configuration.model_joint_actions ? act.getJointStatus(): "-99") + "";
		
		rueckgabe +="\n";
		return rueckgabe;		
	}
	
	/**
	 * 
	 * Schreibe Zeile mit Weginfos (vor der Aktivit�t)
	 * 
	 * @param act
	 * @return
	 */
	public String writeTripBeforeActivity(HActivity act)
	{
		
		assert act.tripBeforeActivityisScheduled(): "Trip is not scheduled!";
	
		String rueckgabe="";
		
		// HHIndex
		rueckgabe += act.getPerson().getHousehold().getHouseholdIndex() + ";";		
		// PersNr
		rueckgabe += act.getPerson().getPersNrinHousehold() + ";";
		// PersIndex
		rueckgabe += act.getPerson().getPersIndex() + ";";
		// WOTAG
		rueckgabe += act.getWeekDay() + ";";
		//anzeit
		rueckgabe += act.getStartTime() + ";";
		// anzeit_woche
		rueckgabe += act.getStartTimeWeekContext() + ";"; 
		// abzeit
		rueckgabe += act.getTripStartTimeBeforeActivity() + ";";		
		// abzeit_woche
		rueckgabe += act.getTripStartTimeBeforeActivityWeekContext() + ";";  
		// Dauer
		rueckgabe += act.getEstimatedTripTimeBeforeActivity() + ";";
		// Zweck
		rueckgabe += act.getType() + ";";
		// jointStatus
		rueckgabe += (Configuration.model_joint_actions ? act.getJointStatus(): "-99") + "";
		
		rueckgabe +="\n";
		return rueckgabe;		
	}
	
	/**
	 * 
	 * Schreibe Zeile mit Weginfos (nach der Aktivit�t)
	 * 
	 * @param act
	 * @return
	 */
	public String writeTripAfterActivity(HActivity act)
	{
		
		assert act.tripAfterActivityisScheduled(): "Trip is not scheduled!";
				
		String rueckgabe="";
		
		// HHIndex
		rueckgabe += act.getPerson().getHousehold().getHouseholdIndex() + ";";		
		// PersNr
		rueckgabe += act.getPerson().getPersNrinHousehold() + ";";
		// PersIndex
		rueckgabe += act.getPerson().getPersIndex() + ";";
		// WOTAG
		rueckgabe += act.getWeekDay() + ";";
		//anzeit
		rueckgabe += act.getTripStartTimeAfterActivity() + act.getEstimatedTripTimeAfterActivity() + ";";
		// anzeit_woche
		rueckgabe += act.getTripStartTimeAfterActivityWeekContext() + act.getEstimatedTripTimeAfterActivity() + ";";
		// abzeit
		rueckgabe += act.getEndTime() + ";";		
		// abzeit_woche
		rueckgabe += act.getEndTimeWeekContext() + ";";  
		// Dauer
		rueckgabe += act.getEstimatedTripTimeAfterActivity() + ";";
		// Zweck ist hier immer HOME, da letzter Weg in Tour nach Hause
		rueckgabe += "H" + ";";
		// jointStatus - aktuell immer 4 (keine Gemeinsamkeit, da Gemeinsamkeiten bei NachHause Wegen noch nicht modelliert werden!
		rueckgabe += (Configuration.model_joint_actions ? "4": "-99") + "";
		
		rueckgabe +="\n";
		return rueckgabe;		
	}
	
}
