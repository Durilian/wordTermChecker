### **FOR ENGLISH SCROLL BELOW**

INFO: If you see this message it means that project is still before official release. 
I am going through review process and family & friends test. Due to that you won't find, for e.g. 
JAR version of app  

# **WYSZUKIWARKA TERMINÓW NA PRAWO JAZDY**
# Spis treści / Table of Contents
 1. [Wprowadzenie](#wprowadzenie)
 2. [Po co to jest](#po-co-to-jest)
 3. [Uruchamianie - nietechniczni](#uruchamianie---osoby-nietechniczne)
 4. [Uruchamianie - techniczni](#uruchamianie---osoby-techniczne)
 5. [Kontakt](#kontakt)
 6. [English - introduction and explanation](#hello-everyone)
 7. [How to run - non-technical](#how-to-run---non-technical)
 8. [How to run - technical](#how-to-run---technical)
 9. [About me / contact](#about-me--contact)
 
## Wprowadzenie
Cześć wszystkim,<br>
Jestem Marek i jestem testerem, poza pracą mam Dziewczynę Patrycję i to w sumie dzięki niej teraz czytacie ten wstęp.<br>
Tak się składa, że Patrycja jest w trakcie zdawania na prawo jazdy, a że z terminami bywa różnie postanowiłem wspomóc ją z moimi zawodowymi umiejętnościami.
<br>
## Po co to jest
Do otrzymywania powiadomień o wolnych terminach na prawo jazdy zaraz po tym jak się zwolnią. <br>
Powyższy projekt uruchamia przeglądarkę i co zadany czas sprawdza 
w wybranych przez nas miastach czy dostępny jest termin egzaminu spełniający nasze kryteria.
W przypadku znalezienia wolnego terminu wysłane na podane przez nas dane (ID) powiadomienie push przez aplikację 
[WirePusher](https://wirepusher.com/) <br>
Na ten moment nie zdecydowałem się na dodawanie automatycznego zapisywania na egzamin, gdyż wymagałoby 
to użycia danych wrażliwych jak np. pesel czy numer profilu kandydata. <br>

**UWAGA:** Program do działania wymaga danych do konta info-car (mail/hasło) i nigdzie więcej te dane nie są używane/zapisywane. 
Jeśli boisz się o swoje dane to zalecam założyć dedykowane konto info-car z innym hasłem tylko na potrzeby tej aplikacji.
<br>
## Uruchamianie - osoby nietechniczne
1. Pobrać i zainstalować: [JAVA](https://www.java.com/pl/download/) (wersja 11 lub nowsza)
2. Pobrać i zainstalować: [aplikację WirePusher](https://play.google.com/store/apps/details?id=com.mrivan.wirepusher) do powiadomień (Android only)
3. Pobrać i uruchomić ten program w postaci pliku JAR
<br>

## Uruchamianie - osoby techniczne
1. Pobrać repozytorium i zbudować projekt (wymaga Java 11+)
2. Ustawić wartości dla [konfiguracji](src/main/resources/configuration/configuration.properties)
3. Uruchomić jeden z dwóch trybów:
  * Uruchomić [UI](src/main/java/pl/durilian/wordTermsChecker/Runner.java)
  * Uruchomić [test](src/main/java/pl/durilian/wordTermsChecker/test/WordTest.java) (testNG)

Oba tryby powinny działać identycznie i działają używając testu z drugiego podpunktu.
<br>
## Kontakt
Jeśli masz uwagi, pomysły, bugi, bądź po prostu chciałbyś wyrazić swoją opinie to zapraszam do kontaktu pod mafikrpg@gmail.com
<br>

## Hello everyone!
I made this project for my girlfriend which is currently trying to get a driving license. But the terms for the exam are very long. So I made this checker for her and then decided to make it public.

Writing this project gave me a lot of fun and I have still a lot of ideas on how to improve it and expand its functionalities. So probably I will be still committing here.

I have decided to write it as well as my programmatic skills allow me to. So if you are here because you are thinking of hiring me this code is something you can expect from me. But before you make your decision some explanation:

1. As there is only one rest post API request I just put everything straight in one method. Because making some larger architecture for it would be an overkill for me.
2. UI class - I just made it to allow non-technical users to use my project. It was my first adventure with UI and I am aware that it is terribly written and probably has tons of anti-patterns in it.
3. Selenide is so great that I do not have to overcome many selenium problems because most of them have a built-in solution in Selenide. For example, I do not have to use the stream() by myself because they have a smart implementation in the filter() method
4. I decided to make this project as simple as possible to use for Polish non-technical users (main "target" for this app) so logs and UI are available only in Polish.
I am open to suggestions and criticism as I want to use this project to develop my programming skills.

## How to run - non-technical
1. Download and install:  [JAVA](https://www.java.com/en/download/) (version 11 or higher)
2. Download and install:  [WirePusher app](https://play.google.com/store/apps/details?id=com.mrivan.wirepusher) for pushes (Android only)
3. Download and run this program from JAR file

## How to run - technical
1. Download and build project (Java 11+)
2. Set values in [configuration](src/main/resources/configuration/configuration.properties)
3. Run one of the two runners
  * Run [UI](src/main/java/pl/durilian/wordTermsChecker/Runner.java)
  * Run [test](src/main/java/pl/durilian/wordTermsChecker/test/WordTest.java) (testNG)

Both runners should work identically. Both of them use in the end test from second point

## About me / contact
I am tester with a few years of experience. I am currently focusing on this project and developing my programmatic skills. If you would like to know more about me or you are looking for contact visit my [Linkedin profile](https://www.linkedin.com/in/marek-stawarczyk-30990912a/)