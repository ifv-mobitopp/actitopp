package edu.kit.ifv.mobitopp.actitopp.demo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import edu.kit.ifv.mobitopp.actitopp.*;

public class ExampleActiTopp {
	
	private static ModelFileBase fileBase = new ModelFileBase();
	private static RNGHelper randomgenerator = new RNGHelper(1234);
	private static DebugLoggers debugloggers = new DebugLoggers();
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
	
		createAndModelOnePerson_Example1();

		createAndModelOnePerson_Example2();
		
	  createAndModelOnePerson_Example3();
	
		createAndModelMultiplePersons_Example1();
	
		createAndModelMultiplePersons_Example2();
		
		/*beispielhafte Verwendung eines Debug-Loggers f�r Entscheidung 2A*/
		debugloggers.addDebugLogger("2A");

		createAndModelMultiplePersons_Example2();

		debugloggers.exportLoggerInfos("2A", "D:/DemoLogger2A.csv");
		
	}
	
	
	/**
	 * 
	 * Erzeugung einer Person inkl. Aktivit�tenplan - Beispiel 1
	 * 
	 */
	public static void createAndModelOnePerson_Example1()
	{
		ActitoppPerson testperson = new ActitoppPerson(
				10, 	// PersIndex
				0, 		// Kinder 0-10
				1, 		// Kinder unter 18
				55, 	// Alter
				1, 		// Beruf
				1, 		// Geschlecht
				2, 		// Raumtyp
				2			// Pkw im HH
				);		
		System.out.println(testperson);
					
	 /* 
	  * Erzeuge Schedules f�r die Person bis der Schedule keine Fehler mehr hat.
	  *  
		* In einigen F�llen kommt es aufgrund ung�nstiger Zufallszahlen zu �berlappungen
		* in den Aktivit�tenpl�nen (bspw. nicht genug Zeit f�r alle Aktivit�ten).
		* In diesen seltenen F�llen wird die Planerstellung mit einer neuen Zufallszahl wiederholt.
		*/
		boolean scheduleOK = false;
    while (!scheduleOK)
    {
      try
      {
    		// Erzeuge Wochenaktivit�tenplan
     	 testperson.generateSchedule(fileBase, randomgenerator);
    		
        scheduleOK = true;                
      }
      catch (InvalidPatternException e)
      {
        System.err.println(e.getReason());
        System.err.println("person involved: " + testperson.getPersIndex());
      }
	  }
		
		//testperson.getweekPattern().printOutofHomeActivitiesList();
		testperson.getWeekPattern().printAllActivitiesList();
	}
	
	
	/**
	 * 
	 * Erzeugung einer Person inkl. Aktivit�tenplan - Beispiel 2
	 * 
	 */
	public static void createAndModelOnePerson_Example2()
	{
		ActitoppPerson testperson = new ActitoppPerson(
				20, 	// PersIndex
				0, 		// Kinder 0-10
				1, 		// Kinder unter 18
				55, 	// Alter
				1, 		// Beruf
				1, 		// Geschlecht
				2, 		// Raumtyp
				2,		// Pkw im HH
				3.0,	// Pendeldistanz zur Arbeit in Kilometern	(0 falls kein Pendeln)
				0.0		// Pendeldistanz zu Bildungszwecken in Kilometern (0 falls kein Pendeln)
				);		
		System.out.println(testperson);
		
	 /* 
	  * Erzeuge Schedules f�r die Person bis der Schedule keine Fehler mehr hat.
	  *  
		* In einigen F�llen kommt es aufgrund ung�nstiger Zufallszahlen zu �berlappungen
		* in den Aktivit�tenpl�nen (bspw. nicht genug Zeit f�r alle Aktivit�ten).
		* In diesen seltenen F�llen wird die Planerstellung mit einer neuen Zufallszahl wiederholt.
		*/
		boolean scheduleOK = false;
		while (!scheduleOK)
		{
			 try
			 {
				// Erzeuge Wochenaktivit�tenplan
				 testperson.generateSchedule(fileBase, randomgenerator);
				
			   scheduleOK = true;                
			 }
			 catch (InvalidPatternException e)
			 {
			   System.err.println(e.getReason());
			   System.err.println("person involved: " + testperson.getPersIndex());
			 }
		}
		
		//testperson.getweekPattern().printOutofHomeActivitiesList();
		testperson.getWeekPattern().printAllActivitiesList();
	}
	
	
	
	
	/**
	 * 
	 * Erzeugung einer Person inkl. Haushaltskontext und Aktivit�tenplan - Beispiel 3
	 * 
	 */
	public static void createAndModelOnePerson_Example3()
	{
		
		ActiToppHousehold testhousehold = new ActiToppHousehold(
				1,		// Haushaltsindex
				0, 		// Kinder 0-10
				1, 		// Kinder unter 18
				2, 		// Raumtyp
				2			// Pkw im HH
				);
		
		ActitoppPerson testperson = new ActitoppPerson(
				testhousehold,  // Haushalt
				1,							// PersNr im Haushalt
				10, 						// PersIndex
				55, 						// Alter
				1, 							// Beruf
				1 							// Geschlecht
				);		
		
		// Person zum Haushalt hinzuf�gen
		testhousehold.addHouseholdmember(testperson, testperson.getPersIndex());
		
		System.out.println(testperson);
					
	 /* 
	  * Erzeuge Schedules f�r die Person bis der Schedule keine Fehler mehr hat.
	  *  
		* In einigen F�llen kommt es aufgrund ung�nstiger Zufallszahlen zu �berlappungen
		* in den Aktivit�tenpl�nen (bspw. nicht genug Zeit f�r alle Aktivit�ten).
		* In diesen seltenen F�llen wird die Planerstellung mit einer neuen Zufallszahl wiederholt.
		*/
		boolean scheduleOK = false;
    while (!scheduleOK)
    {
      try
      {
    		// Erzeuge Wochenaktivit�tenplan
     	 testperson.generateSchedule(fileBase, randomgenerator);
    		
        scheduleOK = true;                
      }
      catch (InvalidPatternException e)
      {
        System.err.println(e.getReason());
        System.err.println("person involved: " + testperson.getPersIndex());
      }
	  }
		
		//testperson.getweekPattern().printOutofHomeActivitiesList();
		testperson.getWeekPattern().printAllActivitiesList();
	}
	
	
	/**
	 * 
	 * Erzeugung mehrerer Personen inkl. Aktivit�tenplan
	 * 
	 */
	public static void createAndModelMultiplePersons_Example1()
	{
		try
		{			
			CSVPersonInputReader loader = new CSVPersonInputReader(ModelFileBase.class.getResourceAsStream("demo/Demopersonen.csv"));
			HashMap<Number, ActitoppPerson> personmap = loader.loadInput_withouthouseholdcontexts();
			
			for (Number key : personmap.keySet())
			{
				ActitoppPerson actperson = personmap.get(key);
				System.out.println(actperson);
				// System.out.println(actperson.getPersIndex());
				
				
			 /* 
			  * Erzeuge Schedules f�r die Person bis der Schedule keine Fehler mehr hat.
			  *  
				* In einigen F�llen kommt es aufgrund ung�nstiger Zufallszahlen zu �berlappungen
				* in den Aktivit�tenpl�nen (bspw. nicht genug Zeit f�r alle Aktivit�ten).
				* In diesen seltenen F�llen wird die Planerstellung mit einer neuen Zufallszahl wiederholt.
				*/
				boolean scheduleOK = false;
		    while (!scheduleOK)
		    {
	        try
	        {
	      		// Erzeuge Wochenaktivit�tenplan
	        	actperson.generateSchedule(fileBase, randomgenerator);
	      		
	          scheduleOK = true;                
	        }
	        catch (InvalidPatternException e)
	        {
	          System.err.println(e.getReason());
	        }
		    }
				
				actperson.getWeekPattern().printAllActivitiesList();
				 
			}
				
			// Output als CSV-Datei
			CSVExportWriter tripwriter = new CSVExportWriter("D:/DemoTripList.csv");
			tripwriter.exportTripData(personmap);
			
			CSVExportWriter activitywriter = new CSVExportWriter("D:/DemoActivityList.csv");
			activitywriter.exportActivityData(personmap);
			
			System.out.println("all persons processed!");	
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * Erzeugung mehrerer Personen inkl. Haushaltskontext & Aktivit�tenplan
	 * 
	 * 
	 */
	public static void createAndModelMultiplePersons_Example2()
	{
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm:ss");   
		
		LocalDateTime start = LocalDateTime.now();
		long timestart = System.currentTimeMillis();
		System.out.println(start.format(df));   
				
		/*
		 * 
		 * read input information from file system
		 * 
		 */
		HashMap<Number, ActiToppHousehold> householdmap = null;
				
		try
		{
			CSVHouseholdInputReader hhloader = new CSVHouseholdInputReader(ModelFileBase.class.getResourceAsStream("demo/Demo_HHInfo.csv"));
			CSVPersonInputReader personloader = new CSVPersonInputReader(ModelFileBase.class.getResourceAsStream("demo/Demo_Personen_mitHHIndex.csv"));
			householdmap = hhloader.loadInput();			
			personloader.loadInput(householdmap);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		assert householdmap!=null : "could not create householdmap.";
		
		// Model Household using parallel streams
		householdmap.values().parallelStream().forEach(ExampleActiTopp::runHousehold);
		
		try
		{
			// Output der Wege als CSV-Datei
			CSVExportWriter tripwriter = new CSVExportWriter("D:/DemoTripList.csv");
			tripwriter.exportTripData(householdmap);
						
			// Output der Aktivit�ten als CSV-Datei
			CSVExportWriter activitywriter = new CSVExportWriter("D:/DemoActivityList.csv");
			activitywriter.exportActivityData(householdmap);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
			
		System.out.println("all persons processed!");	
		
		
		LocalDateTime end = LocalDateTime.now();
		long timeende = System.currentTimeMillis();
		System.out.println("End: " + end.format(df)); 
		
		
		long dauer_msec = (timeende - timestart);
		System.out.println("Duration total: " + dauer_msec + " milli sec"); 
		double dauer_msec_perhh = dauer_msec / householdmap.size();
		System.out.println("Duration per HH: " + dauer_msec_perhh + " milli sec");


	}


	private static void runHousehold(ActiToppHousehold acthousehold) 
	{
		System.out.println("HH: " + acthousehold.getHouseholdIndex() + " HHGRO: " + acthousehold.getNumberofPersonsinHousehold());
		
						
		/* 
		 * Erzeuge Schedules f�r die Person bis der Schedule keine Fehler mehr hat.
		 * 
		 * Reihenfolge der Aktivit�tenplanmodellierung orientiert sich an der Wahrscheinlichkeit des Anteils gemeinsamer Aktivit�ten
		 * 
		 * In einigen F�llen kommt es aufgrund ung�nstiger Zufallszahlen zu �berlappungen
		 * in den Aktivit�tenpl�nen (bspw. nicht genug Zeit f�r alle Aktivit�ten).
		 * In diesen seltenen F�llen wird die Planerstellung mit einer neuen Zufallszahl wiederholt.
		 */
		
		boolean householdscheduleOK = false;
		while (!householdscheduleOK)
		{	
			try
			{
				
				//DebugLogger explizit f�r diesen Haushalt erzeugen
				DebugLoggers hhlogger = new DebugLoggers(debugloggers);
				
				// Pl�ne f�r den gesamten Haushalt generieren
				acthousehold.generateSchedules(fileBase, randomgenerator, hhlogger);

				//System.out.println("HHdone: " + key);
				householdscheduleOK = true;
				
				//Die DebugInformationen zu dem �bergeordneten Logger hinzuf�gen
				debugloggers.addHouseholdDebugInfotoOverallLogger(hhlogger);
				
			}
			catch (InvalidPatternException e)
			{
				System.err.println(e.getReason());
				
		    // Setze die Modellierungsergebnisse dieses Haushalts zur�ck f�r neuen Versuch
		    acthousehold.resetHouseholdModelingResults();
			}
		}
	}

}
