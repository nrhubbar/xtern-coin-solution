Hello Everyone!
---

###This is my submission for the 2016 Xtern Application.

The main idea behind my solution was to have a user class that handles guessing and holds 
its ID. Then we have a Treasury class that handles users' guesses, users' balances, and 
changing the key when needed. Finally we have a main class that allows a user to login and 
start guessing. If they already have an account they can type in thier userId. User balances 
are stoered in a very rudimentary file system database.


###A better solution

My goal solution would be to have mongodb running on a docker container that is linked to 
another container running Express and Node. We would have `/user` routes to take care of 
creating and using users. We would also have a `POST /guess` that expects:
```js

{
	userId : String,
	guess : Integer
}
```
Users and they're balances would be stored in mongodb. To retrieve a balance we would have a 
`GET /balance/:userId` which would return a user's balance.
