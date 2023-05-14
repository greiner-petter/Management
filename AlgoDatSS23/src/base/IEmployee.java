package de.ostfalia.aud.s23ss.base;

import java.time.LocalDate;


public interface IEmployee extends Comparable<IEmployee>{

	/**
	 * Liefert den Schlüssel.
	 * @return - den Schlüssel: int.
	 */
	public int getKey();
	
	/**
	 * Liefert den Nachnamen des Mitarbeiters zurück.
	 * @return - den Nachnamen des Mitarbeiters: String.
	 */
	public String getName();
	
	/**
	 * Liefert den Vornamen des Mitarbeites zurück.
	 * @return - den Vornamen des Mitglieds: String.
	 */
	public String getFirstName();
	
	/**
	 * Liefert das Geschlecht des Mitarbeiters zurück.
	 * @return Geschlecht des Mitglieds: Gender.
	 */
	public Gender getGender();
	
	/**
	 * Liefert das Geburtsdatum des Mitarbeiters zurück.
	 * @return Geburtsdatum des Mitglieds: LocalDate.
	 */
	public LocalDate getBirthdate();
	
	/**
	 * Liefert das Einstellungsdatum des Mitarbeiters zurück.
	 * @return Einstellungsdatum des Mitglieds: LocalDate.
	 */
	public LocalDate getHiredate();
	
	/**
	 * liefert die Sportart zurueck.
	 * @return - die Sportart: KindOfSport;
	 */
	public Department getDepartment();
	
	/**
	 * Liefert den Datensatz als String zurück.
	 * Beispiel: "10002;1964-06-02;Bezalel;Simmel;F;1985-11-21;Sales"
	 * @return Datensatz: String.
	 */
	public String toString();
}
