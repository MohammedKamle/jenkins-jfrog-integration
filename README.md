# Demo Maven App - Jenkins Pipeline with JFrog

A simple Java Maven project demonstrating how to build and publish artifacts to JFrog Artifactory using the JFrog CLI in a Jenkins pipeline.

## Project Overview

This project contains:

- A minimal Java 11 application using Guava, Commons Lang, and SLF4J
- A `Jenkinsfile` that builds the project via `jf mvn`, deploys artifacts to JFrog, and publishes build info
- All Maven dependencies are resolved through JFrog (proxying Maven Central)

## Prerequisites

- **Jenkins** running (e.g., as a Docker container on localhost)
- **JFrog Jenkins Plugin** installed and configured with a working server connection
- **JFrog CLI** configured as a Jenkins Global Tool named `jfrog-cli`
- A **GitHub** repository to host this project

Your JFrog connection should already be verified — running `jf 'c show'` and `jf 'rt ping'` in a Jenkins pipeline should succeed.

---

## Setup Steps

### 1. Create JFrog Repositories

You need three Maven repositories in JFrog Artifactory. Create them via the JFrog UI:

**Navigate to:** JFrog UI > Administration > Repositories

#### a) Local Repository — `demo-maven-local`

1. Click **Add Repository** > **Local Repository**
2. Select package type: **Maven**
3. Set Repository Key: `demo-maven-local`
4. Click **Create Local Repository**

This is where your built artifacts (JARs, POMs) will be deployed.

#### b) Remote Repository — `demo-maven-remote`

1. Click **Add Repository** > **Remote Repository**
2. Select package type: **Maven**
3. Set Repository Key: `demo-maven-remote`
4. Set URL: `https://repo.maven.apache.org/maven2`
5. Click **Create Remote Repository**

This proxies Maven Central — dependencies like Guava and Commons Lang are fetched through here and cached in JFrog.

#### c) Virtual Repository — `demo-maven-virtual`

1. Click **Add Repository** > **Virtual Repository**
2. Select package type: **Maven**
3. Set Repository Key: `demo-maven-virtual`
4. Under **Repositories**, add both:
   - `demo-maven-local`
   - `demo-maven-remote`
5. Set **Default Deployment Repository**: `demo-maven-local`
6. Click **Create Virtual Repository**

This is the single entry point for both resolving dependencies and deploying artifacts.

### 2. Configure Maven in Jenkins

Maven must be available as a Jenkins Global Tool:

1. Go to **Manage Jenkins** > **Tools**
2. Scroll to **Maven installations**
3. Click **Add Maven**
4. Set Name: `Maven-3`
5. Check **Install automatically**
6. Select version (e.g., 3.9.12) from "Install from Apache"
7. Click **Save**

### 3. Push Code to GitHub

Push this project to a GitHub repository:

```bash
cd jenkins-pipeline
git init
git add .
git commit -m "Initial commit: demo Maven app with Jenkins pipeline for JFrog"
git remote add origin https://github.com/<your-username>/<your-repo>.git
git branch -M main
git push -u origin main
```

### 4. Create a Multibranch Pipeline in Jenkins

1. Go to **Jenkins Dashboard** > **New Item**
2. Enter a name (e.g., `demo-maven-pipeline`)
3. Select **Multibranch Pipeline** > click **OK**
4. Under **Branch Sources**:
   - Click **Add source** > **GitHub**
   - Enter your repository URL
   - Add credentials if the repo is private
5. Under **Build Configuration**:
   - Mode: **by Jenkinsfile**
   - Script Path: `Jenkinsfile`
6. Click **Save**

Jenkins will scan the repository and automatically discover branches and pull requests.

### 5. Configure Triggers

Since Jenkins is running on localhost, GitHub webhooks cannot reach it directly. You have two options:

#### Option A: Poll SCM (Simplest)

1. In the Multibranch Pipeline configuration, under **Scan Multibranch Pipeline Triggers**
2. Check **Periodically if not otherwise run**
3. Set interval (e.g., `2 minutes`)
4. Click **Save**

Jenkins will check GitHub for changes every 2 minutes.

#### Option B: GitHub Webhook via ngrok (Real-time)

1. Install and start ngrok: `ngrok http 8080`
2. Copy the public URL (e.g., `https://abc123.ngrok.io`)
3. In GitHub, go to **Settings** > **Webhooks** > **Add webhook**
4. Set Payload URL: `https://abc123.ngrok.io/github-webhook/`
5. Content type: `application/json`
6. Select events: **Pushes** and **Pull requests**
7. Click **Add webhook**

Jenkins will trigger immediately on pushes and pull requests.

---

## Pipeline Stages

The Jenkinsfile defines these stages:

| Stage | What it does |
|-------|-------------|
| **Checkout** | Checks out the source code from the Git repository |
| **Verify JFrog Connection** | Runs `jf c show` and `jf rt ping` to verify connectivity |
| **Configure Maven Repos** | Sets resolution repo (`demo-maven-virtual`) and deployment repo (`demo-maven-local`) |
| **Build & Deploy** | Runs `jf mvn clean install` — compiles, tests, and deploys artifacts to JFrog |
| **Publish Build Info** | Runs `jf rt bp` — publishes build metadata to Artifactory for traceability |

## Dependencies

All dependencies are resolved through JFrog's `demo-maven-virtual` repository (which proxies Maven Central):

| Dependency | Purpose |
|-----------|---------|
| Google Guava | String/collection utilities (`Joiner`, `ImmutableList`) |
| Apache Commons Lang | String helpers (`StringUtils.reverse`, `capitalize`) |
| SLF4J + Logback | Logging framework |
| JUnit 5 | Unit testing (test scope) |

## Verifying the Build

After a successful pipeline run, you can verify:

1. **Artifacts in JFrog**: Navigate to `demo-maven-local` in the JFrog UI — you should see `com/example/demo-app/1.0.0-SNAPSHOT/` with the JAR, POM, and metadata files.

2. **Cached Dependencies**: Navigate to `demo-maven-remote-cache` in the JFrog UI — you should see cached copies of Guava, Commons Lang, SLF4J, etc.

3. **Build Info**: Go to **Builds** in the JFrog UI — you should see the build with its dependencies, artifacts, and environment details.
