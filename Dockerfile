FROM openjdk:11
EXPOSE 1402
ADD target/TransactionsService.jar TransactionsService.jar
ENTRYPOINT ["java","-jar","TransactionsService.jar"]