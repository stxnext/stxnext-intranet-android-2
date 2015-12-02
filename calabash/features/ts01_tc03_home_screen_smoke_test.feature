Feature: Home screen smoke test feature

  Scenario: I can manipulate home screen controls
    When I press "Log in"
    Then I wait for 3 seconds

    When I see "John"
    Then I press view with id "scroll_view_profile_image_view_mapper"

    * I wait for 2 seconds
    * I press "John Smith"
    * I wait for 2 seconds

    When I see "Worked hours"
    Then I scroll down
    * I wait for 1 seconds
    * I scroll up

    When I see "Worked hours"
    Then I press view with id "worked_hours_refresh_hours_card_iv"
    * I wait for 2 seconds

    When I see "Worked hours"
    Then I press view with id "floating_button"
    * I wait for 2 seconds


    When I see "I\'ll be late!"
    Then I press "I\'ll be late!"

