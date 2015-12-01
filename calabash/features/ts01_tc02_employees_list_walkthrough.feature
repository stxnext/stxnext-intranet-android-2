Feature: Login feature

  Scenario: I can see employees and absences list
    When I press "Log in"
    Then I wait for 5 seconds
    When I see "John"
    #Then I swipe right
    #Then I drag from 0:150 to 300:150 moving with 20 steps
    Then I press the menu key

    When I see "Employees"
    Then I press "Employees"
    * I wait for 1 seconds
    * I scroll recyclerview down
    * I wait for 1 seconds
    When I see "Bert Lawnmower"
    Then I press "Bert Lawnmower"
    

