# CLAUDE.md - AI Assistant Guide for TradeValidator

## Project Overview

**TradeValidator** is a production-grade full-stack financial application for validating trade data with reactive processing capabilities. The system validates thousands of trades efficiently using RxJava3 and provides comprehensive validation rules for various trade types (Spot, Forward, VanillaOption).

**Tech Stack:**
- **Backend**: Spring Boot 2.3.12 (Java 8), RxJava 3.1.5, Spring Security, Spring Session, Redis
- **Frontend**: React 15, Redux, Webpack, Babel, Stylus
- **Build**: Gradle 4.2 multi-module project
- **Testing**: JUnit 4, Spock Framework 1.3, Mocha
- **Deployment**: Docker, Docker Compose
- **API Documentation**: Swagger UI

**Key Features:**
- Reactive processing for large datasets (100+ trades)
- Chain of Responsibility validation pattern
- 7 comprehensive validation rules
- Test data generator (up to 500K trades)
- Session-based authentication
- Production-ready containerization

---

## Directory Structure

```
TradeValidator/
├── backend/                          # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/creditsuisse/trader/
│   │   │   │   ├── api/             # REST Controllers
│   │   │   │   │   ├── BulkValidationController.java    # Reactive bulk validation
│   │   │   │   │   ├── ValidateTrades.java              # Standard validation + test data gen
│   │   │   │   │   ├── AuthenticationResource.java      # Session management
│   │   │   │   │   └── AuthenticationExceptionHandler.java
│   │   │   │   ├── config/          # Spring Configuration
│   │   │   │   │   ├── SecurityConfiguration.java       # Security + CORS
│   │   │   │   │   ├── SwaggerConfig.java               # API docs
│   │   │   │   │   ├── SinglePageAppConfig.java         # SPA routing
│   │   │   │   │   └── redis/                           # Session config
│   │   │   │   ├── model/           # Domain models
│   │   │   │   │   ├── User.java, Credentials.java
│   │   │   │   │   └── validator/   # Validation chain
│   │   │   │   │       ├── IValidator.java              # Validator interface
│   │   │   │   │       ├── Validator.java               # Entry point
│   │   │   │   │       ├── ChainofValidators.java       # Orchestrator
│   │   │   │   │       ├── BeforeDateValidator.java
│   │   │   │   │       ├── WeekendValidator.java
│   │   │   │   │       ├── ISO4217Validator.java
│   │   │   │   │       ├── CustomerValidator.java
│   │   │   │   │       ├── StyleValidator.java
│   │   │   │   │       ├── ExcerciseStartDateValidator.java
│   │   │   │   │       └── ExpiryAndPrimiumDateValidator.java
│   │   │   │   ├── service/         # Business services
│   │   │   │   │   └── ReactiveTradeValidationService.java  # RxJava3 reactive processing
│   │   │   │   └── util/            # Utility classes
│   │   │   │       └── Utility.java # Date/currency helpers
│   │   │   └── resources/           # Configuration files
│   │   │       └── application.yml  # Spring profiles
│   │   └── test/
│   │       ├── groovy/              # Spock/Groovy tests
│   │       │   └── com/creditsuisse/trader/
│   │       │       ├── api/         # API integration tests
│   │       │       │   └── AuthenticationResourceSpec.groovy
│   │       │       └── auth/
│   │       │           └── AuthenticationSpec.groovy
│   │       ├── java/                # JUnit tests
│   │       │   └── com/creditsuisse/trader/
│   │       │       ├── CreditSuisseApplicationTests.java
│   │       │       ├── ReactiveValidationTests.java
│   │       │       ├── UtilityTests.java
│   │       │       └── validation/
│   │       │           └── TradeValidationTests.java  # Comprehensive validation tests
│   │       └── resources/
│   │           └── application-test.yml  # Test configuration
│   ├── build.gradle                 # Backend build configuration
│   └── Dockerfile                   # Container image definition
├── frontend/                         # React application
│   ├── src/
│   │   ├── App.js                   # Main component (439 lines)
│   │   ├── index.js                 # Entry point
│   │   └── registerServiceWorker.js # PWA support
│   ├── assets/stylus/               # Stylus CSS
│   ├── test/                        # Mocha tests
│   ├── build.gradle                 # Frontend build (Node/Yarn)
│   ├── package.json                 # NPM dependencies
│   ├── webpack.config.js            # Production webpack config
│   ├── webpack.dev.config.js        # Development webpack config
│   └── server.js                    # Development server
├── .editorconfig                     # Code style enforcement
├── .gitignore                        # Git ignore patterns
├── docker-compose.yml                # Multi-container setup
├── settings.gradle                   # Multi-module project config
├── gradlew / gradlew.bat            # Gradle wrapper
└── README.md                         # Project documentation
```

---

## Key Technologies & Dependencies

### Backend (backend/build.gradle)

| Dependency | Version | Purpose |
|------------|---------|---------|
| Spring Boot | 2.3.12.RELEASE | Application framework |
| Spring Web | - | REST API |
| Spring Security | - | Authentication & authorization |
| Spring Session | - | Session management |
| Spring Data Redis | - | Redis integration |
| RxJava | 3.1.5 | Reactive programming |
| Springfox | 3.0.0 | Swagger/OpenAPI documentation |
| Groovy | - | Spock test support |
| org.json | 20170516 | JSON manipulation |
| Spock Framework | 1.3-groovy-2.5 | BDD testing |

### Frontend (frontend/package.json)

| Dependency | Version | Purpose |
|------------|---------|---------|
| React | 15.x | UI framework |
| Redux | 3.x | State management |
| React Router | 3.x | Client-side routing |
| Webpack | 1.x | Module bundler |
| Babel | 6.x | ES6+ transpilation |
| Stylus | 0.54.x | CSS preprocessing |
| Axios | 0.15.x | HTTP client |
| Mocha | 3.x | Testing framework |

---

## Development Setup

### Prerequisites

- **Java**: JDK 8+ (Java 1.8 compatibility)
- **Node.js**: 14.21.3 (auto-downloaded by Gradle Node plugin)
- **Redis**: Required for production mode (optional in development)
- **Docker**: Optional, for containerized deployment
- **Gradle**: Use included wrapper (`./gradlew`)

### Initial Setup

```bash
# Clone the repository
git clone <repository-url>
cd TradeValidator

# Build the entire project (downloads Node.js, installs dependencies, builds frontend + backend)
./gradlew build

# Run tests
./gradlew check
```

### Development Workflows

#### Option 1: Full-Stack Development (Recommended for most work)

```bash
# Start backend in development mode (no Redis required)
./gradlew bootRun -Dspring.profiles.active=development

# Access the application at http://localhost:8080
```

This mode:
- Bundles the React frontend into the JAR
- Serves frontend from `/static`
- No Redis required (in-memory sessions)
- Spring DevTools enabled (hot reload for backend)

#### Option 2: Separate Frontend Development (For UI-heavy work)

```bash
# Terminal 1: Start backend
./gradlew :backend:bootRun -Dspring.profiles.active=development

# Terminal 2: Start frontend dev server with hot reload
./gradlew :frontend:start

# Frontend: http://localhost:3000 (with hot module replacement)
# Backend API: http://localhost:8080
```

This mode provides:
- Hot module replacement for React
- Instant UI updates without backend restart
- Proxied API calls to backend

#### Option 3: Docker Compose (Production-like environment)

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

This mode:
- Runs app + Redis in containers
- Production configuration
- Port 8080 for app, 6379 for Redis

---

## Build & Deployment

### Build Commands

```bash
# Full build (backend + frontend)
./gradlew build

# Backend only
./gradlew :backend:build

# Frontend bundle only
./gradlew :frontend:bundle

# Clean build
./gradlew clean build

# Build Docker image
./gradlew :backend:buildDocker
```

### Build Process

1. **Frontend Build**:
   - Node.js 14.21.3 downloaded automatically (if not present)
   - Yarn installs dependencies
   - Webpack bundles React app (production mode: minified, source maps)
   - Output: `frontend/dist/`

2. **Backend Build**:
   - Compiles Java sources (Java 8 target)
   - Compiles Groovy test sources
   - Runs all tests (JUnit + Spock)
   - Copies frontend `dist/` into JAR at `/static`
   - Creates executable JAR: `backend/build/libs/com.creditsuisse.trader-0.0.1-SNAPSHOT.jar`

3. **Docker Build**:
   - Uses Palantir Docker plugin
   - Base: OpenJDK 8 JRE Slim
   - Copies JAR into image
   - Creates: `cesarchamal/trader-validator` image

### Running the Application

```bash
# Option 1: Standalone JAR
java -jar backend/build/libs/com.creditsuisse.trader-0.0.1-SNAPSHOT.jar

# Option 2: Gradle bootRun
./gradlew :backend:bootRun

# Option 3: Docker
docker run -p 8080:8080 cesarchamal/trader-validator

# Option 4: Docker Compose (includes Redis)
docker-compose up
```

---

## Testing Strategy

### Test Structure

```
backend/src/test/
├── groovy/                          # Spock (BDD-style) tests
│   └── com/creditsuisse/trader/
│       ├── AbstractMvcSpec.groovy   # Base class for MVC tests
│       ├── api/
│       │   └── AuthenticationResourceSpec.groovy  # Auth endpoint tests
│       └── auth/
│           └── AuthenticationSpec.groovy          # Auth integration tests
└── java/                            # JUnit tests
    └── com/creditsuisse/trader/
        ├── CreditSuisseApplicationTests.java      # Context loading
        ├── ReactiveValidationTests.java           # Reactive service tests
        ├── UtilityTests.java                      # Utility class tests
        └── validation/
            └── TradeValidationTests.java          # Comprehensive validation tests
```

### Running Tests

```bash
# All tests (backend + frontend)
./gradlew check

# Backend tests only
./gradlew :backend:test

# Frontend tests only
./gradlew :frontend:test

# Specific test class
./gradlew :backend:test --tests TradeValidationTests

# With detailed output
./gradlew test --info
```

### Test Frameworks

**JUnit 4** - Standard unit tests
- Individual validator testing
- Service layer testing
- Utility method testing
- Performance benchmarks

**Spock Framework 1.3** - BDD-style integration tests
- `AbstractMvcSpec` provides MockMvc with Spring Security
- `given-when-then` structure
- API endpoint testing
- Authentication flows

**Mocha** - Frontend JavaScript tests
- React component testing
- Configured in `frontend/test/mocha.opts`

### Test Coverage Areas

- Valid trade validation (Spot, Forward, VanillaOption)
- Invalid customer validation
- Date validation (value date before trade date)
- Weekend validation (trade date and value date)
- Currency validation (ISO 4217)
- Option style validation (AMERICAN/EUROPEAN)
- Exercise date validation (American options)
- Expiry/premium date validation
- Multiple trades validation
- Reactive bulk validation
- Performance testing (100 trades in <5 seconds)
- Authentication (good/bad credentials)

### Test Configuration

**Test Profile** (`application-test.yml`):
- Profile: `test`
- Bean overriding: Allowed
- Session autoconfiguration: Disabled
- Default user: `user/password` with `USER,ADMIN` roles
- In-memory session repository
- NoOp password encoder

**Test Config Class** (`TestConfig.java`):
- In-memory `UserDetailsService`
- Map-based session repository
- Header-based session ID resolver

---

## API Endpoints

### Validation Endpoints

#### POST /api/validatetrades
**Standard validation** - For small to medium datasets (<100 trades recommended)

**Request:**
```json
POST /api/validatetrades
Content-Type: application/json

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

**Success Response:**
```
Validation Successful :: No error found in trade data
```

**Error Response:**
```json
[
  {
    "ErrorType": "valueDateNotbeforeTradeDate",
    "TradeNumber": 1
  },
  {
    "ErrorType": "tradeDateFallsOnWeekend",
    "TradeNumber": 2
  }
]
```

**Controller**: `ValidateTrades.java:47`

---

#### POST /api/validatetrades/bulk
**Reactive bulk validation** - Optimized for large datasets (100+ trades)

**Request:** Same as standard endpoint (JSON array of trades)

**Response:** Same format as standard endpoint

**Features:**
- Parallel processing using RxJava3
- `Schedulers.computation()` for CPU-bound validation
- 30-second timeout protection
- Performance metrics in response

**Controller**: `BulkValidationController.java:21`
**Service**: `ReactiveTradeValidationService.java:15`

---

#### GET /api/generate-test-data?count=N
**Test data generator** - Generates random trade data for testing

**Request:**
```
GET /api/generate-test-data?count=1000
```

**Parameters:**
- `count`: Number of trades to generate (max: 5000)

**Response:** JSON array of generated trades

**Usage:**
```bash
# Generate 1000 trades and save to file
curl "http://localhost:8080/api/generate-test-data?count=1000" > test-trades.json

# Validate using bulk endpoint
curl -X POST -H "Content-Type: application/json" \
  -d @test-trades.json \
  http://localhost:8080/api/validatetrades/bulk
```

**Controller**: `ValidateTrades.java:73`

---

### Authentication Endpoints

#### POST /api/session
**Login** - Create authenticated session

**Request:**
```json
{
  "username": "user",
  "password": "password"
}
```

**Response:**
```json
{
  "username": "user",
  "token": "<session-token>",
  "authenticated": true
}
```

**Controller**: `AuthenticationResource.java:34`

---

#### GET /api/session
**Check session** - Verify current authentication status

**Response:**
```json
{
  "username": "user",
  "token": null,
  "authenticated": true
}
```

**Controller**: `AuthenticationResource.java:47`

---

#### DELETE /api/session
**Logout** - Invalidate current session

**Controller**: `AuthenticationResource.java:54`

---

### API Documentation

**Swagger UI**: http://localhost:8080/swagger-ui/index.html

Configuration: `SwaggerConfig.java:14`

---

## Code Organization & Conventions

### Design Patterns

#### 1. Chain of Responsibility (Validation)

**Location**: `backend/src/main/java/com/creditsuisse/trader/model/validator/`

**Structure:**
```
IValidator (interface)
  ↓
Validator (entry point) → ChainofValidators (orchestrator)
  ↓
BeforeDateValidator → WeekendValidator → ISO4217Validator → CustomerValidator
  → StyleValidator → ExcerciseStartDateValidator → ExpiryAndPrimiumDateValidator
```

**How it works:**
1. `Validator.startValidation()` initiates the chain
2. `ChainofValidators` executes each validator sequentially
3. Each validator implements `IValidator.processValidation()`
4. Errors accumulated in shared `JSONArray`
5. Returns `true` if valid, `false` if errors found

**Adding a new validator:**
```java
// 1. Create validator implementing IValidator
public class MyNewValidator implements IValidator {
    private String message;

    @Override
    public boolean processValidation(JSONObject trade, int tradeNumber) {
        // Validation logic
        if (invalid) {
            setMessage("myErrorType");
            return false;
        }
        return true;
    }

    @Override
    public String getMessage() { return message; }

    @Override
    public void setMessage(String message) { this.message = message; }
}

// 2. Add to ChainofValidators.java
public boolean processValidation(JSONObject trade, int tradeNumber) {
    // ... existing validators ...

    MyNewValidator myValidator = new MyNewValidator();
    if (!myValidator.processValidation(trade, tradeNumber)) {
        validationMessages.put(createErrorObject(myValidator.getMessage(), tradeNumber));
    }

    return validationMessages.length() == 0;
}
```

---

#### 2. Reactive Pattern (RxJava3)

**Location**: `ReactiveTradeValidationService.java:15`

**Architecture:**
```
Observable.fromCallable(() -> {...})                    # Create observable
  .subscribeOn(Schedulers.io())                         # I/O thread for JSON parsing
  .timeout(30, TimeUnit.SECONDS)                        # 30s timeout protection
    └── Observable.range(0, trades.length())            # Create stream of trade indices
          .flatMap(i -> Observable.fromCallable(() -> {  # Parallel processing
                Validator.startValidation(trade)        # Validate each trade
              })
              .subscribeOn(Schedulers.computation())    # CPU-bound work
          )
          .blockingSubscribe(messages -> {...})         # Collect results
```

**Key features:**
- **Parallel processing**: `flatMap` with `Schedulers.computation()`
- **Timeout protection**: 30-second limit prevents hanging
- **Error handling**: Reactive error propagation
- **Performance**: Processes 100 trades in <5 seconds

**When to use:**
- Large datasets (100+ trades)
- CPU-intensive validation
- Need for timeout protection
- Performance-critical operations

---

#### 3. REST API Pattern

**Structure:**
```
Controller (API layer) → Service (business logic) → Model/Validator (domain)
```

**Example flow:**
```
BulkValidationController.validateTradesBulk()
  → ReactiveTradeValidationService.validateTradesReactive()
      → Validator.startValidation()
          → ChainofValidators.processValidation()
              → [7 validators execute]
```

**Convention:**
- Controllers: Thin, delegate to services
- Services: Business logic, orchestration
- Models/Validators: Domain logic, no external dependencies

---

#### 4. Single Page Application (SPA)

**Configuration**: `SinglePageAppConfig.java:12`

**How it works:**
- Frontend bundled into `/static` in JAR
- All non-API routes forward to `index.html`
- Client-side routing handled by React Router
- API routes prefixed with `/api/**`

---

### Code Style Conventions

**EditorConfig** (`.editorconfig`):
```ini
[*]
charset = utf-8
end_of_line = lf
indent_style = space
indent_size = 2
insert_final_newline = true
trim_trailing_whitespace = true
```

**Naming Conventions:**
- **Controllers**: Resource-based (`ValidateTrades`, `AuthenticationResource`)
- **Services**: Domain + "Service" (`ReactiveTradeValidationService`)
- **Validators**: Rule + "Validator" (`BeforeDateValidator`)
- **Tests**: Class + "Tests" or Class + "Spec" (Spock)
- **Packages**: Lowercase, domain-driven (`api`, `model`, `service`, `config`)

**Java Conventions:**
- Source/target: Java 8 (1.8)
- 2-space indentation
- UTF-8 encoding
- LF line endings

**JSON Conventions:**
- Error objects: `{ "ErrorType": "errorName", "TradeNumber": N }`
- Success messages: Plain text strings
- Trade fields: camelCase

---

### Validation Rules Reference

| Validator | Applies To | Rule | Error Type |
|-----------|-----------|------|------------|
| **BeforeDateValidator** | Spot, Forward | `valueDate` must not be before `tradeDate` | `valueDateNotbeforeTradeDate` |
| **WeekendValidator** | All types | `tradeDate` and `valueDate` must be weekdays | `tradeDateFallsOnWeekend`, `valueDateFallsOnWeekend` |
| **ISO4217Validator** | VanillaOption | `payCcy` and `premiumCcy` must be valid ISO 4217 codes | `invalidCurrencyCode` |
| **CustomerValidator** | All types | `customer` must be `PLUTO1` or `PLUTO2` | `invalidCustomer` |
| **StyleValidator** | VanillaOption | `style` must be `AMERICAN` or `EUROPEAN` | `invalidStyle` |
| **ExcerciseStartDateValidator** | American options | `tradeDate ≤ excerciseStartDate < expiryDate` | `invalidExcerciseStartDate` |
| **ExpiryAndPrimiumDateValidator** | VanillaOption | `expiryDate < deliveryDate` AND `premiumDate < deliveryDate` | `invalidExpiryDate`, `invalidPremiumDate` |

**Implementation**: `backend/src/main/java/com/creditsuisse/trader/model/validator/`

---

## Configuration Management

### Spring Profiles

#### Default (Production)
**File**: `application.yml` (no profile specified)

```yaml
spring:
  session:
    store-type: redis
  redis:
    host: localhost
    port: 6379
```

**Characteristics:**
- Redis session store required
- External Redis connection
- No DevTools
- Production-ready

**Start:**
```bash
./gradlew bootRun
# or
java -jar backend/build/libs/com.creditsuisse.trader-0.0.1-SNAPSHOT.jar
```

---

#### Development Profile
**File**: `application.yml` (profile: development)

```yaml
spring:
  profiles: development
  devtools:
    livereload:
      enabled: true
  session:
    store-type: none
```

**Characteristics:**
- No Redis required (in-memory sessions)
- Spring DevTools enabled
- LiveReload for auto-restart
- Frontend can run separately on port 3000

**Start:**
```bash
./gradlew bootRun -Dspring.profiles.active=development
```

---

#### Test Profile
**File**: `application-test.yml`

```yaml
spring:
  profiles:
    active: test
  main:
    allow-bean-definition-overriding: true
  autoconfigure:
    exclude: org.springframework.boot.autoconfigure.session.SessionAutoConfiguration
```

**Characteristics:**
- Bean overriding allowed
- Session autoconfiguration disabled
- Map-based session repository (in-memory)
- Default test user: `user/password`
- NoOp password encoder

**Automatically activated** when running tests.

---

### Security Configuration

**Location**: `SecurityConfiguration.java:18`

**In-Memory Users** (NOT for production):
```java
User.withUsername("user").password("password").roles("USER").build()
User.withUsername("admin").password("admin").roles("ADMIN").build()
```

**Public Endpoints** (no authentication required):
- `/api/validatetrades`
- `/api/validatetrades/bulk`
- `/api/generate-test-data`
- `/api/test`
- Swagger UI: `/swagger-ui/**`, `/v2/api-docs`, `/swagger-resources/**`

**Protected Endpoints**:
- `/api/session` (authentication endpoints themselves)

**CORS Configuration** (`CorsConfig.java:11`):
- Allowed origins: `http://localhost:3000`
- Allowed methods: GET, POST, PUT, DELETE, OPTIONS
- Allowed headers: All
- Allow credentials: true

**Important**: Replace in-memory authentication with a proper auth system for production.

---

### Redis Configuration

**Production** (`ExternalRedisConfig.java:17`):
```java
@Profile("default")  // Active when no profile specified
@EnableRedisHttpSession
```
- Connects to external Redis server
- Host/port/password from `application.yml`

**Development** (`EmbeddedSessionConfig.java:12`):
```java
@Profile("development")
@EnableRedisHttpSession
```
- Uses embedded Redis server
- Port: 6370 (not 6379 to avoid conflicts)
- Automatically started with the application

**Test**:
- No Redis (session autoconfiguration disabled)
- Map-based session repository

---

## Common Development Tasks

### Task: Add a New Validation Rule

**Steps:**

1. **Create validator class**:
   ```bash
   # Location: backend/src/main/java/com/creditsuisse/trader/model/validator/
   ```

   ```java
   package com.creditsuisse.trader.model.validator;

   import org.json.JSONException;
   import org.json.JSONObject;

   public class MyNewValidator implements IValidator {
       private String message;

       @Override
       public boolean processValidation(JSONObject trade, int tradeNumber) throws JSONException {
           // Example: Validate amount is positive
           double amount = trade.optDouble("amount1", 0);

           if (amount <= 0) {
               setMessage("invalidAmount");
               return false;
           }

           return true;
       }

       @Override
       public String getMessage() {
           return message;
       }

       @Override
       public void setMessage(String message) {
           this.message = message;
       }
   }
   ```

2. **Add to validation chain**:
   ```java
   // Edit: ChainofValidators.java

   public boolean processValidation(JSONObject trade, int tradeNumber) throws JSONException {
       // ... existing validators ...

       // Add your new validator
       MyNewValidator myValidator = new MyNewValidator();
       if (!myValidator.processValidation(trade, tradeNumber)) {
           validationMessages.put(createErrorObject(myValidator.getMessage(), tradeNumber));
       }

       return validationMessages.length() == 0;
   }
   ```

3. **Write tests**:
   ```java
   // Edit: backend/src/test/java/com/creditsuisse/trader/validation/TradeValidationTests.java

   @Test
   public void testInvalidAmount() throws Exception {
       String trade = "[{\"customer\":\"PLUTO1\",\"amount1\":-1000,\"type\":\"Spot\"}]";

       String response = mockMvc.perform(post("/api/validatetrades")
               .contentType(MediaType.APPLICATION_JSON)
               .content(trade))
           .andExpect(status().isOk())
           .andReturn().getResponse().getContentAsString();

       assertTrue(response.contains("invalidAmount"));
   }
   ```

4. **Run tests**:
   ```bash
   ./gradlew :backend:test --tests TradeValidationTests
   ```

5. **Update documentation**:
   - Add rule to README.md validation rules section
   - Update this CLAUDE.md file

---

### Task: Add a New API Endpoint

**Steps:**

1. **Create controller method**:
   ```java
   // Edit: backend/src/main/java/com/creditsuisse/trader/api/ValidateTrades.java

   @GetMapping("/api/my-endpoint")
   public ResponseEntity<String> myEndpoint(@RequestParam String param) {
       // Implementation
       return ResponseEntity.ok("Result");
   }
   ```

2. **If endpoint should be public, update security config**:
   ```java
   // Edit: SecurityConfiguration.java

   .antMatchers("/api/my-endpoint").permitAll()
   ```

3. **Write integration test**:
   ```java
   // backend/src/test/java/com/creditsuisse/trader/api/MyEndpointTests.java

   @RunWith(SpringRunner.class)
   @SpringBootTest
   @AutoConfigureMockMvc
   public class MyEndpointTests {
       @Autowired
       private MockMvc mockMvc;

       @Test
       public void testMyEndpoint() throws Exception {
           mockMvc.perform(get("/api/my-endpoint")
                   .param("param", "value"))
               .andExpect(status().isOk());
       }
   }
   ```

4. **Test manually**:
   ```bash
   ./gradlew bootRun -Dspring.profiles.active=development
   curl "http://localhost:8080/api/my-endpoint?param=value"
   ```

5. **Update Swagger docs** (automatic via Spring annotations)

---

### Task: Modify Frontend UI

**Steps:**

1. **Edit React component**:
   ```bash
   # Location: frontend/src/App.js
   ```

2. **Start frontend dev server** (for hot reload):
   ```bash
   # Terminal 1: Backend
   ./gradlew :backend:bootRun -Dspring.profiles.active=development

   # Terminal 2: Frontend
   ./gradlew :frontend:start
   ```

3. **View changes**:
   - Frontend: http://localhost:3000 (hot reload enabled)
   - Backend: http://localhost:8080

4. **Build for production**:
   ```bash
   ./gradlew :frontend:bundle
   ```

5. **Test integrated build**:
   ```bash
   ./gradlew build
   ./gradlew :backend:bootRun
   # Access at http://localhost:8080 (frontend bundled in JAR)
   ```

---

### Task: Run Performance Tests

**Steps:**

1. **Start application**:
   ```bash
   ./gradlew bootRun -Dspring.profiles.active=development
   ```

2. **Generate test data**:
   ```bash
   # Generate 1,000 trades
   curl "http://localhost:8080/api/generate-test-data?count=1000" > trades-1k.json

   # Generate 10,000 trades
   curl "http://localhost:8080/api/generate-test-data?count=10000" > trades-10k.json
   ```

3. **Test standard endpoint** (small datasets):
   ```bash
   curl -X POST -H "Content-Type: application/json" \
     -d @trades-1k.json \
     http://localhost:8080/api/validatetrades
   ```

4. **Test bulk endpoint** (large datasets):
   ```bash
   curl -X POST -H "Content-Type: application/json" \
     -d @trades-10k.json \
     http://localhost:8080/api/validatetrades/bulk
   ```

5. **Run automated performance test**:
   ```bash
   ./gradlew :backend:test --tests ReactiveValidationTests.testBulkValidationPerformance
   ```
   - Validates 100 trades complete in <5 seconds
   - Uses reactive bulk validation

6. **Measure performance**:
   - Frontend displays processing time automatically
   - Check logs for detailed timing information

---

### Task: Add Frontend Dependencies

**Steps:**

1. **Edit package.json**:
   ```bash
   cd frontend
   # Edit package.json, add dependency
   ```

2. **Install dependency**:
   ```bash
   ./gradlew :frontend:yarn_install
   ```

3. **Use in React component**:
   ```javascript
   import NewLibrary from 'new-library';
   ```

4. **Test**:
   ```bash
   ./gradlew :frontend:start
   ```

---

### Task: Add Backend Dependencies

**Steps:**

1. **Edit build.gradle**:
   ```gradle
   // backend/build.gradle
   dependencies {
       implementation 'group:artifact:version'
   }
   ```

2. **Refresh Gradle**:
   ```bash
   ./gradlew --refresh-dependencies
   ```

3. **Use in code**:
   ```java
   import com.example.NewLibrary;
   ```

4. **Test**:
   ```bash
   ./gradlew :backend:build
   ```

---

## Troubleshooting

### Issue: Redis Connection Error on Startup

**Error:**
```
Error creating bean with name 'enableRedisKeyspaceNotificationsInitializer'
Cannot get Jedis connection
```

**Solution:**
Use development profile (no Redis required):
```bash
./gradlew bootRun -Dspring.profiles.active=development
```

Or start Redis:
```bash
docker run -d -p 6379:6379 redis:alpine
```

---

### Issue: Frontend Not Loading After Build

**Symptoms:**
- Backend starts successfully
- http://localhost:8080 shows blank page or 404

**Solution:**
1. Ensure frontend was built before backend:
   ```bash
   ./gradlew :frontend:clean :frontend:bundle
   ./gradlew :backend:build
   ```

2. Check JAR contents:
   ```bash
   jar tf backend/build/libs/com.creditsuisse.trader-0.0.1-SNAPSHOT.jar | grep static
   ```
   Should show `BOOT-INF/classes/static/` files

3. Rebuild from scratch:
   ```bash
   ./gradlew clean build
   ```

---

### Issue: Webpack Build Fails

**Error:**
```
Module not found: Error: Cannot resolve module 'xyz'
```

**Solution:**
1. Clean and reinstall dependencies:
   ```bash
   ./gradlew :frontend:clean
   rm -rf frontend/node_modules
   ./gradlew :frontend:yarn_install
   ```

2. Check Node.js version:
   ```bash
   ./gradlew :frontend:nodeVersion
   ```
   Should be 14.21.3

---

### Issue: Tests Fail with "Bean Definition Override" Error

**Error:**
```
The bean 'userDetailsService', defined in class path resource [...], could not be registered.
```

**Solution:**
This should not occur with test profile, but if it does:

1. Ensure test profile is active:
   ```yaml
   # application-test.yml
   spring:
     main:
       allow-bean-definition-overriding: true
   ```

2. Run tests with test profile explicitly:
   ```bash
   ./gradlew test -Dspring.profiles.active=test
   ```

---

### Issue: CORS Errors in Development

**Error (in browser console):**
```
Access to XMLHttpRequest at 'http://localhost:8080/api/validatetrades' from origin 'http://localhost:3000' has been blocked by CORS policy
```

**Solution:**
1. Check CORS configuration in `CorsConfig.java:11`:
   ```java
   .allowedOrigins("http://localhost:3000")
   ```

2. Ensure frontend dev server is running on port 3000:
   ```bash
   ./gradlew :frontend:start
   ```

3. If using different port, update `CorsConfig.java`

---

### Issue: Session Timeout Too Fast

**Symptoms:**
- User logged out after few minutes
- Session expires unexpectedly

**Solution:**
Add session timeout configuration:
```yaml
# application.yml
server:
  servlet:
    session:
      timeout: 30m  # 30 minutes
```

---

### Issue: Reactive Validation Timeout

**Error:**
```
java.util.concurrent.TimeoutException: The source did not signal an event for 30 seconds
```

**Solution:**
1. For very large datasets, increase timeout in `ReactiveTradeValidationService.java:45`:
   ```java
   .timeout(60, TimeUnit.SECONDS)  // Increase from 30 to 60
   ```

2. Or optimize validation logic to run faster

3. Split dataset into smaller batches

---

## Important Files Reference

| File | Purpose | When to Edit |
|------|---------|--------------|
| **backend/build.gradle** | Backend dependencies, build config | Add Java dependencies, change Spring Boot version |
| **frontend/build.gradle** | Frontend build, Node/Yarn tasks | Change Node version, add Gradle tasks |
| **frontend/package.json** | NPM dependencies | Add JavaScript libraries |
| **settings.gradle** | Multi-module project config | Add new Gradle modules |
| **.editorconfig** | Code style rules | Change formatting preferences |
| **docker-compose.yml** | Container orchestration | Change ports, add services |
| **backend/Dockerfile** | Docker image definition | Change base image, add system packages |
| **application.yml** | Spring configuration | Change Redis settings, add profiles |
| **SecurityConfiguration.java** | Spring Security setup | Modify authentication, add endpoints |
| **CorsConfig.java** | CORS policy | Allow different origins |
| **SwaggerConfig.java** | API documentation | Customize Swagger UI |
| **frontend/webpack.config.js** | Production webpack config | Add loaders, change build output |
| **frontend/webpack.dev.config.js** | Development webpack config | Add hot reload features |
| **frontend/server.js** | Development server | Change dev server port |

---

## Git Workflow (For AI Assistants)

### Branch Strategy

- **Main branch**: Production-ready code
- **Feature branches**: `claude/claude-md-<session-id>`

### Commit Guidelines

**Message Format:**
```
<type>: <short description>

<optional detailed description>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `refactor`: Code refactoring
- `test`: Add/update tests
- `docs`: Documentation changes
- `build`: Build system changes
- `perf`: Performance improvements

**Examples:**
```
feat: add currency amount validation rule

Implements validator to ensure trade amounts are positive.
Adds corresponding tests and updates validation chain.

test: add integration tests for bulk validation endpoint

fix: resolve CORS issue with frontend dev server
```

### Making Changes

1. **Verify branch**:
   ```bash
   git status
   # Should show: claude/claude-md-<session-id>
   ```

2. **Make changes** (use Read, Edit, Write tools)

3. **Run tests**:
   ```bash
   ./gradlew check
   ```

4. **Commit changes**:
   ```bash
   git add .
   git commit -m "feat: add new validation rule"
   ```

5. **Push to remote** (IMPORTANT: use -u flag):
   ```bash
   git push -u origin claude/claude-md-<session-id>
   ```

6. **Create Pull Request** (if requested):
   ```bash
   gh pr create --title "Add new validation rule" --body "..."
   ```

---

## Performance Considerations

### Reactive vs Standard Validation

**Use Standard** (`/api/validatetrades`):
- Small datasets (<100 trades)
- Simple use cases
- Synchronous processing acceptable

**Use Bulk** (`/api/validatetrades/bulk`):
- Large datasets (100+ trades)
- Performance-critical
- Need timeout protection

**Performance Characteristics:**
- 100 trades: ~3-5 seconds (bulk), ~10-15 seconds (standard)
- 1,000 trades: ~30-40 seconds (bulk)
- 10,000 trades: May timeout on standard endpoint

### Optimization Tips

1. **Validation Chain**:
   - Order validators from fastest to slowest
   - Put most common failures first (fail fast)

2. **Reactive Processing**:
   - Uses `Schedulers.computation()` (CPU cores)
   - Parallel processing for independent trades
   - Timeout prevents hanging

3. **Frontend**:
   - Automatically uses bulk endpoint for >100 trades
   - Shows processing time to user
   - JSON download for large result sets

---

## Security Notes (IMPORTANT)

### Current Security (Development Only)

**In-Memory Authentication**:
- Users: `user/password`, `admin/admin`
- **NOT SUITABLE FOR PRODUCTION**

**Session Management**:
- Production: Redis-backed sessions
- Development: In-memory sessions
- Test: Map-based sessions

**CORS**:
- Only allows `http://localhost:3000`
- Must be updated for production domains

### Production Recommendations

1. **Replace in-memory authentication** with:
   - Database-backed user store
   - LDAP/Active Directory
   - OAuth2/OpenID Connect
   - JWT tokens

2. **Use HTTPS** in production

3. **Update CORS configuration** for actual frontend domain

4. **Enable CSRF protection** for non-API routes

5. **Implement rate limiting** for validation endpoints

6. **Add input validation** for all endpoints

7. **Use secure password encoding** (BCrypt, not NoOp)

8. **Configure Redis with password** in production

---

## Additional Resources

### Documentation

- **Spring Boot**: https://spring.io/projects/spring-boot
- **RxJava**: https://github.com/ReactiveX/RxJava
- **React**: https://react.dev/
- **Swagger**: http://localhost:8080/swagger-ui/index.html (when running)

### Project Files

- **README.md**: User-facing documentation
- **CLAUDE.md**: This file (AI assistant guide)
- **docs/**: Additional documentation (if any)

### Useful Commands Quick Reference

```bash
# Development
./gradlew bootRun -Dspring.profiles.active=development
./gradlew :frontend:start

# Build
./gradlew build
./gradlew clean build

# Test
./gradlew check
./gradlew :backend:test
./gradlew :frontend:test

# Docker
docker-compose up -d
docker-compose logs -f
docker-compose down

# Generate test data
curl "http://localhost:8080/api/generate-test-data?count=1000" > trades.json

# Validate trades
curl -X POST -H "Content-Type: application/json" -d @trades.json \
  http://localhost:8080/api/validatetrades/bulk
```

---

## AI Assistant Guidelines

### Before Making Changes

1. **Read relevant files** first (use Read tool)
2. **Understand the context** (check related classes)
3. **Check existing tests** to understand expected behavior
4. **Follow established patterns** (Chain of Responsibility, reactive, etc.)

### When Modifying Code

1. **Use Edit tool** for existing files (preferred over Write)
2. **Preserve formatting** (2-space indentation, LF line endings)
3. **Follow naming conventions** (see Code Conventions section)
4. **Add/update tests** for all changes
5. **Run tests** before committing (`./gradlew check`)

### When Adding Features

1. **Follow existing patterns** (see Design Patterns section)
2. **Add to appropriate package** (api/service/model/util)
3. **Update documentation** (README.md, CLAUDE.md)
4. **Write comprehensive tests** (unit + integration)
5. **Consider reactive version** for performance-critical features

### When Fixing Bugs

1. **Write failing test** first (TDD approach)
2. **Fix the issue**
3. **Verify test passes**
4. **Check for similar issues** elsewhere
5. **Add regression test**

### When Writing Commits

1. **Use conventional commit format** (feat/fix/refactor/test/docs)
2. **Be descriptive** (explain why, not just what)
3. **Reference issue numbers** if applicable
4. **Run tests before committing**
5. **Push with -u flag**: `git push -u origin <branch-name>`

### Communication

- **Be concise** in responses
- **Use markdown** for formatting
- **Reference file locations** with line numbers (e.g., `ValidateTrades.java:47`)
- **Provide code examples** when explaining concepts
- **Explain trade-offs** when multiple solutions exist

---

## Conclusion

This CLAUDE.md file provides comprehensive guidance for AI assistants working on the TradeValidator codebase. It covers:

- Project architecture and structure
- Development workflows and setup
- Build and deployment processes
- Testing strategies
- API endpoints and usage
- Design patterns and conventions
- Common tasks and troubleshooting
- Security considerations

**Key Takeaways:**

1. **Multi-module Gradle project** (backend + frontend)
2. **Reactive architecture** with RxJava3 for performance
3. **Chain of Responsibility** for extensible validation
4. **Comprehensive testing** (JUnit + Spock + Mocha)
5. **Spring profiles** for different environments
6. **Docker-ready** for production deployment

When in doubt, refer to existing code patterns and tests. The codebase is well-structured with clear separation of concerns.

**Author**: Cesar Chavez
**Last Updated**: 2025-11-13
**Version**: 1.0.0
