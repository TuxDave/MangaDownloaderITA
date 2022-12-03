cd CoreShared
mvn clean
mvn install
cd ..

cd Scraper
mvn clean
mvn install
cd ..

cd Telegram-UI
mvn clean
mvn package
cd ..

cd Desktop_UI
mvn clean
mvn package
cd ..