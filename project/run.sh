./../mvnw clean package -DskipTests
cp target/project-1.0-SNAPSHOT.jar docker
cd docker
docker-compose down
docker rmi docker-spring-boot-postgres:latest
docker-compose up