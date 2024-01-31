# Leaderboards in HiBet

## Description
**Purpose**: The Leaderboard is designed to rank users based on their performance in betting within a specified range of events and users.

**Scope**: It includes a subset of events and users, focusing solely on bets placed by "accepted" users during "accepted" events.
The [Leaderboard.kt](./src/main/kotlin/ro/hibyte/betting/entity/Leaderboard.kt) file contains the implementation of the Leaderboard class, 
which is responsible for the creation of the leaderboard configuration and the selection of users that participate in the leaderboard and events that should be taken into consideration.

The Leaderboard is dynamic and updates as new bets are placed or results are finalized in the accepted events.
This means that the leaderboard rankings are not saved in the database, but rather computed, on-demand when the leaderboard is requested.
There is a PUT Request in the [LeaderboardController.kt](./src/main/kotlin/ro/hibyte/betting/controller/LeaderboardController.kt) file that requests the computation of a Leaderboard.

The rankings of a leaderboard can be done by using a variety of metrics. The following metrics should be implemented:
* Most Bets
* Most Wins
* Fewest Losses
* Highest Earner                                           
* Highest Loser
* Largest Single Bet
* Largest Single Win
* Largest Single Loss
* _Win/Loss Ratio_
* win/loss ratio is wins divided by losses
* _Highest Average Bet_
* _Highest Average Win_
* **Longest Winning Streak**
* **Last Minute Champion**
* **Smartest Risk Taker** - Calculated based on the ratio of winnings from high-risk bets to total high-risk bets made. This metric highlights users who are successful in making riskier bets.
* **Most Cautious Player**

## Requirement
Please implement the [computeLeaderBoard](./src/main/kotlin/ro/hibyte/betting/service/LeaderboardService.kt) function 
to get all bets placed by the users of the Leaderboard on the Events of the Leaderboard 
and compute the rankings of the Leaderboard based on the specified metrics.

Please implement as many metrics as you can, but at least the first 8 ones. Please also describe each metric in this file or in comments or other ways in the code.
Please pay attention that the implementation you create should be open-closed and extensible. This means that the implementation should be able to handle new metrics.

The metrics that should be calculated for each request are sent within the Request Body of the PUT Request to the LeaderboardController.
Please do not modify how the request body looks like. 

Please modify the [LeaderboardDTO.kt](./src/main/kotlin/ro/hibyte/betting/dto/LeaderboardDTO.kt) file according to the implementation that you write.
To design the LeaderboardDTO, please take into consideration that the Leaderboard should be able to handle any new metrics.
Also, please take into account that the LeaderboardDTO will be consumed by a frontend application that will most likely want to 
display the rankings of the Leaderboard in a table and be able to sort it by any metric. 

## Evaluation Criteria
* The implementation should work and the endpoint should return a response containing users sorted by the required criteria.
* The implementation should be open-closed and extensible.
* The implementation should be efficient.
* The implementation should be easy to understand and maintain.
* As many metrics as possible should be correctly or intuitively implemented.

## Time constraint
The time allocated for this task is 3.5 hours. Please do not spend more than 3.5 hours on this task.


## Test Data
We have prepared a set of test data that you can use to test your implementation. 
We have also an updated, yet not perfect [Postman collection](./src/main/resources/HiBet%20New.postman_collection.json) that you can import.

Please stop your existing Docker container for the HiBet database and start the new one with the new configuration. 
This should start you off with a clean slate, with nothing in your database. You can test this by using the postman collection. 

The test data will be imported by starting the server and then running the `main` function from the [DataPopulator.kt](./src/main/kotlin/ro/hibyte/betting/demodata/DataPopulator.kt) file.

After running this function, you should have 17 users, 100 events, 400 bet types and some random bets placed by users on the event.
The outcome of the events are also randomly populated, so all bets should count.
There is also a leaderboard created that contains all users and all events.

### Observation
For the purposes of this task, the authentication and authorization mechanisms are disabled, to simplify requests through Postman.
