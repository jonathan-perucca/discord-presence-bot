mvnw.cmd package -DskipTests & ^
START /B java -jar -Dspring.config.location=target/ target/bot.jar & ^
cd front/ & ^
START /B npm run dev