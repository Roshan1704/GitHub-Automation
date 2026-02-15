@ui @smoke
Feature: Stripe dashboard payments validation

  Scenario: Validate payments page and refund status
    Given the user logs into Stripe dashboard in test mode
    When the user opens the payments page
    Then the payments page should be displayed
    And the user searches for payment "pymt_test_001"
    Then refund status should be "refunded"

  @regression
  Scenario: Validate payment filters and pagination
    Given the user logs into Stripe dashboard in test mode
    When the user opens the payments page
    And user applies payment filters and validates pagination
    Then the payments page should be displayed
