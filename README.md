# 🛒 Shopping Cart Localization App

## Project Overview
This project is a Java-based Shopping Cart application that supports multi-language (localization) using a database-driven approach.

The application allows users to:
- Enter items, quantity, and price
- Calculate total cost
- Store cart data in a database
- Switch between different languages dynamically

All UI text is stored in the database and retrieved using a Localization Service.

---

## Features
- Multi-language support:
    - English (en-US)
    - Finnish (fi-FI)
    - Swedish (sv-SE)
    - Japanese (ja-JP)
    - Arabic (ar-AR) # Right-to-left support and arbaic language
- Dynamic language switching
- Database-driven translations (no hardcoded UI text)
- Shopping cart functionality
- Stores cart history with timestamp
- Fallback to English if translation is missing

---

## Technologies Used
- Java 21 (JDBC)
- MariaDB or MySQL
- Docker
- Jenkins

---

## Database Schema

The application uses the following tables:

### cart_records
Stores summary of each cart:
- id (Primary Key)
- total_items
- total_cost
- language
- created_at

### cart_items
Stores individual items:
- id (Primary Key)
- cart_record_id (Foreign Key)
- item_number
- price
- quantity
- subtotal

### languages
Stores supported languages:
- id
- code (e.g., en, fi, ja, sv, ar)
- country (e.g., US, FI, JP, SE, AR)
- display_name (e.g., English, Finnish, Japanese, Swedish, Arabic)

### content
Stores UI keys:
- id
- content_key (e.g., app.title, cart.total, button.add)

### translations
Stores translated text:
- id
- content_id (Foreign Key)
- language_id (Foreign Key)
- translated_text

See schema.sql  and seed_data.sql for full table definitions.

---

## Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/Swostika-Lama/ShoppingCart_DB.git
cd ShoppingCart_DB
```

### 2. Setup Database
Create the database:
```sql
CREATE DATABASE shopping_cart_localization;
```

Run schema:
```bash
mysql -u root -p shopping_cart_localization < schema.sql
```

### 3. Configure Database Connection
Set environment variables:

```bash
export DB_URL=jdbc:mysql://localhost:3306/shopping_cart_localization
export DB_USER=shopping_user
export DB_PASSWORD=your_password
```

### 4. ## Run Locally
```bash
mvn clean package
mvn javafx:run
```

---

## Localization Implementation

The application uses a LocalizationService class to fetch translations from the database.

Example:

```java
```

If a translation is not available, the system falls back to English.

---
## Run Tests
```bash
mvn test
```

## 🐳 Docker

Build Docker image:
```bash
# Build
docker build -t shoppingcartgui .

# Run
docker run -it shoppingcartgui

docker login

# Push to Docker Hub
docker tag shopping-cart 218468/shoppingcartgui:latest
docker push 218468/shoppingcartgui:latest
```
## Pull and Run from Docker Hub
```bash
docker pull 218468/shoppingcartgui:latest
docker run -it 218468/shoppingcartgui
```

Run container:
```bash
docker run -p 8080:8080 shopping-cart-app
```

---

## 🔄 Jenkins

This project includes a Jenkinsfile for:
- Build automation
- Continuous Integration
- Pipeline execution

---



---

## 👤 Author
Swostika Lama

---

## License
This project is for educational purposes.
