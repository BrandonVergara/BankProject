# BANK of CLI
Core Ledger bank application.

## Description
A terminal-based banking application backed by a PostgreSQL database. Users can register or log in to an account and perform deposits, withdrawals, and transfers. The system validates every transaction before executing it — insufficient funds, invalid input, or a missing account will stop the operation before any changes are made.

## Features
Inside the application, users will be able to:
* **Log in:** Users will be able to log in to an existing account.
* **Register:** User will be able to create an account.

Once logged in:
* **Balance:** Users can check their balance.
* **Transaction:** Users can make a transaction.
    * `Deposit`: Deposit funds into account.
    * `Withdraw`: Remove funds from account (Will prevent overdrawing).
    * `Transfer`: Move money securely between 2 different accounts.
* **History:** Users can view their transaction history.
* **Delete:** User can delete their account (Balance must be zero).
* **Log out:** User can log out of their account.
* **Exit:** Exit the application.

## The Stack
*   **Language:** Java
*   **Build Tool:** Maven
*   **Database:** Postgres
*   **Testing:** JUnit 5
*   **Version Control:** Git & GitHub

## Architecture
1. **API Layer (Interface):** This is what the user sees. It handles all inputs, navigation, and printing messages. This layer *only* talks to the Service Layer.
2. **Domain Layer (Blueprint):** This layer is accessed by all the other layers. It acts as the blueprint for accounts and transactions.

    ![BankCLI ERD](BankERD.png)

3. **Business Layer (Service):** This layer is where the bank's rules live. It validates that a transaction is allowed before it happens, and it's the only layer permitted to call the Repository Layer. It is called by the API Layer.
4. **Repository Layer (DAO):** This layer abstracts the database — it's the only layer that communicates with PostgreSQL directly. It is *only* called by the Business Layer.

## Testing
* **Positive Test:** Verifies that a user logging in with correct account credentials is authenticated and can proceed into the application.
* **Negative Test:** Verifies that a withdrawal exceeding the account's balance is rejected, and that the failure is handled gracefully rather than crashing or corrupting data.

## Logging
* Log files live in a separate folder called `logs/`. A new file is created for each day.
* The log file tracks all activity, using:
    * `INFO`: Records successful actions (e.g., "Account id# logged in successfully").
    * `ERROR`: Records failed actions or security risks (e.g., "Withdraw failed, insufficient funds").