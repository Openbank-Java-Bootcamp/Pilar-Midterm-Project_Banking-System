# Banking System API

This project represents a simple online banking system API that enables account holders to interact with their bank accounts and third-party users to receive and send money to and from accounts of this bank.


## Setup

  Clone the Banking System API. In a terminal, run:
```bash
$ git clone https://github.com/Openbank-Java-Bootcamp/Pilar-Midterm-Project_Banking-System
```

  Before running the API server, you should set the database config with yours or set your database config with my values.
## Technologies used

Java, SpringBoot, MySQL, Postman.


## Models

![diagram-13559756363580999122](https://user-images.githubusercontent.com/104001417/169570867-16651834-6dc8-42c0-a810-be2a62c9c386.png)

There are four types of accounts: Saving, Checking, Student checking and Credit.
Accounts can only be created by an Admin.
Anyone can create a new account holder user and login, but only admins when create account vinculate an account with its owner (account holder).
Account Holders can make transfers from their account and check their balance. When a transfer is requested, the availability of funds (or the credit limit in the case of credit account) is checked before processing it. Once the transfer is made, if the balance falls below the minimum allowed a penalty fee is charged.
Every time an account holder access their balance, it is checked if it is necessary to add corresponding interests (saving and credit account) or charge maintenance to the account (checking account).


## Server Routes Table
 
| Request Type  | Route              | Description                            | Authorization    |
| ------------- | ---------------    | -------------------------------------- |----------------- |
| `POST`        | /api/login         | Login  with username and password      | PUBLIC           |
| `POST`        | /api/checking      | Create a Checking Account              | ADMIN            |
| `POST`        | /api/saving        | Create a Saving Account                | ADMIN            |
| `POST`        | /api/credit        | Create a Credit Account                | ADMIN            |
| `PATCH`       | /api/transfer      | Transfer Money                         | ACCOUNT HOLDER           |
| `GET`         | /api/balance       | Access account balance                 | ACCOUNT HOLDER           |
| `GET`         | /api/adminbalance  | Access any account balance             | ADMIN            |
| `PATCH`       | /api/setbalance    | Modify any account balance             | ADMIN            |
| `DELETE`      | /api/account       | Delete an account                      | ADMIN          |
| `POST`        | /api/roles         | Create a new authority role            | ADMIN   |
| `POST`        | /api/roletouser    | Add role to a user                     | ADMIN   |
| `POST`        | /api/thirdparties  | Create a Third Party                   | ADMIN   |
| `PATCH`       | /api/thirdtransfer | A third party transfer money to an account      | PUBLIC   |
| `PATCH`       | /api/thirdtake     | A third party takes money from an account      | PUBLIC   |
| `DELETE`      | /api/thirdparties  | Delete a Third Party                   | ADMIN         |
| `POST`        | /api/users         | Create an Account Holder               | PUBLIC         |
| `POST`        | /api/admin         | Create an Admin                        | ADMIN         |
| `DELETE`      | /api/users         | Delete a user                          | ADMIN          |

## Future Work
## Resources
