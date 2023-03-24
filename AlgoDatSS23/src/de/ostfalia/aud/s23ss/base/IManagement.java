package de.ostfalia.aud.s23ss.base;

public interface IManagement {

	/**
	 * Liefert die Anzahl der Datensätze.
	 * @return Anzahl der Datensätze: int.
	 */
	public int size();
	
	
	/**
	 * Fügt einen Mitarbeiter hinzu.
	 * @param member - hinzuzufügender Mitarbeiter: IEmployee.	
	 */
	public void insert(IEmployee member);

	
	/**
	 * Sucht nach einem Datensatz mit dem angegebenen Schlüssel.
	 * @param key - Schlüsselwert: int 
	 * @return - den gefundenen Datensatz, oder null, wenn der Schlüsselwert
	 * nicht gefunden werden konnte: IEmployee.
	 */
	public IEmployee search(int key);

	
	/**
	 * Sucht nach Datensätzen mit dem angegebenen Namen und Vornamen.
	 * @param name - Nachname : String.
	 * @param firstName - Vorname : String. 
	 * @return - die gefundenen Datensätze, oder ein leeres Array, wenn keine
	 * passendern Mitarbeiter gefunden werden konnten: IEmployee[]. 
	 */
	public IEmployee[] search(String name, String firstName);
	
	
	/**
	 * Liefert die Anzahl der Mitarbeiter der angegebenen Abteilung zurück.
	 * @param department die gesuchte Abteilung: Department.
	 * @return - Anzahl der Datensätze mit der angegebenen Abteilung: int.
	 */
	public int size(Department department);
	
	
	/**
	 * Liefert die Datensaetze mit der angegebenen Abteilung zurück.
	 * @param department die gesuchte Abteilung: Department.
	 * @return - die Datensätze mit der der angegebenen Abteilung,
	 * oder ein leeres Array, wenn keine Einträge gefunden wurden: IEmployee[].
	 */
	public IEmployee[] members(Department department);
	
	/**
	 * Liefert alle Datensätze als Array von IEmployee zurück.
	 * @return - alle Datensätze als Array: IEmployee[].
	 */
	public IEmployee[] toArray();
	
	/**
	 * Liefert die Anzahl grundlegenden Operationen bei der zuvor aufgerufenen
	 * Zugriffsmethode zurück.
	 * @return - Anzahl grundlegenden Operationen bei der zuvor aufgerufenen
	 * Zugriffsmethode: int.
	 */
	public int numberOfOperations();
	
	
	/**
	 * Nur für Aufgabe 3: liefert die Höhe des binären Suchbaums zurück. 
	 * @return - die Höhe des binären Suchbaums: int.
	 */
	public default int height() {
		return 0;
	};
	
}
