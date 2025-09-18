//組成映像檔,給容器用的

FROM eclipse-temurin:17-jre

//開啟資料夾 /app
WORKDIR /app

//如果再target 下找不到 commerce.service-0.0.1-SNAPSHOT.jar 在右邊M - LifeCycle - package
CPOY  target/commerce.service-0.0.1-SNAPSHOT.jar  /app/commerce.service.jar

//運行在8080 port，對內是8080
EXPOSE 8080

CMD ["java","-jar","commerce.service.jar"]

//