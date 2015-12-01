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

    When I see "John Smith"
    Then I wait for 2 seconds
    * I go back
    * I scroll recyclerview down
    * I scroll recyclerview down
    When I see "Mieszko Wrightwheel"
    Then I press "Mieszko Wrightwheel"

    When I see "John Smith"
    Then I wait for 2 seconds
    * I go back

    * I go back
    Then I wait for 2 seconds
    Then I press the menu key
    When I see "Absences"
    Then I press "Absences"

    When I see "Out Of Office"
    Then I wait for 2 seconds

    * I press "Mieszko Wrightwheel"
    When I see "John Smith"
    Then I wait for 1 seconds
    * I go back

    * I scroll recyclerview down
    * I scroll recyclerview down
    When I see "Bert Lawnmower"
    Then I press "Bert Lawnmower"
    When I see "John Smith"
    Then I wait for 2 seconds
    * I go back

    