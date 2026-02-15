# 01Blog

## MVC-Based Project Structure

    Here’s how your Spring Boot project for 01Blog should look in MVC style:

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

    Directory Structure : 01blog-frontend/
    src/app/
    ├── core/               # Core singleton services and guards
    │   ├── guards/         # Route protection logic (AuthGuard, AdminGuard)
    │   ├── interceptors/   # HTTP Interceptors (AuthToken)
    │   └── services/       # Global services (AuthService, ApiService)
    ├── features/           # Feature-specific modules (The main UI blocks)
    │   ├── admin/          # Admin Dashboard & Services
    │   ├── auth/           # Login & Register components
    │   ├── posts/          # Feed, Post Card, Create/Edit Post, Details
    │   ├── profile/        # User Profile & Settings
    │   └── reports/        # Report Dialogs
    ├── layout/             # Main layout shell (Navigation, Sidebar)
    └── shared/             # Reusable models and UI components

## How To run

zsh docker.sh
zsh rundb.sh
docker start 01blogdb

psql -U admin -d 01blogdb

## Instructions

    Backend
        Authentication

            User registration, login, and secure password handling

            Role-based access control (user vs admin)

        User Block Page

            Each user has a public profile (their "block") listing all their posts

            Users can subscribe to other profiles

            Subscribed users receive notifications when new posts are published

        Posts

            Users can create/edit/delete posts with media (image or video) and text

            Each post includes a timestamp, description, and media preview

            Other users can like and comment on posts

        Reports

            Users can report profiles for inappropriate or offensive content

            Reports must include a reason and timestamp

            Reports are stored and visible only to admins

        Admin Panel

            Admin can view and manage all users

            Admin can manage posts and remove inappropriate content

            Admin can handle user reports (ban/delete user or post)

            All admin routes must be protected by access control

    Frontend
        User Experience

            Homepage with a feed of posts from subscribed users

            Personal block page with full post management (CRUD)

            View other users’ blocks and subscribe/unsubscribe

        Post Interaction

            Like and comment on posts (comments update in real time or via refresh)

            Upload media (images/videos) with previews

            Display timestamps, likes, and comments on each post

        Notifications

            Notification icon showing updates from subscribed profiles

            Mark notifications as read/unread

        Reporting

            Report a user with a text reason (UI component/modal)

            Confirmation before submitting the report

        Admin Dashboard

            View all users, posts, and submitted reports

            Delete or ban users, remove or hide posts

            Clean UI for moderation tasks

        Use a responsive UI framework: Angular Material or Bootstrap

## Constraints

    Use Spring Security or JWT for authentication and role management

    Store media securely (in file system or using cloud storage like AWS S3)

    Use a relational SQL database (e.g., PostgreSQL or MySQL)

    All routes must be protected according to user roles

    Code generation tools (like JHipster) are not allowed

    The project must include a detailed README with:

        How to run the backend and frontend

        Technologies used

## Evaluation

    This project is evaluated through peer-to-peer code review and functional demo. Evaluation criteria include:

        💡 Functionality: All features implemented and working as expected

        🔐 Security: Proper role-based access and secure user data handling

        🎨 UI/UX: Responsive, intuitive, and clean interface

## Bonus Features (Optional but Recommended)

    Real-time updates using WebSockets (for comments or notifications)

    infinite scroll on feeds

    Dark mode toggle

    Basic analytics for admins (number of posts, most reported users)

    Markdown support for posts
