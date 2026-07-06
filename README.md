Gaming Sessions — mikroservisni sistem za organizovanje gejming sesija, rađen za predmet Softverske komponente. Igrači se registruju, kreiraju sesije za razne igre, pozivaju druge igrače da im se pridruže, a organizator na kraju zaključuje sesiju i evidentira prisustvo. Arhitektura se sastoji od tri nezavisna Spring Boot mikroservisa (korisnici, sesije, notifikacije), svaki sa svojom MySQL bazom, uz Eureku za service discovery i Zuul kao API gateway. Notifikacije (aktivacija naloga, pozivnice, podsetnici) šalju se asinhrono preko ActiveMQ. Autentifikacija i autorizacija rade preko JWT-a. Klijentska aplikacija je urađena u React-u.




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
