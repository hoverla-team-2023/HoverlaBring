
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