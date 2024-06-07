# Currency Converter | Currency Exchange Calculator

### Pre-requisitos

- Java [JDK 17](https://adoptium.net/temurin/releases/?version=17)
- Gestor de depenendencias [Apache Maven 3.8 o superior](https://getcomposer.org/download/)
- IDE [IntelliJ IDEA](https://www.jetbrains.com/idea/download)

### Consumo de API
- El API tiene un endpoint de Registro de Usuario, el cual se puede consumir mediante el siguiente endpoint:
  - POST: /users
  - Request Body:
    ```json
    {
      "name": "Abel",
      "lastName": "Palomino",
      "email": "abel@gmail.com",
      "password": "stri543ngst"
    }
    ```
- Iniciar sesión mediante el siguiente endpoint:
  - POST: /login
  - Request Body:
    ```json
    {
      "username": "user",
      "password": "password"
    }
    ```
- El API tiene un endpoint de Conversión de Moneda y registro, el cual se puede consumir mediante el siguiente endpoint:
  - POST: /exchange-rates
  - Request Body:
    ```json
    {
      "originCurrency": "USD",
      "originAmount":5, 
      "exchangeRateAmount": 3.78, 
      "destinationCurrency": "PEN"
    }
    ```
- El API tiene un endpoint de Conversión de Moneda y actualizacióm, el cual se puede consumir mediante el siguiente endpoint:
  - PUT: /exchange-rates/{id}
  - Request Body:
    ```json
    {
      "originCurrency": "USD",
      "originAmount":5, 
      "exchangeRateAmount": 3.78, 
      "destinationCurrency": "PEN"
    }
    ```
- El API tiene un endpoint de listado de Conversión de Moneda guardadas, el cual se puede consumir mediante el siguiente endpoint:
    - GET: /exchange-rates
  
- El API tiene un endpoint de consulta de una Conversión de Moneda guardada, el cual se puede consumir mediante el siguiente endpoint:
  - GET: /exchange-rates/{id}

    