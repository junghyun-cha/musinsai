# Shopping AI Agent - Project Guidelines

## Project Overview
If the user's prompt starts with “EP:”, then the user wants to enhance the prompt. Read the PROMPT_ENHANCER.md file and follow the guidelines to enhance the user's prompt. Show the user the enhancement and get their permission to run it before taking action on the enhanced prompt.
The enhanced prompts will follow the language of the original prompt (e.g., Korean prompt input will output Korean prompt enhancements, English prompt input will output English prompt enhancements, etc.)

AI-powered shopping assistant that crawls multiple shopping platforms and provides intelligent shopping recommendations through a chat interface.

## Tech Stack
- **Language**: Kotlin
- **Build Tool**: Gradle (Multi-project structure)
- **Architecture**: Modular (api / core)
- **UI**: Chat interface
- **AI Integration**: Claude API for shopping recommendations
- **Web Crawling**: Platform-specific crawlers

## Project Structure
```
musinsai/
├── api/                 # REST API & Chat Interface
│   ├── src/
│   │   ├── main/kotlin/
│   │   │   ├── controller/    # Chat & Shopping endpoints
│   │   │   ├── security/       # Security configuration
│   │   │   ├── exception/      # Exception handling
│   │   │   ├── config/         # API configuration
│   │   └── resources/
│   └── build.gradle.kts
├── core/                # Core business logic & crawling
│   ├── src/
│   │   ├── main/kotlin/
│   │   │   ├── provider/crawler/       # Platform crawlers
│   │   │   ├── ai/            # AI integration
│   │   │   ├── service/       # Business services
│   │   │   ├── domain/        # Domain models
│   │   │   ├── config/        # Configuration classes
│   │   │   ├── exception/     # Custom exceptions
│   │   │   ├── util/          # Utility classes
│   │   └── resources/
│   └── build.gradle.kts
├── build.gradle.kts     # Root build configuration
└── settings.gradle.kts  # Multi-project settings
```

## Development Guidelines

### Code Style
- Use Kotlin idioms and conventions
- Prefer immutable data classes
- Use coroutines for async operations
- Follow clean architecture principles

### Crawler Implementation
- Each platform should have its own crawler implementation
- Use interfaces for crawler abstraction
- Implement rate limiting and respectful crawling
- Handle errors gracefully with retry logic

### AI Integration
- Abstract AI provider behind interface
- Implement prompt engineering for shopping context
- Cache AI responses when appropriate
- Handle API rate limits

### API Design
- RESTful endpoints for chat interface
- WebSocket support for real-time chat
- Proper error handling and status codes
- Request/response validation

### Testing
- Unit tests for core logic
- Integration tests for crawlers
- API endpoint tests
- Mock external dependencies

## Key Features
1. **Multi-Platform Support**: Crawl various shopping platforms
2. **Intelligent Recommendations**: AI-powered product suggestions
3. **Price Comparison**: Compare prices across platforms
4. **Chat Interface**: Natural language shopping assistant
5. **Search & Filter**: Advanced product search capabilities
6. **Wishlist Management**: Save and track products

## Commands
- `./gradlew build` - Build all modules
- `./gradlew :api:bootRun` - Run API server
- `./gradlew test` - Run all tests
- `./gradlew clean` - Clean build artifacts

## Environment Variables
```
# API Module
SERVER_PORT=8080
CLAUDE_API_KEY=your_api_key

# Core Module
CRAWLER_USER_AGENT=ShoppingAgent/1.0
CRAWLER_DELAY_MS=1000
CACHE_TTL_MINUTES=30
```

## Development Workflow
1. Core module handles all business logic and crawling
2. API module provides REST endpoints and chat interface
3. Crawlers fetch data from shopping platforms
4. AI processes queries and provides recommendations
5. Results are formatted and returned through chat interface

## Security Considerations
- Never expose API keys in code
- Implement rate limiting on API endpoints
- Sanitize user inputs
- Use HTTPS for production
- Implement proper authentication

## Performance Optimization
- Cache crawler results
- Implement connection pooling
- Use async/coroutines for I/O operations
- Optimize database queries
- Implement pagination for large result sets
