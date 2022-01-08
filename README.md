# JAVA_JPDSI2_Projekt

Vehicle marketplace website made using JEE, EJB, JSF, Primefaces, Primeflex

![Project image](/project_images/img_02.png)

**[More images](project_images/)**

## Inspiration

Design mostly inspired by [otomoto](https://www.otomoto.pl/) 

## Opis projektu

* **Temat:** Serwis ogłoszeń samochodowych (otomoto/mobile.de)
* **Diagram bazy:** 

![DB Diagram](/sql/diagram.png)

Projekt zawiera system logowania oraz rejestracji, hasła są zapisywane za pomocą algorytmu hashującego PBKDF2. 
Ogłoszenia mogą być sortowane według np. ceny, przebiegu oraz filtrowane na podstawie danych takich jak: marka, model, cena, przebieg, status pojazdu, moc, napęd, liczba drzwi, kolor itd.

Zalogowani użytkownicy mogą wystawiać, edytować oraz kończyć swoje ogłoszenia.
Administrator posiada dodatkowo panel administracyjny, w którym może przeglądać, edytować oraz usuwać marki, modele i generację pojazdów dostępnych w systemie, blokować użytkowników i zmieniać im rolę, zarządzać wyposażeniem dostępnym do wybrania oraz zarządzać ofertami.

Cała aplikacja webowa jest w pełni responsywna i przystosowana do urządzeń mobilnych.
Zdjęcia poszczególnych ofert zapisywane są po stronie serwera WildFly (wymagana konfiguracja serwera).
W skrypcie sql zostały zawarte przykładowe dane do sprawdzenia poprawności działania systemu.
