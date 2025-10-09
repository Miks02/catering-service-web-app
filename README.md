# Catering Service Web Application

This is a Spring Boot web application for a catering service. The application includes a complete website with user-facing pages and an admin panel, supporting role-based access and various functionalities for managing the catering business.

## Features

### Roles and Permissions

The application has three roles with different permissions:

- **Administrator**
  - Full access to the dashboard.
  - View statistics such as total registered users, number of orders, and number of products.
  - Full CRUD operations on managers and products.
  - View orders and update their status (e.g., from pending to delivered). (*Work in progress*)
  
- **Manager** (*Work in progress*)
  - Access to the dashboard and product management.
  - Full CRUD operations on products.
  - View orders and manage order statuses.
  - Note: Manager role functionalities are still under development.

- **Client**
  - Browse the website: Home, Contact, About, and Menu pages.
  - Place orders.
  - Loyalty program: Every 5 purchases, clients earn points that are automatically converted into a 10% discount on their next purchase.

### Website Pages

- Home
- Contact
- About
- Menu
- Admin Panel (for administrators and managers)

### Technical Details

- Built with **Spring Boot**.
- Role-based access control using Spring Security.
- CRUD functionality for users, products, and orders.
- Loyalty points system implemented for clients.
- Admin panel for managing employees, products, and viewing statistics.
- Database integration with JPA/Hibernate.

### Screenshots

<img width="1915" height="915" alt="image" src="https://github.com/user-attachments/assets/c39234ec-cb66-4371-90f1-098ff63b6de2" />

<img width="1918" height="917" alt="image" src="https://github.com/user-attachments/assets/78313c67-244d-4a8f-a631-de8489bbb841" />

<img width="1918" height="915" alt="image" src="https://github.com/user-attachments/assets/e28993e4-e944-4094-98b7-e308dd9afc02" />

<img width="1916" height="924" alt="image" src="https://github.com/user-attachments/assets/6b54fd8f-887c-4f42-928d-305f053ee816" />

<img width="1909" height="919" alt="image" src="https://github.com/user-attachments/assets/887a566e-1621-43c5-9167-fac68958cd8c" />




