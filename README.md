# PlateProfitAI

AI-powered restaurant profitability assistant.

The application will help restaurant owners manage dishes, recipes, ingredients, inventory, sales, expenses, and profitability analytics. Demand prediction and inventory recommendations will be added later as a separate AI service.

## Technology Stack

- Frontend: React, Vite, Tailwind CSS, Axios, React Router, Recharts
- Backend: Java, Spring Boot, Spring Web, Spring Security, JWT, Spring Data JPA, Hibernate, Maven
- Database: PostgreSQL hosted on Supabase
- Storage: Supabase Storage for bills, invoices, and receipts
- Future AI service: Python, FastAPI, Pandas, NumPy, and scikit-learn

## Project Structure

```text
PlateProfitAI/
├── backend/       # Spring Boot REST API
├── frontend/      # React + Vite web application
└── README.md
```

## Current Step

This repository is being built incrementally. The initial setup includes the application shells only.

- Spring Boot backend foundation is being created.
- React + Vite frontend foundation is being created.
- Database tables have not been created yet.
- Database credentials and connections have not been configured yet.
- The Python AI/ML service has not been created yet.
- Business entities, authentication flows, APIs, and dashboard features will be added in later steps.

## Prerequisites

Install the following software:

- Java 21 or newer
- Maven 3.9 or newer
- Node.js 20 or newer
- npm
- PostgreSQL or a Supabase project will be needed in a later database step

Check the local installations:

```powershell
java -version
mvn -version
node --version
npm --version
```

## Run the Backend

From the repository root:

```powershell
cd backend
mvn spring-boot:run
```

The backend will run at `http://localhost:8080` once its initial security configuration is added.

To build the backend without running it:

```powershell
cd backend
mvn package
```

## Run the Frontend

From the repository root:

```powershell
cd frontend
npm install
npm run dev
```

Vite will print the local development URL, normally `http://localhost:5173`.

To create a production build:

```powershell
cd frontend
npm run build
```

## Incremental Development Order

1. Create and verify the project shells.
2. Design the PostgreSQL database schema before creating tables.
3. Implement the backend entities and repositories.
4. Add authentication and restaurant-owner access control.
5. Build dish, recipe, ingredient, inventory, sales, and expense APIs.
6. Build the frontend screens against the verified APIs.
7. Add profitability calculations and dashboard analytics.
8. Add the separate AI/ML service after the core application is working.

## Development Notes

Do not commit secrets such as Supabase keys, JWT signing keys, or database passwords. Use environment variables or local, ignored configuration files when those integrations are introduced.
