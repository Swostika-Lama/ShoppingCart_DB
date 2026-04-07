# 🛒 Shopping Cart App with Database‑Driven Localization

A JavaFX Shopping Cart application with **full database‑driven localization**, supporting multiple languages including **right‑to‑left Arabic**.  
All UI text is stored in the database and loaded dynamically at runtime — no `.properties` files, no hardcoded strings.

This project demonstrates:

- Clean JavaFX architecture
- DAO‑based database access
- Dynamic UI translation
- RTL layout switching
- Shopping cart calculation
- Docker + Jenkins integration

---

## 🌍 Features

### **Localization**
- English (en‑US)
- Finnish (fi‑FI)
- Swedish (sv‑SE)
- Japanese (ja‑JP)
- Arabic (ar‑AR) — **RTL layout supported**

### **Functionality**
- Enter number of items
- Dynamically generate input fields
- Enter price + quantity
- Calculate subtotal + total
- Save cart history to database
- Automatic fallback to English if translation is missing
- Clean MVC architecture

---

## 🧠 How Localization Works (Database‑Driven)

Unlike typical JavaFX apps that use `.properties` files, this project loads all UI text from the database.

### **Flow**
1. UI requests a translation key
2. `LocalizationService` queries the database
3. If translation exists → return it
4. If missing → fallback to English
5. If still missing → return `[key]` for debugging
6. Controller updates UI labels dynamically

### **Why this is better**
- Add new languages without touching code
- Update translations without rebuilding the app
- Centralized translation management
- Supports RTL languages like Arabic

---

## Arabic RTL (Right‑to‑Left) Support

When Arabic is selected:

```java
rootPane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
```

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
Seed initial data:
```bashbash
mysql -u root -p shopping_cart_localization < seed_data.sql
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
docker tag shoppingcartgui 218468/shoppingcartgui:latest
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



## 👤 Author
Swostika Lama

---

## License
This project is for educational purposes.
