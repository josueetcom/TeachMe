# TeachMe

Find teachers and exchange partners for the skill you want to learn!

## Development

### Prerequisites

- [git](https://git-scm.com/)
- [npm](https://www.npmjs.com/get-npm)

If you use Google Cloud Shell, these are already installed.

### Setup

1. Clone the repo.

       git clone https://github.com/josueetcom/TeachMe.git

2. `cd` into the repo and setup pre-commit hooks by running:

       npm install

### Development

To run a local server,
execute this command:

    mvn package exec:java

### Deployment

To deploy the app, you'll need to change the `<googleCloudProjectId>` value in [`pom.xml`](pom.xml#L19) to your Google Cloud Project and then run:

    mvn package appengine:deploy
