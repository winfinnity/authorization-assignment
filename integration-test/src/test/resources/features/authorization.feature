Feature: Authorizations endpoints

  Scenario Outline: Create and get authorization happy flow
    When I send a create authorization request with body '<grantor>' '<grantee>' '<accountNumber>' '<authorization>'
    Then I should get <status> status code
    And I send request to get authorizations for '<grantee>'
    Then I should get 200 status code
    Then I should get <count> authorizations
    Examples: :
      | grantor              | grantee | accountNumber    | authorization  | status | count |
      | john                 | jane     | 00001           | READ           | 201    | 1     |
      | john                 | jane     | 00001           | READ           | 409    | 1     |
      | john                 | jane     | 00001           | WRITE          | 201    | 2     |
      | john                 | jane     | 00002           | READ           | 201    | 3     |


  Scenario Outline: Create and get an authorizations for nonexisting account
    When I send a create authorization request with body '<grantor>' '<grantee>' '<accountNumber>' '<authorization>'
    Then I should get <status> status code
    And I send request to get authorizations for '<grantee>'
    Then I should get 200 status code
    Then I should get <count> authorizations
    Examples:
      | grantor              | grantee                | accountNumber    | authorization  | status | count |
      | john                 | jane_without_account   | non-existing     | READ           | 400    | 0     |


  Scenario Outline: Create and get an authorizations for deleted account
    Given Create a new account '<accountNumber>'
    When I send a create authorization request with body '<grantor>' '<grantee>' '<accountNumber>' '<authorization>'
    Then I should get 201 status code
    And I send request to get authorizations for '<grantee>'
    Then I should get 200 status code
    Then I should get 1 authorizations
    But Account got deleted
    And I send request to get authorizations for '<grantee>'
    Then I should get 200 status code
    Then I should get 0 authorizations
    Examples:
      | grantor              | grantee                | accountNumber    | authorization  |
      | john                 | jane_without_account   | to_be_deleted    | READ           |