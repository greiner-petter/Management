package de.ostfalia.aud.s23ss.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
public class ManagementTestA1 {

	public boolean evalOperations = true;
	public static final int MAXVIEW = 12;
	
	public RuleControl opt = RuleControl.NONE;
	public IMessengerRules messenger = MessengerRule.newInstance(opt);	
	public ITraceRules trace = TraceRule.newInstance(opt);	
	private ITraceRules traceL1 = trace.newSubtrace(opt);
	private ITraceRules traceL2 = traceL1.newSubtrace(opt);
	
	@Rule
	public TestRule chain = RuleChain
							.outerRule(trace)	
							.around(messenger);
	
	@Rule
    public TestRule timeout = new DisableOnDebug(
                              new Timeout(1000, TimeUnit.MILLISECONDS));
	
	private String callMsg   = "Sammeln der Datensaetze.";
	private String evalMsg   = "Auswerten der erhaltenen Datensaetze.";
	private String errMsg    = "Fehlerhafte(r) Datensaetze/-satz in der Mitarbeiterverwaltung.";
	
	private CsvCollector<Integer, IEmployee> csvCollector = 
			new CsvCollector<>(traceL1, "%s;%s;%s;%s;%s;%s;%s", 
					          (c, p) -> Integer.parseInt((String) p[0]));
	
	private FunctionalCollector<Integer, IEmployee> funcCollector = 
			new FunctionalCollector<>(traceL2, "%s", 
					(c, o) -> (o != null) ? o.getKey() : c.index());
	
	private Evaluate eval = new Evaluate(traceL2).occurrenceLimits(MAXVIEW);
	
	/**
	 * Datensatz mit 10 Eintraegen als Testdaten fuer die JUnit-Tests.
	 */
	private static String[] data = {
			/*[0]*/ "10855;1957-08-07;Breannda;Billingsley;F;1991-08-05;Finance",
			/*[1]*/ "10041;1959-08-27;Uri;Lenart;F;1989-11-12;Sales",
			/*[2]*/ "10942;1952-08-08;Toshimitsu;Larfeldt;F;1989-09-08;Development",
			/*[3]*/ "10034;1962-12-29;Bader;Swan;M;1988-09-21;Sales",
			/*[4]*/ "10943;1955-11-19;Berna;Skafidas;M;1988-02-19;Development",
			/*[5]*/ "10938;1958-05-11;Shaowei;Iisaku;F;1985-09-24;Marketing",
			/*[6]*/ "10796;1959-06-30;Breannda;Billingsley;F;1990-11-08;Management",
			/*[7]*/ "10005;1955-01-21;Kyoichi;Maliniak;M;1989-09-12;Manpower",
			/*[8]*/ "10060;1961-10-15;Breannda;Billingsley;F;1987-11-02;Service",
			/*[9]*/ "10948;1952-12-23;Shigehito;Brodie;M;1996-09-30;Development",
	};
	
	private static Integer[] allKeys; 
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
		csvCollector.useSeparator("\\s*;\\s*")
				    .useIdentifier("employee", "[", "]");
		funcCollector.define("toString", (obj, c) -> obj.toString());
		if (expData == null) {			
			trace.add("Erstellen von %d Mitarbeitern.", data.length);
			expData = csvCollector.collectAll(data);
			allKeys = expData.getKeys().toArray(new Integer[0]);
		}	
		if (!evalOperations) {
			System.err.println("Ueberpruefung der Anzahl der Operationen "
					         + "ist aktuell deaktiviert!");
		}
	}
	
	/**
	 * <ul>
	 * 	<li>Ueberpruefen der Klasse Employee: <br>
	 * 		Konstruktoraufruf der Klasse Employee und auswerten
	 * 		der Rueckgaben der Methoden getKey() und toString().</li>
	 *	<li>Erwartet: <br>
	 *		Konstruktor wird erfolgreich durchlaufen, die
	 *		Rueckgaben der Methoden entsprechen den erwarteten Werten.</li>
	 *	<li>Beispiel: "10855;1957-08-07;Breannda;Billingsley;F;1991-08-05;Finance"</li>
	 * </ul>
	 */
	@Test
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
			traceL1.addInfo(PassTrace.ifEquals("Rueckgabe der Methode.", 10855, key));

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
	 * 		Standard-Konstruktoraufruf Management().<br> 
	 * 		Nach dem Aufruf des Standard-Konstruktors duerfen sich keine Datensaetze
	 *      in der Mitarbeiterverwaltung befinden.</li>
	 *	<li>Erwartet: <br>
	 *		Anzahl der Datensaetze = 0.</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testEmployee")
	@TestDescription("Testen des Kontruktors().")
	public void testKonstruktorOhneParameter() {
		testEmployee();
		trace.add("Konstruktoraufruf Management().");
		IManagement mgnt = new Management(); 			
		evaluate(mgnt, true);
	}

	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String[]) mit einem Datensatz.<br> 
	 * 		Nach dem Aufruf des Konstruktors muss sich genau ein Datensatz (10796)
	 * 		in der Mitarbeiterverwaltung befinden.</li>
	 *	<li>Erwartet: <br>
	 *		10796;1959-06-30;Breannda;Billingsley;F;1990-11-08;Management.</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorOhneParameter")
	@TestDescription("Testen des Kontruktors(String[]) mit einem Datensatz.")
	public void testKonstruktorEinDatensatz() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		String[] array = new String[] {expData.get(10796).getPresentation()};
		IManagement mgnt = new Management(array);		
		evaluate(mgnt, true, 10796);
	}	
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String[]) mit 10 Datensaetzen.<br> 
	 * 		Nach dem Aufruf des Konstruktors mussen sich alle 10 Datensaetze in der 
	 * 		Mitarbeiterverwaltung befinden.</li>
	 *	<li>Erwartet: <br>
	 *		data[0] bis data[9] in der Mitarbeiterverwaltung.</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorEinDatensatz")
	@TestDescription("Testen des Kontruktors(String[]) mit 10 Datensaetzen.")
	public void testKonstruktorZehnDatensaetze() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		IManagement mgnt = new Management(data); 
		evaluate(mgnt, true, allKeys);		
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Methode size() mit 10 Datensaetzen.<br> 
	 * 		Nach dem Aufruf des Konstruktors muessen sich alle 10 Datensaetze in der 
	 * 		Mitarbeiterverwaltung befinden.</li>
	 *	<li>Erwartet: <br>
	 *		size() = 10</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorEinDatensatz")
	@TestDescription("Testen size() mit 10 Datensaetzen.")
	public void testSize() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		IManagement mgnt = new Management(data); 
		trace.add("Auftuf size()");
		trace.addInfo(PassTrace.ifEquals("size() muss 10 liefern", 10, mgnt.size()));
		trace.separator();
		assertFalse("Methode size() liefert falsches Ergebnis.",
				trace.hasOccurrences());
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Standard-Konstruktoraufruf Management() und anschliessendes
	 * 		einfuegen von 10 Datensaetzen.<br> 
	 * 		Alle Datensaetze muessen in die Mitarbeiterverwaltung eingefuegt 
	 * 		werden koennen.</li>
	 *	<li>Erwartet: <br>
	 *		data[0] bis data[9] in der Mitarbeiterverwaltung.</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorZehnDatensaetze")
	@TestDescription("Testen der insert(IEmployee)-Methode mit 10 Datensaetzen.")
	public void testInsert() {
		testEmployee();
		trace.add("Konstruktoraufruf Management().");
		IManagement mgnt = new Management(); 
		evaluate(mgnt, true);
		
		trace.add("Datensaetze in die Mitarbeiterverwaltung einfuegen.");
		for (int i = 0; i < data.length; i++) {
			traceL1.add("Konstruktoraufruf Employee(\"%s\").", data[i]);
			IEmployee emp = new Employee(data[i]);
			traceL1.addInfo("Aufruf von insert(%s).", emp);
			mgnt.insert(emp);
		}
		trace.add(traceL1, traceL1.hasOccurrences());
		evaluate(mgnt, true, allKeys);
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String[]) mit 10 Datensaetzen
	 * 		und anschliessendes Suchen nach den Schuesselwerten in der 
	 *		Mitarbeiterverwaltung.
	 *  </li>
	 *	<li>Erwartet: 
	 *		<ul>
	 *			<li>Alle Schuesselwerte muessen in der Mitarbeiterverwaltung
	 *				gefunden werden.</li>
	 *			<li>Die Methode search(int) muss den zugehoerigen
	 *				Datensatz zurueckliefern.</li>
	 *			<li>Anzahl Operationen: 1..10 &plusmn; 1.</li>
	 *		</ul> 
	 *	</li>
	 *  </li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorZehnDatensaetze")
	@TestDescription("Testen der search(int)-Methode mit 10 Datensaetzen.")
	public void testSearchInt() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		IManagement mgnt = new Management(data); 
		evaluate(mgnt, true, allKeys);
		
		trace.add("Suchen nach Schluesseln in der Mitarbeiterverwaltung.");
		String msg = "Erhaltener Datensatz bei der Suche nach %d.";
		for (int i = 0; i < data.length; i++) {
			traceL1.add("Aufruf von search(%d).", allKeys[i]);
			String exp  = data[i];
			IEmployee got = mgnt.search(allKeys[i]);
			traceL1.addInfo(PassTrace.ifEquals(msg, exp, got, allKeys[i]));			
			evaluate(traceL1, mgnt, i + 1, 1);
		}
		trace.add(traceL1, traceL1.hasOccurrences());
		trace.separator();
		assertFalse("Methode search(int) liefert falschen Datensatz.",
				trace.hasOccurrences());
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String[]) mit 10 Datensaetzen
	 * 		und anschliessendes Suchen nach Schuesselwerten, die nicht in der
	 * 		Mitarbeiterverwaltung existieren.</li>
	 *  <li>Erwartet: 
	 *		<ul>
	 *			<li>Kein Schuesselwert darf in der Mitarbeiterverwaltung
	 *				gefunden werden.</li>
	 *			<li>Methode search(int) muss null liefern.</li>
	 *			<li>Anzahl Operationen: 10 &plusmn; 1.</li>
	 *		</ul> 
	 *	</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorZehnDatensaetze")
	@TestDescription("Methode search(int) muss bei nicht vorhandenen Schluesseln null liefern.")
	public void testSearchNotExist() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		IManagement mgnt = new Management(data); 
		evaluate(mgnt, true, allKeys);
		
		trace.add("Suchen nach nicht vorhandenen Schluesseln in der Mitarbeiterverwaltung.");
		String msg = "Erhaltener Datensatz bei der Suche nach Schluessel %d.";
		int ofs = 10000;
		for (int i = 0; i < data.length; i++) {
			int key = allKeys[i] + ofs;
			traceL1.add("Aufruf von search(%d)", key);			
			IEmployee got = mgnt.search(key);
			traceL1.addInfo(PassTrace.ifNull(msg, got, key));	
			evaluate(traceL1, mgnt, 10, 1);
		}
		trace.add(traceL1, traceL1.hasOccurrences());
		trace.separator();
		assertFalse("Methode search(int) liefert falschen Datensatz.",
				trace.hasOccurrences());
	}

	/**
	 * <ul>
	 * 	<li>Testfall: <br> 
	 * 		Konstruktoraufruf Management(String[]) mit 10 Datensaetzen
	 * 		und anschliessendes Suchen nach dem Nach- und Vornamen in der 
	 *		Mitarbeiterverwaltung.</li>
	 * <li>Erwartet: 
	 *		<ul>
	 *			<li>Alle Namen muessen in der Mitarbeiterverwaltung
	 *				gefunden.</li>
	 *			<li>Die Methode search(String, String) muss den zugehoerigen
	 *				Datensatz zurueckliefern.</li>
	 *			<li>Anzahl Operationen: 1..10 &plusmn; 1.</li>
	 *		</ul> 
	 *	</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorZehnDatensaetze")
	@TestDescription("Testen der search(String, String)-Methode mit 10 Datensaetzen.")
	public void testSearchName() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		IManagement mgnt = new Management(data); 
		evaluate(mgnt, true, allKeys);
		
		for (ICollectorItem<Integer, IEmployee> entry : expData.withOut(10060, 10796)) {
			String nachname = entry.getPart(3).toString();
			String vorname  = entry.getPart(2).toString();
			trace.add("Suchen nach %s %s in der Mitarbeiterverwaltung.", nachname, vorname);
			traceL1.add("Aufruf von search(\"%s, %s\")", nachname, vorname);
			IEmployee[] got = mgnt.search(nachname, vorname);			
			evaluate(traceL1, mgnt, 10 , 1);
			ICollected<Integer, IEmployee> exp = expData.where((c, item) -> 
				nachname.equals(item.getPart(3).toString()) && 
				vorname.equals (item.getPart(2).toString()));
			Integer[] expKeys = exp.getKeys().toArray(new Integer[0]);
			evaluate(got, false, expKeys);
		}		
		trace.add(traceL1, traceL1.hasOccurrences());
		assertFalse("Methode search(String, String) liefert falschen Datensatz.",
				trace.hasOccurrences());
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String[]) mit 10 Datensaetzen
	 * 		und anschliessendes Suchen nach dem Nach- und Vornamen in der 
	 *		Mitarbeiterverwaltung.</li>	
	 *	<li>Erwartet: 
	 *		<ul>
	 *			<li>Kein Namen darf in der Mitarbeiterverwaltung
	 *				gefunden werden.</li>
	 *			<li>Methode search(String, String) muss null liefern.</li>
	 *			<li>Anzahl Operationen: 10 &plusmn; 1.</li>
	 *		</ul> 
	 *	</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorZehnDatensaetze")
	@TestDescription("Methode search(String, String) muss bei unbekannten Namen null liefern.")
	public void testSearchUnknown() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		IManagement mgnt = new Management(data); 
		evaluate(mgnt, true, allKeys);
		
		trace.add("Suchen nach nicht vorhandenen Nachnamen in der Mitarbeiterverwaltung.");
		for (ICollectorItem<Integer, IEmployee> entry : expData) {
			String unknown = entry.getPart(2) + "ff";
			traceL1.add("Aufruf von search(\"%s, %s\")", unknown, entry.getPart(3));			
			IEmployee[] got = mgnt.search(unknown, entry.getPart(3).toString());			
			evaluate(traceL1, mgnt, 10, 1);
			evaluate(got, false);
		}
		trace.add(traceL1, traceL1.hasOccurrences());
		//----------------------------------------------------------------------
		traceL1.clear();
		trace.add("Suchen nach nicht vorhandenen Vornamen in der Mitarbeiterverwaltung.");
		for (ICollectorItem<Integer, IEmployee> entry : expData) {
			String unknown = entry.getPart(3) + "ff";
			traceL1.add("Aufruf von search(\"%s, %s\")",entry.getPart(2), unknown);			
			IEmployee[] got = mgnt.search(entry.getPart(2).toString(), unknown);			
			evaluate(traceL1, mgnt, 10, 1);
			evaluate(got, false);
		}
		trace.add(traceL1, traceL1.hasOccurrences());
		assertFalse("Methode search(String, String) liefert falschen Datensatz.",
				trace.hasOccurrences());
	}

	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String[]) mit 10 Datensaetzen
	 * 		und anschliessendes ermitteln der Anzahl der Eintraege fuer die 
	 * 		Departments Development, Sales, Manpower und Research in der 
	 * 		Mitarbeiterverwaltung.</li>
	 *	<li>Erwartet: <br> 
	 *		Die Methode size(Department) muss folgende Werte liefern:
	 *		<ul>
	 *			<li>Development: 3</li>
	 *			<li>Sales: 2</li>
	 *			<li>Manpower: 1</li>
	 *			<li>Research: 0</li>
	 *		</ul> 
	 *		<li>Anzahl Operationen: 10 &plusmn; 1.</li>
	 *	</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorZehnDatensaetze")
	@TestDescription("Testen der size(Department)-Methode mit 10 Datensaetzen.")
	public void testSizeDepartment() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		IManagement mgnt = new Management(data); 
		evaluate(mgnt, true, allKeys);
		
		String msg = "Erhaltene Anzahl der Mitarbeiter fuer das Department %s.";
		
		Department d = Department.DEVELOPMENT;
		trace.add("Aufruf von size(%s).", d);
		trace.addInfo(PassTrace.ifEquals(msg, 3, mgnt.size(d), d));
		evaluate(trace, mgnt, 10, 1);
		
		d = Department.SALES;
		trace.add("Aufruf von size(%s).", d);
		trace.addInfo(PassTrace.ifEquals(msg, 2, mgnt.size(d), d));
		evaluate(trace, mgnt, 10, 1);
		
		d = Department.MANPOWER;
		trace.add("Aufruf von size(%s).", d);
		trace.addInfo(PassTrace.ifEquals(msg, 1, mgnt.size(d), d));
		evaluate(trace, mgnt, 10, 1);
		
		d = Department.RESEARCH;
		trace.add("Aufruf von size(%s).", d);
		trace.addInfo(PassTrace.ifEquals(msg, 0, mgnt.size(d), d));
		evaluate(trace, mgnt, 10, 1);
		
		trace.separator();
		assertFalse("Methode size(Department) liefert falsches Ergebnis.",
				trace.hasOccurrences());
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: Konstruktoraufruf Management(String[]) mit 10 Datensaetzen
	 * 		und anschliessendes ermitteln der Eintraege fuer die 
	 * 		Departments Development, Sales, Human Resources und Research in der 
	 * 		Mitarbeiterverwaltung.</li>
	 *	<li>Erwartet: Die Methode members(Department) muss folgende Werte liefern:
	 *		<ul>
	 *			<li>Development: [10948, 10943, 10948]</li>
	 *			<li>Sales: [10034, 10041]</li>
	 *			<li>Manpower: [10005]</li>
	 *			<li>Research: []</li>
	 *		</ul> 
	 *	</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testKonstruktorZehnDatensaetze")
	@TestDescription("Testen der size(Department)-Methode mit 10 Datensaetzen.")
	public void testMemberDepartment() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		IManagement mgnt = new Management(data); 
		evaluate(mgnt, true, allKeys);
		
		Department d = Department.DEVELOPMENT;
		trace.add("Aufruf von member(%s).", d);
		evaluate(mgnt.members(d), true, 10942, 10943, 10948);
		
		d = Department.SALES;
		trace.add("Aufruf von member(%s).", d);
		evaluate(mgnt.members(d), true, 10041, 10034);
		
		d = Department.MANPOWER;
		trace.add("Aufruf von member(%s).", d);
		evaluate(mgnt.members(d), true, 10005);
		
		d = Department.RESEARCH;
		trace.add("Aufruf von member(%s).", d);		
		evaluate(mgnt.members(d), true);
		
		assertFalse("Methode member(Department) liefert falsches Ergebnis.",
				trace.hasOccurrences());
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String[]) mit 10 Datensaetzen.
	 * 		Suchen nach Namen,  Department und Schluessel in der Mitarbeiterverwaltung.
	 * </li>
	 *	<li>Erwartet: <br>
	 *		Es muessen folgende Eintraege liefert werden:
	 *		<ul>
	 *			<li>search("Billingsley", "Breannda"): {10060}</li>
	 *			<li>members(DEVELOPMENT): {10942, 10943, 10948}</li>
	 *			<li>search(10943): "Berna, Skafidas"</li>
	 *		</ul> 
	 *	</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testMemberDepartment")
	@TestDescription("Aufeinander folgendes Suchen nach Namen, Abteilung und Schluessel.")
	public void testSearchMulti() {
		testEmployee();
		trace.add("Konstruktoraufruf Management(String[]).");
		IManagement mgnt = new Management(data); 
		evaluate(mgnt, true, allKeys);

		trace.add("Suche nach Vor- und Nachnamen.");
		String vormane = "Breannda";
		String nachmane = "Billingsley";
		trace.addInfo("Aufruf von search(\"%s\", \"%s\").", nachmane, vormane);
		IEmployee[] emps = mgnt.search(nachmane, vormane);
		evaluate(emps, true, 10855, 10796, 10060);
		
		trace.add("Suche nach Mitgliedern einer Abteilung.");
		Department d = Department.DEVELOPMENT;
		trace.addInfo("Aufruf von member(%s).", d);
		emps = mgnt.members(d);
		evaluate(emps, true, 10942, 10943, 10948);
		
		trace.add("Suche nach einem Schluessel.");
		int key = 10943;
		trace.addInfo("Aufruf von search(%d).", key);
		IEmployee emp = mgnt.search(key);
		evaluate(new IEmployee[]{emp}, true, key);
	}
	
	/**
	 * <ul>
	 * 	<li>Testfall: <br>
	 * 		Konstruktoraufruf Management(String[]) mit 6 Datensaetzen 
	 * 		fuer die erste Instanz und Konstruktoraufruf Management(String[]) 
	 * 		mit 4 Datensaetzen fuer die zweite Instanz.
	 * </li>
	 *	<li>Erwartet: <br>
	 *		Die Instanzen von Management muessen unabhaengig voneinander 
	 *		arbeiten. 
	 *	</li>
	 * </ul>
	 */
	@Test
	@Define("EmployeeSuccess")
	@AfterMethod("testMemberDepartment")
	@TestDescription("Instanzen von Management muessen unabhaengig voneinander arbeiten.")
	public void testInstances() {
		trace.add(Evaluate.callConstruktor("mgnt1", Management.class));
		ICollected<Integer, IEmployee> data1 = expData.section(0, 6);
		IManagement mgnt1 = new Management(data1.presentationOrderAsArray());
		trace.add(traceL1, traceL1.hasOccurrences());
		
		trace.add(Evaluate.callConstruktor("mgnt2", Management.class));
		ICollected<Integer, IEmployee> data2 = expData.section(6, data.length);
		IManagement mgnt2 = new Management(data2.presentationOrderAsArray());
		trace.add(traceL1, traceL1.hasOccurrences());
		
		trace.separator();
		trace.add("Instanz mgnt1 muss 6 Datensaetze enthalten.");
		trace.separator();
		evaluate(mgnt1, true, data1.getKeys().toArray(new Integer[6]));
		
		trace.add("Instanz mgnt2 muss 4 Datensaetze enthalten.");
		trace.separator();
		evaluate(mgnt2, true, data2.getKeys().toArray(new Integer[4]));
	}

	//--------------------------------------------------------------
	
	/**
	 * Ueberpruefung der durch die Methode toArray() der Mitarbeiterverwaltung
	 * zurueckgelieferten Datensaetze.
	 * @param mgnt - Mitarbeiterverwaltung: IManagement.
	 * @param byOder - Ueberuefung nach Reihenfolge oder Schluessel: boolean.
	 * @param elements - Indizes der erwarteten Testdatensaetze: int...
	 */
	private void evaluate(IManagement mgnt, boolean byOder, Integer... elements) {
		trace.add("Aufruf der toArray()-Methode.");
		evaluate(mgnt.toArray(), byOder, elements);
	}
	
	/**
	 * Ueberpruefung der uebergebenen Datensaetze anhand der Schluessel der 
	 * erwarteten Testdatensaetze.
	 * @param emps - Array mit Datensaetze: IEmployee[].
	 * @param byOder - Ueberuefung nach Reihenfolge oder Schluessel: boolean.
	 * @param elements - Schluessel der erwarteten Testdatensaetze: Integer...
	 */
	private void evaluate(IEmployee[] emps, boolean byOder, Integer... elements) {	
		ICollected<Integer, IEmployee> exp = expData.with(elements);

		traceL1.add(callMsg);
		funcCollector.clear().useIdentifier("employee", "{", "}");
		ICollected<Integer, IEmployee> got = funcCollector.collectAll(emps);
		
		traceL1.add(traceL2, traceL2.hasOccurrences());
		if (traceL1.hasOccurrences()) {
			trace.add(traceL1);
			trace.separator();
			assertFalse(errMsg,	trace.hasOccurrences());			
		}
		
		traceL1.add(evalMsg);
		if (byOder) {
			eval.equalsByOrder(exp, got);			
		} else {
			eval.equalsByKeys(exp, got);
		}
		eval.addToTrace(traceL1);	
		
		trace.add(traceL1);
		trace.separator();
		assertFalse(errMsg,	trace.hasOccurrences());
	}
	
	/**
	 * Ueberpruefung der Anzahl der Operationen.
	 * @param localtrace - Trace fuer die Ausgabe der Meldungen: ITraceRules.
	 * @param mgnt - Die Mitarbeiterverwaltung: int.
	 * @param exp - erwartete Anzahl der Operationen: int.
	 * @param range - gueltiger Bereich um den erwarteten Wert: int.
	 */
	private void evaluate(ITraceRules localtrace, IManagement mgnt, int exp, int range) {
		if (evalOperations) { 
			int count = mgnt.numberOfOperations();
			int min = (exp - range >= 0) ? exp - range : 0;
			int max = exp + range;
			Natural got = Natural.valueOf(count);
			localtrace.addInfo(PassTrace.ifTrue(
					"Anzahl Operationen muss im Bereich %,d..%,d liegen. Erhalten %,d.", 
					got.rangeOf(min, max), min, max, count)
			);		
		} 
	}
	
}
