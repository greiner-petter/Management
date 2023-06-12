# Management
# Algorithmen und Datenstrukturen Laboraufgaben
Im Labor zu Algorithmen und Datenstrukturen werden insgesamt vier Aufgaben gestellt. Die Aufgaben sollen in Zweiergruppen gelöst werden. Die Abgabe besteht aus zwei Teilen: zum einen wird die in das SVN-Repository der Gruppe eingestellte Lösung automatisch bewertet (JUnit- und Checkstyle-Tests), zum anderen müssen beide Teammitglieder zur Abgabe anwesend sein und die Lösung erklären und bei Nachfrage modifizieren können. Die Aufgaben sind im 14-täglichen Wechsel (ungefähr) mit den Aufgaben von „Programmieren“ abzugeben.
Eine korrekte Abgabe einer Laboraufgabe umfasst das geforderte Programm mit Dokumentation in JavaDoc, sowie mehrere, selbst erstellte Testfälle, alles abgelegt in Ihrem SVN Repository. Sie erklären Ihre Lösung dem Dozenten bzw. Betreuer, demonstrieren das Programm und führen eventuell geforderte Modifikationen aus.
Sie erhalten für die ersten drei Aufgaben insgesamt 5 Punkte, sofern alle drei als bestanden bewertet wurden. Für die letzte Aufgabe erhalten Sie noch einmal 5 Punkte. Insgesamt sind also 10 Punkte möglich. Die Abgabe von drei von vier Aufgaben ist Voraussetzung zur Zulassung zur Klausur!
Aktuelle Ankündigungen und Terminhinweise sind bitte im Moodle-Kurs nachzuverfolgen. Dort finden Sie auch die Materialien für die Bearbeitung der Aufgaben.
# Ziele
Sie arbeiten auf dem in Moodle bereitgestellten Datensatz mit Mitarbeiterdaten1. Für eine effiziente Implementierung sind Datenstrukturen erforderlich, die als Feld (Array), Liste, Baum oder Hash-Tabelle angelegt werden können. Daher werden diese Strukturen in den ersten Teilen selbst programmiert, während in der letzten Aufgabe die Standardimplementierungen des Java Collections Frameworks verwendet werden sollen. Die unterschiedlichen Implementierungen sind für bestimmte Operationen auf ihre Effizienz hin zu analysieren und zu vergleichen.
# Ein Mitarbeiterdatensatz ist wie folgt aufgebaut:
• Schlüssel (int)
• Geburtstag (jjjj-mm-tt)
• Vorname (String)
• Nachname (String)
• Geschlecht (String)
• Einstellungsdatum (jjjj-mm-tt)
• Name der Abteilung (String)
Beispieldatensätze:
10049;1961-04-24;Basil;Tramer;F;1992-05-04;Sales 10050;1958-05-21;Yinghua;Dredge;M;1990-12-25;Finance 10051;1953-07-28;Hidefumi;Caine;M;1992-10-15;Production
Die Daten sind aus einer Textdatei (csv) in eine zunächst leere Datenstruktur einzulesen (einfügen – Methode insert()). Über den Schlüssel oder über den Vornamen und Nachnamen ist ein Zugriff auf den jeweiligen Datensatz (suchen – Methoden search()) zu ermöglichen. Achtung: Vor- und Nachname sind ggf. nicht eindeutig. Anfragen nach Anzahl aller Angestellten, aller Angestellten einer Abteilung und deren Auflistung in einem Feld (array) sind ebenfalls zu ermöglichen (Traversieren- Methoden size() und members()). Die zuvor genannten Methoden werden jeweils durch Implementierung des bereitgestellten Interface IManagement realisiert. Die Bedienerführung der Anwendung erfolgt über die Konsole.
Die Programmierung soll in Java2 und daher objektorientiert erfolgen. Dokumentieren Sie mittels JavaDoc alle von Ihnen implementierten Klassen, einschließlich aller Attribute und Methoden. Überschreiben Sie für Ihre Klassen die Methode toString().
# Projekt
Die Bearbeitung von allen Aufgaben erfolgt ein einem Java-Projekt namens "AlgoDatSS23", welches im Package de.ostfalia.aud.s23ss.base u.a. die zwei bereitgestellten Interfaces IManagement und IEmployee beinhaltet.
# Laufzeit- und Komplexitätsmessungen
Die unterschiedlichen Implementierungen sind bezüglich ihrer Zeit-Komplexität und Laufzeit miteinander zu vergleichen. Dies soll durch Zählen der jeweiligen grundlegenden Operationen und der realen Laufzeit (mittels System.nanoTime()) erfolgen. Die Ergebnisse sind tabellarisch darzustellen und auch in Beziehung zu den jeweiligen theoretisch hergeleiteten Ergebnissen aus der Vorlesung zu setzen.
Um aussagekräftige und vergleichbare Werte zu erhalten, werden Test-Datenbestände als Textdateien (csv) zur Verfügung gestellt. Für Ihre Messung nutzen Sie bitte:
• 40k_employees.csv: Mitarbeiter-Datensätze zum Einfügen in die Datenstrukturen, wie oben beschrieben. Anzahl: 40.000, keine Duplikate.
• 40k_keys.csv: Schlüssel-Datensätze zum Suchen in den Datenstrukturen. Anzahl 40.000, davon 20.000, die nicht enthalten sind.

# Aufgabe 1 (Unsortiertes Array)
• Implementieren Sie im Package de.ostfalia.aud.s23ss.base die Aufzählungsklasse Department, welche folgende Informationen enthält: SERVICE("Service"), DEVELOPMENT("Development"), SALES("Sales"), PRODUCTION("Production"), MANPOWER("Manpower"), RESEARCH("Research"), MARKETING("Marketing"), MANAGEMENT("Management"), FINANCE("Finance")
• Implementieren Sie ebenso im Package de.ostfalia.aud.s23ss.base die Aufzählungsklasse Gender, welche die Informationen M für männlich und F für weiblich enthält.
• Für die Mitarbeiter-Datensätze ist die Klasse Employee im Package de.ostfalia.aud.s23ss.base zu erstellen, welche das vorgegebene Interface IEmployee implementiert. Vervollständigen Sie die Klasse mit einem Konstruktor, der Schlüssel, Geburtsdatum, Vorname, Name, Geschlecht, Eintrittsdatum und Abteilung in einem String, durch Semikolon getrennt, als Argumente (so wie aus der Textdatei eingelesen) erhält. Implementieren Sie ebenso die Methode toString(), welche die gespeicherten Daten wie im csv-Format zurückgibt (dies wird für den Test benötigt).
• Für die Mitarbeiterverwaltung ist die Klasse Management im Package de.ostfalia.aud.s23ss.a1 zu erstellen und das vorgegebene Interface IManagement zu implementieren. Der Konstruktor bekommt den Namen der Textdatei mit den Datensätzen als Argument und erstellt eine Datenstruktur, in welche die Datensätze einzulesen sind. Ein weiterer Konstruktor bekommt einen Array von Strings, in dem jeder String ein Datensatz ist.
• Als Datenstruktur für die Mitarbeiterverwaltung wählen Sie in dieser Aufgabe ein unsortiertes Array. Die Datenstruktur soll eine initiale Kapazität von 8 Elementen haben. Wird die Kapazität überschritten, muss das Array dynamisch erweitert werden. Implementieren Sie ebenfalls die Methode toArray, mit der die Elemente der Liste als Array zurückgeliefert werden. Beachten Sie, dass die Klassen aus dem Java Collection-Framework hier nicht verwendet werden dürfen.
• Bestimmen Sie die Zeitkomplexität (in Anzahl grundlegender Operationen) und die Laufzeit (in Millisekunden) jeweils für das initiale Einfügen der Datensätze in diese Datenstruktur, sowie für das Suchen nach bestimmten Datensätzen anhand der bereitgestellten Testdatensätze und für die Bestimmung der Anzahl der Mitarbeiter der Abteilung Development durch Traversierung des gesamten Datenbestandes. Benennen Sie dazu auch die jeweils relevanten grundlegenden Operationen und dokumentieren Sie die Ergebnisse entsprechend der bereitgestellten Tabellen. Das Lesen der Testdatensätze aus den Textdateien ist von der Zeitmessung auszunehmen.
• Hinweis: Das Zählen der grundlegenden Operationen hat so zu erfolgen, dass die Methoden insert(IEmployee), search(int), search(String, String), size(Department), members(Department) der Klasse Management den Zähler auf 0 setzen und anschließend bei jeder grundlegenden Operation erhöhen. Die Konstruktoren der Klasse Management zählen die Gesamtanzahl der grundlegenden Operationen für alle einzufügenden Elemente.

# Aufgabe 2 (Sortiertes Array)
• Verwenden Sie die in Aufgabe 1 erstellten Klassen soweit möglich weiter.
• Als Datenstruktur für die Mitarbeiterverwaltung wählen Sie in dieser Aufgabe nun ein sortiertes Array. Sortieren Sie dazu das Array mithilfe von MergeSort. Der Sortieralgorithmus soll dabei einen Komparator (java.util.Comparator<T>) verwenden. Implementieren Sie 3 Komparatoren, die Ihnen erlauben nach Schlüssel, nach Nachname oder nach Abteilung zu sortieren. Die Datenstruktur soll wiederum eine initiale Kapazität von 8 Elementen haben und dynamisch erweiterbar sein. Beachten Sie, dass mit Ausnahme des Interfaces Comparator auch hier die Klassen aus dem Java Collection-Framework nicht verwendet werden dürfen.
• Die Klasse Management für die Mitarbeiterverwaltung ist im Package de.ostfalia.aud.s23ss.a2 zu erstellen und das Interface IManagement zu implementieren.
• Implementieren Sie die Suche im Array als binäre Suche. Beachten Sie, dass bei der Suche z.B. nach Namen möglicherweise mehrere Elemente gefunden werden, sodass in diesem Fall entsprechend dem Interface ein Array mit den passenden Werten zurückliefert wird. Beachten Sie ebenso, dass die Suche nach dem ersten bzw. letzten Element mit der binären Suche erfolgen soll.
• Die Zeitkomplexitäten und die Laufzeiten sind entsprechend Aufgabe 1 zu bestimmen und tabellarisch für diese Datenstruktur zu dokumentieren.
  
# Aufgabe 3 (Binäre Bäume)
• Verwenden Sie die in Aufgabe 1 erstellten Klassen soweit möglich weiter.
• Als Datenstruktur für die Mitarbeiterverwaltung wählen Sie in dieser Aufgabe nun die Datenstruktur eines binären Suchbaums und fügen Sie die Datensätze ein. Nutzen Sie die Komparatoren aus Aufgabe 2. Das heißt, der Baum kann nach Schlüssel, Nachname oder Abteilung sortiert aufgebaut werden. Da Nachnamen und Abteilungen nicht eindeutig sind, speichern Sie im Knoten des Baums jeweils eine Liste von Elementen ab.
• Die Klassen aus dem Java Collection-Framework dürfen auch hier nicht verwendet werden. Ausnahme ist die Implementierung eines Iterators, bei der gegebenenfalls ein Stack als Hilfsdatenstruktur nützlich ist.
• Die Klasse Management für die Mitarbeiterverwaltung ist im Package de.ostfalia.aud.s23ss.a3 zu erstellen und das Interface IManagement zu implementieren.
• Die Zeitkomplexitäten und die Laufzeiten sind entsprechend Aufgabe 1 zu bestimmen und tabellarisch für diese Datenstruktur zu dokumentieren.
• Vervollständigen Sie Ihre Implementierung mit der Methode height() aus dem Interface IManagement, welche die Höhe des Binärbaums liefert.
  
# Aufgabe 4 (java.util Klassen)
• Modifizieren Sie Aufgabe 1 dahingehend, dass Sie für die Datenstruktur der Mitarbeiterverwaltung (neuer Klassenname: ManagementList), statt der selbst programmierten Klasse die Klasse ArrayList aus den Java Collections verwenden.
• Modifizieren Sie Aufgabe 3 dahingehend, dass Sie für die Datenstruktur der Mitarbeiterverwaltung (neuer Klassenname: ManagementTree), statt der selbst programmierten Klasse die Klasse TreeSet aus den Java Collections verwenden. Die Methode height() braucht nicht implementiert werden.
• Erstellen Sie die Mitarbeiterverwaltung, Klasse ManagementMap, als eine Hashtabelle und verwenden Sie dafür die Klasse HashMap aus den Java Collections.
• Die Klassen ManagementList, ManagementTree und ManagementMap sind im Package de.ostfalia.algo.s23ss.a4 zu erstellen und müssen jeweils das Interface IManagement implementieren.
• Auf die Komplexitätsmessung durch Zählen grundlegender Operationen wird in dieser Aufgabe verzichtet. Bestimmen Sie analog zu den vorangehenden Aufgaben nur die Laufzeiten und dokumentieren Sie diese entsprechend.
