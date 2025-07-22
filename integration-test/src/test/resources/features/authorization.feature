Feature: Authorizations endpoints

  Scenario Outline: Create authorization happy flow
    When I send a create authorization request with body '<grantor>' '<grantee>' '<accountNumber>' '<authorization>'
    Then I should get <status> status code
    And I send request to get authorizations for '<grantee>'
    Then I should get 200 status code
    Then I should get <count> authorizations
    Examples:
      | grantor              | grantee | accountNumber    | authorization  | status | count |
      | john                 | jane     | 00001           | READ           | 201    | 1     |
      | john.                | jane     | 00001           | WRITE          | 201    | 2     |
      | john                 | jane     | 00002           | READ           | 201    | 3     |


