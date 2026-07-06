GAMING SESSIONS

Projekat za predmet Softverske komponente. Sistem za organizovanje gejming sesija, igraci
se registruju, prave sesije za razne igre, pozivaju druge da im se pridruze, a organizator
na kraju zakljucuje sesiju i evidentira ko je bio prisutan.

Tehnologije: Java, Spring Boot, Spring Cloud Netflix (Eureka, Zuul), Spring Security,
JWT, Hibernate/JPA, MySQL, ActiveMQ, Maven, React, Vite.


POKRETANJE

  CREATE DATABASE user_service_db;
  CREATE DATABASE session_service_db;
  CREATE DATABASE notification_service_db;
  CREATE USER 'gaming_app'@'localhost' IDENTIFIED BY 'gaming_app_pw';
  GRANT ALL PRIVILEGES ON user_service_db.* TO 'gaming_app'@'localhost';
  GRANT ALL PRIVILEGES ON session_service_db.* TO 'gaming_app'@'localhost';
  GRANT ALL PRIVILEGES ON notification_service_db.* TO 'gaming_app'@'localhost';
  FLUSH PRIVILEGES;

  brew install activemq
  brew services start activemq

  export JAVA_HOME=$(/usr/libexec/java_home -v 11)

  cd eureka-server         && mvn spring-boot:run
  cd user-service          && mvn spring-boot:run
  cd notification-service  && mvn spring-boot:run
  cd session-service       && mvn spring-boot:run
  cd api-gateway           && mvn spring-boot:run

  cd client
  npm install
  npm run dev

localhost:3000 (klijent)
localhost:8761 (Eureka dashboard)
admin@gaming-sessions.local / admin123 (admin nalog)
