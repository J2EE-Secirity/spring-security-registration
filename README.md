
* [Configuring Encoding for properties Files](https://www.jetbrains.com/help/idea/configuring-encoding-for-properties-files.html)
* [How do I send spring csrf token from Postman rest client](https://stackoverflow.com/questions/27182701/how-do-i-send-spring-csrf-token-from-postman-rest-client) **(** [1](https://stackoverflow.com/questions/27182701/how-do-i-send-spring-csrf-token-from-postman-rest-client/35925413) [2](https://stackoverflow.com/questions/27182701/how-do-i-send-spring-csrf-token-from-postman-rest-client/28316021) **)**


### Build and Deploy the Project
```
mvn clean install
```

This is a Spring Boot project, so you can deploy it by simply using the main class: `Application.java`


### Set up MySQL
```
mysql -u root -p 
> CREATE USER 'tutorialuser'@'localhost' IDENTIFIED BY 'tutorialmy5ql';
> GRANT ALL PRIVILEGES ON *.* TO 'tutorialuser'@'localhost';
> FLUSH PRIVILEGES;
```

### Set up Email

Вам необходимо настроить электронную почту, указав свое собственное имя пользователя и пароль в application.properties
Вам также необходимо использовать свой собственный хост, вы можете использовать Amazon или Google, например.
Вы также можете настроить почтовый сервер локально. Подробнее см.  "email.properties.localhost.sample"
### AuthenticationSuccessHandler configuration for Custom Login Page article
If you want to activate the configuration for the article [Custom Login Page for Returning User](http://www.baeldung.com/custom-login-page-for-returning-user), then you need to comment the @Component("myAuthenticationSuccessHandler") annotation in the MySimpleUrlAuthenticationSuccessHandler and uncomment the same in MyCustomLoginAuthenticationSuccessHandler.
