After some time designing and researching on how to implement this RESTFull API for the mine Sweeper, I've decided to implement it with spring-boot framework to reduce overall development time and increase efficiency by having a default setup for unit and integration tests.  

Requirements and dependencies choosen:

    *RESTful API for the game
        spring-boot-starter-web
    *Persistence
        postgresql
    *Ability to support multiple users/accounts
        spring-boot-starter-security
    *Document API
|       springfox-boot-starter    


Modeling:
    I will create a class that is the Cell for the grid that will contain several properties:
        if it has a mine inside,
        how many cells arround with mines,
        if a red flag is up,
        and a question mark.
    
    a Grid with the following properties:
        columns,
        rows,
        mines


lombok anotations:
    @AllArgsConstructor generates a constructor with 1 parameter for each field in your class. Fields marked with @NonNull result in null checks on those parameters.
    @NoArgsConstructor will generate a constructor with no parameters.
    @Builder lets you automatically produce the code required to have your class be      instantiable with code such as:
        Person.builder().name("Adam Savage").city("San Francisco").job("Mythbusters").job("Unchained Reaction").build();
    Documentation:https://projectlombok.org/features/constructor

