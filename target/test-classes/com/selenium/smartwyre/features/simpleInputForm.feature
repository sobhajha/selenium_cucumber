#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template

Feature: Input Form


@First
Scenario: User is on selenium demo page
Given User in on selenium easy demo page
 When User expands input form menu 
 And click on simple form demo
 Then user should on simple form demo page
 
      
  @Last
  Scenario Outline: Two Input Fields
  Given User is on simple input form page
  When User enters <a> in input field a and <b> in input field b
  And clicks on Get Total  button
  Then user must get <a+b>
   Examples: 
   | a  | b | a+b  |
   | 4 |     8 | 12 |
   