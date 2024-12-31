# Data Crawler Application

## Description

The **Data Crawler Application** is a web crawler designed to extract student result data from a website. It logs into the system using a combination of a student's **Roll Number** and **Date of Birth (DOB)**, navigating through the website to retrieve the **Result Dashboard** and **Score Card** from the next webpage.

This application employs a **brute-force approach**, systematically testing all possible combinations of Roll Numbers with DOBs within a valid range.

- If a valid Roll Number and DOB pair is found, the application fetches the result data and stores it in the database.
- If no match is found, the application records `"Not in range"` in the DOB field of the database for the given Roll Number.

## Technologies Used

- **Java**: Core language for the application.
- **Spring Boot**: Framework for creating and managing the RESTful services and application logic.
- **Hibernate**: ORM framework for database operations.
- **MySQL**: Relational database for storing fetched data.

## Features

- **Efficient Multi-Threaded Data Crawling**: Enables faster processing by leveraging multi-threading.
- **Configurable Thread Count**: Adapts to system capabilities by allowing configuration of the number of threads.
- **Database Integration**: Stores result data systematically for easy access and management.

---
