# Use an official Tomcat image
FROM tomcat:10-jdk17

# Set the working directory inside the container
WORKDIR /usr/local/tomcat/webapps/

# Copy the WAR file to Tomcat's webapps directory
COPY target/LibraryApplication-0.0.1-SNAPSHOT.war ROOT.war  

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]

