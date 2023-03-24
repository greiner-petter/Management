package de.ostfalia.aud.s23ss.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import de.ostfalia.aud.s23ss.base.Employee;
import de.ostfalia.junit.annotations.AfterMethod;
import de.ostfalia.junit.annotations.Define;
import de.ostfalia.junit.annotations.TestDescription;
import de.ostfalia.junit.base.IMessengerRules;
import de.ostfalia.junit.base.ITraceRules;
import de.ostfalia.junit.common.Version;
import de.ostfalia.junit.conditional.PassTrace;
import de.ostfalia.junit.evaluation.Evaluate;
import de.ostfalia.junit.evaluation.collectors.CsvCollector;
import de.ostfalia.junit.evaluation.collectors.FunctionalCollector;
import de.ostfalia.junit.evaluation.collectors.ICollected;
import de.ostfalia.junit.evaluation.collectors.ToStringCollector;
import de.ostfalia.junit.execution.TestCondition;
import de.ostfalia.junit.processing.Prevent;
import de.ostfalia.junit.processing.Spaces;
import de.ostfalia.junit.rules.MessengerRule;
import de.ostfalia.junit.rules.RuleControl;
import de.ostfalia.junit.rules.TraceRule;
import de.ostfalia.junit.runner.TopologicalSortRunner;

@RunWith(TopologicalSortRunner.class)
public class EmployeeTest {

	public RuleControl opt = RuleControl.NONE;
	public IMessengerRules messenger = MessengerRule.newInstance(opt);	
	public ITraceRules trace = TraceRule.newInstance(opt);
	private ITraceRules traceL1 = trace.newSubtrace(opt);
	
	@Rule
	public TestRule chain = RuleChain
							.outerRule(trace)	
							.around(messenger);
	
	@Rule
    public TestRule timeout = new DisableOnDebug(
                              new Timeout(1000, TimeUnit.MILLISECONDS));
	
	private String callMsg   = "Aufruf der %s()-Methoden der erstellten Mitarbeiter.";
	private String returnMsg = "Auswerten der erhaltenen Daten.";
	
	private CsvCollector<Integer, Employee> csvCollector = 
			new CsvCollector<>(traceL1, "%s;%s;%s;%s;%s;%s;%s", 
					          (c, p) -> c.index());
	private ToStringCollector<Integer, Employee> toStrCollector = 
			new ToStringCollector<>(traceL1, (c, o) -> c.index());

	private FunctionalCollector<Integer, Employee> functionCollector = 
			new FunctionalCollector<>(traceL1, "%s", (c, o) -> c.index());
	
	private Evaluate eval = new Evaluate(traceL1);
	
	/**
	 * Datensatz mit 10 Eintraegen als Testdaten fuer die JUnit-Tests.
	 */
	private static String[] data = {
			/*[0]*/ "10005;1955-01-21;Kyoichi;Maliniak;M;1989-09-12;Manpower",
			/*[1]*/ "10034;1962-12-29;Bader;Swan;M;1988-09-21;Sales",
			/*[2]*/ "10041;1959-08-27;Uri;Lenart;F;1989-11-12;Sales",
			/*[3]*/ "10060;1961-10-15;Breannda;Billingsley;M;1987-11-02;Service",
			/*[4]*/ "10948;1952-12-23;Shigehito;Brodie;M;1996-09-30;Development",
			/*[5]*/ "10943;1955-11-19;Berna;Skafidas;M;1988-02-19;Development",
			/*[6]*/ "10942;1952-08-08;Toshimitsu;Larfeldt;F;1989-09-08;Development",
			/*[7]*/ "10938;1958-05-11;Shaowei;Iisaku;F;1985-09-24;Marketing",
			/*[8]*/ "10855;1957-08-07;Kwangho;Reinhart;M;1991-08-05;Finance",
			/*[9]*/ "10796;1959-06-30;Khoa;Rousseau;M;1990-11-08;Management"};
	
	public static Employee[] employees = null;
	
	@BeforeClass
	public static void beforeClass() {
		TestCondition.clear();		
	}
	
	@Before
	public void setUp() throws Exception {
		assertTrue(Version.INCOMPATIBLE, Version.request("4.6.1"));
		PassTrace.preProcessorDefaults();
		PassTrace.preProcessor(PassTrace.CONDITIONS, Prevent.signalNull)
		 	     .append(PassTrace.CONDITIONS, Spaces.removeAll);
		csvCollector.useSeparator("\\s*;\\s*");
		csvCollector.useIdentifier("expected", "[", "]");
		functionCollector.useIdentifier("employee", "[", "]");
		toStrCollector.useIdentifier("employee", "[", "]");
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: Konstruktor der Klasse Employee.<br> 
	 * 		Der Konstruktor wird mit 10 unterschiedlichen
	 *      csv-Datensaetzen aufgerufen.</li>
	 *	<li>Erwartet: Konstruktor wird erfolgreich durchlaufen.</li>
	 *	<li>Beispiel  fuer data[0]: "10005;1955-01-21;Kyoichi;Maliniak;M;1989-09-12;Human Resources"</li>
	 * </ul>
	 */
	@Test
	@TestDescription("Testen des Konstruktors.")
	public void testKonstruktor() {
		if (TestCondition.get() == null) {
			TestCondition.set("Constructor");
			trace.add("Erstellen von %d Mitarbeitern.", data.length);
			employees = new Employee[data.length];
			for (int i = 0; i < data.length; i++) {
				traceL1.add(Evaluate.callConstruktor(Employee.class, data[i]));
				employees[i] = new Employee(data[i]);				
			}			
			trace.add(traceL1, traceL1.hasOccurrences());
			trace.separator();
			TestCondition.set("ConstructorSuccess");
		}
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: toString()-Methode der Klasse Employee.<br> 
	 * 		Es werden alle 10 Datensaetze nacheinander durchlaufen.</li>
	 *	<li>Erwartet: Rueckgabe aller Attribute als String.</li>
	 *	<li>Beispiel  fuer data[0]: "10005;1955-01-21;Kyoichi;Maliniak;M;1989-09-12;Human Resources"</li>
	 * </ul>
	 */
	@Test
	@Define("ConstructorSuccess")
	@AfterMethod("testKonstruktor")
	@TestDescription("Testen der toString()-Methode.")
	public void testToString() {
		testKonstruktor();
		ICollected<Integer, Employee> exp = csvCollector.collectAll(data);
		trace.add("Sammeln der toString()-Rueckgaben.");
		ICollected<Integer, Employee> got = toStrCollector.collectAll(employees);
		trace.add(traceL1);
		
		trace.add(returnMsg);
		eval.equalsByOrder(exp, got);
		eval.addToTrace(trace);
		
		trace.separator();		
		assertFalse("Fehler bei der Implementierung der toString()-Methode.",
				trace.hasOccurrences());
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: getKey()-Methode der Klasse Employee.</li>
	 *	<li>Erwartet: Rueckgabe des Schlüssels als int.</li>
	 *	<li>Beispiel fuer data[0]: 10005.</li>
	 * </ul>
	 */
	@Test
	@Define("ConstructorSuccess")
	@AfterMethod("testKonstruktor")
	@TestDescription("Testen der getKey()-Methode.")
	public void testGetKey() {	
		testKonstruktor();
		String method = "getKey";
		functionCollector.define(method, (obj, c) -> obj.getKey());
		
		evaluate(method, 0);
		assertFalse("Fehler bei der Rueckgabe des Schlüssels.",
				trace.hasOccurrences());
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: getName()-Methode der Klasse Employee.</li>
	 *	<li>Erwartet: Rueckgabe des Nachnamens als String.</li>
	 *	<li>Beispiel fuer data[0]: Kyoichi.</li>
	 * </ul>
	 */
	@Test
	@Define("ConstructorSuccess")
	@AfterMethod("testKonstruktor")
	@TestDescription("Testen der getName()-Methode.")
	public void testGetName() {
		testKonstruktor();
		String method = "getName";
		functionCollector.define(method, (obj, c) -> obj.getName());
		
		evaluate(method, 3);
		assertFalse("Fehler bei der Rueckgabe des Nachnamens.",
				trace.hasOccurrences());
	}

	/**
	 * <ul>
	 * 	<li>Testfall: getFirstName()-Methode der Klasse Employee.</li>
	 *	<li>Erwartet: Rueckgabe des Vornamens als String.</li>
	 *	<li>Beispiel fuer data[0]: Maliniak.</li>
	 * </ul>
	 */
	@Test
	@Define("ConstructorSuccess")
	@AfterMethod("testKonstruktor")
	@TestDescription("Testen der getFirstName()-Methode.")
	public void testGetFirstName() {
		testKonstruktor();
		String method = "getFirstName";
		functionCollector.define(method, (obj, c) -> obj.getFirstName());
		
		evaluate(method, 2);		
		assertFalse("Fehler bei der Rueckgabe des Vornamens.",
				trace.hasOccurrences());
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: getBirthdate()-Methode der Klasse Employee.</li>
	 *	<li>Erwartet: Rueckgabe des Geburtsdatum als LocalDate.</li>
	 *	<li>Beispiel fuer data[0]: 1955-01-21.</li>
	 * </ul>
	 */
	@Test
	@Define("ConstructorSuccess")
	@AfterMethod("testKonstruktor")
	@TestDescription("Testen der getBirthdate()-Methode.")
	public void testGetBirthdate() {
		testKonstruktor();
		String method = "getBirthdate";
		functionCollector.define(method, (obj, c) -> obj.getBirthdate());
		
		evaluate(method, 1);
		assertFalse("Fehler bei der Rueckgabe des Geburtsdatums.",
				trace.hasOccurrences());
	}

	/**
	 * <ul>
	 * 	<li>Testfall: getGender()-Methode der Klasse Employee.</li>
	 *	<li>Erwartet: Rueckgabe des Geschlechts als enum Gender.</li>
	 *	<li>Beispiel fuer data[0]: M.</li>
	 * </ul>
	 */
	@Test
	@Define("ConstructorSuccess")
	@AfterMethod("testKonstruktor")
	@TestDescription("Testen der getGender()-Methode.")
	public void testGender() {
		testKonstruktor();
		String method = "getGender";
		functionCollector.define(method, (obj, c) -> obj.getGender());

		evaluate(method, 4);
		assertFalse("Fehler bei der Rueckgabe des Geschlechts.",
				trace.hasOccurrences());
	}

	/**
	 * <ul>
	 * 	<li>Testfall: getDepartment()-Methode der Klasse Employee.</li>
	 *	<li>Erwartet: Rueckgabe des Departments als enum Department.</li>
	 *	<li>Beispiel fuer data[0]: Manpower.</li>
	 * </ul>
	 */
	@Test
	@Define("ConstructorSuccess")
	@AfterMethod("testKonstruktor")
	@TestDescription("Testen der getDepartment()-Methode.")
	public void testDepartment() {
		testKonstruktor();
		String method = "getDepartment";
		functionCollector.define(method, (obj, c) -> obj.getDepartment());
		
		evaluate(method, 6);
		assertFalse("Fehler bei der Rueckgabe des Departments.",
				trace.hasOccurrences());
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: getHiredate()-Methode der Klasse Employee.</li>
	 *	<li>Erwartet: Rueckgabe des Einstellungsdatums als LocalDate.</li>
	 *	<li>Beispiel fuer data[0]: 1989-09-12.</li>
	 * </ul>
	 */
	@Test
	@Define("ConstructorSuccess")
	@AfterMethod("testKonstruktor")
	@TestDescription("Testen der getHiredate()-Methode.")
	public void testHiredate() {
		testKonstruktor();
		String method = "getHiredate";
		functionCollector.define(method, (obj, c) -> obj.getHiredate());
		
		evaluate(method, 5);
		assertFalse("Fehler bei der Rueckgabe des Einstellungsdatums.",
				trace.hasOccurrences());
	}
	
	//------------------------------------------------------------------------------
	
	private void evaluate(String methodName, int colum) {
		csvCollector.useFormat(Locale.ENGLISH, "%" + (colum + 1) + "$s");		
		ICollected<Integer, Employee> exp = csvCollector.collectAll(data);		
		trace.add(callMsg, methodName);
	
		ICollected<Integer, Employee> got = functionCollector.collectAll(employees);
		trace.add(traceL1);
		assertFalse("Erhaltene Daten sind fehlerhaft.", trace.hasOccurrences());
		
		trace.add(returnMsg);
		eval.equalsByOrder(exp, got);
		eval.addToTrace(trace);
		
		trace.separator();
	}

}
