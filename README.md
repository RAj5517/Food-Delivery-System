# Food Delivery System

A comprehensive Spring Boot-based Food Delivery System backend API with complete features from authentication to analytics.

## Features

- ✅ User Management (Customer, Restaurant, Delivery Partner, Admin)
- ✅ JWT Authentication & Authorization
- ✅ Restaurant Management with Menu Items
- ✅ Customer Profile & Address Management
- ✅ Shopping Cart
- ✅ Order Management with State Machine
- ✅ Payment Integration (Razorpay/Stripe)
- ✅ Delivery Partner Module
- ✅ Reviews & Ratings
- ✅ Email Notifications
- ✅ Caching (Caffeine)
- ✅ Rate Limiting
- ✅ Analytics & Reporting APIs
- ✅ Swagger API Documentation
- ✅ File Upload
- ✅ Complex Queries with Pagination & Sorting

## Technology Stack

- **Framework:** Spring Boot 3.2.0
- **Java Version:** 17+
- **Database:** PostgreSQL
- **Security:** Spring Security + JWT
- **Documentation:** SpringDoc OpenAPI (Swagger)
- **Caching:** Caffeine
- **Email:** Spring Mail (SMTP)
- **Payment:** Razorpay / Stripe
- **Build Tool:** Maven
- **Validation:** Jakarta Validation

## Prerequisites

- Java 17 or higher
- PostgreSQL 12+
- IDE (IntelliJ IDEA, Eclipse, VS Code) - Optional
- **Note:** Maven Wrapper is included, no need to install Maven separately

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd food-delivery-system
```

### 2. Database Setup
```sql
CREATE DATABASE food_delivery_db;
```

### 3. Configure Environment Variables
Create `.env` file or set environment variables:
```bash
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-secret-key-min-64-characters
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
RAZORPAY_KEY_ID=your-razorpay-key-id
RAZORPAY_KEY_SECRET=your-razorpay-key-secret
```

### 4. Build the Project
```powershell
# Windows PowerShell/CMD
.\mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

### 5. Run the Application
```powershell
# Windows PowerShell/CMD
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

Or using IDE: Run `FoodDeliveryApplication.java`

### 6. Access the Application
- API Base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

## Project Structure

```
src/main/java/com/fooddelivery/
├── controller/      # REST Controllers
├── service/         # Business Logic
├── repository/      # Data Access Layer
├── model/           # Entity Models
├── dto/             # Data Transfer Objects
├── config/          # Configuration Classes
├── exception/       # Custom Exceptions & Handlers
├── util/            # Utility Classes
└── FoodDeliveryApplication.java
```

## API Endpoints

### Authentication
- `POST /api/auth/register/customer` - Customer registration
- `POST /api/auth/register/restaurant` - Restaurant registration
- `POST /api/auth/register/delivery` - Delivery partner registration
- `POST /api/auth/login` - Login
- `POST /api/auth/refresh-token` - Refresh JWT token

### Restaurant
- `GET /api/restaurants` - List restaurants with pagination & filtering
- `GET /api/restaurant/{id}` - Get restaurant by ID
- `POST /api/restaurant/register` - Register restaurant
- `PUT /api/restaurant/profile` - Update restaurant profile

### Menu
- `GET /api/restaurant/{id}/menu` - Get restaurant menu
- `POST /api/restaurant/menu/items` - Add menu item
- `PUT /api/restaurant/menu/items/{id}` - Update menu item
- `DELETE /api/restaurant/menu/items/{id}` - Delete menu item

### Customer
- `GET /api/customer/profile` - Get customer profile
- `PUT /api/customer/profile` - Update profile
- `POST /api/customer/addresses` - Add address
- `GET /api/customer/addresses` - List addresses

### Cart
- `GET /api/cart` - Get current cart
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/items/{id}` - Update quantity
- `DELETE /api/cart/items/{id}` - Remove item
- `DELETE /api/cart/clear` - Clear cart

### Orders
- `POST /api/orders/place` - Place order
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders` - List orders with pagination
- `PUT /api/orders/{id}/cancel` - Cancel order

### Payments
- `POST /api/payments/create-order` - Create payment order
- `POST /api/payments/verify` - Verify payment
- `POST /api/payments/refund/{orderId}` - Process refund

### Delivery
- `GET /api/delivery/orders/available` - Get available orders
- `POST /api/delivery/orders/{id}/accept` - Accept order
- `PUT /api/delivery/orders/{id}/update-location` - Update location
- `PUT /api/delivery/orders/{id}/deliver` - Mark delivered

### Reviews
- `POST /api/reviews/submit` - Submit review
- `GET /api/reviews/restaurant/{id}` - Get restaurant reviews

### Analytics
- `GET /api/admin/analytics/overview` - Admin overview
- `GET /api/restaurant/analytics/dashboard` - Restaurant dashboard
- `GET /api/delivery/analytics/earnings` - Delivery earnings

### Admin
- `GET /api/admin/users` - List all users
- `PUT /api/admin/users/{id}/activate` - Activate/deactivate user
- `GET /api/admin/restaurants/pending-approval` - Pending restaurants
- `PUT /api/admin/restaurants/{id}/approve` - Approve restaurant

## Testing

See [HOW_TO_TEST.md](./HOW_TO_TEST.md) for detailed testing guide.

### Quick Test Commands
```powershell
# Run all tests
.\mvnw.cmd clean test

# Run specific test
.\mvnw.cmd test -Dtest=ApplicationStartupTest
```

### Using Postman
1. Start the application: `.\mvnw.cmd spring-boot:run`
2. Set base URL: `http://localhost:8080`
3. For protected endpoints, add JWT token in Authorization header:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

## Development Phases

See [PROJECT_PLAN.md](./PROJECT_PLAN.md) for complete development plan.

## License

This project is for educational purposes.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

