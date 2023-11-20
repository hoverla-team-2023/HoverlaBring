
<h2>1. Branch Naming</h2>
Choose descriptive and meaningful branch names that convey the purpose of the branch. Use lowercase letters and hyphens or underscores to separate words. Avoid special characters and spaces.

>Good examples:
>* feature/BRING-4-user-authentication
>* bugfix/BRING-19-error-handling
>
<h2>2. Branch Types</h2>
Define clear branch types to help categorize your branches. Common branch types include:

>* Feature branches
>* Bug fix branches
>* Release branches
>* Hotfix branches

<h2>3. Pull Requests</h2>
Use pull requests to propose and review code changes before merging. Enforce code reviews to maintain code quality. Require at least two approvals before merging changes into the main branch.

1. Assign reviewers to pull requests.
2. Ensure at least **two** reviewers approve the changes before merging.
3. Require automated tests and checks to pass before merging.
4. Use automated code analysis tools when possible.
5. Enforcing a minimum of **two** approvals for merging into the main branch helps ensure that changes have been thoroughly reviewed and meet the project's quality standard
6. Branch should be squashed

When you want to get an early feedback, and the PR is in progress please use draft pull requests.

<h2>4. Commit Messages</h2>
The commit message should starts with [BRING-7] and then informative commit messages.

Follow a format like: **type: description**.

Common types include feat, fix, docs, style, refactor, test, and chore.


## Logging

This project leverages Log4j2 as a logging framework. To customize Log4j2 configuration, refer to `BringContext/src/main/resources/log4j2.yaml`. 
In your classes, use Lombok's `@Log4j2` annotation to create a static variable named **log** for logging.

Log4j2 provides 6 levels of logs:

1. **TRACE:** A fine-grained debug message, typically capturing the flow through the application.
2. **DEBUG:** A general debugging event.
3. **INFO:** An event for informational purposes.
4. **WARN:** An event that might possibly lead to an error.
5. **ERROR:** An error in the application, possibly recoverable.
6. **FATAL:** A severe error that will prevent the application from continuing.

Log4j order: TRACE < DEBUG < INFO < WARN < ERROR < FATAL

### Main Points about Logging Configuration

- All logs go to the console and a log file with the name pattern `logs/bring-yyyy-MM-dd.log`.
- Log files are recreated daily, keeping logs for the last 10 days. Older files are deleted.
- Log pattern includes date, level, PID, logger, and log message.
- Log4j is configured with level INFO. Change the value for the ROOT_LOGGER_LEVEL property in the config file to adjust the level.

### Example

   ```java
   import lombok.extern.log4j.Log4j2;
   
   @Log4j2
   public class Main {
   
     public static void main(String[] args) {
       log.trace("Trace log");
       log.info("Info log");
       log.debug("Debug log");
       log.warn("Warn log");
       log.error("Error log");
     }
   }
   ```

### Best Practices

1. Avoid logging in critical performance areas executed frequently.
2. Use checks like:
    ```java
    if (logger.isEnabled(Level.INFO)) {
        logger.info(String.format("The result is %d.", superExpensiveMethod()));
    }
    ```
3. Avoid logging financial info, security-sensitive data, and PII.
4. Include exception details in error log messages, including stack traces.
5. Use different log levels to convey the severity of log messages.
6. Be selective about what you log; avoid excessive logging.
7. Use MDC (Mapped Diagnostic Context) to store contextual information for log entries.
