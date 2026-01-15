# How to Test After Each Phase

## Quick Start Testing Guide

### ✅ **Yes, you can test after every phase!**

---

## 🚀 **3 Ways to Test**

### **1. Automated Unit/Integration Tests** (Fast & Reliable)
```powershell
# Run all tests
.\mvnw.cmd clean test

# Run tests for a specific phase
.\mvnw.cmd test -Dtest=*AuthTest          # Phase 3
.\mvnw.cmd test -Dtest=*RestaurantTest    # Phase 4
.\mvnw.cmd test -Dtest=*OrderTest         # Phase 6
```

### **2. Manual API Testing with Postman** (Real-world testing)
```powershell
# 1. Start the application
.\mvnw.cmd spring-boot:run

# 2. Use Postman to test APIs
#    - Test each endpoint
#    - Verify responses
```

### **3. Direct Database Verification** (Database structure check)
```bash
# Connect to PostgreSQL
psql -U postgres -d food_delivery_db

# List all tables
\dt

# Check table structure
\d user
\d customer
\d restaurant
```

---

## 📋 **Testing After Each Phase**

### **Phase 1: Project Setup** ✅
```powershell
# Test: Application starts successfully
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
# Expected: Application starts on port 8080 without errors

# Or run automated test

```

**✅ Checklist:**
- [ ] No compilation errors
- [ ] Application starts successfully
- [ ] Logs show "Started FoodDeliveryApplication"

---

### **Phase 2: Database Design** ✅
```powershell
# 1. Start application
.\mvnw.cmd spring-boot:run

# 2. Check database tables are created
psql -U postgres -d food_delivery_db
\dt  # List tables

# 3. Verify table structure
\d user
\d customer
\d restaurant
```

**✅ Checklist:**
- [ ] All tables exist
- [ ] Foreign keys are created
- [ ] Primary keys are auto-generated

---

### **Phase 3: Authentication** ✅
```powershell
# 1. Start application
.\mvnw.cmd spring-boot:run

# 2. Test with Postman or curl:
curl -X POST http://localhost:8080/api/auth/register/customer \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password123","name":"Test User"}'

curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password123"}'
# Expected: Returns JWT token

# 3. Test protected endpoint
curl -X GET http://localhost:8080/api/customer/profile \
  -H "Authorization: Bearer YOUR_TOKEN"
# Expected: 200 OK with customer data
```

**✅ Checklist:**
- [ ] Registration works
- [ ] Login returns JWT token
- [ ] Protected endpoints require token (401 without token)
- [ ] Protected endpoints work with token (200 OK)

---

### **Phase 4: Restaurants** ✅
```bash
# Test Restaurant APIs
curl -X POST http://localhost:8080/api/restaurant/register \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Restaurant","cuisine":"Italian","address":"123 Main St"}'

curl -X GET http://localhost:8080/api/restaurant/1 \
  -H "Authorization: Bearer TOKEN"

curl -X GET "http://localhost:8080/api/restaurants?page=0&size=10&cuisine=Italian" \
  -H "Authorization: Bearer TOKEN"
```

**✅ Checklist:**
- [ ] Create restaurant works
- [ ] Get restaurant works
- [ ] List restaurants with pagination works
- [ ] Filter by cuisine works

---

## 🧪 **Test Utilities Provided**

### **1. TestUtil.java**
- `asJsonString()` - Convert objects to JSON for testing
- `randomEmail()` - Generate test emails
- `randomPhone()` - Generate test phone numbers

### **2. BaseIntegrationTest.java**
- Base class for all integration tests
- Automatically sets up MockMvc and EntityManager
- Handles database cleanup

### **3. Test Configuration**
- Uses H2 in-memory database for tests
- Separate `application-test.properties`
- Mock email sender for testing

---

## 📊 **Test Coverage Goals**

- **Unit Tests:** > 80% code coverage
- **Integration Tests:** All API endpoints tested
- **Critical Paths:** 100% coverage (auth, orders, payments)

---

## 🔍 **What to Check After Each Phase**

### **✅ Code Level:**
1. No compilation errors
2. All tests pass
3. No linting errors

### **✅ Functionality Level:**
1. APIs work as expected
2. Database operations work correctly
3. Business logic is correct

### **✅ Integration Level:**
1. Endpoints are accessible
2. Authentication works
3. Database tables are created correctly
4. Relationships work

---

## 🛠️ **Testing Tools Setup**

### **Postman Collection** (Recommended)
Create a Postman collection with:
- Environment variables (base URL, tokens)
- All endpoints organized by phase
- Pre-request scripts for token generation
- Test scripts for automated assertions

### **Swagger UI** (After Phase 14)
- Access at: `http://localhost:8080/swagger-ui.html`
- Test all endpoints directly from browser
- Built-in authentication support

---

## 📝 **Testing Workflow**

### **After Each Phase:**

1. **Run Automated Tests**
   ```bash
   mvn clean test
   ```

2. **Manual API Testing**
   - Start application: `mvn spring-boot:run`
   - Test with Postman
   - Verify responses

3. **Database Verification**
   - Check tables are created
   - Verify data is stored correctly

4. **Document Results**
   - Note what works
   - Note any issues
   - Fix issues before next phase

---

## ❓ **Common Testing Questions**

### **Q: Do I need to test after every phase?**
**A:** Yes! It's much easier to find and fix issues early.

### **Q: What if a test fails?**
**A:** Fix the issue before moving to the next phase. Don't accumulate technical debt.

### **Q: Can I skip testing?**
**A:** Not recommended. Testing ensures everything works correctly.

### **Q: How long does testing take?**
**A:** Usually 5-10 minutes per phase. Automated tests run in seconds.

---

## 🎯 **Quick Test Commands**

```powershell
# Compile and check for errors
.\mvnw.cmd clean compile

# Run application
.\mvnw.cmd spring-boot:run

# Run all tests
.\mvnw.cmd clean test

# Run specific test
.\mvnw.cmd test -Dtest=ApplicationStartupTest
```

---

## 📚 **Testing Documentation**

- **TESTING_STRATEGY.md** - Detailed testing strategy for all phases
- **TESTING_CHECKLIST.md** - Quick checklist for each phase
- **This file (HOW_TO_TEST.md)** - Quick start guide

---

## ✅ **Summary**

**Yes, you can and should test after every phase!**

**Testing Methods:**
1. ✅ **Automated Tests** - Fast, reliable, repeatable
2. ✅ **Manual API Testing** - Real-world verification
3. ✅ **Database Verification** - Ensure structure is correct

**After Each Phase:**
1. Run automated tests
2. Test APIs manually (Postman)
3. Verify database state
4. Fix any issues
5. Move to next phase

**We'll create tests as we build each phase!** 🚀

---

Ready to start? Let's proceed with Phase 2! 

