# wroclawhelper-b

EN
wroclawhelper-b processes data which are available as open-data and shares them through endpoints locally at server port 8080
wroclawhelper-b shares:
- current indications from weather stations,
- current information about WRM stations,
- current information about Vozilla cars.
The application works with H2:file DB so it does not require configuration of connection with DB before starting .
Shared endpoints enable also user service (registration, logging, user account management).

There is also a simple frontend application available, which is using Vaadin framework: https://github.com/RadoslawSulikowski/wroclawhelper-f

PL
wroclawhelper-b przetwarza dane udostępnione jako open-data i udostępnia je poprzez endpointy lokalnie na porcie 8080
wroclawhelper-b udostępnia: 
- aktualne odczyty ze stacji pogodowych we Wrocławiu,
- aktualne informacje odnośnie stacji Wrocławskiego Roweru miejskiego
- aktualne dane odnośnie pojazdów Voziila.
Aplikacja współpracuje z wbudowaną bazą danyh h2:file i przed uruchomieniem nie wymaga konfigurowania połączenia z bazą danych.
Udostępnione endpointy pozwalają również na obsługę użytkowników (rejestracja, logowanie, zarządzanie kontem użytkownika).

Dostępna jest również prosta aplikacja frontendowa oparta na bibliotece Vaadin: https://github.com/RadoslawSulikowski/wroclawhelper-f
