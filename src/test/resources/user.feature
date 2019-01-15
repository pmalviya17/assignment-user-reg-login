Feature: User login features
  I want to use this template for my feature file

  Scenario: When using the tool for the first time
    Given Providing Username and Password
    When  I enter the username "prit007"
    And   I enter the required password value "amdocs-password"
    Then  I see I am eligible to see user details like user first name "pritesh"
    Then  User details like user last name "malviya"
    Then  User details like user Status "Deactivated"
    Then  User details like user id 1