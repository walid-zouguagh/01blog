# 01Blog

## MVC-Based Project Structure
    Here’s how your Spring Boot project for 01Blog should look in MVC style:
    ```
    01blog-backend/
    ├── src/
    │   ├── main/
    │   │   ├── java/com/_Blog/backend/
    │   │   │   ├── controller/        → Controllers (C)
    │   │   │   │    ├── AuthController.java
    │   │   │   │    ├── UserController.java
    │   │   │   │    ├── PostController.java
    │   │   │   │    ├── CommentController.java
    │   │   │   │    ├── ReportController.java
    │   │   │   │    └── AdminController.java
    │   │   │   │
    │   │   │   ├── model/             → Models (M)
    │   │   │   │    ├── entity/       → JPA Entities (User, Post, Like, etc.)
    │   │   │   │    │    ├── User.java
    │   │   │   │    │    ├── Post.java
    │   │   │   │    │    ├── Comment.java
    │   │   │   │    │    ├── Like.java
    │   │   │   │    │    ├── Subscription.java
    │   │   │   │    │    ├── Report.java
    │   │   │   │    │    └── Notification.java
    │   │   │   │    ├── repository/   → Spring Data JPA Repositories
    │   │   │   │    │    ├── UserRepository.java
    │   │   │   │    │    ├── PostRepository.java
    │   │   │   │    │    ├── CommentRepository.java
    │   │   │   │    │    ├── LikeRepository.java
    │   │   │   │    │    ├── SubscriptionRepository.java
    │   │   │   │    │    ├── ReportRepository.java
    │   │   │   │    │    └── NotificationRepository.java
    │   │   │   │    └── dto/          → Data Transfer Objects (request/response models)
    │   │   │   │         ├── AuthRequest.java
    │   │   │   │         ├── AuthResponse.java
    │   │   │   │         ├── PostDTO.java
    │   │   │   │         └── CommentDTO.java
    │   │   │   │
    │   │   │   ├── service/           → Services (Business Logic)
    │   │   │   │    ├── AuthService.java
    │   │   │   │    ├── UserService.java
    │   │   │   │    ├── PostService.java
    │   │   │   │    ├── CommentService.java
    │   │   │   │    ├── NotificationService.java
    │   │   │   │    └── ReportService.java
    │   │   │   │
    │   │   │   ├── config/            → Configurations (Security, JWT, etc.)
    │   │   │   │    ├── SecurityConfig.java
    │   │   │   │    ├── JwtAuthFilter.java
    │   │   │   │    ├── WebConfig.java
    │   │   │   │    └── CorsConfig.java
    │   │   │   │
    │   │   │   ├── util/              → Utilities (file upload, email, etc.)
    │   │   │   │    ├── FileStorageUtil.java
    │   │   │   │    └── DateUtil.java
    │   │   │   │
    │   │   │   ├── exception/         → Global exception handling
    │   │   │   │    ├── GlobalExceptionHandler.java
    │   │   │   │    └── ResourceNotFoundException.java
    │   │   │   │
    │   │   │   └── BackendApplication.java
    │   │   │
    │   │   └── resources/
    │   │       ├── application.yml        → DB, JWT, storage configs
    │   │       ├── static/                → Static resources (if needed)
    │   │       └── templates/             → Thymeleaf views (optional)
    │   │
    │   └── test/java/com/example/oneblog/ → Tests
    │       ├── controller/
    │       ├── service/
    │       └── repository/
    └── pom.xml

    ```