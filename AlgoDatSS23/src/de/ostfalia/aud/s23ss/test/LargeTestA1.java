package de.ostfalia.aud.s23ss.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import de.ostfalia.aud.s23ss.a1.Management;
import de.ostfalia.aud.s23ss.base.Department;
import de.ostfalia.aud.s23ss.base.Employee;
import de.ostfalia.aud.s23ss.base.IEmployee;
import de.ostfalia.aud.s23ss.base.IManagement;
import de.ostfalia.junit.annotations.AfterMethod;
import de.ostfalia.junit.annotations.Define;
import de.ostfalia.junit.annotations.TestDescription;
import de.ostfalia.junit.base.IMessengerRules;
import de.ostfalia.junit.base.ITraceRules;
import de.ostfalia.junit.common.Format;
import de.ostfalia.junit.common.Version;
import de.ostfalia.junit.conditional.Natural;
import de.ostfalia.junit.conditional.PassTrace;
import de.ostfalia.junit.evaluation.Evaluate;
import de.ostfalia.junit.evaluation.collectors.CsvCollector;
import de.ostfalia.junit.evaluation.collectors.FunctionalCollector;
import de.ostfalia.junit.evaluation.collectors.ICollected;
import de.ostfalia.junit.evaluation.collectors.ICollectorItem;
import de.ostfalia.junit.execution.TestCondition;
import de.ostfalia.junit.processing.Prevent;
import de.ostfalia.junit.processing.Spaces;
import de.ostfalia.junit.rules.MessengerRule;
import de.ostfalia.junit.rules.RuleControl;
import de.ostfalia.junit.rules.TraceRule;
import de.ostfalia.junit.runner.TopologicalSortRunner;

@RunWith(TopologicalSortRunner.class)
public class LargeTestA1 {

	public boolean evalOperations = true;
	public static final int MAXVIEW = 10;
	
	public RuleControl opt = RuleControl.NONE;
	public IMessengerRules messenger = MessengerRule.newInstance(opt);	
	public ITraceRules trace = TraceRule.newInstance(opt);
	private ITraceRules traceL1 = trace.newSubtrace(RuleControl.NONE);
		
	@Rule
	public TestRule chain = RuleChain
							.outerRule(trace)	
							.around(messenger);
	
	/**
	 * Datei mit 10000 Datensaetze fuer die JUnit-Tests.
	 */
	private int fileLength  = 10000;
	public String fileName = "Materialien/10k_employees.csv";
	
	private String msgNotNull = "Methode %s(%s) darf nicht null liefern.";
	private String msgError   = "Fehlerhafte Datensaetze in der Mitarbeiterverwaltung.";
	private String msgCount   = "Aufruf von numberOfOperations() und aufsummieren der Anzahl an Operationen.";
	
	private CsvCollector<Integer, IEmployee> csvCollector = 
			new CsvCollector<>(traceL1, "%s;%s;%s;%s;%s;%s;%s", 
			(c, p) -> Integer.parseInt(p[0].toString()));
	
//	private ToStringCollector<Integer, IEmployee> toStrCollector = 
//			new ToStringCollector<>(traceL1, (c, o) -> c.index());
	
	private FunctionalCollector<Integer, IEmployee> funcCollector = 
			new FunctionalCollector<>(traceL1, "%s", 
					(c, o) -> (o != null) ? o.getKey() : c.index());
	
	private static String[] data = {
			/*[0]*/ "10005;1955-01-21;Kyoichi;Maliniak;M;1989-09-12;Manpower",
			/*[1]*/ "10034;1962-12-29;Bader;Swan;M;1988-09-21;Sales",
	};
	
	private Evaluate eval = new Evaluate(traceL1).occurrenceLimits(MAXVIEW);
	public static ICollected<Integer, IEmployee> expData = null;
	
	@BeforeClass
	public static void beforeClass() {
		TestCondition.clear();		
	}
	
	@Before
	public void setUp() throws Exception {
		assertTrue(Version.INCOMPATIBLE, Version.request("4.6.1"));
		PassTrace.preProcessor(PassTrace.CONDITIONS, Prevent.signalNull)
				 .append(PassTrace.CONDITIONS, Spaces.removeAll);
		Format.useLocale(Locale.GERMAN);
		funcCollector.define("toString", (obj, c) -> obj.toString());
		if (expData == null) {			
			expData = csvCollector.useSeparator("\\s*;\\s*")
			  					  .useCsvRows(1)
			  					  .collectAll(fileName);
		}
		if (!evalOperations) {
			System.err.println("Ueberpruefung der Anzahl der Operationen "
							 + "ist aktuell deaktiviert!");
		}
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Ueberpruefen der Klasse Employee.<br> 
	 * 		Konstruktoraufruf der Klasse Employee und auswerten
	 * 		der Rueckgaben der Methoden getKey() und toString().</li>
	 *	<li>Erwartet: <br>
	 *		Konstruktor wird erfolgreich durchlaufen, die
	 *		Rueckgaben der Methoden entsprechen den erwarteten Werten.</li>
	 *	<li>Beispiel: "10005;1955-01-21;Kyoichi;Maliniak;M;1989-09-12;Human Resources"</li>
	 * </ul>
	 */
	@Test (timeout = 1000)
	@TestDescription("Testen der Employee-Klasse.")
	public void testEmployee() {
		if (TestCondition.get() == null) {
			TestCondition.set("Employee");
			trace.add("Ueberpruefen der Employee-Klasse.");
			traceL1.add(Evaluate.callConstruktor("employee",Employee.class, data[0]));
			IEmployee empl = new Employee(data[0]);
			traceL1.add(Evaluate.callConstruktor(Employee.class, data[1]));
			new Employee(data[1]);

			traceL1.add(Evaluate.callMethod("employee", "getKey"));
			int key = empl.getKey();
			traceL1.addInfo(PassTrace.ifEquals("Rueckgabe der Methode.", 10005, key));

			traceL1.add(Evaluate.callMethod("employee", "toString"));
			String str = empl.toString();
			traceL1.addInfo(PassTrace.ifEquals("Rueckgabe der Methode.", data[0], str));
			trace.add(traceL1, traceL1.hasOccurrences());
			
			trace.separator();		
			assertFalse("Employee-Klasse fehlerhaft implementiert.",
					trace.hasOccurrences());
			TestCondition.set("EmployeeSuccess");
		}
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br> 
	 * 		Konstruktoraufruf Management(String) und anschliessendes 
	 * 		ueberpruefen der gespeicherten Eintraege in der Mitarbeiterverwaltung. 
	 * 		Die verwendete Datensatzdatei enthaelt 10.000 Eintraege.</li>
	 *	<li>Erwartet: 
	 *		<ul>
	 *			<li>Anzahl Eintraege in der Mitarbeiterverwaltung: 10.000.</li>
	 *			<li>toArray() liefert ein Array der Groesse 10.000.</li>
	 *			<li>Die gespeicherten Daten entsprechen den uebergebenen Datensaetzen.</li>
	 *			<li>Anzahl Operationen: 10.000 &plusmn; 1.000.</li>
	 *		</ul> 
	 *	</li>
	 * </ul>
	 * @throws IOException 
	 * wird ausgeloest, wenn ein E/A-Fehler auftritt, also das Lesen aus 
	 * der Datensatzdatei fehlschlaegt.
	 */		
	@Test (timeout = 3000)
	@Define("EmployeeSuccess")
	@AfterMethod("testEmployee")
	@TestDescription("Testen des Kontruktors(String).")
	public void testKonstruktorString() throws IOException {
		testEmployee();
		trace.add(Evaluate.callConstruktor("mgnt", Management.class, fileName));
		IManagement mgnt = new Management(fileName);
		
		trace.add("Aufruf der Methode mgnt.numberOfOperations().");
		int count = mgnt.numberOfOperations();
		trace.add("Aufruf der Methode mgnt.size().");
		int size = mgnt.size();
		trace.add("Aufruf der Methode mgnt.toArray().");
		IEmployee[] emp = mgnt.toArray();		
						
		evaluate(emp, size, fileLength);
		evaluate(expData, true, emp);
		evaluate(count, fileLength, fileLength / 10);
	}

	/**
	 * <ul>
	 * 	<li>Testfall: <br> 
	 * 		Konstruktoraufruf Management() und anschliessendes einfuegen
	 *  	von 10.000 Datensaetze in die Mitarbeiterverwaltung. 
	 * 		Die 10.000 Datensaetze werden durch Aufruf der Methode insert(IEmployee) 
	 * 		einzeln	in die Mitarbeiterverwaltung eingefuegt.</li>
	 *	<li>Erwartet: 
	 *		<ul>
	 *			<li>Anzahl Eintraege in der Mitarbeiterverwaltung: 10.000.</li>
	 *			<li>toArray() liefert ein Array der Groesse 10.000.</li>
	 *			<li>Die gespeicherten Daten entsprechen den uebergebenen Datensaetzen.</li>
	 *			<li>Anzahl Operationen: 10.000 &plusmn; 1.000.</li>
	 *		</ul> 
	 *	</li>
	 * </ul>
	 * @throws IOException 
	 * wird ausgeloest, wenn ein E/A-Fehler auftritt, also das Lesen aus 
	 * der Datensatzdatei fehlschlaegt.
	 */
	@Test (timeout = 2000)
	@Define("EmployeeSuccess")
	@AfterMethod ("testKonstruktorString")
	@TestDescription("Testen der Insert-Methode.")
	public void testEinfuegen() throws IOException {
		testEmployee();
		trace.add(Evaluate.callConstruktor("mgnt", Management.class, fileName));
		IManagement mgnt = new Management();
		trace.add("Aufruf der Methode mgnt.toArray().");
		IEmployee[] emps = mgnt.toArray();
		trace.add("Aufruf der Methode mgnt.size().");
		int size = mgnt.size();
		
		evaluate(emps, size, 0);
		
		trace.add("Einfuegen der Datensaetze in die Mitarbeiterverwaltung.");
		int count = 0;		
		for (ICollectorItem<Integer, IEmployee> entry : expData) {
			String csv = entry.getPresentation();
			mgnt.insert(new Employee(csv));
			count += mgnt.numberOfOperations();
		}
		trace.separator();
		trace.add("Aufruf der Methode mgnt.toArray() nach dem Einfuegen.");
		emps = mgnt.toArray();
		
		evaluate(emps, mgnt.size(), fileLength);
		evaluate(expData, true, emps);
		evaluate(count, fileLength, fileLength / 10);
	}

	/**
	 * <ul>
	 * 	<li>Testfall: <br> 
	 * 		Konstruktoraufruf Management(String) und anschliessendes 
	 * 		Suchen nach allen Schluesselwerten in der Mitarbeiterverwaltung. 
	 * 		Die Datensatzdatei enthaelt 10.000 Eintraege.</li>
	 *	<li>Erwartet: 
	 *		<ul>
	 *			<li>Anzahl Eintraege in der Mitarbeiterverwaltung: 10.000.</li>
	 *			<li>Alle Schluesselwerte muessen in der Mitarbeiterverwaltung 
	 *				gefunden werden.</li>
	 *			<li>Gesamtanzahl Operationen beim Suchen aller 10.000 Eintraege: 
	 *				5.0005.000 &plusmn; 10.000.</li>
	 *		</ul> 
	 *	</li>
	 * </ul>
	 * @throws IOException 
	 * wird ausgeloest, wenn ein E/A-Fehler auftritt, also das Lesen aus 
	 * der Datensatzdatei fehlschlaegt.
	 */
	@Test (timeout = 4000)
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorString")
	@TestDescription("Testen der search(int)-Methode.")
	public void testSucheSchluessel() throws IOException {
		testEmployee();
		trace.add(Evaluate.callConstruktor("mgnt", Management.class, fileName));
		IManagement mgnt = new Management(fileName);
		trace.add("Aufruf der Methode mgnt.toArray().");
		IEmployee[] emps = mgnt.toArray();		
		trace.add("Aufruf der Methode mgnt.size().");
		int size = mgnt.size();
		
		evaluate(emps, size, fileLength);
		
		trace.add("Suchen nach Datensaetzen in der Mitarbeiterverwaltung.");
		int count = 0;
		Iterator<Integer> keysIt = expData.getKeys().iterator();
		while (keysIt.hasNext() && traceL1.getOccurrences() < MAXVIEW) {
			int key = keysIt.next();
			traceL1.add("Aufruf der Methode mgnt.search(%s).", key);
			IEmployee got = mgnt.search(key);
			traceL1.addInfo(PassTrace.ifEquals("Suchen nach Schluessel \"%d\".", 
					expData.get(key).getPresentation(), got, key));
			count += mgnt.numberOfOperations();
		}
		trace.add(traceL1, traceL1.hasOccurrences());
		trace.separator();
		hint(trace);
		assertFalse("Fehlerhafte / fehlende Datensaetze in der Mitarbeiterverwaltung.",
				trace.hasOccurrences());
		evaluate(count, 50005000, fileLength);				
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String) und anschliessendes 
	 * 		Suchen nach einem Vor- und Nachnamen in der Mitarbeiterverwaltung. 
	 * 		Die Datensatzdatei enthaelt 10.000 Eintraege.</li>
	 *	<li>Erwartet: 
	 *		<ul>
	 *			<li>Anzahl Eintraege in der Mitarbeiterverwaltung: 10.000.</li>
	 *			<li>Vor- und Nachname muessen in der Mitarbeiterverwaltung 
	 *				gefunden werden.</li>
	 *			<li>Gesamtanzahl Operationen beim Suchen 6152 &plusmn; 10.</li>
	 *		</ul> 
	 *	</li>
	 * </ul>
	 * @throws IOException 
	 * wird ausgeloest, wenn ein E/A-Fehler auftritt, also das Lesen aus 
	 * der Datensatzdatei fehlschlaegt.
	 */
	@Test (timeout = 1000)
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorString")
	@TestDescription("Testen der search(String, String)-Methode.")
	public void testSucheNamen() throws IOException {
		testEmployee();
		trace.add(Evaluate.callConstruktor("mgnt", Management.class, fileName));
		IManagement mgnt = new Management(fileName);
		trace.add("Aufruf der Methode mgnt.toArray().");
		IEmployee[] emps = mgnt.toArray();		
		trace.add("Aufruf der Methode mgnt.size().");
		int size = mgnt.size();
		
		evaluate(emps, size, fileLength);
		
		trace.add("Suchen nach \"Domenico Eastman\" in der Mitarbeiterverwaltung.");
		ICollected<Integer, IEmployee> exp = expData.with(11288, 11955);
		IEmployee[] got = mgnt.search("Eastman", "Domenico");
		
		evaluate(exp, false, got);
		
		trace.separator();
		assertFalse("Fehlerhafte / fehlende Datensaetze in der Mitarbeiterverwaltung.",
				trace.hasOccurrences());
		evaluate(mgnt.numberOfOperations(), 10000, 100);
	}

	/**
	 * <ul>
	 * 	<li>Testfall: <br> 
	 * 		Konstruktoraufruf Management(String) und anschliessendes
	 * 		ueberpruefen der Anzahl der Eintraege der Abteilungen in der 
	 * 		Mitarbeiterverwaltung. 
	 * 		Die Datensatzdatei enthaelt 10.000 Eintraege.</li>
	 *	<li>Erwartet: <br>
	 *		Methode size(Department) muss folgende Werte liefern: 
	 *		<ul>		

	 *			<li>SERVICE: 789.</li>
	 *			<li>DEVELOPMENT: 2.804.</li>
	 *			<li>SALES: 1.525.</li>
	 * 			<li>PRODUCTION: 2.239.</li>
	 * 			<li>MANPOWER: 523.</li>
	 * 			<li>RESEARCH: 477.</li>
	 * 			<li>MARKETING: 576.</li>
	 * 			<li>MANAGEMENT: 466.</li>
	 * 			<li>FINANCE: 601.</li>
	 * 		
	 * 		</ul>
	 * 	</li>
	 * 	<li>Erwartete Gesamtanzahl Operationen beim Suchen aller 9 Abteilungen: 
	 *	    90.000 &plusmn; 1000.	
	 * 	</li>
	 * </ul>
	 * @throws IOException 
	 * wird ausgeloest, wenn ein E/A-Fehler auftritt, also das Lesen aus 
	 * der Datensatzdatei fehlschlaegt.
	 */
	@Test (timeout = 1000)
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorString")
	@TestDescription("Testen der size(Department)-Methode.")
	public void testDepartmentSize() throws IOException {
		testEmployee();
		final int[] exp = {789, 2804, 1525, 2239, 523, 477, 576, 466, 601};
		
		trace.add(Evaluate.callConstruktor("mgnt", Management.class, fileName));
		IManagement mgnt = new Management(fileName); 
		
		String msg = "Anzahl der Mitarbeiter in der Abteilung %s.";
		int index = 0, count = 0;
		for (Department dep : Department.values()) {		
			trace.add("Aufruf von mgnt.size(%s).", dep);
			trace.addInfo(PassTrace.ifEquals(msg, exp[index++], mgnt.size(dep), dep));
			trace.addInfo(msgCount);			
			count += mgnt.numberOfOperations();
		}
		trace.separator();
		assertFalse("Methode size(Department) liefert falsches Ergebnis.", trace.hasOccurrences());
		evaluate(count, 9 * fileLength, 1000);
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br> 
	 * 		Konstruktoraufruf Management(String) und anschliessendes
	 * 		ueberpruefen der Eintraege der Abteilungen in der Mitarbeiterverwaltung.
	 * 		Mit Hilfe einer Pruefsumme wird festgestellt, ob die Eintraege 
	 * 		korrekt sind. Die Datensatzdatei enthaelt 10.000 Eintraege.</li>
	 *	<li>Erwartet: <br>
	 *		Methode members(Department) muss folgende Werte liefern: 
	 *		<ul>		
	 *			<li>SERVICE: 789 Eintraege, Pruefsumme: 23.430</li>
	 *			<li>DEVELOPMENT: 2.804 Eintraege, Pruefsumme: 4.020</li>
	 *			<li>SALES: 1.525 Eintraege, Pruefsumme: 14.493</li>
	 * 			<li>PRODUCTION: 2239 Eintraege, Pruefsumme: 10.794</li>
	 * 			<li>MANPOWER: 523 Eintraege, Pruefsumme: 13.705</li>
	 * 			<li>RESEARCH: 477 Eintraege, Pruefsumme: 13.183</li>
	 * 			<li>MARKETING: 576 Eintraege, Pruefsumme: 31.640</li>
	 * 			<li>MANAGEMENT: 466 Eintraege, Pruefsumme: 28.240</li>
	 * 			<li>FINANCE: 601 Eintraege, Pruefsumme: 15.499</li>
	 * 		</ul>
	 * 	</li> 
	 * </ul>
	 * @throws IOException 
	 * wird ausgeloest, wenn ein E/A-Fehler auftritt, also das Lesen aus 
	 * der Datensatzdatei fehlschlaegt.
	 */
	@Test (timeout = 2000)
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorString")
	@TestDescription("Testen der members(Department)-Methode.")
	public void testDepartmentMembers() throws IOException {
		testEmployee();
		String[] exp = {"Service;789;23.430", "Development;2.804;4.020", "Sales;1.525;14.493", 
						"Production;2.239;10.794", "Manpower;523;13.705", "Research;477;13.183", 
						"Marketing;576;31.640", "Management;466;28.240", "Finance;601;15.499"};
		
		CsvCollector<String, IEmployee> expCltr = 
				new CsvCollector<>(traceL1, "%s : %s : %s", (c, p) -> p[0].toString());
		expCltr.useSeparator(";").collectAll(exp);
		
		CsvCollector<String, IEmployee> gotCltr = 
				new CsvCollector<>(traceL1, "%2$s : %3$,d : %4$,d", (c, p) -> p[1].toString());
		gotCltr.useLocale(Locale.GERMAN);
		
		trace.add(Evaluate.callConstruktor("mgnt", Management.class, fileName));
		IManagement mgnt = new Management(fileName); 

		for (Department dep : Department.values()) {
			traceL1.add("Aufruf von mgnt.members(%s).", dep);
			IEmployee[] depEmpl = mgnt.members(dep);	
			
			traceL1.addInfo(PassTrace.ifFalse(msgNotNull, depEmpl == null, "members", dep));
			long chkSum = 0;
			if (depEmpl != null) { 
				for (IEmployee empl : depEmpl) {
					chkSum ^= (empl != null) ? empl.getKey() : 0;
				}
				gotCltr.collect("", dep, depEmpl.length, chkSum);
			}
		}
		trace.add(traceL1);
		trace.add("Ueberpruefung der Daten: <Abteilung>:<Anzahl>:<Pruefsumme>.");
		eval.equalsByKeys(expCltr.storage(), gotCltr.storage());
		eval.addToTrace(trace);	
		trace.separator();
		assertFalse("Methode members(Department) liefert falsches Ergebnis.",
				trace.hasOccurrences());		
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String) und anschliessendes
	 * 		ueberpruefen der Eintraege der Abteilungen SERVICE in der Mitarbeiterverwaltung.
	 * </li>
	 *	<li>Erwartet: <br>
	 *		Methode members(SERVICE) muss ein Array mit 789 Eintraegen liefern.
	 *	    Alle Eintraege werden ueberprueft.
	 * 	</li> 
	 * </ul>
	 * @throws IOException 
	 * wird ausgeloest, wenn ein E/A-Fehler auftritt, also das Lesen aus 
	 * der Datensatzdatei fehlschlaegt.
	 */
	@Test (timeout = 2000)
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorString")
	@TestDescription("Testen der members(Department)-Methode.")
	public void testDepartment() throws IOException {
		testEmployee();
		trace.add(Evaluate.callConstruktor("mgnt", Management.class, fileName));
		IManagement mgnt = new Management(fileName);
		
		Department dep = Department.SERVICE;
		trace.add(Evaluate.callMethod("mgnt", "members", dep));
		IEmployee[] got = mgnt.members(dep);
		
		trace.addInfo(PassTrace.ifFalse(msgNotNull, got == null, "members", dep));
		assertFalse("Methode members(Department) liefert null.", trace.hasOccurrences());
		
		trace.add(PassTrace.ifEquals("Anzahl Mitglieder in der Abteilung %s: %d", 
				789, got.length, dep, 789));		
		evaluate(expData.where((c, it) -> "Service".equals(it.getPart(6))), true, got);
		trace.separator();	
	}
	
	//-----------------------------------------------------------------
	
	/**
	 * Ueberpruefung der Anzahl Mitarbeiter in der Mitarbeiterverwaltung.
	 * @param emp - Mitarbeiter in der Mitarbeiterverwaltung: IEmployee[]. 
	 * @param size - erhaltene Anzahl der Mitarbeiter: int.
	 * @param exp - erwartete Anzahl der Mitarbeiter: int.
	 */
	private void evaluate(IEmployee[] emp, int size, int exp) {
		trace.add(PassTrace.ifEquals(
				"Anzahl der Datensaetze in der Mitarbeiterverealtung muss %,d betragen.", 
				 exp, size, exp));
		
		trace.add(PassTrace.ifTrue("Methode toArray() darf nicht null liefern.", emp != null));
		trace.separator(trace.hasOccurrences());
		assertNotNull("Methode toArray() liefert null", emp);

		trace.add(PassTrace.ifEquals(
				"Methode toArray() muss ein Array der Laenge %,d liefern.", 
				 exp, emp.length, exp));
		trace.separator(trace.hasOccurrences());
		assertFalse("Fehlerhafte Anzahl von Datensaetze in der Mitarbeiterverwaltung.",
				trace.hasOccurrences());
	}
	
	/**
	 * Ueberpueft die Datensaetze in der Mitarbeiterverwaltung mit den erwarteten 
	 * Datensaetzen.
	 * @param exp - Erwartete Mitarbeiter in der Mitarbeiterverwaltung: ICollected<>.
	 * @param byOder - Ueberuefung nach Reihenfolge oder Schluessel: boolean.
	 * @param emps - Erhaltene Mitarbeiter in der Mitarbeiterverwaltung: IEmployee[].
	 */
	private void evaluate(ICollected<Integer, IEmployee> exp, boolean byOder, IEmployee[] emps) {
		trace.add("Ueberpruefung der Datensaetze im Array mit den eingelesen Daten.");
		
		ICollected<Integer, IEmployee> got = funcCollector.collectAll(emps);
		trace.add(traceL1, traceL1.hasOccurrences());
		trace.separator(trace.hasOccurrences());
		assertFalse(msgError, trace.hasOccurrences());
		
		if (byOder) {
			eval.equalsByOrder(exp, got);			
		} else {
			eval.equalsByKeys(exp, got);
		}
		eval.addToTrace(trace);		
		trace.separator(trace.hasOccurrences());
		assertFalse(msgError, trace.hasOccurrences());
	}
	
	/**
	 * Ueberpruefung der Anzahl der Operationen.
	 * @param count - erhaltene Anzahl der Operationen: int.
	 * @param exp - erwartete Anzahl der Operationen: int.
	 * @param range - gueltiger Bereich um den erwarteten Wert: int.
	 */
	private void evaluate(int count, int exp, int range) {
		if (evalOperations) { 
			int min = exp - range;
			int max = exp + range;
			Natural got = Natural.valueOf(count);
			trace.add(PassTrace.ifTrue(
					"Anzahl Operationen muss im Bereich %,d..%,d liegen. Erhalten %,d.", 
					got.rangeOf(min, max), min, max, count)
			);			
			trace.separator();
			assertFalse("Fehlerhafte Anzahl Operationen.", trace.hasOccurrences());	
		} 
	}
	
	/**
	 * Hinweistext dem Trace hinzufuegen, wenn die Anzahl der Eintraege 
	 * groesser MAXVIEW ist.
	 * @param subTrace - untergeordneter Trace. 
	 */
	private void hint(ITraceRules subTrace) {
		if (!(subTrace.getOccurrences() < MAXVIEW)) {
			subTrace.addInfo("ACHTUNG! Es werden nur maximal %d Datensaetze angezeigt.", MAXVIEW);
		}
	}

}
