# Swagger doc on endpoint /api/doc/swagger-ui/index.html#/
![image](https://github.com/user-attachments/assets/80bc4ffc-66e9-4d34-b3b1-04fc792c3a0e)

## Deployment

To deploy the application and database using Docker Compose, follow these steps:

### 1. Clone the repository:

```bash
git clone https://github.com/Trymad1/task-management-system.git
cd task-management-system
```
### 2. Local environment variables

This is not necessary, but you can create a .env file in the project root and specify your values ​​there. The values written here are the default:

```bash
DB_USER=admin
DB_PASSWORD=admin
DB_PORT=5432
APP_PORT=8080
JWT_EXPIRATION_SECOND=1800s
JWT_SECRET=3f5ff51b9ab3f7b6b646eca2ac65b0b920add5673ce892814eeb5a4ebd45db21
```

### 3 Build and run the application
Use Docker Compose to build and start application. 
It is important that the init.sql file is in the same directory as the Docker Compose when first up containers

![image](https://github.com/user-attachments/assets/749bdddb-ee1a-41a8-a0e3-0e8465c66def)

```bash
docker-compose up
```
