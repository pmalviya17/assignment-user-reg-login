# assignment-user-reg-login
Spring based web application with a controller exposing API's for CRUD operation on user's profile
## ----------------------------------------------------------------------------------
## ---------------------------- Assignment Summary ----------------------------
## ----------------------------------------------------------------------------------
```
Dear Reviewer,

Thanks for giving me this opportunity to work on this assignment.
I am glad i have got the above artifact with clear scenarios of the assignment. PFB below the summary of the efforts
```
## Get started

### Assignment Development approach & project development summary:

I started with the `Test Driven Development (TDD)` first using `JUnit`-Using Spring test and simple Junit.[Please note] due to time constraint and issue faced during the integrated testing I commented few of the test scenerios where service call is made via JUnit to test it else it where failing the build.

### Technology Stack

* **Framework     :** `Spring-Boot (2.0.3)`
* **Testing Api's :** `Junit, SpringBootTest`
* **build tool    :** `Maven`
* **Cache         :** ``
* **Java Version  :** `1.8`
* **Java IDE      :** `IntelliJ IDEA (JetBrains)`
* **Design Patters:** 
[Creational Design Patterns used]
`- Abstract Factory, Builder, Prototype`
[Structural Design Patterns used]
`Adapter, Decorator, Facade`
[Behavior Design Patterns]
`- Chain of Responsibility, Iterator, Mediator, Template Method`
* **Other API features:** `springfox-swagger, lombok`


### Internal Architecture
The **Spring Service** itself has a pretty common internal architecture:

  * `Controller` classes provide _REST_ endpoints and deal with _HTTP_ requests and responses.
  * `service` classes takes controller calls and do the business logic which in response return the requested value as needed.In our case it fetches `list of users` _JSON_ via _HTTP_ GET, POST, PUT & DELETE request and `List of Active/Deactive` users via GET request.

  ```
  Request  ┌────────── Spring Service ───────────┐
   ─────────→ ┌─────────────┐    ┌─────────────┐ │   ┌─────────────┐
   ←───────── │  Controller │ ←→ │  ☕Services  │←--→ | ☁ H2 DB │
  Response │  └─────────────┘    └─────────────┘ │   └─────────────┘
           └─────────────────────────────────────┘
  ```  
