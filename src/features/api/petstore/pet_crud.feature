@Regression
@crud
Feature: Petstore Pet CRUD Operations

  As a QA Engineer
  I want to test Petstore Pet APIs
  So that I can verify CRUD operations with positive and negative scenarios

  Background:
    Given Petstore API is available

  # =====================
  # CREATE PET
  # =====================

  @create
  Scenario: Create a new pet successfully
    When I create a pet with valid data
    Then the response status code should be 200
    And the pet should be created successfully

  @create @negative
  Scenario: Create pet with missing required fields
    When I create a pet with invalid data
    Then the response status code should be 400

  # =====================
  # GET PET
  # =====================

  @get
  Scenario: Get pet by valid ID
    Given a pet is already created
    When I retrieve the pet by ID
    Then the response status code should be 200
    And pet information should be returned

  @get @negative
  Scenario: Get pet with invalid ID
    When I retrieve the pet with invalid ID
    Then the response status code should be 404

  # =====================
  # UPDATE PET
  # =====================

  @update
  Scenario: Update an existing pet
    Given a pet is already created
    When I update the pet information
    Then the response status code should be 200
    And the pet should be updated successfully

  # =====================
  # DELETE PET
  # =====================

  @delete
  Scenario: Delete an existing pet
    Given a pet is already created
    When I delete the pet
    Then the response status code should be 200

  @delete @negative
  Scenario: Delete pet with invalid ID
    When I delete the pet with invalid ID
    Then the response status code should be 404
