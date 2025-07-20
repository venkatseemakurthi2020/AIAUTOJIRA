# AIAUTOJIRA
exciting and powerful application! Building a system that leverages AI to streamline the creation of user stories and integrate with Jira can significantly boost productivity for product teams.
How to Set Up and Run This Backend:
Prerequisites:

Java 17 or higher installed.

Maven installed.

An LLM API Key (OpenAI API Key or Google Cloud Project ID/Location for Gemini).

Project Structure:

Create a new directory for your project (e.g., userstory-ai-app).

Inside it, create pom.xml.

Create src/main/java/com/example/userstoryaiapp/ and place UserstoryAiAppApplication.java, model/UserStory.java, model/TechnicalTask.java, repository/UserStoryRepository.java, service/AiService.java, controller/StoryController.java in their respective subdirectories.

Create src/main/resources/application.properties.

API Key:

Open application.properties.

If using OpenAI, replace ${OPENAI_API_KEY} with your actual OpenAI API key.

If using Gemini (after uncommenting the dependency in pom.xml), replace ${GCP_PROJECT_ID} and ${GCP_LOCATION}.

Crucially, never commit your API keys directly to public repositories. Use environment variables for production.

Build:

Open your terminal in the userstory-ai-app root directory.

Run mvn clean install to download dependencies and build the project.

Run:

Run mvn spring-boot:run.

The application will start on http://localhost:8080.

Test (e.g., with Postman/cURL):

Send a POST request to http://localhost:8080/api/stories/generate with a JSON body like:

JSON

{
    "useCase": "As an online customer, I want to manage my subscriptions, so that I can easily pause, resume, or cancel them without contacting support."
}
You should get a JSON response containing the generated user stories and tasks. You can also visit http://localhost:8080/api/stories to see all saved stories.

If you're looking for a more structured representation of the Angular frontend, as you would find in a typical Angular CLI project, I can break down the code into the individual files (e.g., .ts for logic, .html for templates, .css for styles).

This would involve generating the content for files like:

src/app/app.module.ts

src/app/app.component.ts

src/app/app.component.html

src/app/services/story.service.ts

src/app/user-story-generator/user-story-generator.component.ts

src/app/user-story-generator/user-story-generator.component.html

src/styles.css (for global styles, including the icon SVGs)
