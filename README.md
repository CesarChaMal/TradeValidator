# Trade Validator

A full-stack application for validating financial trade data with a Spring Boot backend and React frontend.

## Architecture

- **Backend**: Spring Boot REST API with trade validation logic
- **Frontend**: React application for user interface
- **Database**: Redis for session management
- **Build**: Gradle multi-module project
- **Deployment**: Docker containerization

## Features

### Trade Validation Rules
- **Date Validation**: Value date cannot be before trade date
- **Weekend Validation**: Trade dates cannot fall on weekends
- **Currency Validation**: ISO 4217 currency code validation
- **Customer Validation**: Customer existence verification
- **Style Validation**: Option style validation (American/European)
- **Exercise Date Validation**: Exercise start date validation for American options
- **Expiry/Premium Date Validation**: Date consistency checks

### API Endpoints
- `POST /api/validatetrades` - Validates JSON array of trade data

## Project Structure

```
TradeValidator/
├── backend/                 # Spring Boot application
│   ├── src/main/java/
│   │   └── com/creditsuisse/trader/
│   │       ├── api/         # REST controllers
│   │       ├── config/      # Configuration classes
│   │       ├── model/       # Data models and validators
│   │       └── util/        # Utility classes
│   └── build.gradle         # Backend dependencies
├── frontend/                # React application
│   ├── src/                 # React components
│   └── package.json         # Frontend dependencies
├── docker-compose.yml       # Multi-container setup
└── settings.gradle          # Multi-module configuration
```

## Prerequisites

- Java 8+
- Node.js 6.10+
- Docker (optional)
- Redis (for production)

## Quick Start

### Development Mode

1. **Install dependencies**:
   ```bash
   gradlew frontend:yarn_install
   ```

2. **Start frontend** (development server):
   ```bash
   gradlew frontend:start
   ```

3. **Start backend**:
   ```bash
   gradlew bootRun
   ```

### Production Build

1. **Build application**:
   ```bash
   gradlew build
   ```

2. **Create Docker image**:
   ```bash
   gradlew backend:buildDocker
   ```

3. **Run with Docker Compose**:
   ```bash
   docker-compose up -d
   ```

## API Usage

### Validate Trades

**Endpoint**: `POST /api/validatetrades`

**Request Body** (JSON array):
```json
[
  {
    "customer": "PLUTO1",
    "ccyPair": "EURUSD",
    "type": "Spot",
    "direction": "BUY",
    "tradeDate": "2016-08-11",
    "amount1": 1000000.00,
    "amount2": 1120000.00,
    "rate": 1.12,
    "valueDate": "2016-08-15",
    "legalEntity": "CS Zurich",
    "trader": "Johann Baumfiddler"
  }
]
```

**Success Response**:
```
Validation Successful :: No error found in trade data
```

**Error Response** (JSON array):
```json
[
  {
    "ErrorType": "valueDateNotbeforeTradeDate",
    "TradeNumber": 1
  }
]
```

## Testing

- **Run all tests**:
  ```bash
  gradlew check
  ```

- **Frontend tests**:
  ```bash
  gradlew frontend:test
  ```

## Configuration

### Application Properties
- Development: Embedded session configuration
- Production: External Redis configuration in `application.yml`

### Docker Configuration
- Application runs on port 8080
- Redis runs on port 6379
- Uses `cesarchamal/trader-validator` image

## Technology Stack

### Backend
- Spring Boot 1.5.4
- Spring Security
- Spring Session
- Redis
- Swagger UI
- Spock/Groovy (testing)

### Frontend
- React 15
- Redux
- Webpack
- Babel
- Stylus

### Build & Deployment
- Gradle 4.2
- Docker
- Docker Compose

## Development Notes

- The application uses a chain of responsibility pattern for trade validation
- Frontend is bundled into the backend JAR for single deployment
- Swagger UI available for API documentation
- Session management via Redis in production

## Author

Cesar Chavez
