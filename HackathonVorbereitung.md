Anmeldeseite: https://www.xing.com/events/android-hackathon-456416

# Eclipse für Android vorbereiten #

  * Eclipse 3.5: http://www.eclipse.org/downloads/
  * SVN Plugin installieren: Am besten (der Einheitlichkeit wegen) Subversive, Anleitung z.B. hier: http://jars.de/java/subversion-mit-eclipse-35-galileo-und-subversive
  * Eclipse ADT Plug-In von Google: http://developer.android.com/sdk/eclipse-adt.html
  * Android SDK: http://developer.android.com/sdk/index.html
  * Android 1.6, 2.0 und 2.1 Komponenten (+JavaDoc): http://developer.android.com/sdk/adding-components.html (im Zweifelsfall einfach alle Komponenten installieren)
  * Erste Schritte mit dem Android SDK: http://developer.android.com/guide/developing/eclipse-adt.html
  * AVDs (=Emulator-Images) für Android 1.6 und 2.1 anlegen: Google API Level 4 und 7, z.B. 32 MB SD card.

# Sourceverwaltung #
Das einfachste für die Teams ist es dieses (android-munich) Open Source Projekt für die Entwicklung zu nutzen. Die Teams können sich auch für Alternativen entscheiden, falls sie das nicht wünschen.

  * Google Account anlegen, falls noch nicht vorhanden
  * Mir (Markus Junginger) die Email-Adresse des Accounts schicken
  * Ihr werdet daraufhin von mir als Committer des Projektes eintragen (wenn ihr angemeldete Teilnehmer seid)
  * SVN Repository in Eclipse einrichten. Dafür diese URL nutzen: https://android-munich.googlecode.com/svn/trunk/hackathon (mit eurem Login)

Die einzelnen Projekte sollen dann jeweils als Unterverzeichnis angelegt werden. Als Beispiel habe ich das Projekt LiveWallpaper1 vorbereitet. Um eure Installation zu testen, am besten das Projekt auschecken und auf einem AVD starten.