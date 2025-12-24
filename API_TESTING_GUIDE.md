# Media App gRPC - Complete Guide & Testing Instructions

## 🎯 Project Overview

This is a **microservices application** demonstrating **gRPC communication** between services using Spring Boot. It manages videos and creators (content creators/YouTubers).

### Architecture

```
┌─────────────────┐     REST API      ┌─────────────────┐     gRPC       ┌─────────────────┐
│                 │ ◄────────────────► │                 │ ◄────────────► │                 │
│   Web Client    │    HTTP/JSON      │  Media Client   │  Binary/Proto  │  Media Server   │
│  (Your Browser) │                   │  (Port 8080)    │                │  (Port 9090)    │
│                 │                   │                 │                │                 │
└─────────────────┘                   └─────────────────┘                └─────────────────┘
                                            │                                      │
                                            │                                      │
                                            │                                      ▼
                                            │                              ┌────────────────┐
                                            │                              │  H2 Database   │
                                            │                              │   (In-Memory)  │
                                            │                              └────────────────┘
                                            ▼
                                      REST Endpoints
                                      (JSON Responses)
```

### What Each Component Does:

#### 1. **Proto Module** (`proto/`)
- **Purpose**: Defines the data structure and service contracts
- **Technology**: Protocol Buffers (protobuf) - Google's language-neutral data serialization
- **Contains**: 
  - `schema.proto` - Defines messages (Video, Creator) and services (VideoService, CreatorService)
  - Generated Java classes for gRPC communication
- **Why**: Ensures both client and server speak the same "language"

#### 2. **Media Server** (`mediaserver/`) - Port 9090
- **Role**: Backend data service (gRPC Server)
- **Technology**: Spring Boot + gRPC + JPA + H2 Database
- **Responsibilities**:
  - Store videos and creators in database
  - Expose gRPC services (not REST!)
  - Handle business logic
  - Persist data
- **Services Exposed**:
  - `VideoService`: Upload and retrieve videos
  - `CreatorService`: Get creator info and their videos

#### 3. **Media Client** (`mediaclient/`) - Port 8080
- **Role**: API Gateway / REST facade
- **Technology**: Spring Boot + gRPC Client + REST Controllers
- **Responsibilities**:
  - Expose REST endpoints for web/mobile apps
  - Translate REST requests to gRPC calls
  - Call Media Server via gRPC
  - Return JSON responses
- **Why Needed**: Web browsers can't speak gRPC directly, so this provides HTTP/JSON interface

---

## 🚀 How to Use the Application

### Step 1: Verify Both Services Are Running

You should see in your terminals:
- **Media Server** (Terminal 1): `Started MediaserverApplication` on port 9090
- **Media Client** (Terminal 2): `Started MediaclientApplication` on port 8080

---

## 📝 Available API Endpoints

### 1. **Upload a Video** (Creates Video + Creator)
```bash
# PowerShell
Invoke-RestMethod -Uri "http://localhost:8080/api/videos/upload" -Method POST
```

**What happens**:
1. REST request → Media Client (8080)
2. Media Client creates gRPC request with:
   - Video: "grpc 101" course
   - Creator: "Xproce" (hirchoua.badr@gmail.com)
3. Media Client → calls Media Server via gRPC (9090)
4. Media Server saves to H2 database
5. Media Server returns Video with generated ID
6. Media Client converts gRPC response to JSON
7. You get JSON response

**Expected Response**:
```json
{
  "id": "1",
  "title": "grpc 101",
  "description": "The gRPC 101 is an introductory course to master Grpc",
  "url": "https://github.com/badrhr/gRPC101",
  "durationSeconds": 380,
  "creator": {
    "id": "2",
    "name": "Xproce",
    "email": "hirchoua.badr@gmail.com"
  }
}
```

---

### 2. **Get a Specific Video by ID**
```bash
# PowerShell (replace {id} with actual video ID from previous response)
Invoke-RestMethod -Uri "http://localhost:8080/api/videos/1" -Method GET
```

**What happens**:
1. REST GET → Media Client
2. Media Client sends `VideoIdRequest` via gRPC to Media Server
3. Media Server queries database
4. Returns Video via gRPC
5. Media Client converts to JSON

---

### 3. **Get Creator Information**
```bash
# PowerShell (replace {id} with creator ID)
Invoke-RestMethod -Uri "http://localhost:8080/api/creators/2" -Method GET
```

**Expected Response**:
```json
{
  "id": "2",
  "name": "Xproce",
  "email": "hirchoua.badr@gmail.com"
}
```

---

### 4. **Get All Videos by a Creator**
```bash
# PowerShell
Invoke-RestMethod -Uri "http://localhost:8080/api/creators/2/videos" -Method GET
```

**Expected Response**:
```json
[
  {
    "id": "1",
    "title": "grpc 101",
    "description": "The gRPC 101 is an introductory course to master Grpc",
    "url": "https://github.com/badrhr/gRPC101",
    "durationSeconds": 380,
    "creator": {
      "id": "2",
      "name": "Xproce",
      "email": "hirchoua.badr@gmail.com"
    }
  }
]
```

---

## 🧪 Complete Testing Workflow

### Execute these commands in order:

```powershell
# 1. Upload a video (this creates both video and creator)
$response1 = Invoke-RestMethod -Uri "http://localhost:8080/api/videos/upload" -Method POST
Write-Host "Video Uploaded:" -ForegroundColor Green
$response1 | ConvertTo-Json

# 2. Get the video you just uploaded
Write-Host "`nFetching Video with ID 1..." -ForegroundColor Cyan
$response2 = Invoke-RestMethod -Uri "http://localhost:8080/api/videos/1" -Method GET
$response2 | ConvertTo-Json

# 3. Get creator information
Write-Host "`nFetching Creator with ID 2..." -ForegroundColor Cyan
$response3 = Invoke-RestMethod -Uri "http://localhost:8080/api/creators/2" -Method GET
$response3 | ConvertTo-Json

# 4. Get all videos by this creator
Write-Host "`nFetching all videos by Creator 2..." -ForegroundColor Cyan
$response4 = Invoke-RestMethod -Uri "http://localhost:8080/api/creators/2/videos" -Method GET
$response4 | ConvertTo-Json

Write-Host "`n✅ All tests completed!" -ForegroundColor Green
```

---

## 🔍 Understanding gRPC Benefits (Why This Architecture?)

### Traditional REST (what most apps use):
```
Client → HTTP/JSON → Server
- Text-based (JSON)
- Larger payload size
- HTTP/1.1 or HTTP/2
- Manual API documentation
```

### gRPC (what this project uses):
```
Client → Binary/Protobuf → Server
- Binary format (smaller, faster)
- HTTP/2 (multiplexing, streaming)
- Strongly typed contracts (schema.proto)
- Auto-generated code
- Built-in streaming support
```

### Real-World Use Cases:
- **Microservices**: Internal service communication (Netflix, Uber)
- **Mobile Apps**: Efficient battery/bandwidth usage
- **IoT Devices**: Low overhead communication
- **Real-time Features**: Streaming data (chat, live updates)

---

## 📊 Database Access (H2 Console)

Access the in-memory database:
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave empty)
```

**Note**: The database is in the Media Server, but you access it through a different mechanism.

---

## 🎓 Key Concepts Demonstrated

1. **Service-to-Service Communication**: Client ↔ Server via gRPC
2. **Protocol Buffers**: Efficient binary serialization
3. **API Gateway Pattern**: REST facade over gRPC backend
4. **Microservices Architecture**: Separated concerns (client vs server)
5. **Spring Boot Integration**: Modern Java framework with gRPC
6. **Code Generation**: Proto files generate Java classes automatically

---

## 🛠️ Troubleshooting

### If uploads fail:
- Ensure Media Server started FIRST (port 9090)
- Then start Media Client (port 8080)
- Check no other apps are using these ports

### To restart:
1. Press `Ctrl+C` in both terminal windows
2. Start Media Server first: `mvn spring-boot:run`
3. Start Media Client second: `mvn spring-boot:run`

### View logs:
- Both terminals show detailed logs
- Look for errors in RED text
- Look for gRPC connections being established

---

## 📚 Next Steps to Explore

1. **Modify the video upload** to accept parameters from the request body
2. **Add more endpoints** (delete video, update creator)
3. **Implement streaming** (upload video file chunks via gRPC)
4. **Add authentication** (secure the gRPC channel)
5. **Deploy to containers** (Docker/Kubernetes)

---

## 🎉 You're All Set!

Your application is now running and ready to test. Start with the "Complete Testing Workflow" section above!
