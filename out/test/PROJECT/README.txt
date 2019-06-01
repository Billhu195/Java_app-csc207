CSC207 Project Phase 2

This program is designed for anyone who uses a travel card to to enter and exit the system. In this
program, users can find a path and decided the path by themselves. The program will calculate the
fare for that trip and user will decided which card (from his wallet) to be deducted. In addition,
there are some extra features such as user can view total trips for a certain day. The admin user
can also view some statistics such as total station for a certain day.

###################################################################################################

Getting Start

The program will start with checking if this is the first time. If yes, The program will read
Card.txt, map.txt, Users.txt and store the information in manager. Also, map.txt should not be
empty otherwise there will be no stops. After that, there will be a window come out and user can
start using the program. There are two choice of login, one is for Admin user, another is for
regular user.

a. At very beginning, you have a window with a button said "home page", click it, and you will goes
   into the home page
b. Then you will see the home page, which wants you to select between a regular user
   (or card holder), and an admin user
c. Welcome to log in! After push the corresponding button, you will see different log in page:
   admin log in or user log in
d 1) user log in page:
     we can register new user account using name, email, and password in the register page which
     link to the button in user log in page
     we can also log in to the user account page using the input email and password
  2) admin log in page(same as user)
e 1) user account page:
     we can see the information about the current user, including the user name, user email,
     and today's cost of current user.
     Setting -- change the name and password of current user by click the setting button and
                operate on the new setting window
     wallet -- see the information about cards which belongs to current user by click the wallet
               button and choose one specific card which you want to get information from
     active a new card -- active a new card which not active and not suspend into current user's
                          wallet
     view statistics -- see statistic of current user, including the money cost and total stations
                        passed by current user
     map -- see the map
  2) admin user page:
     A new window with 5 button. The last button is used to deleted old data and others are used to
     view statistic.
f. map of the transit system:
     A new window with all stops button and all routes button.
     1) stop button -- will open a new stop window and the program will set this stop as current_stop
     2) route button -- will open a new route window with stop buttons(all stops in route)
                        click any stop button in route window will do the same thing as in map window
g. Stop window
     1) Tap Activity button -- user need to confirm some data such as transit type to
                               complete a tap_in or tap_out
     2) confirm button -- user need to choice the route and it will show when and where is next
                          transit coming after clicking this button
     3) find path button -- user need to choice the destination stop and card type and it will show
                            the fastest path and cheapest path after clicking

###################################################################################################

Features

1. Regular user can activate a new card and save it in their wallet.
2. Regular user can view total cost for a period of time.
3. Regular user can view cost list for a period of time.
4. Regular user can view total station travelled for a period of time.
5. Regular user can view station travelled for a period of time.
6. Regular user can find shortest path and cheapest path.
7. Admin user can view statistics(such as number of cards).
8. Admin user can delete old data which is more than 5 years ago(means all the data will keep for
at most 5 years).
9. Regular user can check next transit time for a certain stop.(Show when and what direction)
10. There are two kind of cards, one is monthly card, another is regular card. monthly card cannot
be charged, but no fare for that month. Regular card has different fee strategy for different
group of people.
11. Monthly card can extend expire date.
12. Regular user can change name and password.

###################################################################################################

We design a test class to test for this program and all of them work well.

Test case:(line in test.txt)

(line 2-4) Add_cardholder : Test if the system can add new cardholder
(line 5) Change_name: Test if the regular user can change their name
(line 6-8 && 10) Activate_card: Test if the regular user can activate a new regular card
(line 9) Activate_card: Test if the password is wrong, the regular cannot activate it
(line 11) Activate_card: Test if the regular user can activate a new monthly card
(line 12) Activate_card: Test if the card has already been activated, the regular user cannot activate it
(line 13) Suspend_card: Test if the regular user can suspend a card from his wallet
(line 14) Activate_card: Test if the card has already been suspended, the regular user cannot activate it
(line 15-16 && 21-26 && 29-30 && 34-35 && 43-46) Tap: Test if the regular user can use his card to tap_in or tap_out
(line 17-18) Tap: Test if this trip will be added with last trip (in same route)
(line 19-20 && 27-28) Tap: Test if this is not a trip (tap_in and tap_out in same stop)
(line 32) Add_balance: Test if the regular user can add balance to his card
(line 33) Tap_out: Test if this is an invalid tap_out
(line 36-38) Tap_in: Test if this is an invalid tap_in
(line 39-40) Tap: Test if this is an invalid tap_out (tap_in and tap_out in different routes)
(line 47) Three_recent_trips_Card: Test if regular user can view three recent trips
(line 48) View_trip_for_date_Card: Test if regular user can view all trips on a certain date
(line 49) Total_money_for_range_CH: Test if regular user can view all cost for a range of date
(line 50) Total_station_for_range_CH: Test if regular user can view all stations for a range of date
(line 51) Total_money_for_range_Admin: Test if Admin user can view all cost for a range of date
(line 52) Total_station_for_range_Admin: Test if Admin user can view all stations for a range of date
(line 53-60) Find_path: Test if regular user can use the "Find Path" method
(line 61-62) Next_Transit: Test if regular user can check the next transit arrive time

###################################################################################################
