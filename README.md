# Trade Validator

A full-stack application for validating financial trade data with a Spring Boot backend and React frontend.

## Architecture

- **Backend**: Spring Boot REST API with trade validation logic
- **Frontend**: React application for user interface
- **Database**: Redis for session management
- **Build**: Gradle multi-module project
- **Deployment**: Docker containerization

## Features

### Reactive Processing
- **RxJava Integration**: Handles thousands of trades efficiently using reactive streams
- **Parallel Processing**: Concurrent validation for improved performance
- **Bulk Mode**: Automatic reactive processing for datasets > 100 trades
- **Performance Metrics**: Processing time measurement and reporting

### Trade Validation Rules
- **Date Validation**: Value date cannot be before trade date
- **Weekend Validation**: Trade dates cannot fall on weekends
- **Currency Validation**: ISO 4217 currency code validation
- **Customer Validation**: Customer existence verification
- **Style Validation**: Option style validation (American/European)
- **Exercise Date Validation**: Exercise start date validation for American options
- **Expiry/Premium Date Validation**: Date consistency checks

### API Endpoints
- `POST /api/validatetrades` - Validates JSON array of trade data (standard)
- `POST /api/validatetrades/bulk` - Reactive validation for large datasets
- `GET /api/generate-test-data?count=N` - Generates test data (up to 5,000 trades)

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

### Production Mode (Default)

1. **Build and run** (requires Redis):
   ```bash
   gradlew build
   gradlew bootRun
   ```

2. **Run with Docker Compose**:
   ```bash
   docker-compose up -d
   ```

### Development Mode

1. **Install dependencies**:
   ```bash
   gradlew frontend:yarn_install
   ```

2. **Start backend** (development profile):
   ```bash
   gradlew bootRun -Dspring.profiles.active=development
   ```

3. **Start frontend** (separate development server):
   ```bash
   gradlew frontend:start
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
- **Production (Default)**: Redis session store, requires Redis server
- **Development Profile**: No session store, embedded configuration with live reload

### Docker Configuration
- Application runs on port 8080
- Redis runs on port 6379
- Uses `cesarchamal/trader-validator` image

## Technology Stack

### Backend
- Spring Boot 2.3.12
- Spring Security
- Spring Session
- Redis
- RxJava 3.1.5 (Reactive Processing)
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

- **Reactive Architecture**: Uses RxJava for processing large trade datasets
- **Chain of Responsibility**: Trade validation pattern for extensible rules
- **Parallel Processing**: Concurrent validation using RxJava schedulers
- **Test Data Generation**: Built-in generator for performance testing
- **Frontend Bundling**: React app bundled into Spring Boot JAR
- **Swagger Documentation**: API documentation available
- **Session Management**: Redis-based sessions in production

## Performance Testing

### Generate Test Data
```bash
curl "http://localhost:8080/api/generate-test-data?count=1000" > test-trades.json
```

### Bulk Validation
```bash
curl -X POST -H "Content-Type: application/json" \
  -d @test-trades.json \
  http://localhost:8080/api/validatetrades/bulk
```

### Performance Benchmarks
- **Small datasets (< 100 trades)**: Standard validation
- **Large datasets (100+ trades)**: Reactive bulk validation
- **Concurrent processing**: RxJava parallel streams
- **Timeout protection**: 30-second timeout for bulk operations

## Author

Cesar Chavez
