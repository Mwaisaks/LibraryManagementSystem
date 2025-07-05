| **Endpoint**                | **Method** | **Description**                                                                       | **Access**                                            |
|-----------------------------| ---------- |---------------------------------------------------------------------------------------| ----------------------------------------------------- |
| `/api/auth/register`        | POST       | Registers a new user (Student, Teacher, or Admin)                                     | Public                                                |
| `/api/auth/login`           | POST       | Authenticates a user and returns a JWT token                                          | Public                                                |
| `/api/auth/forgot-password` | POST       | Initiates the password reset process                                                  | Public                                                |
| `/api/auth/reset-password`  | POST       | Resets the password after verification                                                | Public                                                |
| `/api/users`                | GET        | Retrieves a list of all registered users                                              | Admin                                                 |
| `/api/users/{userId}`       | GET        | Retrieves a specific user by their Id                                                 | Admin                                                 |
| `/api/users/{userId}`       | PUT        | Updates user profile details (e.g., name, email, password)                            | Authenticated (user themselves or Admin)              |
| `/api/users/{userId}`       | DELETE     | Deletes a user account                                                                | Admin                                                 |
| `/api/books`                | GET        | Retrieves all books or searches by filters (title, author, category)                  | Public                                                |
| `/api/books`                | POST       | Adds a new book to the library                                                        | Admin                                                 |
| `/api/books/{id}`           | GET        | Retrieves detailed information for a specific book                                    | Public                                                |
| `/api/books/{id}`           | PUT        | Updates book details                                                                  | Admin                                                 |
| `/api/books/{id}`           | DELETE     | Removes a book from the library                                                       | Admin                                                 |
| `/api/borrow`               | POST       | Borrows a book (verifies availability, updates status, creates a transaction record)  | Student/Teacher                                       |
| `/api/return`               | POST       | Returns a borrowed book (updates status, logs the return transaction)                 | Student/Teacher                                       |
| `/api/transactions`         | GET        | Retrieves borrowing/return transaction history                                        | Authenticated (user views their own or Admin for all) |
| `/api/reservations`         | POST       | Reserves a book that is currently borrowed                                            | Student/Teacher                                       |
| `/api/reservations`         | GET        | Retrieves reservation details (user’s own reservations or all reservations for Admin) | Authenticated (user/Admin)                            |
| `/api/notifications`        | GET        | Retrieves notifications (e.g., due dates, overdue alerts)                             | Authenticated                                         |
| `/api/notifications`        | POST       | Sends/creates notifications (triggered by Admin or system events)                     | Admin/System                                          |
| `/api/fines`                | GET        | Retrieves fine details for overdue books                                              | Authenticated (user views their own or Admin for all) |
| `/api/fines`                | POST       | Processes payments or updates fine records                                            | Authenticated (user/Admin)                            |
