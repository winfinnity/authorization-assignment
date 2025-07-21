Feature: Authorizations endpoints

  Scenario Outline: Create authorization happy flow
    When I send a 'POST' request to '/api/authorizations' with body '<grantor>' '<grantee>' '<accountNumber>' '<authorization>'
    Then I should get <status> status code
    Examples:
      | grantor              | grantee | accountNumber    | authorization  | status |
      | john                 | jane     | 00001         | READ           | 201   |
      | john.                | jane     | 00001         | WRITE          | 201   |
      | john                 | jane     | 00002         | READ           | 201   |


