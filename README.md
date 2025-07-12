# URL Shortener Application

A full-stack URL shortener that lets users convert long URLs into shorter ones, valid for 5 minutes. After expiration, the shortened link will no longer redirect to the original URL and will show appropriate error messages.
## Live Demo
- **Frontend (Netlify)**: [https://url-shortner-assessement.netlify.app](https://url-shortner-assessement.netlify.app)
- **Backend (Render)**: [https://url-shortener-bxjf.onrender.com](https://url-shortener-bxjf.onrender.com)
##  Features
-  Enter and validate long URLs
-  Generate short URLs
-  Clipboard copy with toast message
-  Redirection to original URL
-  Expiration after 5 minutes
-  Error handling for expired or non-existent URLs
-  View all generated URLs in a modal table
-  Deployed frontend & backend
-  Responsive UI using React + Bootstrap
-  RESTful APIs with Spring Boot and MongoDB
## Tech Stack
 Frontend
- React.js
- Axios
- Bootstrap
- React Router
Backend
- Java Spring Boot
- MongoDB (Atlas)
- Maven

## Deployment
- Frontend: Netlify
- Backend: Render
- Database: MongoDB Atlas
## Getting Started
Prerequisites
- Node.js
- Java 17+
- MongoDB (Local or Atlas)

## Frontend Setup
Clone the repository
- git clone https://github.com/GaneshEttaboina/url-shortener.git
- cd url-shortener/client
- npm install
- npm start

## Backend Setup
Clone the repository
- git clone https://github.com/GaneshEttaboina/url-shortener.git
- cd url-shortener/backend
Configure MongoDB URI in src/main/resources/application.properties
- spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster.mongodb.net/linkshortener
Build and run the project
- ./mvnw spring-boot:run



