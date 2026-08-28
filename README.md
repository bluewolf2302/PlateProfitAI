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
├── backend/       # Spring Boot REST API and AI proxy
├── frontend/      # React + Vite web application
├── ai-service/    # FastAPI demand forecasting service
└── README.md
```

## Current Step

This repository is being built incrementally. The current backend step includes the persistence model and API boundaries only.

- Spring Boot backend foundation, JPA entities, repositories, DTOs, and API scaffolds are present.
- React + Vite frontend workspace with routed pages and live API loading states is present.
- Database tables have not been created yet. Hibernate is configured with `ddl-auto=validate` for safety.
Database credentials are read from environment variables and are not stored in the repository.
- FastAPI AI service currently supports prototype demand prediction with a sparse-history baseline fallback.
- Business entities, authentication flows, APIs, and dashboard features will be added in later steps.

## Prerequisites


An example template is available at [backend/.env.example](backend/.env.example). Spring Boot does not load `.env` files automatically, so either export the variables in PowerShell as shown above or configure them in your IDE's run configuration. Never commit a populated `.env` file.
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

## Backend Environment Variables

Before starting the backend, configure these variables in the terminal or your IDE run configuration:

```powershell
$env:SUPABASE_DB_URL = "jdbc:postgresql://<host>:5432/postgres?sslmode=require"
$env:SUPABASE_DB_USERNAME = "<database-user>"
$env:SUPABASE_DB_PASSWORD = "<database-password>"
```

The backend uses `spring.jpa.hibernate.ddl-auto=validate`, so it will not create, update, or delete database tables. Database migrations will be introduced explicitly after the schema is reviewed.

## Run the Backend

From the repository root:

```powershell
cd backend
mvn spring-boot:run
```

The backend will run at `http://localhost:8080`. Authentication and JWT security are intentionally not included yet.

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

The frontend reads `VITE_API_BASE_URL` from `frontend/.env.example`. It does not include mock business data. Pages show loading, empty, or unavailable states when the backend has no records or is offline.

## Run the AI Service

Python 3.11 or newer is recommended.

```powershell
cd ai-service
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8001
```

The AI service exposes `GET /health` and `POST /api/ai/demand/predict`. It returns a baseline estimate when fewer than seven matching observations are supplied and clearly labels that result. Set `AI_SERVICE_URL=http://localhost:8001` for the Spring Boot proxy.

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
