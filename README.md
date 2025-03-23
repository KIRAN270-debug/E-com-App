# E-Commerce App

E-Commerce App is a Spring Boot-based web application designed to provide users with a seamless shopping experience while offering admins powerful tools to manage products, categories, and accounts. The application features role-based authentication, secure password encryption, and an intuitive user interface.

---

## Features

### **User Features**
- **User Registration & Login**: Secure registration and login with encrypted passwords.
- **Product Browsing**: Browse through a wide range of products with detailed descriptions.
- **Order Placement**: Add products to the cart and place orders effortlessly.

### **Admin Features**
- **Product Management**: Add, edit, and delete products with image upload support.
- **Category Management**: Add, edit, and delete product categories with image upload support.
- **Admin Account Management**: Create, update, and delete admin accounts.

### **Security Features**
- **Role-Based Authentication**: Separate access for users and admins.
- **Password Encryption**: Uses BCrypt for secure password storage.
- **Secure Session Handling**: Ensures safe and secure user sessions.
- **CSRF Protection**: Enabled to prevent cross-site request forgery attacks.

---

## Technologies Used

- **Backend**: Spring Boot (Spring Security, Spring Data JPA, Spring MVC)
- **Frontend**: Thymeleaf, Bootstrap, HTML5, CSS3
- **Database**: MySQL
- **Security**: Spring Security, BCrypt Password Encoder
- **File Upload**: Multipart file upload support for images
- **Build Tool**: Maven

---

## Installation & Setup

### **Prerequisites**
Ensure you have the following installed:
- Java 17 or higher
- Maven 3.x
- MySQL 8.x

### **Clone the Repository**
```bash
git clone https://github.com/your-repo/ecommerce-app.git
cd e-commerce-app
```

### **Configure Database**
Update the `application.properties` file with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerceapp
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

### **Run the Application**
```bash
mvn spring-boot:run
```
The application will be available at: [http://localhost:8080](http://localhost:8080)

---

## API Endpoints

### **Authentication**
- `POST /login` - Authenticate user/admin
- `POST /register` - Register a new user

### **User Endpoints**
- `GET /products` - View all products
- `POST /order` - Place an order
- `GET /cart` - View cart items
- `POST /cart/add` - Add a product to the cart

### **Admin Endpoints**
- `POST /admin/add-product` - Add a new product
- `PUT /admin/edit-product/{id}` - Edit an existing product
- `DELETE /admin/delete-product/{id}` - Delete a product
- `POST /admin/add-category` - Add a new category
- `PUT /admin/edit-category/{id}` - Edit an existing category
- `DELETE /admin/delete-category/{id}` - Delete a category
- `GET /admin/orders` - View all orders

---

## File Upload Support
- Admins can upload product and category images.
- Maximum file size: **25MB** per file.
- Configured in `application.properties`:
```properties
spring.servlet.multipart.max-file-size=25MB
spring.servlet.multipart.max-request-size=25MB
```

---

## Security
- **Role-Based Access Control**: Admins have access to all endpoints, while users are restricted to user-specific operations.
- **Password Encryption**: Uses BCrypt Password Encoder for secure password storage.
- **CSRF Protection**: Enabled to prevent cross-site request forgery attacks.
- **Secure Session Handling**: Ensures safe and secure user sessions.

---

## Future Enhancements
- **JWT-Based Authentication**: Implement JSON Web Tokens for stateless authentication.
- **Payment Gateway Integration**: Add support for Google Pay, PayPal, and other payment methods.
- **Email Verification**: Implement email verification for user registration.
- **Product Search & Filtering**: Add advanced search and filtering options for products.
- **Order Tracking**: Allow users to track their orders in real-time.
- **Wishlist Feature**: Enable users to save products to a wishlist.
- **Multi-Language Support**: Add support for multiple languages.
- **Responsive Design**: Improve the frontend for better mobile and tablet compatibility.

---

## Contributors
- **[Kiran Sebastian](https://github.com/kiransebastian)** - Developer

---

## Support
For any issues or feature requests, please open an issue on the [GitHub repository](https://github.com/your-repo/ecommerce-app/issues).

---

Enjoy shopping and managing your e-commerce store with ease! 🚀

---
