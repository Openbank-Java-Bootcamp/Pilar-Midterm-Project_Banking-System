# :computer: Banking System API

This project represents a simple online banking system API that enables account holders to interact with their bank accounts and third-party users to receive and send money to and from accounts of this bank.


## :gear: Setup

  Clone the Banking System API. In a terminal, run:
```bash
$ git clone https://github.com/Openbank-Java-Bootcamp/Pilar-Midterm-Project_Banking-System
```
  You will need to download the Postman tool from getpostman.com/postman.

  Before running the API server, you should set the database config with yours or set your database config with my values.
  
  With Postman installed, and this repository cloned and the server running, you can start making your requests.
  
## :rocket: Technologies used

Java, SpringBoot, MySQL, Postman.


## Models

![diagram-13559756363580999122](https://user-images.githubusercontent.com/104001417/169570867-16651834-6dc8-42c0-a810-be2a62c9c386.png)

There are four types of accounts:

-Saving 

-Checking

-Student checking  

-Credit

Accounts can only be created by an Admin.

Anyone can create a new account holder user and login, but only admins when create account vinculate an account with its owner (account holder).

Account Holders can make transfers from their account and check their balance. 

When a transfer is requested, the availability of funds (or the credit limit in the case of credit account) is checked before processing it. Once the transfer is made, if the balance falls below the minimum allowed a penalty fee is charged.

In addition, suspicions of fraud are detected by freezing the account and preventing the transfer. Only Admin can reactivate the account.

Every time an account holder access their balance, it is checked if it is necessary to add corresponding interests (in saving and credit account) or charge maintenance to the account (checking account).


## Server Routes Table
 
| Request Type  | Route              | Description                            | Authorization    | Requested Data      |
| ------------- | ---------------    | -------------------------------------- |----------------- |---------------------|
| `POST`        | /api/login         | Login  with username and password      | PUBLIC           |username & password|
| `POST`        | /api/checking      | Create a Checking Account              | ADMIN            |CheckingAccountDTO object  |
| `POST`        | /api/saving        | Create a Saving Account                | ADMIN            |SavingAccountDTO object|
| `POST`        | /api/credit        | Create a Credit Account                | ADMIN            |CreditAccountDTO object|
| `PATCH`       | /api/transfer      | Transfer Money                         | ACCOUNT HOLDER   |OwnerTransferDTO object|
| `GET`         | /api/balance       | Access account balance                 | ACCOUNT HOLDER   |accountId|
| `GET`         | /api/adminbalance  | Access any account balance             | ADMIN            |accountId|
| `PATCH`       | /api/setbalance    | Modify any account balance             | ADMIN            |accountId & newBalance|
| `DELETE`      | /api/account       | Delete an account                      | ADMIN            |accountId|
| `PATCH`       | /api/status        | Change account status                  | ADMIN            |accountId & status|
| `POST`        | /api/roles         | Create a new authority role            | ADMIN            |Role object|
| `POST`        | /api/roletouser    | Add role to a user                     | ADMIN            |RoleToUserDTO object|
| `POST`        | /api/thirdparties  | Create a Third Party                   | ADMIN            |ThirdParty object|
| `PATCH`       | /api/thirdtransfer | A third party transfer money to an account   | PUBLIC   |hashedKey & ThirdPartyTransferDTO object|
| `PATCH`       | /api/thirdtake     | A third party takes money from an account   | PUBLIC   |hashedKey & ThirdPartyTransferDTO object|
| `DELETE`      | /api/thirdparties  | Delete a Third Party                   | ADMIN         |thirdPartyId|
| `POST`        | /api/users         | Create an Account Holder               | PUBLIC         |AccountHolder object|
| `POST`        | /api/admin         | Create an Admin                        | ADMIN         |Admin object|
| `DELETE`      | /api/users         | Delete a user                          | ADMIN          |userId|

Objects must be provided in the body of the request as raw and JSON format, as examples shown below.

CheckingAccountDTO

```bash
{
    "balance": {
        "amount": "500",
        "currency": "EUR"
    } ,
    "primaryAccountOwnerId": "5" ,
    "secondaryAccountOwnerId": "",
    "secretKey": "5688"
}
```

SavingAccountDTO

```bash
{
    "balance": {
        "amount": "500",
        "currency": "EUR"
    } ,
    "primaryAccountOwnerId": "1" ,
    "secondaryAccountOwnerId": "",
    "secretKey": "5689",
    "minimumBalance":{
        "amount":"1500",
        "currency": "EUR"
    },
    "interestRate": "0.6"
}
```

CreditAccountDTO

```bash
{
    "balance": {
        "amount": "1000",
        "currency": "EUR"
    },
    "primaryAccountOwnerId": "5" ,
    "secondaryAccountOwnerId": null,
    "creditLimit":{
        "amount": "5000",
        "currency": "EUR"       
    },
    "interestRate": "0.5"
}
```

OwnerTransferDTO

```bash
{
    "transferAmount": {
        "amount":"10",
        "currency" : "EUR"
    },
    "ownerTargetName": "James Smith",
    "targetAccountId": "2",
    "ownAccountId":"3"
}
```

Role

```bash
{
    "name": "Luis Perez"
}
```

RoleToUserDTO

```bash
{
    "username": "Luis Perez",
    "roleName":"ROLE_ADMIN"
}
```

ThirdParty

```bash
{
    "name": "Pilar Alvarez",
    "hashedKey": "2345"
}
```

ThirdPartyTransferDTO

```bash
{
    "amount": "50",
    "accountId": "1",
    "accountSecretKey": "1357"
}
```

AccountHolder

```bash
{
    "name": "Pilar Alvarez",
    "username": "pili",
    "password": "4321",
    "dateOfBirth" : "1990-01-15",
    "primaryAddress": {
        "streetAddress": "Pasaje 12",
        "postalCode":"08041",
        "city": "Madrid"
    },
    "mailingAddress": null
}
```

Admin

```bash
{
    "name": "Pilar Alvarez",
    "username": "pili",
    "password": "4321"
}
```

## Resources

Instructors of my Ironhack Bootcamp: Raymond & Shaun. :wink:

https://www.baeldung.com/

https://stackoverflow.com/
